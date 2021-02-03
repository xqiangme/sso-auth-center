package com.sso.common.enums.exception;

/**
 * 角色返回相关
 * 编号600xx
 *
 * @author 程序员小强
 */
public enum RoleResStatusEnum implements EnumInterface {

	/**
	 * 角色操作状态枚举
	 */
	ALREADY_DELETE(6001, "角色已删除"),
	ROLE_NAME_EXISTS(6002, "角色名称{0}已经存在"),
	KEY_NAME_EXISTS(6002, "角色标识{0}已经存在"),
	ROLE_NOT_EXISTS(6003, "该角色不存在"),
	ALREADY_BIND_USER(6004, "该角色已被用户使用"),

	;


	RoleResStatusEnum(Integer code, String msg) {
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
