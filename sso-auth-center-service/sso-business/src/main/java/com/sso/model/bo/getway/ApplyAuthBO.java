package com.sso.model.bo.getway;

import lombok.Data;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 申请认证参数
 *
 * @author 程序员小强
 * @version ApplyAuthBO.java
 */
@Data
public class ApplyAuthBO implements Serializable {

	private static final long serialVersionUID = -3755046680856409972L;

	/**
	 * 登录token
	 */
	private String ssoToken;

	/**
	 * 跳转url
	 */
	private String redirectUrl;

	/**
	 * 退出登录访问地址
	 */
	private String loginOutUrl;

	/**
	 * 菜单类型
	 * 1-列表; 2-树形列表
	 */
	@Pattern(regexp = "[1-2]", message = "菜单类型可选值为1~2")
	private String menuType;


}
