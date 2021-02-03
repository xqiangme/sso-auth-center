/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.service.admin;

import com.sso.model.vo.captcha.CaptchaVO;

import javax.servlet.http.HttpServletResponse;

/**
 * 验证码接口
 *
 * @author 程序员小强
 */
public interface CaptchaService {

	/**
	 * 生成验证码
	 *
	 * @param response
	 */
	CaptchaVO genCode(HttpServletResponse response);
}
