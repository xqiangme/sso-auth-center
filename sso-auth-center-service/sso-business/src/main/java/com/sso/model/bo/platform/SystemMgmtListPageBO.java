package com.sso.model.bo.platform;

import com.sso.common.model.page.BasePageModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户-系统关系分页查询bo
 *
 * @author 程序员小强
 */
@Data
public class SystemMgmtListPageBO extends BasePageModel {

	private static final long serialVersionUID = -27397984110384822L;

	/**
	 * 系统编码
	 */
	@NotBlank(message = "系统编码不能为空")
	private String sysCode;

	/**
	 * 用户登录名
	 */
	private String usernameLike;

	/**
	 * 用户昵称
	 */
	private String nickNameLike;

	/**
	 * 真实姓名
	 */
	private String realNameLike;

	/**
	 * 手机号码
	 */
	private String phoneLike;

	/**
	 * 用户状态 0-正常;1-停用
	 */
	private Integer userStatus;

	/**
	 * 绑定关系状态 0-正常;1-停用
	 */
	private Integer relationStatus;

}
