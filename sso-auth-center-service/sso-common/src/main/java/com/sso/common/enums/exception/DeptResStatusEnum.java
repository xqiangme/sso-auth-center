package com.sso.common.enums.exception;

/**
 * 部门返回相关
 * 编号300xx
 *
 * @author 程序员小强
 */
public enum DeptResStatusEnum implements EnumInterface {

	/**
	 * 部门操作状态枚举
	 */
	REPEAT_NAME(3000, "部门名称{0}已存在"),
	ALREADY_DELETE(3001, "部门已删除"),
	HASH_CHILD_USER(3002, "部门下存在用户"),
	HASH_CHILD_DEPT(3003, "部门下存在子部门"),
	DEPT_NOT_EXISTS(3004, "部门不存在"),
	PARENT_NOT_EXISTS(3005, "父部门不存在"),
	PARENT_IS_DELETE(3006, "父部门已删除"),
	NOT_EXISTS_OR_DELETE(3007, "部门不存在或者已删除"),
	PARENT_IS_DISABLE(3008, "父部门已停用"),
	HASH_ENABLE_CHILD(3009, "该部门包含未停用的子部门"),
	PARENT_CANNOT_SELF(3010, "父部门不能选自己"),
	;


	DeptResStatusEnum(Integer code, String msg) {
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
