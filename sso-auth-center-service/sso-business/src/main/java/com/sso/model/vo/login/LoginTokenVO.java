package com.sso.model.vo.login;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录token 返回
 * @author 程序员小强
 */
@Data
public class LoginTokenVO implements Serializable {

	private static final long serialVersionUID = 226785375056951167L;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 请求标识
	 */
	private String requestId;

	/**
	 * token
	 */
	private String token;

	public LoginTokenVO(String username, String requestId, String token) {
		this.username = username;
		this.requestId = requestId;
		this.token = token;
	}

}
