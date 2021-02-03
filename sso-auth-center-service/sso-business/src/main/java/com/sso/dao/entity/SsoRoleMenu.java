/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 角色和菜单关系实体类
 *
 * @author 程序员小强
 * @date 2021-01-08 18:01:08
 */
@Data
public class SsoRoleMenu implements Serializable {

	private static final long serialVersionUID = -8633658293267783613L;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 菜单ID
	 */
	private Long menuId;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;


}
