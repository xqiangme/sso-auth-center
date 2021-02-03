package com.sso.model.vo.menu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sso.common.utils.tree.TreeParentNode;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 菜单树-返回对象
 *
 * @author 程序员小强
 */
@Data
public class MenuTreeNodeVO implements Serializable, TreeParentNode<MenuTreeNodeVO> {

	private static final long serialVersionUID = 5815980115125373089L;

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

	/**
	 * 菜单类型（M目录 C菜单 F按钮）
	 */
	private String menuType;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;

	/**
	 * 显示顺序
	 */
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
	private String permissions;

	/**
	 * 使用类型 0-授权访问;1-开放访问;
	 */
	private Integer useType;

	/**
	 * 是否显示 0-显示;1-隐藏
	 */
	private Integer visible;

	/**
	 * 创建者
	 */
	private String createBy;

	/**
	 * 最后修改者
	 */
	private String updateBy;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;


	private final List<MenuTreeNodeVO> children = new ArrayList<>();

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
	public List<MenuTreeNodeVO> getChildTreeNodes() {
		return children;
	}


}
