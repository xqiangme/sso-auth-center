/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.mapper;

import com.sso.dao.entity.SsoUserSystem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户与目标系统关系基础mapper接口
 *
 * @author 程序员小强
 * @date 2021-01-10 22:22:34
 */
public interface SsoUserSystemMapper {

	/**
	 * 根据用户ID与 系统编码查询
	 *
	 * @param userId  用户ID
	 * @param sysCode 系统编码
	 * @return 用户与目标系统关系实体
	 */
	SsoUserSystem getByUserIdAndSysCode(@Param("userId") Long userId, @Param("sysCode") String sysCode);

	/**
	 * 根据用户ID集 与 系统编码查询
	 *
	 * @param userIdList 用户ID集
	 * @param sysCode    系统编码
	 * @return 用户与目标系统关系实体
	 */
	List<SsoUserSystem> listBySysCodeAndUserIdList(@Param("sysCode") String sysCode, @Param("userIdList") List<Long> userIdList);

	/**
	 * 新增接口
	 *
	 * @param ssoUserSystem 用户与目标系统关系实体
	 * @return 新增的行数
	 */
	int insert(SsoUserSystem ssoUserSystem);

	/**
	 * 动态参数新增接口
	 *
	 * @param ssoUserSystem 用户与目标系统关系实体
	 * @return 新增的行数
	 */
	int insertSelective(SsoUserSystem ssoUserSystem);

	/**
	 * 根据自增主键删除接口
	 *
	 * @param id 自增主键主键
	 * @return 删除的行数
	 */
	int deleteByPrimaryKey(Long id);

	/**
	 * 根据自增主键编辑接口
	 *
	 * @param ssoUserSystem 用户与目标系统关系实体
	 * @return 编辑的行数
	 */
	int updateById(SsoUserSystem ssoUserSystem);

	/**
	 * 根据自增主键动态参数编辑接口
	 *
	 * @param ssoUserSystem 用户与目标系统关系实体
	 * @return 编辑的行数
	 */
	int updateByIdSelective(SsoUserSystem ssoUserSystem);

	/**
	 * 根据自增主键动态参数编辑接口
	 *
	 * @param ssoUserSystem 用户与目标系统关系实体
	 * @return 编辑的行数
	 */
	int updateBySysCodeAndUserIdSelective(SsoUserSystem ssoUserSystem);

	/**
	 * 根据 系统编码 统计
	 *
	 * @param syCode
	 * @return 统计数
	 */
	int countBySysCode(@Param("sysCode") String syCode);


}
