/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.mapper;

import com.sso.dao.entity.SsoMenu;
import com.sso.dao.query.SsoMenuQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单权限基础mapper
 *
 * @author 程序员小强
 * @date 2021-01-08 18:03:31
 */
public interface SsoMenuMapper {

	/**
	 * 根据 menuId 查询
	 *
	 * @param menuId 自增主键
	 * @return 菜单权限
	 */
	SsoMenu getByMenuId(Long menuId);

	/**
	 * 根据系统编码与角色名称查询
	 *
	 * @param sysCode  系统编码
	 * @param menuName 菜单名称
	 * @return 角色信息
	 */
	SsoMenu getBySysCodeAndName(@Param("sysCode") String sysCode, @Param("menuName") String menuName);

	/**
	 * 根据系统编码查询-未删除-并且启用的菜单
	 *
	 * @param sysCode
	 * @return 权限集
	 */
	List<SsoMenu> getEnableMenuListBySysCode(@Param("sysCode") String sysCode);

	/**
	 * 根据系统编码与用户ID查询权限查询-未删除-并且启用的菜单
	 *
	 * @param sysCode
	 * @param userId
	 * @return 权限集
	 */
	List<SsoMenu> getMenuListBySysCodeAndUserId(@Param("sysCode") String sysCode, @Param("userId") Long userId);

	/**
	 * 根据条件统计
	 *
	 * @param query 过滤条件
	 * @return 统计数
	 */
	int countByCondition(SsoMenuQuery query);

	/**
	 * 根据条件-查询
	 *
	 * @param query 过滤条件
	 * @return 统计数
	 */
	List<SsoMenu> listByCondition(SsoMenuQuery query);

	/**
	 * 新增
	 *
	 * @param ssoMenu 菜单权限
	 * @return 新增的行数
	 */
	int insert(SsoMenu ssoMenu);

	/**
	 * 动态参数新增
	 *
	 * @param ssoMenu 菜单权限
	 * @return 新增的行数
	 */
	int insertSelective(SsoMenu ssoMenu);

	/**
	 * 根据 菜单ID 编辑
	 *
	 * @param ssoMenu 菜单权限
	 * @return 编辑的行数
	 */
	int updateByMenuId(SsoMenu ssoMenu);

	/**
	 * 根据 菜单ID 动态参数编辑
	 *
	 * @param ssoMenu 菜单权限
	 * @return 编辑的行数
	 */
	int updateByMenuIdSelective(SsoMenu ssoMenu);

	/**
	 * 根据 系统编码 统计
	 *
	 * @param syCode
	 * @return 统计数
	 */
	int countBySysCode(@Param("sysCode") String syCode);


}
