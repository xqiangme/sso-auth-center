/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.query;

import com.sso.common.model.page.BasePageQuery;

/**
 * 在线用户分页查询bo
 *
 * @author 程序员小强
 */
public class UserOnlineQuery extends BasePageQuery {

	private static final long serialVersionUID = 6745860184771356374L;

	/**
	 * 用户登录名
	 */
	private String usernameLike;

	/**
	 * 手机号
	 */
	private String phoneLike;

	/**
	 * 效期开始时间
	 */
	private Long expireStartTime;

	/**
	 * 登录开始时间
	 */
	private Long loginStartTime;

	/**
	 * 登录截止时间
	 */
	private Long loginEndTime;

	public String getUsernameLike() {
		return usernameLike;
	}

	public UserOnlineQuery setUsernameLike(String usernameLike) {
		this.usernameLike = usernameLike;
		return this;
	}

	public String getPhoneLike() {
		return phoneLike;
	}

	public UserOnlineQuery setPhoneLike(String phoneLike) {
		this.phoneLike = phoneLike;
		return this;
	}

	public Long getExpireStartTime() {
		return expireStartTime;
	}

	public UserOnlineQuery setExpireStartTime(Long expireStartTime) {
		this.expireStartTime = expireStartTime;
		return this;
	}

	public Long getLoginStartTime() {
		return loginStartTime;
	}

	public UserOnlineQuery setLoginStartTime(Long loginStartTime) {
		this.loginStartTime = loginStartTime;
		return this;
	}

	public Long getLoginEndTime() {
		return loginEndTime;
	}

	public UserOnlineQuery setLoginEndTime(Long loginEndTime) {
		this.loginEndTime = loginEndTime;
		return this;
	}
}
