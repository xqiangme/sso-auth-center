/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.common.utils;

import com.sso.common.enums.exception.SysResStatusEnum;
import com.sso.common.exception.BusinessException;
import com.sso.common.model.login.LoginResultVO;
import com.sso.common.model.login.LoginUserVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Security 工具类
 *
 * @author 程序员小强
 */
public class SecurityUtils {

	/**
	 * 获取用户名
	 **/
	public static String getUsername() {
		try {
			return getLoginUserResult().getUsername();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获取操作人名称
	 **/
	public static String getOperateName() {
		try {
			return getLoginUserResult().getOperateName();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获取用户登录信息
	 **/
	public static LoginResultVO getLoginUserResult() {
		try {
			return (LoginResultVO) getAuthentication().getPrincipal();
		} catch (Exception e) {
			throw new BusinessException(SysResStatusEnum.UNAUTHORIZED.getCode(), "获取用户信息异常");
		}
	}

	/**
	 * 获取登录用户基本信息
	 **/
	public static LoginUserVO getLoginUser() {
		try {
			LoginResultVO loginResult = (LoginResultVO) getAuthentication().getPrincipal();
			if (null == loginResult.getUser()) {
				throw new BusinessException(SysResStatusEnum.UNAUTHORIZED.getCode(), "获取用户信息异常");
			}
			return loginResult.getUser();
		} catch (Exception e) {
			throw new BusinessException(SysResStatusEnum.UNAUTHORIZED.getCode(), "获取用户信息异常");
		}
	}

	/**
	 * 获取Authentication
	 */
	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 生成BCryptPasswordEncoder密码
	 *
	 * @param password 密码
	 * @return 加密字符串
	 */
	public static String encryptPassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(password);
	}

	/**
	 * 判断密码是否相同
	 *
	 * @param rawPassword     真实密码
	 * @param encodedPassword 加密后字符
	 * @return 结果
	 */
	public static boolean matchesPassword(String rawPassword, String encodedPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	/**
	 * 是否为管理员
	 *
	 * @param userId 用户ID
	 * @return 结果
	 */
	public static boolean isAdmin(Long userId) {
		return userId != null && 1L == userId;
	}
}
