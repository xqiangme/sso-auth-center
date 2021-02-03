/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.controller.admin;


import com.sso.common.model.login.LoginUserVO;
import com.sso.common.model.result.ResultModel;
import com.sso.common.utils.SecurityUtils;
import com.sso.model.bo.user.UserUpdateProfileBO;
import com.sso.model.bo.user.UserUpdatePwdBO;
import com.sso.model.vo.user.UserProfileVO;
import com.sso.service.admin.FileUploadService;
import com.sso.service.admin.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户-个人详情接口
 *
 * @author 程序员小强
 */
@Slf4j
@RestController
@RequestMapping("/user/profile")
public class UserProfileController {

	@Autowired
	private UserService userService;

	@Resource
	private FileUploadService fileUploadService;

	/**
	 * 个人详情
	 */
	@RequestMapping("/detail")
	public ResultModel<UserProfileVO> getUserProfile() {
		LoginUserVO loginUser = SecurityUtils.getLoginUser();
		return ResultModel.success(userService.getUserProfile(loginUser.getUserId()));
	}

	/**
	 * 修改个人信息
	 *
	 * @param updateBO
	 */
	@PutMapping("/update")
	public ResultModel<?> updateProfile(@Valid @RequestBody UserUpdateProfileBO updateBO) {
		updateBO.setOperateBy(SecurityUtils.getOperateName());
		userService.updateProfile(updateBO);
		return ResultModel.success();
	}

	/**
	 * 修改个人密码
	 *
	 * @param updateBO
	 */
	@PutMapping("/updatePwd")
	public ResultModel<?> updatePwd(@Valid @RequestBody UserUpdatePwdBO updateBO) {
		updateBO.setOperateBy(SecurityUtils.getOperateName());
		userService.updatePwd(updateBO);
		return ResultModel.success();
	}

	/**
	 * 个人-头像上传
	 */
	@PostMapping("/avatar")
	public ResultModel<?> avatar(@RequestParam("avatarFile") MultipartFile avatarFile) {
		LoginUserVO loginUser = SecurityUtils.getLoginUser();
		String operateName = SecurityUtils.getOperateName();
		return ResultModel.success(fileUploadService.uploadUserAvatar(loginUser.getUserId(), operateName, avatarFile));
	}

}
