package com.sso.common.enums;

import lombok.Getter;

/**
 * 菜单类型-枚举
 *
 * @author 程序员小强
 */
@Getter
public enum MenuTypeEnum {

	/**
	 * 菜单类型枚举
	 */
	FOLDER("M", "目录"),
	MENU("C", "菜单"),
	FUNCTION("F", "按钮");

	private final String type;
	private final String desc;

	MenuTypeEnum(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public static boolean isMenu(String type) {
		if (FOLDER.type.equals(type)) {
			return true;
		}
		return MENU.type.equals(type);
	}

}
