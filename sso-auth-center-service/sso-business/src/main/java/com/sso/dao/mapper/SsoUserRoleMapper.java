/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.mapper;

import com.sso.dao.dto.RoleCountDTO;
import com.sso.dao.dto.SsoUserRoleDTO;
import com.sso.dao.entity.SsoUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户和角色关联基础mapper
 *
 * @author 程序员小强
 * @date 2021-01-08 15:41:48
 */
public interface SsoUserRoleMapper {

	/**
	 * 根据自增主键id查询
	 *
	 * @param id 自增主键
	 * @return 用户和角色关联实体
	 */
	SsoUserRole selectByPrimaryKey(Long id);

	/**
	 * 根据系统编码 与用户ID 查询角色ID
	 *
	 * @param sysCode
	 * @param userId
	 * @return 角色ID集
	 */
	List<Long> getRoleIdListBySysCodeAndUserId(@Param("sysCode") String sysCode, @Param("userId") Long userId);

	/**
	 * 根据 系统编码与用户ID 查询
	 *
	 * @param sysCode    用户ID
	 * @param userIdList 用户ID
	 * @return 部门实体
	 */
	List<SsoUserRoleDTO> getRoleBySysCodeAndUserIdList(@Param("sysCode") String sysCode, @Param("userIdList") List<Long> userIdList);

	/**
	 * 新增
	 *
	 * @param ssoUserRole 用户和角色关联实体
	 * @return 新增的行数
	 */
	int insert(SsoUserRole ssoUserRole);

	/**
	 * 动态参数新增
	 *
	 * @param ssoUserRole 用户和角色关联实体
	 * @return 新增的行数
	 */
	int insertSelective(SsoUserRole ssoUserRole);

	/**
	 * 批量新增
	 *
	 * @param list
	 * @return
	 */
	int batchSave(List<SsoUserRole> list);

	/**
	 * 根据用户ID删除
	 *
	 * @param sysCode 系统编码
	 * @param userId  用户ID
	 * @return 删除的行数
	 */
	int deleteBySysCodeAndUserId(@Param("sysCode") String sysCode, @Param("userId") Long userId);

	/**
	 * 根据用户ID删除
	 *
	 * @param roleId 角色ID
	 * @param userId  用户ID
	 * @return 删除的行数
	 */
	int deleteByRoleIdAndUserId(@Param("roleId") Long roleId, @Param("userId") Long userId);

	/**
	 * 根据自增主键编辑
	 *
	 * @param ssoUserRole 用户和角色关联实体
	 * @return 编辑的行数
	 */
	int updateByPrimaryKey(SsoUserRole ssoUserRole);

	/**
	 * 根据自增主键动态参数编辑
	 *
	 * @param ssoUserRole 用户和角色关联实体
	 * @return 编辑的行数
	 */
	int updateByPrimaryKeySelective(SsoUserRole ssoUserRole);

	/**
	 * 根据 角色ID 统计
	 *
	 * @param roleId 角色ID
	 * @return 统计数
	 */
	int countByRoleId(Long roleId);

	/**
	 * 根据 角色ID 统计
	 *
	 * @param roleIdList 角色ID集
	 * @return 统计数
	 */
	List<RoleCountDTO> countByRoleIdList(@Param("roleIdList") List<Long> roleIdList);
}
