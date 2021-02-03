package com.sso.common.enums;

import lombok.Getter;

/**
 * 登录状态枚举
 *
 * @author 程序员小强
 */
@Getter
public enum LoginStatusEnum {

	/**
	 * 系统状态
	 */
	SUCCESS(0, "成功"),
	FAIL(1, "失败");

	private final Integer status;
	private final String desc;

	LoginStatusEnum(Integer status, String desc) {
		this.status = status;
		this.desc = desc;
	}
}
