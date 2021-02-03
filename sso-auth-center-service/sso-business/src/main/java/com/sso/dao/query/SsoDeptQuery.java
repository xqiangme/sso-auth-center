/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.query;

import com.sso.common.model.page.BasePageQuery;

/**
 * 部门查询
 * mapper 层 xml 入参
 *
 * @author 程序员小强
 */
public class SsoDeptQuery extends BasePageQuery {

	private static final long serialVersionUID = 9032669004611549658L;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 父部门ID
	 */
	private Long deptParentId;

	/**
	 * 需要排除的-部门ID
	 */
	private Long excludeDeptId;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 部门右模糊
	 */
	private String deptNameLike;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;

	/**
	 * 删除标志 0-正常;1-删除
	 */
	private Integer delFlag;


	public String getSysCode() {
		return sysCode;
	}

	public SsoDeptQuery setSysCode(String sysCode) {
		this.sysCode = sysCode;
		return this;
	}

	public Long getDeptParentId() {
		return deptParentId;
	}

	public SsoDeptQuery setDeptParentId(Long deptParentId) {
		this.deptParentId = deptParentId;
		return this;
	}

	public Long getExcludeDeptId() {
		return excludeDeptId;
	}

	public SsoDeptQuery setExcludeDeptId(Long excludeDeptId) {
		this.excludeDeptId = excludeDeptId;
		return this;
	}

	public String getDeptName() {
		return deptName;
	}

	public SsoDeptQuery setDeptName(String deptName) {
		this.deptName = deptName;
		return this;
	}

	public String getDeptNameLike() {
		return deptNameLike;
	}

	public SsoDeptQuery setDeptNameLike(String deptNameLike) {
		this.deptNameLike = deptNameLike;
		return this;
	}

	public Integer getStatus() {
		return status;
	}

	public SsoDeptQuery setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public SsoDeptQuery setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
		return this;
	}
}
