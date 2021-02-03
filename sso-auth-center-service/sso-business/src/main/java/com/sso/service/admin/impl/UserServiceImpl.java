package com.sso.service.admin.impl;

import com.sso.common.constant.SsoConstants;
import com.sso.common.enums.DelFlagEnum;
import com.sso.common.enums.exception.UserResStatusEnum;
import com.sso.common.exception.BusinessException;
import com.sso.common.model.BaseOperateBO;
import com.sso.common.model.result.ResultPageModel;
import com.sso.common.utils.SecurityUtils;
import com.sso.common.utils.StringUtils;
import com.sso.common.utils.bean.BeanCopierUtil;
import com.sso.dao.dto.SsoUserDeptDTO;
import com.sso.dao.dto.SsoUserRoleDTO;
import com.sso.dao.entity.*;
import com.sso.dao.mapper.*;
import com.sso.dao.query.UserPageQuery;
import com.sso.model.bo.user.*;
import com.sso.model.vo.user.UserDetailVO;
import com.sso.model.vo.user.UserOptionVO;
import com.sso.model.vo.user.UserPageVO;
import com.sso.model.vo.user.UserProfileVO;
import com.sso.service.admin.UserService;
import com.sso.service.base.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户管理接口
 *
 * @author 程序员小强
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

	@Resource
	private SsoUserMapper ssoUserMapper;
	@Resource
	private SsoDeptMapper ssoDeptMapper;
	@Resource
	private SsoUserDeptMapper ssoUserDeptMapper;
	@Resource
	private SsoUserRoleMapper ssoUserRoleMapper;
	@Resource
	private SysConfigService sysConfigService;
	@Resource
	private SsoUserSystemMapper ssoUserSystemMapper;
	@Resource
	private SsoSystemManagerMapper ssoSystemManagerMapper;

	/**
	 * 用户分页列表
	 *
	 * @param pageBO
	 * @return 用户
	 */
	@Override
	public ResultPageModel<UserPageVO> listPageUser(UserListPageBO pageBO) {
		//构建参数参数
		UserPageQuery query = this.buildUserPageQuery(pageBO);

		//统计行数
		int total = ssoUserMapper.countByCondition(query);
		if (total <= 0) {
			return ResultPageModel.defaultValue();
		}

		//分页查询用户
		List<SsoUser> userList = ssoUserMapper.listPageByCondition(query);
		if (CollectionUtils.isEmpty(userList)) {
			return ResultPageModel.defaultValue();
		}

		//提取用户ID
		List<Long> userIdList = userList.stream().map(SsoUser::getUserId).collect(Collectors.toList());

		//用户系统关系
		Map<Long, SsoUserSystem> userSystemMap = this.getUserSystemMap(pageBO.getSysCode(), userIdList);

		//用户部门关系Map
		Map<Long, String> userDeptMap = this.getUserDeptMap(pageBO.getSysCode(), userIdList);

		//用户角色关系Map
		Map<Long, String> userRoleMap = this.getUserRolesMap(pageBO.getSysCode(), userIdList);

		//组装结果返回
		return ResultPageModel.success(this.buildUserListResult(userList, userSystemMap, userDeptMap, userRoleMap), total);
	}

	/**
	 * 用户详情
	 *
	 * @param detailBO
	 * @return 用户详情
	 */
	@Override
	public UserDetailVO getUserDetail(UserDetailBO detailBO) {
		SsoUser ssoUser = ssoUserMapper.getByUserId(detailBO.getUserId());
		if (null == ssoUser) {
			throw new BusinessException(UserResStatusEnum.USER_NOT_EXISTS);
		}

		UserDetailVO result = BeanCopierUtil.copy(ssoUser, UserDetailVO.class);
		result.setSysCode(detailBO.getSysCode());

		//所属部门
		SsoUserDept ssoUserDept = ssoUserDeptMapper.getDeptBySysCodeAndUserId(detailBO.getSysCode(), detailBO.getUserId());
		if (null != ssoUserDept) {
			result.setDeptId(ssoUserDept.getDeptId());
		}

		//用户角色
		List<Long> roleIdList = ssoUserRoleMapper.getRoleIdListBySysCodeAndUserId(detailBO.getSysCode(), detailBO.getUserId());
		if (!CollectionUtils.isEmpty(roleIdList)) {
			result.setRoleIdList(roleIdList);
		} else {
			result.setRoleIdList(new ArrayList<>(0));
		}
		return result;
	}

	/**
	 * 个人中心详情
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public UserProfileVO getUserProfile(Long userId) {
		SsoUser ssoUser = ssoUserMapper.getByUserId(userId);
		if (null == ssoUser) {
			throw new BusinessException(UserResStatusEnum.USER_NOT_EXISTS);
		}

		return BeanCopierUtil.copy(ssoUser, UserProfileVO.class);
	}

	/**
	 * 根据手机号查询用户
	 *
	 * @param phone
	 * @return 用户
	 */
	@Override
	public UserOptionVO getUserByPhone(String phone) {
		SsoUser ssoUser = ssoUserMapper.getByPhone(phone);
		if (null == ssoUser) {
			return null;
		}
		return BeanCopierUtil.copy(ssoUser, UserOptionVO.class);
	}

	/**
	 * 根据用户名查询用户
	 *
	 * @param username
	 * @return 用户
	 */
	@Override
	public UserOptionVO getUserByUserName(String username) {
		SsoUser ssoUser = ssoUserMapper.getByUserName(username);
		if (null == ssoUser) {
			return null;
		}
		return BeanCopierUtil.copy(ssoUser, UserOptionVO.class);
	}

	/**
	 * 根据用户名或手机号查询用户列表
	 *
	 * @param keywords
	 * @return 用户
	 */
	@Override
	public List<UserOptionVO> listUserOption(String keywords) {
		List<SsoUser> userList = ssoUserMapper.listByPhoneOrUserNameLike(keywords, SsoConstants.OPTION_LIMIT);
		if (CollectionUtils.isEmpty(userList)) {
			return new ArrayList<>();
		}

		return BeanCopierUtil.copyList(userList, UserOptionVO.class);
	}

	/**
	 * 添加用户
	 *
	 * @param addBO
	 */
	@Override
	public void addUser(UserAddBO addBO) {
		//校验用户名唯一性
		this.checkUserNameUniq(addBO.getUsername());

		//校验手机号唯一性
		this.checkPhoneUniq(addBO.getPhone());

		//校验邮箱唯一性
		this.checkEmailUniq(addBO.getEmail());

		//执行新增用户
		SsoUser ssoUser = this.doInsertUser(addBO);

		//添加用户与当前系统关系
		this.addUserSystemRelation(addBO.getSysCode(), ssoUser.getUserId(), addBO);

		//新增用户部门关系
		this.doInsertUserDeptRelation(addBO.getSysCode(), ssoUser.getUserId(), addBO.getDeptId());

		//新增用户角色关系
		this.doInsertUserRoleRelation(addBO.getSysCode(), ssoUser.getUserId(), addBO.getRoleIdList());

		log.info("[ 用户 ] >> [ 新增完成 ] >> {}", addBO.getLogValue());
	}

	/**
	 * 添加用户平台关系
	 *
	 * @param addBO
	 */
	@Override
	public void addUserPlatformRelation(UserPlatformAddBO addBO) {
		//删除原先的部门关系
		ssoUserDeptMapper.deleteBySysCodeAndUserId(addBO.getSysCode(), addBO.getUserId());
		//删除原先的用户角色关系
		ssoUserRoleMapper.deleteBySysCodeAndUserId(addBO.getSysCode(), addBO.getUserId());

		//添加用户与当前系统关系
		this.addUserSystemRelation(addBO.getSysCode(), addBO.getUserId(), addBO);

		//新增用户部门关系
		this.doInsertUserDeptRelation(addBO.getSysCode(), addBO.getUserId(), addBO.getDeptId());

		//新增用户角色关系
		this.doInsertUserRoleRelation(addBO.getSysCode(), addBO.getUserId(), addBO.getRoleIdList());

		log.info("[ 用户 ] >> [ 添加用户平台关系完成 ] >> {}", addBO.getLogValue());
	}

	/**
	 * 修改用户
	 *
	 * @param updateBO
	 */
	@Override
	public void updateUser(UserUpdateBO updateBO) {
		//校验超管账号不可修改
		this.checkSupperAdminUser(updateBO.getUserId());

		//排除自身-校验手机号唯一性
		this.checkPhoneUniq(updateBO.getPhone(), updateBO.getUserId());

		//排除自身-校验邮箱唯一性
		this.checkEmailUniq(updateBO.getEmail(), updateBO.getUserId());

		//执行修改用户
		SsoUser ssoUser = this.doUpdateUser(updateBO);

		//删除原先的部门关系
		ssoUserDeptMapper.deleteBySysCodeAndUserId(updateBO.getSysCode(), updateBO.getUserId());
		//删除原先的用户角色关系
		ssoUserRoleMapper.deleteBySysCodeAndUserId(updateBO.getSysCode(), updateBO.getUserId());

		//新增用户部门关系
		this.doInsertUserDeptRelation(updateBO.getSysCode(), ssoUser.getUserId(), updateBO.getDeptId());

		//新增用户角色关系
		this.doInsertUserRoleRelation(updateBO.getSysCode(), ssoUser.getUserId(), updateBO.getRoleIdList());

		//添加了部门或者角色情况下-为用户建立与当前平台的关系
		if (!CollectionUtils.isEmpty(updateBO.getRoleIdList()) || null != updateBO.getDeptId()) {
			//添加用户与当前系统关系
			this.addUserSystemRelation(updateBO.getSysCode(), ssoUser.getUserId(), updateBO);
		}

		log.info("[ 用户 ] >> [ 修改平台关系完成 ] >> {}", updateBO.getLogValue());
	}

	/**
	 * 移除用户与系统关系
	 *
	 * @param removeBO
	 */
	@Override
	public void removeUserSystem(UserSystemRemoveBO removeBO) {
		//删除用户系统权限
		this.doDeleteUserSystem(removeBO.getSysCode(), removeBO.getUserId(), removeBO);

		//删除部门关系
		ssoUserDeptMapper.deleteBySysCodeAndUserId(removeBO.getSysCode(), removeBO.getUserId());

		//删除用户角色关系
		ssoUserRoleMapper.deleteBySysCodeAndUserId(removeBO.getSysCode(), removeBO.getUserId());

		//删除用户管理员权限
		this.doDeleteUserSystemManager(removeBO.getSysCode(), removeBO.getUserId(), removeBO);

		log.info("[ 用户 ] >> [ 移除用户与系统关系 ] >> {}", removeBO.getLogValue());
	}

	/**
	 * 修改个人信息
	 *
	 * @param updateBO
	 */
	@Override
	public void updateProfile(UserUpdateProfileBO updateBO) {
		//排除自身-校验手机号唯一性
		this.checkPhoneUniq(updateBO.getPhone(), updateBO.getUserId());

		//排除自身-校验邮箱唯一性
		this.checkEmailUniq(updateBO.getEmail(), updateBO.getUserId());

		//执行修改用户个人信息
		this.doUpdateUserProfile(updateBO);

		log.info("[ 用户 ] >> [ 修改个人信息 ] >> {}", updateBO.getLogValue());

	}

	/**
	 * 修改个人密码
	 *
	 * @param updateBO
	 */
	@Override
	public void updatePwd(UserUpdatePwdBO updateBO) {
		SsoUser ssoUser = ssoUserMapper.getByUserId(updateBO.getUserId());
		if (null == ssoUser) {
			return;
		}
		if (DelFlagEnum.DELETED.getStatus().equals(ssoUser.getDelFlag())) {
			return;
		}
		//修改密码失败，旧密码错误
		if (!SecurityUtils.matchesPassword(updateBO.getOldPassword(), ssoUser.getPassword())) {
			throw new BusinessException(UserResStatusEnum.OLD_PWD_ERROR);
		}
		//新密码不能与旧密码相同
		if (SecurityUtils.matchesPassword(updateBO.getNewPassword(), ssoUser.getPassword())) {
			throw new BusinessException(UserResStatusEnum.OLD_PWD_EVAL_NEW_PWD);
		}

		String newPassword = SecurityUtils.encryptPassword(updateBO.getNewPassword());
		ssoUserMapper.updatePwd(updateBO.getUserId(), newPassword, updateBO.getOperateBy());

		log.info("[ 用户 ] >> [ 修改个人密码 ] >> userId:{}", updateBO.getUserId());
	}

	/**
	 * 重置密码
	 *
	 * @param restPwdBO
	 */
	@Override
	public void resetPwd(UserRestPwdBO restPwdBO) {
		//校验超管账号不可修改
		this.checkSupperAdminUser(restPwdBO.getUserId());

		SsoUser ssoUser = new SsoUser();
		ssoUser.setUserId(restPwdBO.getUserId());
		ssoUser.setPassword(SecurityUtils.encryptPassword(restPwdBO.getPassword()));
		ssoUser.setUpdateBy(restPwdBO.getOperateBy());
		ssoUserMapper.updateByUserIdSelective(ssoUser);

		log.info("[ 用户 ] >> [ 重置密码 ] >> userId:{} , operateBy:{}", restPwdBO.getUserId(), restPwdBO.getOperateBy());

	}

	/**
	 * 删除用户
	 *
	 * @param deleteBO
	 */
	@Override
	public void deleteUser(UserDeleteBO deleteBO) {
		for (Long userId : deleteBO.getUserIdList()) {
			//校验超管账号不可删除
			this.checkSupperAdminUser(userId);

			SsoUser ssoUser = ssoUserMapper.getByUserId(userId);
			if (null == ssoUser) {
				return;
			}
			if (DelFlagEnum.DELETED.getStatus().equals(ssoUser.getDelFlag())) {
				return;
			}

			//标记用户为删除状态
			this.doDeleteUser(deleteBO, userId);

			//删除用户系统权限
			this.doDeleteUserSystem(deleteBO.getSysCode(), userId, deleteBO);

			//删除部门关系
			ssoUserDeptMapper.deleteBySysCodeAndUserId(deleteBO.getSysCode(), userId);

			//删除用户角色关系
			ssoUserRoleMapper.deleteBySysCodeAndUserId(deleteBO.getSysCode(), userId);

			//删除用户管理员权限
			this.doDeleteUserSystemManager(deleteBO.getSysCode(), userId, deleteBO);

			log.info("[ 用户 ] >> [ 删除用户 ] >> userId:{} , operateBy:{}", userId, deleteBO.getOperateBy());
		}
	}

	/**
	 * 添加用户-系统关系
	 *
	 * @param sysCode
	 * @param userId
	 * @param operateBO
	 */
	public void addUserSystemRelation(String sysCode, Long userId, BaseOperateBO operateBO) {
		SsoUserSystem ssoUserSystem = ssoUserSystemMapper.getByUserIdAndSysCode(userId, sysCode);
		if (null != ssoUserSystem) {
			return;
		}

		SsoUserSystem userSystem = new SsoUserSystem();
		userSystem.setUserId(userId);
		userSystem.setSysCode(sysCode);
		userSystem.setCreateBy(operateBO.getOperateBy());
		userSystem.setUpdateBy(operateBO.getOperateBy());
		ssoUserSystemMapper.insertSelective(userSystem);
	}

	/**
	 * 标记为用户删除态
	 *
	 * @param deleteBO
	 */
	private void doDeleteUser(UserDeleteBO deleteBO, Long userId) {
		SsoUser ssoUser = new SsoUser();
		ssoUser.setUserId(userId);
		ssoUser.setDelFlag(DelFlagEnum.DELETED.getStatus());
		ssoUser.setUpdateBy(deleteBO.getOperateBy());
		ssoUserMapper.updateByUserIdSelective(ssoUser);
	}

	/**
	 * 删除用户系统关系
	 *
	 * @param operateBO
	 */
	private void doDeleteUserSystem(String sysCode, Long userId, BaseOperateBO operateBO) {
		SsoUserSystem ssoUserSystem = new SsoUserSystem();
		ssoUserSystem.setUserId(userId);
		ssoUserSystem.setSysCode(sysCode);
		ssoUserSystem.setUpdateBy(operateBO.getOperateBy());
		ssoUserSystem.setDelFlag(DelFlagEnum.DELETED.getStatus());
		ssoUserSystemMapper.updateBySysCodeAndUserIdSelective(ssoUserSystem);
	}

	/**
	 * 删除用户管理权限关系
	 *
	 * @param operateBO
	 */
	private void doDeleteUserSystemManager(String sysCode, Long userId, BaseOperateBO operateBO) {
		SsoSystemManager updateEntity = new SsoSystemManager();
		updateEntity.setUserId(userId);
		updateEntity.setSysCode(sysCode);
		updateEntity.setDelFlag(DelFlagEnum.DELETED.getStatus());
		updateEntity.setUpdateBy(operateBO.getOperateBy());
		ssoSystemManagerMapper.updateBySysCodeAndUserId(updateEntity);
	}

	/**
	 * 新增用户部门关系记录
	 */
	private void doInsertUserDeptRelation(String sysCode, Long userId, Long deptId) {
		if (null == userId || null == deptId) {
			return;
		}
		SsoUserDept ssoUserDept = new SsoUserDept();
		ssoUserDept.setSysCode(sysCode);
		ssoUserDept.setUserId(userId);
		ssoUserDept.setDeptId(deptId);
		ssoUserDeptMapper.insert(ssoUserDept);
	}

	/**
	 * 新增用户角色关系记录
	 */
	private void doInsertUserRoleRelation(String sysCode, Long userId, List<Long> roleIdList) {
		if (null == userId || CollectionUtils.isEmpty(roleIdList)) {
			return;
		}

		SsoUserRole ssoUserRole = null;
		List<SsoUserRole> saveList = new ArrayList<>(roleIdList.size());
		for (Long roleId : roleIdList) {
			ssoUserRole = new SsoUserRole();
			ssoUserRole.setSysCode(sysCode);
			ssoUserRole.setUserId(userId);
			ssoUserRole.setRoleId(roleId);
			saveList.add(ssoUserRole);
		}

		if (CollectionUtils.isEmpty(saveList)) {
			return;
		}

		ssoUserRoleMapper.batchSave(saveList);
	}

	/**
	 * 组装结果集
	 *
	 * @param userList
	 * @param userDeptMap
	 * @return
	 */
	private List<UserPageVO> buildUserListResult(List<SsoUser> userList,
												 Map<Long, SsoUserSystem> userSystemMap,
												 Map<Long, String> userDeptMap, Map<Long, String> userRoleMap) {
		UserPageVO pageItem = null;
		List<UserPageVO> resultList = new ArrayList<>(userList.size());
		Long supperAdminUserId = sysConfigService.getSupperAdminUserId();
		for (SsoUser ssoUser : userList) {
			pageItem = BeanCopierUtil.copy(ssoUser, UserPageVO.class);
			pageItem.setDeptName(userDeptMap.getOrDefault(ssoUser.getUserId(), ""));
			pageItem.setRoleNames(userRoleMap.getOrDefault(ssoUser.getUserId(), ""));
			pageItem.setSupperAdminFlag(ssoUser.getUserId().equals(supperAdminUserId));
			pageItem.setSysBindFlag(false);
			if (userSystemMap.containsKey(ssoUser.getUserId())) {
				pageItem.setSysBindFlag(true);
				//修改添加人为-本系统的关键建立人
				SsoUserSystem ssoUserSystem = userSystemMap.get(ssoUser.getUserId());
				pageItem.setCreateBy(ssoUserSystem.getCreateBy());
				pageItem.setCreateTime(ssoUserSystem.getCreateTime());
			}
			resultList.add(pageItem);
		}
		return resultList;
	}

	/**
	 * 用户部门关系Map
	 *
	 * @param userIdList
	 */
	private Map<Long, String> getUserDeptMap(String sysCode, List<Long> userIdList) {
		if (StringUtils.isEmpty(sysCode) || CollectionUtils.isEmpty(userIdList)) {
			return new HashMap<>(0);
		}
		List<SsoUserDeptDTO> deptByUserIdList = ssoDeptMapper.getDeptBySysCodeAndUserIdList(sysCode, userIdList);
		if (CollectionUtils.isEmpty(deptByUserIdList)) {
			return new HashMap<>(0);
		}
		return deptByUserIdList.stream().collect(Collectors.toMap(SsoUserDeptDTO::getUserId, SsoUserDeptDTO::getDeptName, (k1, k2) -> k1));
	}


	/**
	 * 用户角色关系Map
	 *
	 * @param userIdList
	 */
	private Map<Long, String> getUserRolesMap(String sysCode, List<Long> userIdList) {
		if (StringUtils.isEmpty(sysCode) || CollectionUtils.isEmpty(userIdList)) {
			return new HashMap<>(0);
		}
		List<SsoUserRoleDTO> roleList = ssoUserRoleMapper.getRoleBySysCodeAndUserIdList(sysCode, userIdList);
		if (CollectionUtils.isEmpty(roleList)) {
			return new HashMap<>(0);
		}
		return roleList.stream().collect(Collectors.toMap(SsoUserRoleDTO::getUserId, SsoUserRoleDTO::getRoleNames, (k1, k2) -> k1));
	}


	/**
	 * 用户系统关系Map
	 *
	 * @param userIdList
	 */
	private Map<Long, SsoUserSystem> getUserSystemMap(String sysCode, List<Long> userIdList) {
		if (StringUtils.isEmpty(sysCode) || CollectionUtils.isEmpty(userIdList)) {
			return new HashMap<>(0);
		}
		List<SsoUserSystem> systemList = ssoUserSystemMapper.listBySysCodeAndUserIdList(sysCode, userIdList);
		if (CollectionUtils.isEmpty(systemList)) {
			return new HashMap<>(0);
		}
		return systemList.stream().collect(Collectors.toMap(SsoUserSystem::getUserId, Function.identity(), (k1, k2) -> k1));
	}


	/**
	 * 执行新增用户
	 *
	 * @param addBO
	 */
	private SsoUser doInsertUser(UserAddBO addBO) {
		SsoUser ssoUser = new SsoUser();
		ssoUser.setUsername(addBO.getUsername());
		ssoUser.setNickName(addBO.getNickName());
		ssoUser.setRealName(addBO.getRealName());
		ssoUser.setSex(addBO.getSex());
		ssoUser.setPhone(addBO.getPhone());
		ssoUser.setEmail(addBO.getEmail());
		ssoUser.setStatus(addBO.getStatus());
		ssoUser.setRemarks(addBO.getRemarks());
		ssoUser.setCreateBy(addBO.getOperateBy());
		ssoUser.setUpdateBy(addBO.getOperateBy());
		//密码加密处理
		ssoUser.setPassword(SecurityUtils.encryptPassword(addBO.getPassword()));
		ssoUserMapper.insertSelective(ssoUser);
		return ssoUser;
	}

	/**
	 * 执行修改用户
	 *
	 * @param updateBO
	 */
	private SsoUser doUpdateUser(UserUpdateBO updateBO) {
		SsoUser ssoUser = new SsoUser();
		ssoUser.setUserId(updateBO.getUserId());
		ssoUser.setNickName(StringUtils.dealBlankDefault(updateBO.getNickName(), ""));
		ssoUser.setRealName(StringUtils.dealBlankDefault(updateBO.getRealName(), ""));
		ssoUser.setSex(updateBO.getSex());
		ssoUser.setStatus(updateBO.getStatus());
		ssoUser.setPhone(StringUtils.dealBlankDefault(updateBO.getPhone(), ""));
		ssoUser.setEmail(StringUtils.dealBlankDefault(updateBO.getEmail(), ""));
		ssoUser.setRemarks(StringUtils.dealBlankDefault(updateBO.getRemarks(), ""));
		ssoUser.setUpdateBy(updateBO.getOperateBy());
		ssoUserMapper.updateByUserId(ssoUser);
		return ssoUser;
	}


	/**
	 * 执行修改用户个人信息
	 *
	 * @param updateBO
	 */
	private void doUpdateUserProfile(UserUpdateProfileBO updateBO) {
		SsoUser ssoUser = new SsoUser();
		ssoUser.setUserId(updateBO.getUserId());
		ssoUser.setNickName(StringUtils.dealBlankDefault(updateBO.getNickName(), ""));
		ssoUser.setRealName(StringUtils.dealBlankDefault(updateBO.getRealName(), ""));
		ssoUser.setSex(updateBO.getSex());
		ssoUser.setPhone(StringUtils.dealBlankDefault(updateBO.getPhone(), ""));
		ssoUser.setEmail(StringUtils.dealBlankDefault(updateBO.getEmail(), ""));
		ssoUser.setUpdateBy(updateBO.getOperateBy());
		ssoUserMapper.updateProfileByUserId(ssoUser);
	}

	/**
	 * 校验用户名唯一性
	 *
	 * @param username      用户名
	 * @param excludeUserId 需要排除的用户ID
	 */
	private void checkUserNameUniq(String username, Long... excludeUserId) {
		if (StringUtils.isEmpty(username)) {
			return;
		}
		SsoUser ssoUser = ssoUserMapper.getByUserName(username);
		if (null == ssoUser) {
			return;
		}
		if (!ObjectUtils.isEmpty(excludeUserId) && ssoUser.getUserId().equals(excludeUserId[0])) {
			return;
		}
		//用户名已经存在
		throw new BusinessException(UserResStatusEnum.USER_NAME_EXISTS, username);
	}

	/**
	 * 校验手机号唯一性
	 *
	 * @param phone         手机号
	 * @param excludeUserId 需要排除的用户ID
	 */
	private void checkPhoneUniq(String phone, Long... excludeUserId) {
		if (StringUtils.isEmpty(phone)) {
			return;
		}
		SsoUser ssoUser = ssoUserMapper.getByPhone(phone);
		if (null == ssoUser) {
			return;
		}
		if (!ObjectUtils.isEmpty(excludeUserId) && ssoUser.getUserId().equals(excludeUserId[0])) {
			return;
		}
		//手机号已经存在
		throw new BusinessException(UserResStatusEnum.PHONE_EXISTS, phone);
	}

	/**
	 * 校验邮箱唯一性
	 *
	 * @param email         邮箱地址
	 * @param excludeUserId 需要排除的用户ID
	 */
	private void checkEmailUniq(String email, Long... excludeUserId) {
		if (StringUtils.isEmpty(email)) {
			return;
		}
		SsoUser ssoUser = ssoUserMapper.getByEmail(email);
		if (null == ssoUser) {
			return;
		}
		if (!ObjectUtils.isEmpty(excludeUserId) && ssoUser.getUserId().equals(excludeUserId[0])) {
			return;
		}
		//邮箱已经存在
		throw new BusinessException(UserResStatusEnum.EMAIL_EXISTS, email);
	}

	/**
	 * 超级管理员账户，不可操作
	 *
	 * @param userId
	 */
	private void checkSupperAdminUser(Long userId) {
		if (null == userId) {
			return;
		}
		Long supperAdminUserId = sysConfigService.getSupperAdminUserId();
		if (null == supperAdminUserId) {
			return;
		}
		if (supperAdminUserId.equals(userId)) {
			throw new BusinessException(UserResStatusEnum.SUPPER_ADMIN_USER);
		}
	}


	/**
	 * 构建查询参数
	 *
	 * @param pageBO
	 * @return
	 */
	private UserPageQuery buildUserPageQuery(UserListPageBO pageBO) {
		UserPageQuery query = new UserPageQuery()
				.setUsernameLike(pageBO.getUsernameLike())
				.setNickNameLike(pageBO.getNickNameLike())
				.setRealNameLike(pageBO.getRealNameLike())
				.setPhoneLike(pageBO.getPhoneLike())
				.setDeptId(pageBO.getDeptId())
				.setRoleId(pageBO.getRoleId())
				.setStatus(pageBO.getStatus())
				//未删除
				.setDelFlag(DelFlagEnum.OK.getStatus());

		query.setPage(pageBO.getPage());
		query.setPageSize(pageBO.getPageSize());
		return query;
	}
}
