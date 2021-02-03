package com.sso.common.constant;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 权限相关常量信息
 *
 * @author 程序员小强
 */
public class SsoPermissionConstants {

	/**
	 * 所有权限标识
	 */
	public static final String ALL_PERMISSION = "**";

	/**
	 * 认证中心管理员角色-权限标识
	 */
	public static final String ADMIN_ROLE_KEY = "admin";

	/**
	 * 管理员账户名
	 */
	public static final Set<String> ADMIN_USER_SET = new HashSet<>(Collections.singletonList("admin"));

}
