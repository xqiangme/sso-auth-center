package com.sso.common.enums.exception;

/**
 * 菜单返回相关
 * 编号500xx
 *
 * @author 程序员小强
 */
public enum MenuResStatusEnum implements EnumInterface {

	/**
	 * 菜单操作状态枚举
	 */
	ALREADY_DELETE(5001, "菜单已删除"),
	HASH_CHILD_DEPT(5003, "菜单下存在未删除的子菜单"),
	MENU_NAME_EXISTS_IN_SAME_PARENT(5004, "同上级下菜单名称{0}已经存在"),
	PARENT_CANNOT_SELF(5005, "父菜单不能选自己"),
	;


	MenuResStatusEnum(Integer code, String msg) {
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
