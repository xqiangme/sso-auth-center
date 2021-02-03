/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.controller.admin;


import com.sso.common.model.result.ResultModel;
import com.sso.common.model.result.ResultPageModel;
import com.sso.common.utils.SecurityUtils;
import com.sso.model.bo.user.UserOnlineListPageBO;
import com.sso.model.vo.user.UserOnlinePageVO;
import com.sso.model.vo.user.UserOnlineSysVO;
import com.sso.service.admin.UserOnlineService;
import com.sso.service.base.SsoTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 在线用户管理接口
 *
 * @author 程序员小强
 */
@Slf4j
@RestController
@RequestMapping("/user/online")
public class UserOnlineController {

	@Resource
	private SsoTokenService ssoTokenService;
	@Resource
	private UserOnlineService userOnlineService;

	/**
	 * 在线用户分页列表
	 *
	 * @param pageBO
	 */
	@PreAuthorize("@ss.hasPermission('user:online:listPage')")
	@RequestMapping("/listPage")
	public ResultPageModel<UserOnlinePageVO> listPage(@Valid UserOnlineListPageBO pageBO) {
		return userOnlineService.listPageUser(pageBO);
	}

	/**
	 * 查看用户已登录的子系统
	 *
	 * @param userId 用户ID
	 */
	@PreAuthorize("@ss.hasPermission('user:online:listOnlineSys')")
	@RequestMapping("/listOnlineSys/{userId}")
	public ResultPageModel<UserOnlineSysVO> listOnlineSys(@PathVariable Long userId) {
		return ResultPageModel.success(userOnlineService.listOnlineSys(userId));
	}

	/**
	 * 强退认证中心+所有已登录子系统
	 *
	 * @param userId 用户ID
	 */
	@PreAuthorize("@ss.hasPermission('user:online:retreatUser')")
	@RequestMapping("/retreatUser/{userId}")
	public ResultModel<UserOnlineSysVO> retreatUser(@PathVariable Long userId) {
		ssoTokenService.logoOutUser(userId);
		log.info("[ 强退用户 ]  >> userId:{}, operateBy:{}", userId, SecurityUtils.getOperateName());
		return ResultModel.success();
	}

}
