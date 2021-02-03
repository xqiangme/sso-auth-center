/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.model.vo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录日志实体类
 *
 * @author 程序员小强
 * @date 2021-01-23 10:45:22
 */
@Data
public class UserOnlineSysVO implements Serializable {

	private static final long serialVersionUID = -3040345372070636603L;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 系统名称
	 */
	private String sysName;

	/**
	 * 登录时间
	 */
	private String loginTime;

}
