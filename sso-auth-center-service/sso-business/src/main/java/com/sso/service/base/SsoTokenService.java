/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.service.base;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.sso.common.constant.CacheConstants;
import com.sso.common.constant.UserLoginConstants;
import com.sso.common.model.login.LoginResultVO;
import com.sso.common.utils.ObjectUtils;
import com.sso.common.utils.ServletUtils;
import com.sso.common.utils.StringUtils;
import com.sso.common.utils.UUIDUtil;
import com.sso.common.utils.ip.IpAddressUtils;
import com.sso.common.utils.ip.IpUtils;
import com.sso.dao.mapper.SsoOnlineUserMapper;
import com.sso.framework.config.property.SysConfigProperty;
import com.sso.framework.redis.RedisCache;
import com.sso.model.vo.getway.SysLoginCacheVO;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author 程序员小强
 */
@Slf4j
@Component
public class SsoTokenService {
	protected static final long MILLIS_SECOND = 1000;
	protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
	private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

	@Autowired
	private RedisCache redisCache;

	@Resource
	private SsoOnlineUserMapper ssoOnlineUserMapper;

	@Autowired
	private SysConfigProperty sysConfigProperty;

	/**
	 * 根据HttpServletRequest 获取用户
	 *
	 * @return 用户信息
	 */
	public LoginResultVO getLoginUser(HttpServletRequest request) {
		//获取请求携带的令牌
		String token = this.getTokenByHeader(request);
		//获取不到token-未登录
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		return this.getUserByToken(token);
	}

	/**
	 * 根据token 查询用户
	 *
	 * @param token
	 * @return 用户信息
	 */
	public LoginResultVO getUserByToken(String token) {
		if (StringUtils.isBlank(token)) {
			return null;
		}

		//toke缓存key
		String tokenCacheKey = CacheConstants.getLoginUserTokenKey(token);
		return redisCache.hGet(tokenCacheKey, token, LoginResultVO.class);
	}

	/**
	 * 根据userId 查询用户
	 *
	 * @param userId
	 * @return 用户信息
	 */
	public LoginResultVO getUserByUserId(Long userId) {
		if (null == userId) {
			return null;
		}

		String userIdCacheKey = CacheConstants.getLoginUserIdKey(userId);
		//根据unionId查询redis，若用户信息存在
		return redisCache.get(userIdCacheKey, LoginResultVO.class);
	}

	/**
	 * 用户退出
	 * 清除token记录
	 */
	public void logoOutUser(Long userId) {
		if (ObjectUtils.isNull(userId)) {
			return;
		}
		//登录缓存用户信息
		LoginResultVO loginUser = redisCache.get(CacheConstants.getLoginUserIdKey(userId), LoginResultVO.class);
		if (ObjectUtils.isNull(loginUser)) {
			return;
		}
		this.logoOutUser(loginUser);
	}

	/**
	 * 用户退出
	 * 清除token记录
	 */
	public void logoOutUser(LoginResultVO loginResultVO) {
		if (null == loginResultVO) {
			return;
		}
		if (StringUtils.isEmpty(loginResultVO.getToken())) {
			return;
		}
		if (null == loginResultVO.getUser()) {
			return;
		}

		String userTokenKey = CacheConstants.getLoginUserTokenKey(loginResultVO.getToken());

		//若已登录子系统-则退出所有子系统
		this.loginOutChildSysByToken(loginResultVO.getToken());

		//清除token记录
		redisCache.del(userTokenKey);

		//清除用户记录
		String userIdKey = CacheConstants.getLoginUserIdKey(loginResultVO.getUser().getUserId());
		redisCache.del(userIdKey);

		//更新在线用户记录截止时间
		this.updateOnlineUserExpire(loginResultVO);
	}

	/**
	 * 更新在线用户记录截止时间
	 *
	 * @param loginResultVO
	 */
	private void updateOnlineUserExpire(LoginResultVO loginResultVO) {
		long expireTime = loginResultVO.getExpireTime();
		long currentTimeMillis = System.currentTimeMillis();
		//还未截止-则改为当前时间
		if (expireTime > currentTimeMillis) {
			expireTime = currentTimeMillis;
		}
		//效期截止
		ssoOnlineUserMapper.updateByLogoOut(loginResultVO.getRequestId(), expireTime, currentTimeMillis);
	}

	/**
	 * 创建令牌
	 *
	 * @param loginResultVO 用户信息
	 * @return 令牌
	 */
	public String createToken(LoginResultVO loginResultVO) {
		String token = UUIDUtil.getUUID();
		loginResultVO.setToken(token);
		this.setUserAgent(loginResultVO);
		//创建token
		this.doCreateToken(loginResultVO);
		return token;
	}

