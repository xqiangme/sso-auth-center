package com.sso.common.model.login;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户对象 sys_user
 *
 * @author 程序员小强
 */
@Getter
@Setter
@ToString
public class LoginUserVO implements Serializable {

	private static final long serialVersionUID = 7849003000148433962L;

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

}
