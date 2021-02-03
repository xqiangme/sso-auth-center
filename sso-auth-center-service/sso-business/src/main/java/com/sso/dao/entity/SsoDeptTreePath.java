/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.entity;

import lombok.Data;

import java.io.Serializable;


/**
 * 部门祖先/后代关系实体类
 *
 * @author 程序员小强
 * @date 2020-12-27 18:07:20
 */
@Data
public class SsoDeptTreePath implements Serializable {

	private static final long serialVersionUID = -8794488706755626248L;

	/**
	 * 祖先
	 */
	private Long ancestor;

	/**
	 * 后代
	 */
	private Long descendant;


}
