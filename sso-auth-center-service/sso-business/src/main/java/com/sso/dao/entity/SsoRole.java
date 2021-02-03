/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色信息实体类
 *
 * @author 程序员小强
 * @date 2021-01-07 15:56:40
 */
@Data
public class SsoRole implements Serializable {

	private static final long serialVersionUID = -9193130704066823912L;

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 角色标识key
	 */
	private String roleKey;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;

	/**
	 * 显示顺序
	 */
	private Integer sortNum;

	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 删除标志 0-正常;1-删除
	 */
	private Integer delFlag;

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
