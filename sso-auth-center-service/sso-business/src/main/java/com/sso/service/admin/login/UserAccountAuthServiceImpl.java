/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.service.admin.login;

import com.sso.common.enums.DelFlagEnum;
import com.sso.common.enums.system.UserStatusEnum;
import com.sso.common.model.login.LoginResultVO;
import com.sso.common.model.login.LoginUserVO;
import com.sso.common.utils.bean.BeanCopierUtil;
import com.sso.dao.entity.SsoUser;
import com.sso.dao.mapper.SsoUserMapper;
import com.sso.model.vo.login.LoginUserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户登录认证-验证处理
 *
 * @author 程序员小强
 */
@Slf4j
@Service
public class UserAccountAuthServiceImpl implements UserDetailsService {

	@Resource
	private SsoUserMapper ssoUserMapper;
	@Autowired
	private SysPermissionService permissionService;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SsoUser user = ssoUserMapper.getByUserName(username);
		if (null == user) {
			log.info("登录用户：{} 不存在.", username);
			throw new UsernameNotFoundException("登录用户不存在");
		} else if (DelFlagEnum.DELETED.getStatus().equals(user.getDelFlag())) {
			log.info("登录用户：{} 已被删除.", username);
			throw new AccountExpiredException("用户已被删除");
		} else if (UserStatusEnum.DISABLE.getStatus().equals(user.getStatus())) {
			log.info("登录用户：{} 已被停用.", username);
			throw new DisabledException("用户已停用");
		}

		return createLoginUser(user);
	}

	public UserDetails createLoginUser(SsoUser user) {
		LoginUserVO loginUserVO = BeanCopierUtil.copy(user, LoginUserVO.class);
		LoginUserInfoVO menuPermission = permissionService.getMenuPermission(loginUserVO);
		LoginResultVO loginResultVO = new LoginResultVO(loginUserVO, menuPermission.getPermissionList());
		loginResultVO.setRoleKeyList(permissionService.getRolePermission(loginUserVO));
		return loginResultVO;
	}
}
