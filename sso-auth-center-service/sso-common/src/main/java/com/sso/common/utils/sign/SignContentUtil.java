package com.sso.common.utils.sign;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 统一认证中心 签名工具
 *
 * @author 程序员小强
 */
@Slf4j
public class SignContentUtil {

	/**
	 * 生成要加签的参数
	 *
	 * @param paramMap
	 * @param removeKeys
	 * @return 生成要加签的参数
	 */
	public static String getSignContent(Map<String, Object> paramMap, List<String> removeKeys) {
		//按参数名字母顺序（升序、区分大小写）
		TreeMap<String, String> signParamMap = getTreeMap(paramMap);
		//移除签名参数
		removeKeys.forEach(signParamMap::remove);

		//待签名串（以&符逐个拼接参数名=参数值而形成）
		return getSignContent(signParamMap);
	}

	/**
	 * 以&符逐个拼接参数名=参数值而形成
	 *
	 * @param sortedParams
	 */
	private static String getSignContent(Map<String, String> sortedParams) {
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
	private static TreeMap<String, String> getTreeMap(Map<String, Object> paramMap) {
		TreeMap<String, String> signMap = new TreeMap<>();
		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			signMap.put(entry.getKey(), entry.getValue().toString());
		}
		return signMap;
	}
}
