/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志实体类
 *
 * @author 程序员小强
 * @date 2021-01-23 10:45:22
 */
@Data
public class SsoLoginLog implements Serializable {

	private static final long serialVersionUID = -7814417622883518147L;

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 请求标识
	 */
	private String requestId;

	/**
	 * 用户登录名
	 */
	private String username;

	/**
	 * 来源IP
	 */
	private String sourceIp;

	/**
	 * 来源地址
	 */
	private String sourceAddress;

	/**
	 * 浏览器
	 */
	private String browserName;

	/**
	 * 操作系统
	 */
	private String operateSystem;

	/**
	 * 登录状态 0-成功;1-失败
	 */
	private Integer status;

	/**
	 * 登录时间戳
	 */
	private Long loginTime;

	/**
	 * 返回结果
	 */
	private String operateMsg;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;


}
