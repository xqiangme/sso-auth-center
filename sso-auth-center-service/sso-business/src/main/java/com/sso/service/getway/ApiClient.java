/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.service.getway;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sso.common.enums.SignTypeEnum;
import com.sso.common.enums.exception.ApiExceptionEnum;
import com.sso.common.exception.BusinessException;
import com.sso.common.model.result.ResultModel;
import com.sso.common.utils.SsoSignUtil;
import com.sso.common.utils.ValidateUtils;
import com.sso.common.utils.sign.RSASignUtils;
import com.sso.common.utils.sign.SignContentUtil;
import com.sso.dao.entity.SsoSystem;
import com.sso.framework.context.ApplicationContextHelper;
import com.sso.framework.gateway.ApiContainer;
import com.sso.framework.gateway.ApiModel;
import com.sso.model.bo.getway.GetWayReqBO;
import com.sso.service.base.SystemCacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

/**
 * Api请求客户端
 *
 * @author 程序员小强
 */
@Slf4j
@Service
public class ApiClient {

	/**
	 * jackson 序列化工具类
	 */
	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	/**
	 * 时间最大间隔毫秒数
	 * 5分钟
	 */
	private static final Long TIMESTAMP_MAX_BETWEEN = 300000L;

	/**
	 * Api本地容器
	 */
	private final ApiContainer apiContainer;

	public ApiClient(ApiContainer apiContainer) {
		this.apiContainer = apiContainer;
	}

	@Autowired
	private SystemCacheService systemCacheService;


	/**
	 * 校验时间戳
	 *
	 * @param reqBO
	 * @param paramMap
	 */
	public void checkTimestamp(GetWayReqBO reqBO, Map<String, Object> paramMap) {
		long timestamp = Long.parseLong(reqBO.getTimestamp());
		long betweenTime = System.currentTimeMillis() - timestamp;

		//间隔不能超过5分钟
		if (Math.abs(betweenTime) > TIMESTAMP_MAX_BETWEEN) {
			throw new BusinessException("请求时间间隔超时");
		}
	}

	/**
	 * 验签
	 *
	 * @author 程序员小强
	 */
	public void checkSign(GetWayReqBO reqBO, Map<String, Object> paramMap) {
		SsoSystem ssoSystem = systemCacheService.getBySysCode(reqBO.getSysCode());
		if (null == ssoSystem) {
			throw new BusinessException("系统不存在");
		}

		//未配置公钥-不验签
		if (SignTypeEnum.EMPTY.getValue().equals(ssoSystem.getSignType()) ||
				StringUtils.isBlank(ssoSystem.getPublicKey())) {
			log.warn("[ 未配置公钥或已配置不验签 ]  >> sysCode:{}", ssoSystem.getSysCode());
			return;
		}

		SignTypeEnum signValueType = SignTypeEnum.getByValue(reqBO.getSignType());
		if (null == signValueType) {
			//签名类型不存在
			throw new BusinessException(ApiExceptionEnum.SIGN_TYPE_NOT_EXIST);
		}

		log.info("signType:{}, 验签参数 {}", reqBO.getSignType(), paramMap);
		switch (signValueType) {
			case MD5:
				//MD5 签名校验
				this.checkMd5Sign(paramMap, ssoSystem.getPublicKey());
				break;
			case RSA:
				//RSA 签名校验
				this.checkRsaSign(paramMap, ssoSystem.getPublicKey());
				break;
			default:
				throw new BusinessException(ApiExceptionEnum.SIGN_TYPE_NOT_EXIST);
		}
	}

	/**
	 * Api调用方法
	 *
	 * @param reqBO 请求参数
	 * @author 程序员小强
	 */
	public ResultModel<?> invoke(GetWayReqBO reqBO) throws Throwable {
		//获取api方法
		ApiModel apiModel = apiContainer.get(reqBO.getMethod());

		if (null == apiModel) {
			log.info("[ API方法不存在 ] >> method = {}", reqBO.getMethod());
			throw new BusinessException(ApiExceptionEnum.API_NOT_EXIST);
		}

		//获得spring bean
		Object bean = ApplicationContextHelper.getBean(apiModel.getBeanName());
		if (null == bean) {
			log.warn("[ API方法不存在 ] >> method = {}, beanName = {}", reqBO.getMethod(), apiModel.getBeanName());
			throw new BusinessException(ApiExceptionEnum.API_NOT_EXIST);
		}

		// 处理业务参数
		// 忽略JSON字符串中存在，而在Java中不存在的属性
		JSON_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		Object result = JSON_MAPPER.readValue(StringUtils.isBlank(reqBO.getContent()) ? "{}" : reqBO.getContent(), Class.forName(apiModel.getParamName()));

		//校验业务参数
		ValidateUtils.validate(result);

		//执行对应方法
		try {
			Object obj = apiModel.getMethod().invoke(bean, reqBO.getSysCode(), result);
			return ResultModel.success(obj);
		} catch (Exception e) {
			if (e instanceof InvocationTargetException) {
				throw ((InvocationTargetException) e).getTargetException();
			}
			log.info("[ invoke ] 异常 ", e);
			throw new BusinessException(ApiExceptionEnum.SYSTEM_ERROR);
		}
	}

	/**
	 * 校验MD5签名
	 *
	 * @param paramMap
	 * @param publicKey
	 */
	private void checkMd5Sign(Map<String, Object> paramMap, String publicKey) {
		//生成Md5签名
		String sign = SsoSignUtil.getMd5Sign(paramMap, Arrays.asList("signType", "sign"), publicKey);
		//入参中的签名
		String requestSign = (String) paramMap.get("sign");
		if (!sign.equals(requestSign)) {
			log.info("[ MD5验签失败 ] >> sign:{}, requestSign:{} , params = {}", sign, requestSign, paramMap);
			throw new BusinessException(ApiExceptionEnum.INVALID_SIGN);
		}
	}

	/**
	 * 校验RSA 签名
	 *
	 * @param paramMap
	 * @param publicKey
	 */
	private void checkRsaSign(Map<String, Object> paramMap, String publicKey) {
		try {
			String sign = (String) paramMap.get("sign");
			//拼接生成签名的参数
			String signContent = SignContentUtil.getSignContent(paramMap, Arrays.asList("sign", "signType"));
			log.info("[ RSA 验签内容 ] >> signContent:{},sign:{}", signContent, sign);
			boolean verify = RSASignUtils.verify(signContent.getBytes(StandardCharsets.UTF_8), publicKey, sign);
			if (!verify) {
				log.info("[ RSA 验签失败 ] >> signContent:{},sign:{}", signContent, sign);
				throw new BusinessException(ApiExceptionEnum.INVALID_SIGN);
			}
		} catch (Exception e) {
			log.error("[ RSA 验签异常 ] >> paramMap = {}, error = {}", paramMap, ExceptionUtils.getStackTrace(e));
			throw new BusinessException(ApiExceptionEnum.INVALID_SIGN);
		}
	}

}
