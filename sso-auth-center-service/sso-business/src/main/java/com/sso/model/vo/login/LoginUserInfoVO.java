/**
 * haifenbb.com
 * Copyright (C) 2019-2020 All Rights Reserved.
 */
package com.sso.model.vo.login;

import com.sso.common.model.login.LoginUserVO;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
public class LoginUserInfoVO implements Serializable {

	private static final long serialVersionUID = -8487394407696680994L;

	private LoginUserVO user;

	private Set<String> roleKeyList;

	private Set<String> permissionList;

	private List<LoginMenuVO> menuList;

	public LoginUserInfoVO() {
	}

	public LoginUserInfoVO(Set<String> permissionList, List<LoginMenuVO> menuList) {
		this.permissionList = permissionList;
		this.menuList = menuList;
	}

	public static LoginUserInfoVO initDefault() {
		LoginUserInfoVO result = new LoginUserInfoVO();
		result.setPermissionList(new HashSet<>(0));
		result.setRoleKeyList(new HashSet<>(0));
		result.setMenuList(new ArrayList<>(0));
		return result;
	}

}
