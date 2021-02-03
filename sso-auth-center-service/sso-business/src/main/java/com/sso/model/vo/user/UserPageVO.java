package com.sso.model.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 *
 * @author 程序员小强
 */
@Data
public class UserPageVO implements Serializable {

	private static final long serialVersionUID = 4387256959848907939L;

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
	 * 用户性别 0-男;1-女;2-未知
	 */
	private Integer sex;

	/**
	 * 手机号码
	 */
	private String phone;

	/**
	 * 所属部门名称
	 */
	private String deptName;

	/**
	 * 角色名称集
	 */
	private String roleNames;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;

	/**
	 * 最后登录时间
	 */
	private Date lastLoginTime;

	/**
	 * 创建者
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 是否已绑定系统
	 */
	private Boolean sysBindFlag;

	/**
	 * 是否超级管理员用户
	 */
	private Boolean supperAdminFlag;

}
