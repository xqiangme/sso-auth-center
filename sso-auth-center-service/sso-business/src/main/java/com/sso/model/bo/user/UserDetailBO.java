package com.sso.model.bo.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户查询详情bo
 *
 * @author 程序员小强
 */
@Data
public class UserDetailBO implements Serializable {

	private static final long serialVersionUID = 8048715426913745373L;

	/**
	 * 系统编码
	 */
	@NotBlank(message = "系统编码不能为空")
	private String sysCode;

	/**
	 * 用户ID
	 */
	@NotNull(message = "用户ID不为空")
	private Long userId;

}
