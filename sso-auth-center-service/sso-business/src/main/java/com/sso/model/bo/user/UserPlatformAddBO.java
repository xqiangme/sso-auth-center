package com.sso.model.bo.user;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用户-平台关系-新增bo
 *
 * @author 程序员小强
 */
@Data
public class UserPlatformAddBO extends BaseOperateBO {

	private static final long serialVersionUID = -7111999878891732483L;

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
	 * 所属部门
	 */
	private Long deptId;

	/**
	 * 角色ID集
	 */
	@Size(min = 1, message = "请至少选择一个角色")
	private List<Long> roleIdList;

	public String getLogValue() {
		return String.format("sysCode:%s , userId:%d, deptId:%d , roleIdList:%s , operateBy:%s",
				this.sysCode, this.userId, this.deptId, this.roleIdList, this.getOperateBy());
	}
}
