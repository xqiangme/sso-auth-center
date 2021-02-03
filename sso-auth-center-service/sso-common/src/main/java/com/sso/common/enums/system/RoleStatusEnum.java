package com.sso.common.enums.system;

import lombok.Getter;

/**
 * 角色状态-枚举
 *
 * @author 程序员小强
 */
@Getter
public enum RoleStatusEnum {

	OK(0, "正常"),
	DISABLE(1, "停用");

	private Integer status;
	private String desc;

	RoleStatusEnum(Integer status, String desc) {
		this.status = status;
		this.desc = desc;
	}

}
