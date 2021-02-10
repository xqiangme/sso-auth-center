/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.controller.getway;

import com.alibaba.fastjson.JSON;
import com.sso.common.model.result.ResultModel;
import com.sso.common.utils.SystemClock;
import com.sso.model.bo.getway.GetWayReqBO;
import com.sso.service.getway.ApiClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * 统一网关
 */
@Slf4j
@RestController
@RequestMapping("/open")
public class OpenApiController {

	@Autowired
	private ApiClient apiClient;

	/**
	 * 统一网关入口
	 *
	 * @author 程序员小强
	 */
	@PostMapping("/gateway")
	public ResultModel<?> gateway(@Valid GetWayReqBO reqBO,
								  HttpServletRequest request) throws Throwable {
		Map<String, Object> mapParams = WebUtils.getParametersStartingWith(request, "");
		//日志ID (系统编码+请求的唯一标识)
		MDC.put("logId", String.format("[%s-%s]", reqBO.getSysCode(),reqBO.getApiRequestId()));

		log.info("[ 统一认证开放接口 start ] >> method={} mapParams = {}", reqBO.getMethod(), mapParams);
		long start = SystemClock.millisClock().now();

		//校验请求时间戳间隔
		apiClient.checkTimestamp(reqBO, mapParams);

		//验签
		apiClient.checkSign(reqBO, mapParams);

		//请求接口
		ResultModel<?> result = apiClient.invoke(reqBO);

		log.info("[ 统一认证开放接口 end ] >> method={},result = {}, times = {} ms", reqBO.getMethod(), JSON.toJSONString(result), (SystemClock.millisClock().now() - start));

		return result;
	}

}
