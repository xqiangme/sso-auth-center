/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.controller.admin;


import com.sso.common.model.result.ResultPageModel;
import com.sso.model.bo.login.LoginLogListPageBO;
import com.sso.model.vo.login.LoginLogPageVO;
import com.sso.service.admin.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 登录日志管理接口
 *
 * @author 程序员小强
 */
@RestController
@RequestMapping("/login/log")
public class LoginLogController {

	@Autowired
	private LoginLogService loginLogService;

	/**
	 * 登录日志分页列表
	 *
	 * @param pageBO
	 */
	@PreAuthorize("@ss.hasPermission('login:log:listPage')")
	@RequestMapping("/listPage")
	public ResultPageModel<LoginLogPageVO> listPage(@Valid LoginLogListPageBO pageBO) {
		return loginLogService.listPageUser(pageBO);
	}

}
