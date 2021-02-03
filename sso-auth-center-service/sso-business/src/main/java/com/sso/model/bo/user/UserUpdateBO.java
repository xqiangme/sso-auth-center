package com.sso.model.bo.user;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户修改bo
 *
 * @author 程序员小强 Profile
 */
@Data
public class UserUpdateBO extends BaseOperateBO {

	private static final long serialVersionUID = -2437911052557089621L;

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
	 * 用户性别
	 */
	@NotNull(message = "用户性别不为空")
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
	@NotNull(message = "状态不为空")
	private Integer status;

	/**
	 * 备注
	 */
	private String remarks;

	public String getLogValue() {
		return String.format("sysCode:%s , userId:%d , realName:%s , phone:%s, deptId:%d , roleIdList:%s , operateBy:%s",
				this.sysCode, this.userId, this.realName, this.phone, this.deptId, this.roleIdList, this.getOperateBy());
	}

}
