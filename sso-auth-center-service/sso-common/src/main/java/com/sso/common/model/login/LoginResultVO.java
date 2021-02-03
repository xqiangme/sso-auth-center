/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.common.model.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * 用户登录信息
 *
 * @author 程序员小强
 */
@Getter
@Setter
@ToString
public class LoginResultVO implements UserDetails {

	private static final long serialVersionUID = 8497926226669977207L;

	/**
	 * 请求唯一标识
	 */
	private String requestId;

	/**
	 * 用户唯一标识
	 */
	private String token;

	/**
	 * 登录时间
	 */
	private Long loginTime;

	/**
	 * 过期时间
	 */
	private Long expireTime;

	/**
	 * 来源IP
	 */
	private String sourceIp;

	/**
	 * 来源地址
	 */
	private String sourceAddress;

	/**
	 * 浏览器
	 */
	private String browserName;

	/**
	 * 操作系统
	 */
	private String operateSystem;

	/**
	 * 权限列表
	 */
	private Set<String> permissionList;

	/**
	 * 角色标识
	 */
	private Set<String> roleKeyList;

	/**
	 * 用户信息
	 */
	private LoginUserVO user;

	public LoginResultVO() {
	}

	public LoginResultVO(LoginUserVO user, Set<String> permissionList) {
		this.user = user;
		this.permissionList = permissionList;
	}

	/**
	 * 获取操作人名称
	 */
	public String getOperateName() {
		if (null == this.user) {
			return "";
		}
		if (StringUtils.isNotBlank(this.user.getRealName())) {
			return this.user.getRealName();
		}
		if (StringUtils.isNotBlank(this.user.getUsername())) {
			return this.user.getUsername();
		}
		if (StringUtils.isNotBlank(this.user.getNickName())) {
			return this.user.getNickName();
		}
		return "";
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	/**
	 * 账户是否未过期,过期无法验证
	 */
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 指定用户是否解锁,锁定的用户无法进行身份验证
	 *
	 * @return
	 */
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
	 *
	 * @return
	 */
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 是否可用 ,禁用的用户不能身份验证
	 *
	 * @return
	 */
	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
}
