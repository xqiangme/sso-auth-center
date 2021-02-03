package com.sso.model.vo.menu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sso.common.utils.tree.TreeParentNode;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单树-返回对象
 *
 * @author 程序员小强
 */
@Data
public class MenuOptionTreeNodeVO implements Serializable, TreeParentNode<MenuOptionTreeNodeVO> {

	private static final long serialVersionUID = 2794310462077211850L;

	/**
	 * 菜单ID
	 */
	private Long menuId;

	/**
	 * 父菜单ID
	 */
	private Long menuParentId;

	/**
	 * 菜单名称
	 */
	private String menuName;


	private final List<MenuOptionTreeNodeVO> children = new ArrayList<>();

	@JsonIgnore
	@Override
	public String getTreeNodeId() {
		return menuId.toString();
	}

	@JsonIgnore
	@Override
	public String getParentTreeNodeId() {
		return menuParentId.toString();
	}

	@JsonIgnore
	@Override
	public List<MenuOptionTreeNodeVO> getChildTreeNodes() {
		return children;
	}

}
