/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.service.getway.impl;

import com.sso.common.annotation.OpenApiService;
import com.sso.common.constant.CacheConstants;
import com.sso.common.constant.SsoConstants;
import com.sso.common.enums.MenuTypeEnum;
import com.sso.common.exception.BusinessException;
import com.sso.common.model.login.LoginResultVO;
import com.sso.common.model.login.LoginUserVO;
import com.sso.common.utils.StringUtils;
import com.sso.common.utils.bean.BeanCopierUtil;
import com.sso.common.utils.tree.ListToTreeUtil;
import com.sso.dao.entity.SsoMenu;
import com.sso.dao.entity.SsoSystem;
import com.sso.dao.mapper.SsoMenuMapper;
import com.sso.dao.mapper.SsoOnlineUserMapper;
import com.sso.dao.mapper.SsoRoleMapper;
import com.sso.framework.config.property.SysConfigProperty;
import com.sso.framework.redis.RedisCache;
import com.sso.model.bo.getway.ApplyAuthBO;
import com.sso.model.vo.getway.ApplyAuthVO;
import com.sso.model.vo.getway.MenuTreeAuthVO;
import com.sso.model.vo.getway.SysLoginCacheVO;
import com.sso.service.base.SystemCacheService;
import com.sso.service.getway.ApplyAuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 申请认证开放接口
 *
 * @author 程序员小强
 */
@Slf4j
@Service
@OpenApiService
public class ApplyAuthServiceImpl implements ApplyAuthService {

	@Autowired
	private RedisCache redisCache;
	@Resource
	private SsoMenuMapper ssoMenuMapper;
	@Resource
	private SsoRoleMapper ssoRoleMapper;
	@Resource
	private SsoOnlineUserMapper ssoOnlineUserMapper;
	@Autowired
	private SystemCacheService systemCacheService;
	@Autowired
	private SysConfigProperty sysConfigProperty;

	private static final Integer MENU_TYPE_TREE = 2;

	/**
	 * 申请认证开放接口
	 *
	 * @param authBO
	 */
	@Override
	public ApplyAuthVO applyAuth(String sysCode, ApplyAuthBO authBO) {
		//根据sysCode查询目标系统跳转地址
		SsoSystem system = systemCacheService.getBySysCode(sysCode);
		if (system == null) {
			throw new BusinessException("系统不存在");
		}

		//入参token为空，说明未登录，返回扫码授权url
		if (StringUtils.isEmpty(authBO.getSsoToken())) {
			log.info("[ 认证失败 ] >> token 为空 ，返回认证中心登录地址 ");
			String targetRedirectUrl = StringUtils.isNoneBlank(authBO.getRedirectUrl()) ? authBO.getRedirectUrl() : system.getSysUrl();
			return ApplyAuthVO.authFailNeedLogin(sysConfigProperty.getSsoLoginUrl(), targetRedirectUrl);
		}

		//toke缓存key
		String tokenCacheKey = CacheConstants.getLoginUserTokenKey(authBO.getSsoToken());
		LoginResultVO loginUserInfo = redisCache.hGet(tokenCacheKey, authBO.getSsoToken(), LoginResultVO.class);
		//根据token获取redis缓存数据，取不到，说明token已经过期
		if (loginUserInfo == null || ObjectUtils.isEmpty(loginUserInfo.getUser())) {
			log.info("[ 认证失败 ] >> token 已失效或不存在 ，返回认证中心登录地址 ");
			String targetRedirectUrl = StringUtils.isNoneBlank(authBO.getRedirectUrl()) ? authBO.getRedirectUrl() : system.getSysUrl();
			return ApplyAuthVO.authFailNeedLogin(sysConfigProperty.getSsoLoginUrl(), targetRedirectUrl);
		}

		//构建返回信息
		ApplyAuthVO result = this.buildApplyUserAuth(system, loginUserInfo);
		//角色key标识集
		result.setRoleKeyList(ssoRoleMapper.getRoleKeyBySysCodeAndUserId(sysCode, result.getUserId()));

		//构建菜单与权限信息
		this.buildMenuAndPermission(sysCode, result, authBO, result.getUserId());

		//token有效-认证后 刷新token有效时间
		String userIdCacheKey = CacheConstants.getLoginUserIdKey(result.getUserId());
		loginUserInfo.setExpireTime(System.currentTimeMillis() + sysConfigProperty.getTokenExpireTime() * 1000);
		//记录用户-注册的子系统
		redisCache.hSet(tokenCacheKey, sysCode, new SysLoginCacheVO(sysCode, authBO.getLoginOutUrl(), System.currentTimeMillis()));
		//刷新用户缓存有效期
		redisCache.set(userIdCacheKey, loginUserInfo, sysConfigProperty.getTokenExpireTime(), TimeUnit.MINUTES);
		//刷新token有效期
		redisCache.hSet(tokenCacheKey, loginUserInfo.getToken(), loginUserInfo, sysConfigProperty.getTokenExpireTime(), TimeUnit.MINUTES);

		//刷新在线用户记录表效期
		if (StringUtils.isNotBlank(loginUserInfo.getRequestId())) {
			ssoOnlineUserMapper.refreshExpireTimeByRequestId(loginUserInfo.getRequestId(), loginUserInfo.getExpireTime());
		}

		log.info("[ 认证成功 ] >> sysCode:{} , ssoToken:{} ", sysCode, authBO.getSsoToken());
		return result;
	}

