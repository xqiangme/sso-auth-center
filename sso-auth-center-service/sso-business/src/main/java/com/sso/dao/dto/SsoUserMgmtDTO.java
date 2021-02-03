/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户-系统管理
 *
 * @author 程序员小强
 * @date 2021-01-02 17:20:41
 */
@Data
public class SsoUserMgmtDTO implements Serializable {

	private static final long serialVersionUID = -7065547317808718215L;


	private Long id;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 用户登录名
	 */
	private String username;

	/**
	 * 用户昵称
	 */
	private String nickName;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 用户性别 0-男;1-女;2-未知
	 */
	private Integer sex;

	/**
	 * 手机号码
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 用户状态 0-正常;1-停用
	 */
	private Integer userStatus;

	/**
	 * 关系状态 0-正常;1-停用
	 */
	private Integer relationStatus;

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
