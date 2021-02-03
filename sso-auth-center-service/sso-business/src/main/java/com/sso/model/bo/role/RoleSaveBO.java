package com.sso.model.bo.role;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 角色添加 bo
 *
 * @author 程序员小强
 */
@Data
public class RoleSaveBO extends BaseOperateBO {

	private static final long serialVersionUID = -2662983486894971210L;

	/**
	 * 系统编码
	 */
	@NotBlank(message = "系统编码不能为空")
	private String sysCode;

	/**
	 * 状态
	 */
	@NotNull(message = "状态不为空")
	private Integer status;

	/**
	 * 显示顺序
	 */
	@NotNull(message = "显示顺序不为空")
	private Integer sortNum;

	/**
	 * 角色名称
	 */
	@NotBlank(message = "角色名称不能为空")
	private String roleName;

	/**
	 * 角色key
	 */
	private String roleKey;

	/**
	 * 备注
	 */
	@Size(max = 200, message = "备注最大长度不超{max}")
	private String remarks;

	/**
	 * 菜单ID集
	 */
	private List<Long> menuIdList;

	public String getLogValue() {
		return String.format("sysCode:%s , roleName:%s , roleKey:%s ,operateBy:%s", this.sysCode, this.roleName, this.roleKey, this.getOperateBy());
	}
}
