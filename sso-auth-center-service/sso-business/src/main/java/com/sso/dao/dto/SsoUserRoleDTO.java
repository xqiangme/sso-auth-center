/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户角色
 *
 * @author 程序员小强
 * @date 2020-12-27 18:07:20
 */
@Data
public class SsoUserRoleDTO implements Serializable {

	private static final long serialVersionUID = 219537171519448483L;
	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 角色id多个逗号分隔
	 */
	private String roleIds;

	/**
	 * 角色名称多个逗号分隔
	 */
	private String roleNames;

}
