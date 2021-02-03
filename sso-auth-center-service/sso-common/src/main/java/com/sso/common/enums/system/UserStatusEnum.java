package com.sso.common.enums.system;

/**
 * 用户状态
 *
 * @author 程序员小强
 */
public enum UserStatusEnum {

	/**
	 * 用户状态
	 */
	OK(0, "正常"),
	DISABLE(1, "停用");

	private Integer status;
	private String info;

	UserStatusEnum(int status, String info) {
		this.status = status;
		this.info = info;
	}

	public Integer getStatus() {
		return status;
	}

	public String getInfo() {
		return info;
	}
}
