package com.sso.model.bo.menu;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 菜单删除 bo
 *
 * @author 程序员小强
 */
@Data
public class MenuDeleteBO extends BaseOperateBO {

	private static final long serialVersionUID = -8354384638349511426L;

	/**
	 * 菜单ID
	 */
	@NotNull(message = "菜单ID不能为空")
	private Long menuId;


	public String getLogValue() {
		return String.format("menuId:%d, operateBy:%s",
				this.menuId, this.getOperateBy());
	}

}
