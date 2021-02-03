/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色统计
 *
 * @author 程序员小强
 */
@Data
public class RoleCountDTO implements Serializable {
	private static final long serialVersionUID = -9112752803214125220L;

	private Long roleId;

	private Integer count;

}
