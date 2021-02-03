package com.sso.model.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户详情
 *
 * @author 程序员小强
 */
@Data
public class UserDetailVO implements Serializable {

	private static final long serialVersionUID = -8029802544403883540L;

	/**
	 * 系统编码
	 */
	private String sysCode;

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
	 * 头像
	 */
	private String avatar;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;

	/**
	 * 最后登录IP
	 */
	private String loginIp;

	/**
	 * 最后登录时间
	 */
	private Date lastLoginTime;

	/**
	 * 备注
	 */
	private String remarks;


	private Long deptId;

	private List<Long> roleIdList;

}
