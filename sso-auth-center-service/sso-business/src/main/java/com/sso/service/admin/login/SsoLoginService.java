/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.service.admin.login;

import com.sso.common.constant.CacheConstants;
import com.sso.common.enums.LoginStatusEnum;
import com.sso.common.enums.exception.SysResStatusEnum;
import com.sso.common.exception.BusinessException;
import com.sso.common.model.login.LoginResultVO;
import com.sso.common.utils.ServletUtils;
import com.sso.common.utils.StringUtils;
import com.sso.common.utils.ip.IpAddressUtils;
import com.sso.common.utils.ip.IpUtils;
import com.sso.dao.entity.SsoLoginLog;
import com.sso.dao.entity.SsoOnlineUser;
import com.sso.dao.mapper.SsoLoginLogMapper;
import com.sso.dao.mapper.SsoOnlineUserMapper;
import com.sso.dao.mapper.SsoUserMapper;
import com.sso.framework.redis.RedisCache;
import com.sso.model.bo.login.LoginBO;
import com.sso.model.vo.login.LoginTokenVO;
import com.sso.service.base.SsoTokenService;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 登录校验方法
 *
 * @author 程序员小强
 */
@Slf4j
@Component
public class SsoLoginService {

	@Resource
	private RedisCache redisCache;

	@Resource
	private SsoUserMapper ssoUserMapper;

	@Resource
	private SsoTokenService tokenService;

	@Resource
	private SsoLoginLogMapper ssoLoginLogMapper;

	@Resource
	private SsoOnlineUserMapper ssoOnlineUserMapper;

	@Resource
	private AuthenticationManager authenticationManager;

	/**
	 * 登录验证
	 *
	 * @param loginBO 登录
	 * @return 结果
	 */
	public LoginTokenVO login(LoginBO loginBO) {

		//验证码校验
		this.checkCaptcha(loginBO);

		//账号校验(账密,状态)
		LoginResultVO loginResult = this.doCheckAccount(loginBO);

		//本次请求唯一标识
		loginResult.setRequestId(loginBO.getRequestId());

		// 生成token
		String token = tokenService.createToken(loginResult);

		//记录最后登录时间
		ssoUserMapper.updateLastLogin(loginResult.getUser().getUserId(), loginResult.getSourceIp(), new Date(loginResult.getLoginTime()));

		//记录登录日志
		this.doAddLoginSuccessLog(loginResult);

		//记录在线用户记录
		this.doSaveOnlineUserRecord(loginResult);

		return new LoginTokenVO(loginBO.getUsername(), loginBO.getRequestId(), token);
	}

	/**
	 * 记录登录日志
	 */
	private void doAddLoginFailLog(LoginBO loginBO, String operateMsg) {
		doAddLoginLog(loginBO.getRequestId(), LoginStatusEnum.FAIL, loginBO.getUsername(), System.currentTimeMillis(), operateMsg);
	}

	/**
	 * 记录登录日志
	 */
	private void doAddLoginSuccessLog(LoginResultVO loginResult) {
		doAddLoginLog(loginResult.getRequestId(), LoginStatusEnum.SUCCESS, loginResult.getUsername(), loginResult.getLoginTime(), "");
	}

	/**
	 * 记录登录日志
	 */
	private SsoLoginLog doAddLoginLog(String requestId, LoginStatusEnum statusEnum, String username, Long loginTime, String operateMsg) {
		SsoLoginLog loginLog = new SsoLoginLog();
		loginLog.setRequestId(requestId);
		//用户登录名
		loginLog.setUsername(username);
		//IP地址
		String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
		//来源IP
		loginLog.setSourceIp(StringUtils.dealBlankDefault(ip, ""));
		//来源地址
		loginLog.setSourceAddress(StringUtils.dealBlankDefault(IpAddressUtils.getRealAddressByIP(ip), ""));
		//登录浏览器相关
		UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
		//浏览器
		loginLog.setBrowserName(StringUtils.dealBlankDefault(userAgent.getBrowser().getName(), ""));
		//操作系统
		loginLog.setOperateSystem(StringUtils.dealBlankDefault(userAgent.getOperatingSystem().getName(), ""));

		//登录状态 0-成功;1-失败
		loginLog.setStatus(statusEnum.getStatus());
		//登录时间戳
		loginLog.setLoginTime(loginTime);
		//返回结果
		loginLog.setOperateMsg(StringUtils.dealBlankDefault(operateMsg, ""));
		ssoLoginLogMapper.insertSelective(loginLog);
		return loginLog;
	}

