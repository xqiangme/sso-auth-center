/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单权限实体类
 *
 * @author 程序员小强
 * @date 2021-01-08 18:01:08
 */
@Data
public class SsoMenu implements Serializable {

	private static final long serialVersionUID = -1115470419419879842L;

	/**
	 * 菜单ID
	 */
	private Long menuId;

	/**
	 * 菜单名称
	 */
	private String menuName;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 菜单类型（M目录 C菜单 F按钮）
	 */
	private String menuType;

	/**
	 * 父菜单ID
	 */
	private Long menuParentId;

	/**
	 * 显示顺序
	 */
	private Integer sortNum;

	/**
	 * 菜单路由地址
	 */
	private String path;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 组件路径
	 */
	private String component;

	/**
	 * 权限标识,多个|分隔
	 */
	private String permissions;

	/**
	 * 使用类型 0-授权访问;1-开放访问;
	 */
	private Integer useType;

	/**
	 * 是否显示 0-显示;1-隐藏
	 */
	private Integer visible;

	/**
	 * 菜单状态 0-正常;1停用
	 */
	private Integer status;

	/**
	 * 删除标志 0-正常;1-删除
	 */
	private Integer delFlag;

	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 创建者
	 */
	private String createBy;

	/**
	 * 最后修改者
	 */
	private String updateBy;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;


}
