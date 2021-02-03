package com.sso.model.bo.menu;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 菜单修改 bo
 *
 * @author 程序员小强
 */
@Data
public class MenuUpdateBO extends MenuSaveBO {

	private static final long serialVersionUID = 6016756261051760463L;

	/**
	 * 菜单ID
	 */
	@NotNull(message = "菜单ID不能为空")
	private Long menuId;


	@Override
	public String getLogValue() {
		return String.format("menuId:%d,sysCode:%s menuName:%s , menuParentId:%d ,permissionList:%s, operateBy:%s",
				this.menuId, this.getSysCode(), this.getMenuName(), this.getMenuParentId(), this.getPermissionList(), this.getOperateBy());
	}

}
