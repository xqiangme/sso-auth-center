package com.sso.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 统一认证中心 签名工具
 *
 * @author 程序员小强
 */
@Slf4j
public class SsoSignUtil {

	/**
	 * 生成Md5签名
	 * 规则：
	 * 1.按参数名字母顺序（升序、区分大小写）
	 * 2.移除不需要加入签名参数
	 * 3.待签名串（以&符逐个拼接参数名=参数值而形成）
	 * 4.拼接秘钥 格式&secret= xxxx
	 * 5.MD5加密
	 *
	 * @param paramMap   参数Map
	 * @param removeKeys 需要移除的参数名
	 * @param secret     秘钥值
	 */
	public static String getMd5Sign(Map<String, Object> paramMap, List<String> removeKeys, String secret) {
		try {
			//按参数名字母顺序（升序、区分大小写）
			TreeMap<String, String> signParamMap = getTreeMap(paramMap);
			//移除签名参数
			removeKeys.forEach(signParamMap::remove);
			//待签名串（以&符逐个拼接参数名=参数值而形成）
			String content = getSignContent(signParamMap);

			//MD5 方式拼接 &secret
			content = content + "&secret=" + secret;

			//MD5加签
			return DigestUtils.md5Hex(content);
		} catch (Exception e) {
			throw new RuntimeException("MD5加签异常", e);
		}
	}

	/**
	 * 以&符逐个拼接参数名=参数值而形成
	 *
	 * @param sortedParams
	 */
	public static String getSignContent(Map<String, String> sortedParams) {
		StringBuilder content = new StringBuilder();
		ArrayList<String> keys = new ArrayList<>(sortedParams.keySet());
		Collections.sort(keys);
		int index = 0;

		for (Object o : keys) {
			String key = (String) o;
			String value = sortedParams.get(key);
			if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
				content.append(index == 0 ? "" : "&").append(key).append("=").append(value);
				++index;
			}
		}
		return content.toString();
	}

	/**
	 * 按参数名字母顺序（升序、区分大小写）
	 *
	 * @param paramMap
	 */
	public static TreeMap<String, String> getTreeMap(Map<String, Object> paramMap) {
		TreeMap<String, String> signMap = new TreeMap<>();
		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			signMap.put(entry.getKey(), entry.getValue().toString());
		}
		return signMap;
	}
}
