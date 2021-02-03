package com.sso.common.enums;

import lombok.Getter;

/**
 * 数据删除状态
 *
 * @author 程序员小强
 */
@Getter
public enum DelFlagEnum {

    OK(0, "正常"),
    DELETED(1, "删除");

    private Integer status;
    private String desc;

    DelFlagEnum(Integer status, String desc) {
	this.status = status;
	this.desc = desc;
    }

}
