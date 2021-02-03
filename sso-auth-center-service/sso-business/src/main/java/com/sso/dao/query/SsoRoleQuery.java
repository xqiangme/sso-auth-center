/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.query;


import com.sso.common.model.page.BasePageModel;
import com.sso.common.model.page.BasePageQuery;

/**
 * 角色查询
 * mapper 层 xml 入参
 *
 * @author 程序员小强
 */
public class SsoRoleQuery extends BasePageQuery {

	private static final long serialVersionUID = 4663369561352780923L;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 需要排除的角色ID
	 */
	private Long excludeRoleId;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 角色名称 右模糊
	 */
	private String roleNameLike;

	/**
	 * 角色key 右模糊
	 */
	private String roleKeyLike;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;

	/**
	 * 删除标志 0-正常;1-删除
	 */
	private Integer delFlag;

	public SsoRoleQuery() {
	}


	public SsoRoleQuery(BasePageModel pageModel) {
		this.setPage(pageModel.getPage());
		this.setPageSize(pageModel.getPageSize());
	}

	public String getSysCode() {
		return sysCode;
	}

	public SsoRoleQuery setSysCode(String sysCode) {
		this.sysCode = sysCode;
		return this;
	}

	public Long getExcludeRoleId() {
		return excludeRoleId;
	}

	public SsoRoleQuery setExcludeRoleId(Long excludeRoleId) {
		this.excludeRoleId = excludeRoleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public SsoRoleQuery setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public String getRoleNameLike() {
		return roleNameLike;
	}

	public SsoRoleQuery setRoleNameLike(String roleNameLike) {
		this.roleNameLike = roleNameLike;
		return this;
	}

	public String getRoleKeyLike() {
		return roleKeyLike;
	}

	public SsoRoleQuery setRoleKeyLike(String roleKeyLike) {
		this.roleKeyLike = roleKeyLike;
		return this;
	}

	public Integer getStatus() {
		return status;
	}

	public SsoRoleQuery setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public SsoRoleQuery setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
		return this;
	}
}
