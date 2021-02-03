/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.mapper;

import com.sso.dao.dto.SsoOnlineUserDTO;
import com.sso.dao.entity.SsoOnlineUser;
import com.sso.dao.query.UserOnlineQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 在线用户基础mapper接口
 *
 * @author 程序员小强
 * @date 2021-01-30
 */
public interface SsoOnlineUserMapper {

	/**
	 * 根据 requestId 查询接口
	 *
	 * @param requestId 请求标识
	 * @return 在线用户实体
	 * @date 2021-01-30 11:47:18
	 */
	SsoOnlineUser getByRequestId(String requestId);

	/**
	 * 新增接口
	 *
	 * @param ssoOnlineUser 在线用户实体
	 * @return 新增的行数
	 * @date 2021-01-30 11:47:18
	 */
	int insert(SsoOnlineUser ssoOnlineUser);

	/**
	 * 动态参数新增接口
	 *
	 * @param ssoOnlineUser 在线用户实体
	 * @return 新增的行数
	 * @date 2021-01-30 11:47:18
	 */
	int insertSelective(SsoOnlineUser ssoOnlineUser);

	/**
	 * 根据自增主键删除接口
	 *
	 * @param id 自增主键主键
	 * @return 删除的行数
	 * @date 2021-01-30 11:47:18
	 */
	int deleteByPrimaryKey(Long id);

	/**
	 * 根据自增主键编辑接口
	 *
	 * @param ssoOnlineUser 在线用户实体
	 * @return 编辑的行数
	 * @date 2021-01-30 11:47:18
	 */
	int updateByPrimaryKey(SsoOnlineUser ssoOnlineUser);

	/**
	 * 根据自增主键动态参数编辑接口
	 *
	 * @param ssoOnlineUser 在线用户实体
	 * @return 编辑的行数
	 * @date 2021-01-30 11:47:18
	 */
	int updateByPrimaryKeySelective(SsoOnlineUser ssoOnlineUser);

	/**
	 * 统计
	 *
	 * @param query
	 * @return 统计值
	 */
	int countByCondition(UserOnlineQuery query);

	/**
	 * 查询列表
	 *
	 * @param query
	 * @return 日志列表
	 */
	List<SsoOnlineUserDTO> listPageByCondition(UserOnlineQuery query);

	/**
	 * 根据请求标识-刷新效期时间
	 *
	 * @param requestId
	 * @param expireTime
	 * @return
	 */
	int refreshExpireTimeByRequestId(@Param("requestId") String requestId, @Param("expireTime") Long expireTime);

	/**
	 * 退出时-根据请求标识-更新效期时间
	 *
	 * @param requestId
	 * @param expireTime
	 * @param loginOutTime
	 * @return
	 */
	int updateByLogoOut(@Param("requestId") String requestId, @Param("expireTime") Long expireTime, @Param("loginOutTime") Long loginOutTime);

}
