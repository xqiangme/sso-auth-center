/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.mapper;


import com.sso.dao.entity.SsoRoleMenu;

import java.util.List;

/**
 * 角色和菜单关系基础mapper
 *
 * @author 程序员小强
 * @date 2021-01-08 18:03:32
 */
public interface SsoRoleMenuMapper {

	/**
	 * 根据自增主键id查询
	 *
	 * @param roleId 角色ID
	 * @return 角色和菜单关系
	 */
	List<Long> getMenuIdListByRoleId(Long roleId);

	/**
	 * 批量新增
	 *
	 * @param list
	 * @return 新增的行数
	 */
	int batchSave(List<SsoRoleMenu> list);


	/**
	 * 新增
	 *
	 * @param ssoRoleMenu 角色和菜单关系
	 * @return 新增的行数
	 */
	int insert(SsoRoleMenu ssoRoleMenu);

	/**
	 * 动态参数新增
	 *
	 * @param ssoRoleMenu 角色和菜单关系
	 * @return 新增的行数
	 */
	int insertSelective(SsoRoleMenu ssoRoleMenu);

	/**
	 * 根据 roleId 删除
	 *
	 * @param roleId 角色ID
	 * @return 删除的行数
	 */
	int deleteByRoleId(Long roleId);

	/**
	 * 根据 menuId 删除
	 *
	 * @param menuId 菜单ID
	 * @return 删除的行数
	 */
	int deleteByMenuId(Long menuId);

	/**
	 * 根据自增主键编辑
	 *
	 * @param ssoRoleMenu 角色和菜单关系
	 * @return 编辑的行数
	 */
	int updateByPrimaryKey(SsoRoleMenu ssoRoleMenu);

	/**
	 * 根据自增主键动态参数编辑
	 *
	 * @param ssoRoleMenu 角色和菜单关系
	 * @return 编辑的行数
	 */
	int updateByPrimaryKeySelective(SsoRoleMenu ssoRoleMenu);


}
