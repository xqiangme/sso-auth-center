package com.sso.service.admin;

import com.sso.common.model.result.ResultPageModel;
import com.sso.model.bo.user.*;
import com.sso.model.vo.user.UserDetailVO;
import com.sso.model.vo.user.UserOptionVO;
import com.sso.model.vo.user.UserPageVO;
import com.sso.model.vo.user.UserProfileVO;

import java.util.List;

/**
 * 用户管理接口
 *
 * @author 程序员小强
 */
public interface UserService {

	/**
	 * 用户分页列表
	 *
	 * @param pageBO
	 * @return 用户
	 */
	ResultPageModel<UserPageVO> listPageUser(UserListPageBO pageBO);

	/**
	 * 用户详情
	 *
	 * @param detailBO
	 * @return 用户详情
	 */
	UserDetailVO getUserDetail(UserDetailBO detailBO);

	/**
	 * 个人中心详情
	 *
	 * @param userId
	 * @return
	 */
	UserProfileVO getUserProfile(Long userId);

	/**
	 * 根据手机号查询用户
	 *
	 * @param phone
	 * @return 用户
	 */
	UserOptionVO getUserByPhone(String phone);

	/**
	 * 根据用户名查询用户
	 *
	 * @param username
	 * @return 用户
	 */
	UserOptionVO getUserByUserName(String username);

	/**
	 * 根据用户名或手机号查询用户列表
	 *
	 * @param keywords
	 * @return 用户
	 */
	List<UserOptionVO> listUserOption(String keywords);

	/**
	 * 添加用户
	 *
	 * @param addBO
	 */
	void addUser(UserAddBO addBO);

	/**
	 * 添加用户平台关系
	 *
	 * @param addBO
	 */
	void addUserPlatformRelation(UserPlatformAddBO addBO);

	/**
	 * 修改用户
	 *
	 * @param updateBO
	 */
	void updateUser(UserUpdateBO updateBO);

	/**
	 * 移除用户与系统关系
	 *
	 * @param removeBO
	 */
	void removeUserSystem(UserSystemRemoveBO removeBO);

	/**
	 * 修改个人信息
	 *
	 * @param updateBO
	 */
	void updateProfile(UserUpdateProfileBO updateBO);

	/**
	 * 修改个人密码
	 *
	 * @param updateBO
	 */
	void updatePwd(UserUpdatePwdBO updateBO);

	/**
	 * 重置密码
	 *
	 * @param restPwdBO
	 */
	void resetPwd(UserRestPwdBO restPwdBO);

	/**
	 * 删除用户
	 *
	 * @param deleteBO
	 */
	void deleteUser(UserDeleteBO deleteBO);


}
