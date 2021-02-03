package com.sso.model.vo.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户详情
 *
 * @author 程序员小强
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserOptionVO implements Serializable {

	private static final long serialVersionUID = -2168630461954434904L;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 用户登录名
	 */
	private String username;

	/**
	 * 用户昵称
	 */
	private String nickName;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 手机号码
	 */
	private String phone;

}
