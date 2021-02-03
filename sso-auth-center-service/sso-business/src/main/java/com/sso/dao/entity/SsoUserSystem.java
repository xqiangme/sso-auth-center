/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户与目标系统关系实体类
 *
 * @author 程序员小强
 * @date 2021-01-10 22:22:34
 */
@Data
public class SsoUserSystem implements Serializable {

	private static final long serialVersionUID = -6154959745933571133L;

	/**
	 * 自增ID
	 */
	private Long id;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 显示顺序
	 */
	private Integer sortNum;

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
