package com.sso.common.constant;

/**
 * 通用常量信息
 *
 * @author 程序员小强
 */
public class CacheConstants {

	/**
	 * 验证码 redis key
	 */
	private static final String CAPTCHA_CODE_KEY = "captcha_cache_key:";

	/**
	 * 验证码有效期（5分钟）
	 */
	public static final Integer CAPTCHA_EXPIRE = 5;

	/**
	 * 验证码有效期（5分钟）
	 */
	public static final Integer CAPTCHA_EXPIRE_FIVE = 5;

	/**
	 * 登录用户 redis key
	 */
	private static final String LOGIN_TOKEN_CACHE_KEY = "login_tokens:";

	/**
	 * 登录用户 redis key
	 */
	private static final String LOGIN_USER_ID_CACHE_KEY = "login_user_id:";

	/**
	 * 系统平台配置 缓存key
	 */
	private static final String SYSTEM_CONFIG_CACHE_KEY = "sso_admin_system_cache_key:";

	/**
	 * 注册子系统缓存key
	 */
	private static final String REGISTER_SYS_CACHE_KEY = "register_sys:";

	/**
	 * 系统平台-缓存有效期（720分钟）12小时
	 */
	public static final Integer SYSTEM_CONFIG_CACHE_EXPIRE = 720;

	public static String getCaptchaCodeKey(String requestId) {
		return CacheConstants.CAPTCHA_CODE_KEY + requestId;
	}

	public static String getSystemCaptchaKey(String sysCode) {
		return CacheConstants.SYSTEM_CONFIG_CACHE_KEY + sysCode;
	}

	public static String getLoginUserIdKey(Long userId) {
		return CacheConstants.LOGIN_USER_ID_CACHE_KEY + userId;
	}

	public static String getLoginUserTokenKey(String token) {
		return CacheConstants.LOGIN_TOKEN_CACHE_KEY + token;
	}
}
