/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.query;


import com.sso.common.model.page.BasePageQuery;

/**
 * 菜单查询
 * mapper 层 xml 入参
 *
 * @author 程序员小强
 */
public class SsoMenuQuery extends BasePageQuery {

	private static final long serialVersionUID = -4210187210718040578L;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 父菜单ID
	 */
	private Long menuParentId;

	/**
	 * 需要排除的-菜单ID
	 */
	private Long excludeMenuId;

	/**
	 * 菜单名称
	 */
	private String menuName;

	/**
	 * 菜单名称右模糊
	 */
	private String menuNameLike;

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

	public SsoMenuQuery setSysCode(String sysCode) {
		this.sysCode = sysCode;
		return this;
	}

	public Long getMenuParentId() {
		return menuParentId;
	}

	public SsoMenuQuery setMenuParentId(Long menuParentId) {
		this.menuParentId = menuParentId;
		return this;
	}

	public Long getExcludeMenuId() {
		return excludeMenuId;
	}

	public SsoMenuQuery setExcludeMenuId(Long excludeMenuId) {
		this.excludeMenuId = excludeMenuId;
		return this;
	}

	public String getMenuName() {
		return menuName;
	}

	public SsoMenuQuery setMenuName(String menuName) {
		this.menuName = menuName;
		return this;
	}

	public String getMenuNameLike() {
		return menuNameLike;
	}

	public SsoMenuQuery setMenuNameLike(String menuNameLike) {
		this.menuNameLike = menuNameLike;
		return this;
	}

	public Integer getStatus() {
		return status;
	}

	public SsoMenuQuery setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public SsoMenuQuery setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
		return this;
	}
}
