/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.mapper;


import com.sso.dao.dto.SsoUserMgmtDTO;
import com.sso.dao.entity.SsoSystemManager;
import com.sso.dao.query.SystemMgmtPageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 平台管理员关系基础mapper
 *
 * @author 程序员小强
 * @date 2021-01-16 10:22:57
 */
public interface SsoSystemManagerMapper {

	/**
	 * 根据自增主键id查询
	 *
	 * @param id 自增主键
	 * @return SsoSystemManager
	 * @date 2021-01-16 10:22:57
	 */
	SsoSystemManager getById(Long id);

	/**
	 * 根据-系统编码和用户ID查询
	 *
	 * @param sysCode 自增主键
	 * @param userId  自增主键
	 * @return SsoSystemManager
	 * @date 2021-01-16 10:22:57
	 */
	SsoSystemManager getBySysCodeAndUserId(@Param("sysCode") String sysCode, @Param("userId") Long userId);

	/**
	 * 根据-自增主键id统计
	 *
	 * @param sysCode 自增主键
	 * @param userId  自增主键
	 * @return SsoSystemManager
	 * @date 2021-01-16 10:22:57
	 */
	int countBySysCodeAndUserId(@Param("sysCode") String sysCode, @Param("userId") Long userId);

	/**
	 * 统计-平台管理员用户
	 *
	 * @param query 用户
	 * @return 用户集合
	 */
	int countSysMgmtUser(SystemMgmtPageQuery query);

	/**
	 * 查询平台管理员用户列表
	 *
	 * @param query 用户
	 * @return 用户集合
	 */
	List<SsoUserMgmtDTO> listPageSysMgmtUser(SystemMgmtPageQuery query);

	/**
	 * 新增
	 *
	 * @param ssoSystemManager
	 * @return 新增的行数
	 */
	int insert(SsoSystemManager ssoSystemManager);

	/**
	 * 动态参数新增
	 *
	 * @param ssoSystemManager
	 * @return 新增的行数
	 */
	int insertSelective(SsoSystemManager ssoSystemManager);

	/**
	 * 根据 自增ID 编辑
	 *
	 * @param ssoSystemManager
	 * @return 编辑的行数
	 */
	int updateById(SsoSystemManager ssoSystemManager);

	/**
	 * 根据用户ID与系统编码更新
	 *
	 * @param ssoSystemManager
	 * @return 编辑的行数
	 */
	int updateBySysCodeAndUserId(SsoSystemManager ssoSystemManager);

}
