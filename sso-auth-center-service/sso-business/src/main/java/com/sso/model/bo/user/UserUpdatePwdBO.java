package com.sso.model.bo.user;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 用户修改个人密码 bo
 *
 * @author 程序员小强
 */
@Data
public class UserUpdatePwdBO extends BaseOperateBO {

	private static final long serialVersionUID = -2947529017224375730L;

	/**
	 * 用户ID
	 */
	@NotNull(message = "用户ID不为空")
	private Long userId;

	/**
	 * 旧密码
	 */
	@NotBlank(message = "旧密码不为空")
	private String oldPassword;

	/**
	 * 新密码
	 */
	@NotBlank(message = "新密码不为空")
	@Size(max = 20, message = "密码最大长度{max}")
	private String newPassword;

}
