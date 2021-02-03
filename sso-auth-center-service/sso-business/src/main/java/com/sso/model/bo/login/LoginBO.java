package com.sso.model.bo.login;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户登录对象
 *
 * @author 程序员小强
 */
@Data
public class LoginBO implements Serializable {

	private static final long serialVersionUID = 3516676046779777417L;

	/**
	 * 用户名
	 */
	@NotBlank(message = "用户名不为空")
	private String username;

	/**
	 * 用户密码
	 */
	@NotBlank(message = "用户密码不为空")
	private String password;

	/**
	 * 验证码
	 */
	@NotBlank(message = "验证码不为空")
	private String captchaCode;

	/**
	 * 请求唯一标识
	 */
	@NotBlank(message = "请求唯一标识不为空")
	private String requestId;

}
