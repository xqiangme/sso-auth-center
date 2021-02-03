/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.mapper;

import com.sso.dao.entity.SsoLoginLog;
import com.sso.dao.query.LoginLogQuery;

import java.util.List;

/**
 * 登录日志基础mapper接口
 *
 * @author 程序员小强
 * @date 2021-01-23 10:45:22
 */
public interface SsoLoginLogMapper {

	/**
	 * 根据 requestId 查询接口
	 *
	 * @param requestId 自增主键
	 * @return 登录日志实体
	 * @date 20-01-19 10:45:22
	 */
	SsoLoginLog getByRequestId(String requestId);

	/**
	 * 统计
	 *
	 * @param query
	 * @return 统计值
	 */
	int countByCondition(LoginLogQuery query);

	/**
	 * 查询列表
	 *
	 * @param query
	 * @return 日志列表
	 */
	List<SsoLoginLog> listPageByCondition(LoginLogQuery query);

	/**
	 * 新增接口
	 *
	 * @param ssoLoginLog 登录日志实体
	 * @return 新增的行数
	 * @date 2021-01-23 10:45:22
	 */
	int insert(SsoLoginLog ssoLoginLog);

	/**
	 * 动态参数新增接口
	 *
	 * @param ssoLoginLog 登录日志实体
	 * @return 新增的行数
	 * @date 2021-01-23 10:45:22
	 */
	int insertSelective(SsoLoginLog ssoLoginLog);

	/**
	 * 根据自增主键删除接口
	 *
	 * @param id 自增主键主键
	 * @return 删除的行数
	 * @date 2021-01-23 10:45:22
	 */
	int deleteByPrimaryKey(Long id);

	/**
	 * 根据自增主键编辑接口
	 *
	 * @param ssoLoginLog 登录日志实体
	 * @return 编辑的行数
	 * @date 2021-01-23 10:45:22
	 */
	int updateByPrimaryKey(SsoLoginLog ssoLoginLog);

	/**
	 * 根据自增主键动态参数编辑接口
	 *
	 * @param ssoLoginLog 登录日志实体
	 * @return 编辑的行数
	 * @date 2021-01-23 10:45:22
	 */
	int updateByPrimaryKeySelective(SsoLoginLog ssoLoginLog);


}
