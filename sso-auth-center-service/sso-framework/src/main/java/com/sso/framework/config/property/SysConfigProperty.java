package com.sso.framework.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统配置属性
 *
 * @author 程序员小强
 */
@Component
@ConfigurationProperties(prefix = "sys.config")
public class SysConfigProperty {

	/**
	 * 认证中心登录地址
	 */
	private String ssoLoginUrl;

	/**
	 * 验证码类型
	 * 可选 math , char
	 */
	private String captchaType;

	/**
	 * 令牌自定义标识
	 */
	private String tokenHeader;

	/**
	 * 令牌秘钥
	 */
	private String tokenSecret;

	/**
	 * 认证中心系统编码
	 */
	private String authSsoSysCode;

	/**
	 * 超级管理员用户(用户名|ID)
	 */
	private String supperAdminUser;

	/**
	 * 令牌有效期（默认60分钟）
	 */
	private int tokenExpireTime = 60;

	/**
	 * 文件上传父路径
	 */
	private static String fileProfile;


	public String getSsoLoginUrl() {
		return ssoLoginUrl;
	}

	public void setSsoLoginUrl(String ssoLoginUrl) {
		this.ssoLoginUrl = ssoLoginUrl;
	}

	public String getCaptchaType() {
		return captchaType;
	}

	public void setCaptchaType(String captchaType) {
		this.captchaType = captchaType;
	}

	public String getTokenHeader() {
		return tokenHeader;
	}

	public void setTokenHeader(String tokenHeader) {
		this.tokenHeader = tokenHeader;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	public int getTokenExpireTime() {
		return tokenExpireTime;
	}

	public String getAuthSsoSysCode() {
		return authSsoSysCode;
	}

	public void setAuthSsoSysCode(String authSsoSysCode) {
		this.authSsoSysCode = authSsoSysCode;
	}

	public String getSupperAdminUser() {
		return supperAdminUser;
	}

	public void setSupperAdminUser(String supperAdminUser) {
		this.supperAdminUser = supperAdminUser;
	}

	public void setTokenExpireTime(int tokenExpireTime) {
		this.tokenExpireTime = tokenExpireTime;
	}

	public static String getFileProfile() {
		return fileProfile;
	}

	public void setFileProfile(String fileProfile) {
		SysConfigProperty.fileProfile = fileProfile;
	}

}
