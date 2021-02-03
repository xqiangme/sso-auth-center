package com.sso.common.enums.exception;

/**
 * 目标系统返回相关
 * 编号700xx
 *
 * @author 程序员小强
 */
public enum SystemResStatusEnum implements EnumInterface {

	/**
	 * 目标系统 操作状态枚举
	 */
	ALREADY_DELETE(7001, "系统{0}已删除"),
	SYS_CODE_EXISTS(7002, "系统编码{0}已经存在"),
	SYS_NOT_EXISTS(7003, "该系统不存在"),
	ALREADY_BIND_USER(7004, "该系统已被用户引用,不可修改系统编码"),
	ALREADY_BIND_DEPT(7005, "该系统已被部门引用,不可修改系统编码"),
	ALREADY_BIND_MENU(7006, "该系统已被菜单引用,不可修改系统编码"),
	ALREADY_BIND_ROLE(7007, "该系统已被角色引用,不可修改系统编码"),
	;


	SystemResStatusEnum(Integer code, String msg) {
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
