package com.sso.common.enums.exception;

/**
 * 用户返回相关
 * 编号400xx
 *
 * @author 程序员小强
 */
public enum UserResStatusEnum implements EnumInterface {

	/**
	 * 用户操作状态枚举
	 */
	ALREADY_DELETE(4001, "用户已删除"),
	USER_NAME_EXISTS(4002, "登录名{0}已经存在"),
	PHONE_EXISTS(4003, "手机号{0}已经存在"),
	EMAIL_EXISTS(4004, "邮箱{0}已经存在"),
	USER_NOT_EXISTS(4005, "用户不存在"),
	OLD_PWD_ERROR(4006, "修改密码失败，旧密码错误"),
	OLD_PWD_EVAL_NEW_PWD(4007, "新密码不能与旧密码相同"),
	SUPPER_ADMIN_USER(4008, "超级管理员账号不可操作"),

	;


	UserResStatusEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private Integer code;

	private String msg;

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}
}
