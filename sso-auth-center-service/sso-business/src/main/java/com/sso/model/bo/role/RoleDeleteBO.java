package com.sso.model.bo.role;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 角色删除bo
 *
 * @author 程序员小强
 */
@Data
public class RoleDeleteBO extends BaseOperateBO {

	private static final long serialVersionUID = -4350853964122690858L;

	/**
	 * 角色ID
	 */
	@NotNull(message = "角色ID不为空")
	private Long roleId;


	public String getLogValue() {
		return String.format("roleId:%d , operateBy:%s", this.roleId, this.getOperateBy());
	}
}