	/**
	 * 构建菜单与权限返回信息
	 *
	 * @param result
	 * @param authBO
	 * @param userId
	 */
	private void buildMenuAndPermission(String sysCode, ApplyAuthVO result, ApplyAuthBO authBO, Long userId) {
		//根据用户查询拥有的菜单权限
		List<SsoMenu> menuList = ssoMenuMapper.getMenuListBySysCodeAndUserId(sysCode, userId);
		if (CollectionUtils.isEmpty(menuList)) {
			result.setMenuList(new ArrayList<>(0));
			result.setPermissionList(new HashSet<>(0));
			return;
		}

		MenuTreeAuthVO menuVO = null;
		List<MenuTreeAuthVO> menuResList = new ArrayList<>(menuList.size());
		//权限标识集
		Set<String> permissionList = new HashSet<>(menuList.size());
		for (SsoMenu menu : menuList) {
			if (StringUtils.isNotBlank(menu.getPermissions())) {
				permissionList.addAll(Arrays.asList(menu.getPermissions().split(SsoConstants.SPLIT_ESCAPE)));
			}
			//菜单
			if (MenuTypeEnum.isMenu(menu.getMenuType())) {
				menuVO = BeanCopierUtil.copy(menu, MenuTreeAuthVO.class);
				menuResList.add(menuVO);
			}
		}

		result.setMenuList(menuResList);
		result.setPermissionList(permissionList);

		//非树结构返回
		if (!MENU_TYPE_TREE.equals(NumberUtils.toInt(authBO.getMenuType(), 0))) {
			return;
		}

		//树结构返回  >> 转换成树返回
		ListToTreeUtil<MenuTreeAuthVO> treeUtil = new ListToTreeUtil<>();
		result.setMenuList(treeUtil.getTreeListObject(result.getMenuList()));
	}

	/**
	 * 构建用户基本认证信息
	 *
	 * @param loginUserInfo
	 */
	private ApplyAuthVO buildApplyUserAuth(SsoSystem system, LoginResultVO loginUserInfo) {
		LoginUserVO user = loginUserInfo.getUser();
		ApplyAuthVO result = new ApplyAuthVO();
		result.setSysCode(system.getSysCode());
		result.setSysName(system.getSysName());
		result.setAuthResult(true);
		result.setUserId(user.getUserId());
		result.setUsername(user.getUsername());
		result.setNickName(user.getNickName());
		result.setRealName(user.getRealName());
		result.setAvatar(user.getAvatar());
		result.setSex(user.getSex());
		result.setPhone(user.getPhone());
		result.setEmail(user.getEmail());
		return result;
	}

}
