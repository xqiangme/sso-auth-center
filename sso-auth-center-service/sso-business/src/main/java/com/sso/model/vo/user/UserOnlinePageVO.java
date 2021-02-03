/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.model.vo.user;

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
public class UserOnlinePageVO implements Serializable {

	private static final long serialVersionUID = -3040345372070636603L;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 用户登录名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 用户昵称
	 */
	private String nickName;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 手机号码
	 */
	private String phone;

	/**
	 * 请求标识
	 */
	private String requestId;

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
	private String loginTime;

	/**
	 * 主动退出时间戳
	 */
	private String loginOutTime;

	/**
	 * 失效时间戳
	 */
	private String expireTime;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 是否超级管理员用户
	 */
	private Boolean supperAdminFlag;

}
