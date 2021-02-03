package com.sso.model.vo.login;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录日志分页
 *
 * @author 程序员小强
 * @date 2021-01-23 10:45:22
 */
@Data
public class LoginLogPageVO implements Serializable {

	private static final long serialVersionUID = 4602708640932864463L;

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
	private String loginTime;

	/**
	 * 返回结果
	 */
	private String operateMsg;

}
