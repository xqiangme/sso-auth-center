package com.sso.model.bo.role;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 角色修改 bo
 *
 * @author 程序员小强
 */
@Data
public class RoleUpdateBO extends RoleSaveBO {

	private static final long serialVersionUID = -7337969581151948190L;

	/**
	 * 角色ID
	 */
	@NotNull(message = "角色ID")
	private Long roleId;


	@Override
	public String getLogValue() {
		return String.format("sysCode:%s , roleId:%d , roleName:%s , roleKey:%s ,operateBy:%s", this.getSysCode(), roleId, this.getRoleName(), this.getRoleKey(), this.getOperateBy());
	}
}
