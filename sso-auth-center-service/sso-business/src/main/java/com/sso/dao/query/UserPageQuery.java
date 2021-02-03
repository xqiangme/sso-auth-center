/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.query;

import com.sso.common.model.page.BasePageQuery;

/**
 * 用户分页查询bo
 *
 * @author 程序员小强
 */
public class UserPageQuery extends BasePageQuery {

	private static final long serialVersionUID = -7892866355117742382L;

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
	 * 所属部门ID
	 */
	private Long deptId;

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;

	/**
	 * 删除标志 0-正常;1-删除
	 */
	private Integer delFlag;

	public String getUsernameLike() {
		return usernameLike;
	}

	public UserPageQuery setUsernameLike(String usernameLike) {
		this.usernameLike = usernameLike;
		return this;
	}

	public String getNickNameLike() {
		return nickNameLike;
	}

	public UserPageQuery setNickNameLike(String nickNameLike) {
		this.nickNameLike = nickNameLike;
		return this;
	}

	public String getRealNameLike() {
		return realNameLike;
	}

	public UserPageQuery setRealNameLike(String realNameLike) {
		this.realNameLike = realNameLike;
		return this;
	}

	public String getPhoneLike() {
		return phoneLike;
	}

	public UserPageQuery setPhoneLike(String phoneLike) {
		this.phoneLike = phoneLike;
		return this;
	}

	public Long getDeptId() {
		return deptId;
	}

	public UserPageQuery setDeptId(Long deptId) {
		this.deptId = deptId;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public UserPageQuery setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public Integer getStatus() {
		return status;
	}

	public UserPageQuery setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public UserPageQuery setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
		return this;
	}
}
