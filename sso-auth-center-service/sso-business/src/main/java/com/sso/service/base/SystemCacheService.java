package com.sso.service.base;

import com.sso.common.constant.CacheConstants;
import com.sso.common.utils.StringUtils;
import com.sso.dao.entity.SsoSystem;
import com.sso.dao.mapper.SsoSystemMapper;
import com.sso.framework.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class SystemCacheService {

	@Autowired
	private RedisCache redisCache;

	@Resource
	private SsoSystemMapper ssoSystemMapper;

	/**
	 * 获取系统配置 - 有缓存，优先获取缓存内容
	 *
	 * @param sysCode
	 */
	public SsoSystem getBySysCode(String sysCode) {
		if (StringUtils.isBlank(sysCode)) {
			return null;
		}

		String systemCaptchaKey = CacheConstants.getSystemCaptchaKey(sysCode);

		//查询缓存
		SsoSystem systemFromCache = redisCache.get(systemCaptchaKey, SsoSystem.class);
		if (null != systemFromCache) {
			return systemFromCache;
		}

		//默认720分钟=12小时（配置不经常改变）
		int expireMinutes = CacheConstants.SYSTEM_CONFIG_CACHE_EXPIRE;

		SsoSystem systemFromDb = ssoSystemMapper.getBySysCode(sysCode);
		if (null == systemFromDb) {
			//数据库不存在-也缓存 -> 防止缓存穿透
			systemFromDb = new SsoSystem();
			//失效时间设置短一点
			expireMinutes = CacheConstants.CAPTCHA_EXPIRE_FIVE;
		}
		//缓存到redis
		redisCache.set(systemCaptchaKey, systemFromDb, expireMinutes, TimeUnit.MINUTES);

		return systemFromDb;
	}

	/**
	 * 移除配置缓存
	 * <p>
	 * 使用场景-编辑系统配置之后
	 *
	 * @param sysCode
	 */
	public void removeSystemCache(String sysCode) {
		if (StringUtils.isBlank(sysCode)) {
			return;
		}
		String systemCaptchaKey = CacheConstants.getSystemCaptchaKey(sysCode);
		redisCache.del(systemCaptchaKey);
	}
}
