/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.mapper;

import com.sso.dao.entity.SsoUserDept;
import org.apache.ibatis.annotations.Param;

/**
 * 用户部门关系基础mapper
 *
 * @author 程序员小强
 * @date 2021-01-02 17:20:41
 */
public interface SsoUserDeptMapper {

	/**
	 * 根据 系统编码与用户ID 查询
	 *
	 * @param sysCode 系统编码
	 * @param userId  用户ID
	 * @return 用户部门关系实体
	 */
	SsoUserDept getDeptBySysCodeAndUserId(@Param("sysCode") String sysCode, @Param("userId") Long userId);

	/**
	 * 新增
	 *
	 * @param ssoUserDept 用户部门关系实体
	 * @return 新增的行数
	 */
	int insert(SsoUserDept ssoUserDept);

	/**
	 * 动态参数新增
	 *
	 * @param ssoUserDept 用户部门关系实体
	 * @return 新增的行数
	 */
	int insertSelective(SsoUserDept ssoUserDept);

	/**
	 * 根据 系统编码与 用户ID 删除
	 *
	 * @param sysCode 系统编码
	 * @param userId  用户ID
	 * @return 删除的行数
	 */
	int deleteBySysCodeAndUserId(@Param("sysCode") String sysCode, @Param("userId") Long userId);

	/**
	 * 根据 系统编码与 部门ID 删除
	 *
	 * @param sysCode 系统编码
	 * @param deptId  部门ID
	 * @return 删除的行数
	 */
	int deleteBySysCodeAndDeptId(@Param("sysCode") String sysCode, @Param("deptId") Long deptId);

	/**
	 * 根据自增主键编辑
	 *
	 * @param ssoUserDept 用户部门关系实体
	 * @return 编辑的行数
	 */
	int updateByPrimaryKey(SsoUserDept ssoUserDept);

	/**
	 * 根据自增主键动态参数编辑
	 *
	 * @param ssoUserDept 用户部门关系实体
	 * @return 编辑的行数
	 */
	int updateByPrimaryKeySelective(SsoUserDept ssoUserDept);

	/**
	 * 根据部门ID统计
	 *
	 * @param deptId
	 * @return 统计行数
	 */
	int countByDeptId(Long deptId);

}