	/**
	 * 记录在线用户记录
	 *
	 * @param loginResult
	 */
	private void doSaveOnlineUserRecord(LoginResultVO loginResult) {
		SsoOnlineUser ssoOnlineUser = new SsoOnlineUser();
		ssoOnlineUser.setRequestId(loginResult.getRequestId());
		ssoOnlineUser.setUserId(loginResult.getUser().getUserId());
		ssoOnlineUser.setSourceIp(loginResult.getSourceIp());
		ssoOnlineUser.setSourceAddress(loginResult.getSourceAddress());
		ssoOnlineUser.setBrowserName(loginResult.getBrowserName());
		ssoOnlineUser.setOperateSystem(loginResult.getOperateSystem());
		ssoOnlineUser.setLoginTime(loginResult.getLoginTime());
		ssoOnlineUser.setExpireTime(loginResult.getExpireTime());
		ssoOnlineUserMapper.insertSelective(ssoOnlineUser);
	}

	/**
	 * 账号校验
	 *
	 * @param loginBO
	 */
	private LoginResultVO doCheckAccount(LoginBO loginBO) {
		//用户验证
		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginBO.getUsername(), loginBO.getPassword());
			//该方法会去调用UserDetailsServiceImpl.loadUserByUsername
			Authentication authentication = authenticationManager.authenticate(authToken);
			return (LoginResultVO) authentication.getPrincipal();
		} catch (Exception e) {
			if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
				log.info("用户名或密码错误 username:{}", loginBO.getUsername());
				//记录登录日志
				this.doAddLoginFailLog(loginBO, SysResStatusEnum.USER_PASSWORD_NOT_MATCH.getMsg());
				//用户名或密码错误
				throw new BusinessException(SysResStatusEnum.USER_PASSWORD_NOT_MATCH);
			} else if (e instanceof DisabledException || e instanceof InternalAuthenticationServiceException) {
				log.info("用户已停用 username:{}", loginBO.getUsername());
				//记录登录日志
				this.doAddLoginFailLog(loginBO, SysResStatusEnum.USER_DISABLE.getMsg());
				//用户已停用
				throw new BusinessException(SysResStatusEnum.USER_DISABLE);
			} else if (e instanceof AuthenticationException) {
				log.info("用户名或密码错误 username:{}", loginBO.getUsername());
				//记录登录日志
				this.doAddLoginFailLog(loginBO, SysResStatusEnum.USER_NAME_NOT_EXISTS.getMsg());
				//用户已停用
				throw new BusinessException(SysResStatusEnum.USER_NAME_NOT_EXISTS);
			} else {
				//记录登录日志
				this.doAddLoginFailLog(loginBO, SysResStatusEnum.SYSTEM_ERROR.getMsg());
				log.info("[ 登录异常 ] username:{} >> ", loginBO.getUsername(), e);
				//其它登录失败
				throw new BusinessException(SysResStatusEnum.SYSTEM_ERROR);
			}
		}
	}

	/**
	 * 校验验证码
	 *
	 * @param loginBO
	 */
	private void checkCaptcha(LoginBO loginBO) {
		String verifyKey = CacheConstants.getCaptchaCodeKey(loginBO.getRequestId());
		String captcha = redisCache.get(verifyKey);
		//验证码已失效
		if (captcha == null) {
			//记录登录日志
			this.doAddLoginFailLog(loginBO, SysResStatusEnum.CAPTCHA_EXPIRE.getMsg());
			throw new BusinessException(SysResStatusEnum.CAPTCHA_EXPIRE);
		}
		//仅一次有效，使用后作废
		redisCache.del(verifyKey);
		//验证码错误
		if (!loginBO.getCaptchaCode().equalsIgnoreCase(captcha)) {
			//记录登录日志
			this.doAddLoginFailLog(loginBO, SysResStatusEnum.CAPTCHA_ERROR.getMsg());
			throw new BusinessException(SysResStatusEnum.CAPTCHA_ERROR);
		}
	}
}
