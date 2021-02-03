/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.query;

import com.sso.common.model.page.BasePageQuery;

/**
 * 登录日志分页查询bo
 *
 * @author 程序员小强
 */
public class LoginLogQuery extends BasePageQuery {

	private static final long serialVersionUID = -7137359307341275655L;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 用户登录名
	 */
	private String usernameLike;

	/**
	 * 登录状态 0-成功;1-失败
	 */
	private Integer status;

	/**
	 * 登录开始时间
	 */
	private Long loginStartTime;

	/**
	 * 登录截止时间
	 */
	private Long loginEndTime;


	public String getSysCode() {
		return sysCode;
	}

	public LoginLogQuery setSysCode(String sysCode) {
		this.sysCode = sysCode;
		return this;
	}

	public String getUsernameLike() {
		return usernameLike;
	}

	public LoginLogQuery setUsernameLike(String usernameLike) {
		this.usernameLike = usernameLike;
		return this;
	}

	public Integer getStatus() {
		return status;
	}

	public LoginLogQuery setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public Long getLoginStartTime() {
		return loginStartTime;
	}

	public LoginLogQuery setLoginStartTime(Long loginStartTime) {
		this.loginStartTime = loginStartTime;
		return this;
	}

	public Long getLoginEndTime() {
		return loginEndTime;
	}

	public LoginLogQuery setLoginEndTime(Long loginEndTime) {
		this.loginEndTime = loginEndTime;
		return this;
	}

}
