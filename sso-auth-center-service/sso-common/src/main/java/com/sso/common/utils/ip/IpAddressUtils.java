/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.common.utils.ip;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取地址类
 * 注:根据IP地址查询所属地
 *
 * @author 程序员小强
 */
public class IpAddressUtils {
	private static final Logger log = LoggerFactory.getLogger(IpAddressUtils.class);

	/**
	 * IP地址查询
	 * 太平洋网络IP地址查询接口
	 */
	public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";

	/**
	 * 未知地址
	 */
	public static final String UNKNOWN = "XX XX";

	/**
	 * 超时毫秒数 10s
	 */
	public static final int TIME_OUT_MILLISECONDS = 10 * 1000;

	/**
	 * 根据IP-获取真实地址
	 *
	 * @param ip
	 */
	public static String getRealAddressByIP(String ip) {
		//内网IP
		if (IpUtils.internalIp(ip)) {
			return "内网IP";
		}

		try {
			String response = HttpUtil.get(String.format(IP_URL, ip), TIME_OUT_MILLISECONDS);
			if (StringUtils.isEmpty(response)) {
				log.error("[ 根据IP-获取地理位置失败 ] >>  {}", ip);
				return UNKNOWN;
			}
			JSONObject jsonObject = JSONObject.parseObject(response);
			if (null == jsonObject) {
				return UNKNOWN;
			}

			//省份
			String province = jsonObject.getString("pro");
			//城市
			String city = jsonObject.getString("city");

			//详细地址 示例：浙江省杭州市 电信
			String addr = jsonObject.getString("addr");
			if (StringUtils.isNoneBlank(addr)) {
				addr = addr.replaceAll(province, "")
						.replaceAll(city, "").trim();
				return addr;
			}

			//示例：浙江省 杭州市 电信
			return String.format("%s %s %s", province, city, addr);
		} catch (Exception e) {
			log.error("[ 根据IP-获取地理位置异常 ] >>  {}", ip);
		}
		return UNKNOWN;
	}
}
