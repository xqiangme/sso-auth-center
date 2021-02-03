/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 在线用户实体类
 *
 * @author 程序员小强
 */
@Data
public class SsoOnlineUser implements Serializable {

	private static final long serialVersionUID = 4271347681726609352L;

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 请求标识
	 */
	private String requestId;

	/**
	 * 用户ID
	 */
	private Long userId;

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
	 * 登录时间戳
	 */
	private Long loginTime;

	/**
	 * 主动退出时间戳
	 */
	private Long loginOutTime;

	/**
	 * 失效时间戳
	 */
	private Long expireTime;

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
