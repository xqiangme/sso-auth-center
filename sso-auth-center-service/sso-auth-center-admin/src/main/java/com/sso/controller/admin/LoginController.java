/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.controller.admin;


import com.sso.common.model.result.ResultModel;
import com.sso.model.bo.login.LoginBO;
import com.sso.model.vo.login.LoginTokenVO;
import com.sso.model.vo.login.LoginUserInfoVO;
import com.sso.service.admin.login.SsoLoginService;
import com.sso.service.admin.login.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 登录验证
 *
 * @author 程序员小强
 */
@RestController
public class LoginController {

	@Autowired
	private SsoLoginService ssoLoginService;

	@Autowired
	private SysPermissionService permissionService;

	/**
	 * 登录方法
	 *
	 * @param loginBO 登录信息
	 * @return 结果
	 */
	@PostMapping("/login")
	public ResultModel<LoginTokenVO> login(@Valid @RequestBody LoginBO loginBO) {
		return ResultModel.success(ssoLoginService.login(loginBO));
	}

	/**
	 * 获取用户信息
	 *
	 * @return 用户信息
	 */
	@GetMapping("getUserInfo")
	public ResultModel<?> getInfo() {
		LoginUserInfoVO userInfo = permissionService.getLoginUserInfo();
		return ResultModel.success(userInfo);
	}
}
