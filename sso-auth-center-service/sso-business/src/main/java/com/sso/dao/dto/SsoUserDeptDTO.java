/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 部门实体类
 *
 * @author 程序员小强
 */
@Data
public class SsoUserDeptDTO implements Serializable {

	private static final long serialVersionUID = -2778006438843424841L;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 部门ID
	 */
	private Long deptId;

	/**
	 * 部门名称
	 */
	private String deptName;

}
