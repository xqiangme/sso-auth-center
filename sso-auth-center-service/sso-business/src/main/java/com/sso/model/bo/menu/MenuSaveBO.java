package com.sso.model.bo.menu;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 菜单添加 bo
 *
 * @author 程序员小强
 */
@Data
public class MenuSaveBO extends BaseOperateBO {

	private static final long serialVersionUID = -2219298721551456293L;

	/**
	 * 系统编码
	 */
	@NotBlank(message = "系统编码不能为空")
	private String sysCode;

	/**
	 * 菜单名称
	 */
	@NotBlank(message = "菜单名称不能为空")
	private String menuName;

	/**
	 * 菜单类型（M目录 C菜单 F按钮）
	 */
	@NotBlank(message = "菜单类型不能为空")
	private String menuType;

	/**
	 * 父菜单ID
	 */
	@NotNull(message = "父菜单ID不能为空")
	private Long menuParentId;

	/**
	 * 显示顺序
	 */
	@NotNull(message = "显示顺序不能为空")
	@Digits(integer = 999999999, fraction = 0, message = "显示顺序最大长度不能超过{integer}")
	private Integer sortNum;

	/**
	 * 菜单路由地址
	 */
	private String path;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 组件路径
	 */
	private String component;

	/**
	 * 权限标识,多个|分隔
	 */
	@Size(max = 10, message = "权限标识最多不超过{max}")
	private List<String> permissionList;

	/**
	 * 使用类型 0-授权访问;1-开放访问;
	 */
	private Integer useType;

	/**
	 * 是否显示 0-显示;1-隐藏
	 */
	private Integer visible;

	/**
	 * 菜单状态 0-正常;1停用
	 */
	private Integer status;

	/**
	 * 备注
	 */
	@Size(max = 200, message = "备注最大长度不超{max}")
	private String remarks;


	public String getLogValue() {
		return String.format("sysCode:%s , menuName:%s , menuParentId:%d ,permissionList:%s, operateBy:%s", this.sysCode, this.menuName, this.menuParentId,
				this.permissionList, this.getOperateBy());
	}


}
