package com.sso.common.constant;

/**
 * 通用常量信息
 *
 * @author 程序员小强
 */
public class SsoConstants {

	/**
	 * 下拉选项，默认限制长度
	 */
	public static final Integer OPTION_LIMIT = 10;

	/**
	 * 分隔符 |
	 */
	public static final String SPLIT_ESCAPE = "\\|";

	/**
	 * 分隔符 |
	 */
	public static final String PERMISSIONS_SPLIT = "|";

	/**
	 * 验证码类型-math
	 */
	public static final String CAPTCHA_TYPE_MATH = "math";

	/**
	 * 验证码类型-char
	 */
	public static final String CAPTCHA_TYPE_CHAR = "char";

	/**
	 * UTF-8 字符集
	 */
	public static final String UTF8 = "UTF-8";

	/**
	 * GBK 字符集
	 */
	public static final String GBK = "GBK";

	/**
	 * http请求
	 */
	public static final String HTTP = "http://";

	/**
	 * https请求
	 */
	public static final String HTTPS = "https://";

	/**
	 * 通用成功标识
	 */
	public static final String SUCCESS = "0";

	/**
	 * 修改秘钥权限 key
	 */
	public static final String UPDATE_SECRET_KEY = "system:updateSecret";

	/**
	 * 资源映射路径 前缀
	 */
	public static final String RESOURCE_PREFIX = "/profile";

	/**
	 * 获取头像上传路径
	 */
	public static final String AVATAR_PATH = "/avatar";

	/**
	 * 平台头像上传路径
	 */
	public static final String SYSTEM_ICON_PATH = "/sysIcon";

}
