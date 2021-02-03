package com.sso.common.enums;

import lombok.Getter;

/**
 * 平台管理员关系表-枚举
 *
 * @author 程序员小强
 */
@Getter
public enum SystemManagerStatusEnum {

	OK(0, "正常"),
	DISABLE(1, "停用");

	private Integer status;
	private String desc;

	SystemManagerStatusEnum(Integer status, String desc) {
		this.status = status;
		this.desc = desc;
	}

}
