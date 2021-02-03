/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.mapper;


import com.sso.dao.entity.SsoUser;
import com.sso.dao.query.UserPageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户基础mapper
 *
 * @author 程序员小强
 * @date 2021-01-02 17:20:41
 */
public interface SsoUserMapper {

	/**
	 * 根据 userId 查询
	 *
	 * @param userId 用户ID
	 * @return 用户信息
	 */
	SsoUser getByUserId(Long userId);

	/**
	 * 根据用户名-查询
	 *
	 * @param userName 用户
	 * @return 用户
	 */
	SsoUser getByUserName(String userName);

	/**
	 * 根据手机号-查询
	 *
	 * @param phone 用户手机号
	 * @return 用户
	 */
	SsoUser getByPhone(String phone);

	/**
	 * 根据邮箱-查询
	 *
	 * @param email 用户邮箱
	 * @return 用户
	 */
	SsoUser getByEmail(String email);

	/**
	 * 根据手机号或用户名-查询
	 *
	 * @param keywords 关键字
	 * @param limit
	 * @return 用户
	 */
	List<SsoUser> listByPhoneOrUserNameLike(@Param("keywords") String keywords, @Param("limit") Integer limit);

	/**
	 * 统计用户
	 *
	 * @param query 用户
	 * @return 用户集合
	 */
	int countByCondition(UserPageQuery query);

	/**
	 * 查询用户列表
	 *
	 * @param query 用户
	 * @return 用户集合
	 */
	List<SsoUser> listPageByCondition(UserPageQuery query);

	/**
	 * 根据自增主键id查询
	 *
	 * @param id 自增主键
	 * @return 用户实体
	 */
	SsoUser selectByPrimaryKey(Long id);

	/**
	 * 新增
	 *
	 * @param ssoUser 用户实体
	 * @return 新增的行数
	 */
	int insert(SsoUser ssoUser);

	/**
	 * 动态参数新增
	 *
	 * @param ssoUser 用户实体
	 * @return 新增的行数
	 */
	int insertSelective(SsoUser ssoUser);

	/**
	 * 根据 用户ID 编辑
	 *
	 * @param ssoUser 用户实体
	 * @return 编辑的行数
	 */
	int updateByUserId(SsoUser ssoUser);

	/**
	 * 修改个人信息
	 *
	 * @param ssoUser 用户实体
	 * @return 编辑的行数
	 */
	int updateProfileByUserId(SsoUser ssoUser);

	/**
	 * 更新
	 *
	 * @param userId        用户userId
	 * @param loginIp       用户loginIp
	 * @param lastLoginTime 登录时间
	 * @return 编辑的行数
	 */
	int updateLastLogin(@Param("userId") Long userId, @Param("loginIp") String loginIp, @Param("lastLoginTime") Date lastLoginTime);

	/**
	 * 修改密码
	 *
	 * @param userId   用户ID
	 * @param password 密码
	 * @param updateBy 最后修改人
	 * @return 编辑的行数
	 */
	int updatePwd(@Param("userId") Long userId, @Param("password") String password, @Param("updateBy") String updateBy);

	/**
	 * 修改头像
	 *
	 * @param userId   用户ID
	 * @param avatar   头像地址
	 * @param updateBy 最后修改人
	 * @return 编辑的行数
	 */
	int updateAvatar(@Param("userId") Long userId, @Param("avatar") String avatar, @Param("updateBy") String updateBy);

	/**
	 * 根据自增主键动态参数编辑
	 *
	 * @param ssoUser 用户实体
	 * @return 编辑的行数
	 */
	int updateByUserIdSelective(SsoUser ssoUser);


}