	/**
	 * 验证令牌有效期，
	 * 相差不足20分钟，自动刷新 token续期
	 *
	 * @param loginResult
	 * @return 令牌
	 */
	public void renewTokenByAdminWeb(LoginResultVO loginResult) {
		String token = loginResult.getToken();
		long expireTime = loginResult.getExpireTime();
		long currentTime = System.currentTimeMillis();

		//若效期时间不足20分钟则进行续期
		if (expireTime - currentTime > MILLIS_MINUTE_TEN) {
			return;
		}

		String userIdCacheKey = CacheConstants.getLoginUserIdKey(loginResult.getUser().getUserId());
		LoginResultVO lastLoginUser = redisCache.get(userIdCacheKey, LoginResultVO.class);

		if (null == lastLoginUser || !token.equals(lastLoginUser.getToken())) {
			return;
		}

		//若token一致-则效期延长
		loginResult.setExpireTime(loginResult.getLoginTime() + sysConfigProperty.getTokenExpireTime() * MILLIS_MINUTE);
		String userTokenCacheKey = CacheConstants.getLoginUserTokenKey(loginResult.getToken());
		redisCache.set(userIdCacheKey, loginResult, sysConfigProperty.getTokenExpireTime(), TimeUnit.MINUTES);
		redisCache.hSet(userTokenCacheKey, loginResult.getToken(), loginResult, sysConfigProperty.getTokenExpireTime(), TimeUnit.MINUTES);

		//刷新登录记录表效期
		if (StringUtils.isNotBlank(lastLoginUser.getRequestId())) {
			ssoOnlineUserMapper.refreshExpireTimeByRequestId(lastLoginUser.getRequestId(), loginResult.getExpireTime());
		}
	}

	/**
	 * 创建令牌-并指定有效期
	 *
	 * @param loginResultVO 登录信息
	 */
	public void doCreateToken(LoginResultVO loginResultVO) {
		loginResultVO.setLoginTime(System.currentTimeMillis());
		loginResultVO.setExpireTime(loginResultVO.getLoginTime() + sysConfigProperty.getTokenExpireTime() * MILLIS_MINUTE);

		String userIdCacheKey = CacheConstants.getLoginUserIdKey(loginResultVO.getUser().getUserId());
		//根据unionId查询redis，若用户信息存在
		LoginResultVO lastLoginUser = redisCache.get(userIdCacheKey, LoginResultVO.class);
		if (ObjectUtils.isNotNull(lastLoginUser)) {
			//若已登录子系统-则退出所有子系统
			this.loginOutChildSysByToken(lastLoginUser.getToken());
			//删除同一用户,旧的token信息,避免目标系统申请认证直接刷新token信息
			redisCache.del(CacheConstants.getLoginUserTokenKey(lastLoginUser.getToken()));
			//更新在线用户记录截止时间
			this.updateOnlineUserExpire(lastLoginUser);
		}

		String userTokenCacheKey = CacheConstants.getLoginUserTokenKey(loginResultVO.getToken());
		redisCache.set(userIdCacheKey, loginResultVO, sysConfigProperty.getTokenExpireTime(), TimeUnit.MINUTES);
		redisCache.hSet(userTokenCacheKey, loginResultVO.getToken(), loginResultVO, sysConfigProperty.getTokenExpireTime(), TimeUnit.MINUTES);
	}

	/**
	 * 根据token-查询已经登录的子系统
	 * 依次登出子系统
	 *
	 * @param token
	 */
	private void loginOutChildSysByToken(String token) {
		String userTokenKey = CacheConstants.getLoginUserTokenKey(token);
		//key:系统编码 sysCode , value:系统退出地址
		Map<String, String> loginSystemMap = redisCache.hgetAll(userTokenKey, String.class);
		if (ObjectUtils.isEmpty(loginSystemMap)) {
			return;
		}

		CompletableFuture.runAsync(() -> this.loginOutChildSysAsync(token, loginSystemMap));
	}

	/**
	 * 异步调用-子系统退出接口
	 *
	 * @param token
	 * @param loginSystemMap
	 */
	private void loginOutChildSysAsync(String token, Map<String, String> loginSystemMap) {
		for (Map.Entry<String, String> map : loginSystemMap.entrySet()) {
			String key = map.getKey();
			String value = map.getValue();
			if (key.equals(token)) {
				continue;
			}
			if (StringUtils.isBlank(value)) {
				continue;
			}
			SysLoginCacheVO loginCache = JSON.parseObject(value, SysLoginCacheVO.class);
			if (StringUtils.isBlank(loginCache.getLoginOutUrl())) {
				continue;
			}
			log.info("[ 登出子系统 ] >> sysCode:{},logoOutUrl:{}", key, loginCache.getLoginOutUrl());
			HttpUtil.get(loginCache.getLoginOutUrl());
		}
	}

	/**
	 * 设置用户代理信息
	 *
	 * @param loginResultVO 登录信息
	 */
	public void setUserAgent(LoginResultVO loginResultVO) {
		//IP地址相关
		String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
		loginResultVO.setSourceIp(ip);
		loginResultVO.setSourceAddress(IpAddressUtils.getRealAddressByIP(ip));

		//登录浏览器相关
		UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
		//浏览器名称
		loginResultVO.setBrowserName(userAgent.getBrowser().getName());
		//操作系统名称
		loginResultVO.setOperateSystem(userAgent.getOperatingSystem().getName());
	}

	/**
	 * 获取请求token
	 *
	 * @param request
	 * @return token
	 */
	private String getTokenByHeader(HttpServletRequest request) {
		String token = request.getHeader(sysConfigProperty.getTokenHeader());
		if (StringUtils.isNotEmpty(token) && token.startsWith(UserLoginConstants.TOKEN_PREFIX)) {
			token = token.replace(UserLoginConstants.TOKEN_PREFIX, "");
		}
		return token;
	}
}
