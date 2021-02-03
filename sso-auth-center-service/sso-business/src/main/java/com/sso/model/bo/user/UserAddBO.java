package com.sso.model.bo.user;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用户新增bo
 *
 * @author 程序员小强
 */
@Data
public class UserAddBO extends BaseOperateBO {

	private static final long serialVersionUID = -8559961404756899677L;

	/**
	 * 系统编码
	 */
	@NotBlank(message = "系统编码不能为空")
	private String sysCode;

	/**
	 * 用户登录名
	 */
	@NotBlank(message = "用户登录名不为空")
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message = "用户登录密码不为空")
	@Size(max = 20, message = "密码最大长度{max}")
	private String password;

	/**
	 * 用户昵称
	 */
	private String nickName;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 所属部门
	 */
	private Long deptId;

	/**
	 * 角色ID集
	 */
	private List<Long> roleIdList;

	/**
	 * 用户性别 0-男;1-女;2-未知
	 */
	private Integer sex;

	/**
	 * 手机号码
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;

	/**
	 * 备注
	 */
	private String remarks;


	public String getLogValue() {
		return String.format("sysCode:%s , username:%s , realName:%s , phone:%s, deptId:%d , roleIdList:%s , operateBy:%s",
				this.sysCode, this.username, this.realName, this.phone, this.deptId, this.roleIdList, this.getOperateBy());
	}

}
