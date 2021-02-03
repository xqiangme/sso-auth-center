/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.mapper;


import com.sso.dao.dto.SsoUserDeptDTO;
import com.sso.dao.entity.SsoDept;
import com.sso.dao.query.SsoDeptQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门基础mapper
 *
 * @author 程序员小强
 * @date 2020-12-27 18:07:20
 */
public interface SsoDeptMapper {

	/**
	 * 根据 部门ID 查询
	 *
	 * @param deptId 部门ID
	 * @return 部门实体
	 */
	SsoDept getByDeptId(Long deptId);

	/**
	 * 根据 系统编码与用户ID 查询
	 *
	 * @param sysCode    用户ID
	 * @param userIdList 用户ID
	 * @return 部门实体
	 */
	List<SsoUserDeptDTO> getDeptBySysCodeAndUserIdList(@Param("sysCode") String sysCode, @Param("userIdList") List<Long> userIdList);

	/**
	 * 新增
	 *
	 * @param ssoDept 部门实体
	 * @return 新增的行数
	 */
	int insert(SsoDept ssoDept);

	/**
	 * 动态参数新增
	 *
	 * @param ssoDept 部门实体
	 * @return 新增的行数
	 */
	int insertSelective(SsoDept ssoDept);

	/**
	 * 根据自增主键编辑
	 *
	 * @param ssoDept 部门实体
	 * @return 编辑的行数
	 */
	int updateByDeptId(SsoDept ssoDept);

	/**
	 * 更新所有父级的状态
	 *
	 * @param ssoDept
	 * @return 编辑的行数
	 */
	int updateAllParentStatus(SsoDept ssoDept);

	/**
	 * 根据自增主键动态参数编辑
	 *
	 * @param ssoDept 部门实体
	 * @return 编辑的行数
	 */
	int updateByDeptIdSelective(SsoDept ssoDept);

	/**
	 * 根据条件-查询
	 *
	 * @param query 过滤条件
	 * @return 统计数
	 */
	List<SsoDept> listByCondition(SsoDeptQuery query);

	/**
	 * 根据条件统计
	 *
	 * @param query 过滤条件
	 * @return 统计数
	 */
	int countByCondition(SsoDeptQuery query);

	/**
	 * 根据父部门ID与状态统计
	 *
	 * @param deptParentId
	 * @param status
	 * @return 统计数
	 */
	int countByDeptParentIdAndStatus(@Param("deptParentId") Long deptParentId, @Param("status") Integer status);

	/**
	 * 根据 系统编码 统计
	 *
	 * @param syCode
	 * @return 统计数
	 */
	int countBySysCode(@Param("sysCode") String syCode);

}
