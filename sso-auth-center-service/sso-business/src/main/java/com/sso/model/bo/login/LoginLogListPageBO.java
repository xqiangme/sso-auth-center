package com.sso.model.bo.login;

import com.sso.common.model.page.BasePageModel;
import lombok.Data;

/**
 * 登录日志分页查询bo
 *
 * @author 程序员小强
 */
@Data
public class LoginLogListPageBO extends BasePageModel {

	private static final long serialVersionUID = -3442414830796499182L;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 用户登录名
	 */
	private String usernameLike;

	/**
	 * 登录状态 0-成功;1-失败
	 */
	private Integer status;

	/**
	 * 登录开始时间
	 */
	private String loginStartTime;

	/**
	 * 登录截止时间
	 */
	private String loginEndTime;

}
