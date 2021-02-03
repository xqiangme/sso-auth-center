package com.sso.common.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统状态枚举
 *
 * @author 程序员小强
 */
@Getter
public enum SystemStatusEnum {

	/**
	 * 系统状态
	 */
	ENABLE(0, "正常"),
	DISABLE(1, "停用");

	private Integer status;
	private String desc;

	SystemStatusEnum(Integer status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	/**
	 * 启用的状态集
	 */
	public static List<Integer> getEnableStatusList() {
		List<Integer> statusList = new ArrayList<>();
		statusList.add(ENABLE.getStatus());
		return statusList;
	}

	/**
	 * 所有状态集
	 */
	public static List<Integer> getAllStatusList() {
		List<Integer> statusList = new ArrayList<>();
		for (SystemStatusEnum statusEnum : SystemStatusEnum.values()) {
			statusList.add(statusEnum.getStatus());
		}
		return statusList;
	}
}
