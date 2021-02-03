package com.sso.model.bo.user;

import com.sso.common.model.page.BasePageModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户分页查询bo
 *
 * @author 程序员小强
 */
@Data
public class UserListPageBO extends BasePageModel {

	private static final long serialVersionUID = 4627460201144477292L;

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
	 * 所属部门ID
	 */
	private Long deptId;

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;


}
