package com.sso.model.bo.user;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户重置密码bo
 *
 * @author 程序员小强
 */
@Data
public class UserRestPwdBO extends BaseOperateBO {
	private static final long serialVersionUID = -3944856451297952013L;

	/**
	 * 用户ID
	 */
	@NotNull(message = "用户ID不为空")
	private Long userId;

	/**
	 * 密码
	 */
	@NotBlank(message = "密码不能为空")
	private String password;

}
