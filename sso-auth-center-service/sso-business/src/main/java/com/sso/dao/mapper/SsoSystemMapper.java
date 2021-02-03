/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.mapper;


import com.sso.dao.entity.SsoSystem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 目标系统基础mapper
 *
 * @author 程序员小强
 * @date 2020-12-27 10:59:43
 */
public interface SsoSystemMapper {

	/**
	 * 根据系统编码-查询
	 *
	 * @param sysId 系统ID
	 * @return 目标系统实体
	 */
	SsoSystem getBySysId(@Param("sysId") Long sysId);

	/**
	 * 根据系统编码-查询
	 *
	 * @param sysCode 系统编码
	 * @return 目标系统实体
	 */
	SsoSystem getBySysCode(@Param("sysCode") String sysCode);

	/**
	 * 根据系统编码集-查询
	 *
	 * @param sysCodeList 系统编码集
	 * @return 目标系统实体
	 */
	List<SsoSystem> getBySysCodeList(@Param("sysCodeList") List<String> sysCodeList);

	/**
	 * 根据状态查询
	 *
	 * @param statusList
	 * @return 目标系统实体
	 */
	List<SsoSystem> listByStatusList(@Param("statusList") List<Integer> statusList);

	/**
	 * 根据用户ID查询-管理员权限的系统
	 *
	 * @param userId
	 * @return 目标系统实体
	 */
	List<SsoSystem> listMySystemByAdmin(@Param("userId") Long userId, @Param("statusList") List<Integer> statusList);

	/**
	 * 根据用户ID查询-拥有跳转权限的系统
	 *
	 * @param userId
	 * @return 目标系统实体
	 */
	List<SsoSystem> listMySystemByUserId(@Param("userId") Long userId);

	/**
	 * 根据用户ID查询-拥有管理权限的系统平台
	 *
	 * @param userId
	 * @return 目标系统实体
	 */
	List<SsoSystem> listSystemMgmtByUserId(@Param("userId") Long userId);

	/**
	 * 新增
	 *
	 * @param ssoSystem 目标系统实体
	 * @return 新增的行数
	 */
	int insert(SsoSystem ssoSystem);

	/**
	 * 动态参数新增
	 *
	 * @param ssoSystem 目标系统实体
	 * @return 新增的行数
	 */
	int insertSelective(SsoSystem ssoSystem);

	/**
	 * 根据 sysId 编辑
	 *
	 * @param ssoSystem 目标系统实体
	 * @return 编辑的行数
	 */
	int updateBySysId(SsoSystem ssoSystem);

	/**
	 * 根据 sysId 修改秘钥属性
	 *
	 * @param ssoSystem 目标系统实体
	 * @return 编辑的行数
	 */
	int updateSecretBySysId(SsoSystem ssoSystem);

	/**
	 * 修改图标
	 *
	 * @param sysId    系统ID
	 * @param sysIcon  头像地址
	 * @param updateBy 最后修改人
	 * @return 编辑的行数
	 */
	int updateIconBySysId(@Param("sysId") Long sysId, @Param("sysIcon") String sysIcon, @Param("updateBy") String updateBy);

	/**
	 * 根据自增主键动态参数编辑
	 *
	 * @param ssoSystem 目标系统实体
	 * @return 编辑的行数
	 */
	int updateBySysIdSelective(SsoSystem ssoSystem);


}
