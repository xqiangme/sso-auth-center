/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.mapper;

import com.sso.dao.entity.SsoRole;
import com.sso.dao.query.SsoRoleQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色信息mapper
 *
 * @author 程序员小强
 * @date 2021-01-07 15:56:40
 */
public interface SsoRoleMapper {

	/**
	 * 根据 角色ID 查询
	 *
	 * @param roleId 角色ID
	 * @return 角色信息
	 */
	SsoRole getByRoleId(Long roleId);

	/**
	 * 根据系统编码与角色名称查询
	 *
	 * @param sysCode 系统编码
	 * @param roleName    角色名称
	 * @return 角色信息
	 */
	SsoRole getBySysCodeAndName(@Param("sysCode") String sysCode, @Param("roleName") String roleName);

	/**
	 * 根据系统编码与角色key查询
	 *
	 * @param sysCode 系统编码
	 * @param roleKey    角色名称
	 * @return 角色信息
	 */
	SsoRole getBySysCodeAndRoleKey(@Param("sysCode") String sysCode, @Param("roleKey") String roleKey);

	/**
	 * 根据 系统编码 与 用户ID查询角色key
	 *
	 * @param sysCode   系统编码
	 * @param userId    用户ID
	 * @return 角色信息
	 */
	List<String> getRoleKeyBySysCodeAndUserId(@Param("sysCode") String sysCode, @Param("userId") Long userId);

	/**
	 * 根据条件统计
	 *
	 * @param query 过滤条件
	 * @return 统计数
	 */
	int countByCondition(SsoRoleQuery query);

	/**
	 * 根据条件-查询
	 *
	 * @param query 过滤条件
	 * @return 统计数
	 */
	List<SsoRole> listPageByCondition(SsoRoleQuery query);

	/**
	 * 新增
	 *
	 * @param ssoRole 角色信息实体
	 * @return 新增的行数
	 */
	int insert(SsoRole ssoRole);

	/**
	 * 动态参数新增
	 *
	 * @param ssoRole 角色信息实体
	 * @return 新增的行数
	 */
	int insertSelective(SsoRole ssoRole);

	/**
	 * 根据自增主键编辑
	 *
	 * @param ssoRole 角色信息
	 * @return 编辑的行数
	 */
	int updateByRoleId(SsoRole ssoRole);

	/**
	 * 根据自增主键动态参数编辑
	 *
	 * @param ssoRole 角色信息
	 * @return 编辑的行数
	 */
	int updateByRoleIdSelective(SsoRole ssoRole);

	/**
	 * 根据 系统编码 统计
	 *
	 * @param syCode
	 * @return 统计数
	 */
	int countBySysCode(@Param("sysCode") String syCode);

}
