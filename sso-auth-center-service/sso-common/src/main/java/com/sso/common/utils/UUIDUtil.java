package com.sso.common.utils;

import java.util.Locale;
import java.util.UUID;

/**
 * UUID 工具类
 *
 * @author 程序员小强
 */
public final class UUIDUtil {

	/**
	 * 获取32位UUID
	 *
	 * @author 程序员小强
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 获取32位大写UUID
	 *
	 * @author 程序员小强
	 */
	public static String get32UpperCaseUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase(Locale.ENGLISH);
	}

	private UUIDUtil() {
	}

}
