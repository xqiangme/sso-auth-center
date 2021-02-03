/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.query;

import com.sso.common.model.page.BasePageQuery;

/**
 * 系统管理员权限-分页查询bo
 *
 * @author 程序员小强
 */
public class SystemMgmtPageQuery extends BasePageQuery {

	private static final long serialVersionUID = 7588427768619166745L;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 用户登录名
	 */
	private String usernameLike;

	/**
	 * 用户昵称
	 */
	private String nickNameLike;

	/**
	 * 真实姓名
	 */
	private String realNameLike;

	/**
	 * 手机号码
	 */
	private String phoneLike;

	/**
	 * 用户状态 0-正常;1-停用
	 */
	private Integer userStatus;

	/**
	 * 绑定关系状态 0-正常;1-停用
	 */
	private Integer relationStatus;

	/**
	 * 删除标志 0-正常;1-删除
	 */
	private Integer delFlag;

	public String getSysCode() {
		return sysCode;
	}

	public SystemMgmtPageQuery setSysCode(String sysCode) {
		this.sysCode = sysCode;
		return this;
	}

	public String getUsernameLike() {
		return usernameLike;
	}

	public SystemMgmtPageQuery setUsernameLike(String usernameLike) {
		this.usernameLike = usernameLike;
		return this;
	}

	public String getNickNameLike() {
		return nickNameLike;
	}

	public SystemMgmtPageQuery setNickNameLike(String nickNameLike) {
		this.nickNameLike = nickNameLike;
		return this;
	}

	public String getRealNameLike() {
		return realNameLike;
	}

	public SystemMgmtPageQuery setRealNameLike(String realNameLike) {
		this.realNameLike = realNameLike;
		return this;
	}

	public String getPhoneLike() {
		return phoneLike;
	}

	public SystemMgmtPageQuery setPhoneLike(String phoneLike) {
		this.phoneLike = phoneLike;
		return this;
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public SystemMgmtPageQuery setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
		return this;
	}

	public Integer getRelationStatus() {
		return relationStatus;
	}

	public SystemMgmtPageQuery setRelationStatus(Integer relationStatus) {
		this.relationStatus = relationStatus;
		return this;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public SystemMgmtPageQuery setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
		return this;
	}
}
