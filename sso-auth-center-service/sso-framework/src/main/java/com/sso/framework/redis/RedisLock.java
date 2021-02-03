package com.sso.framework.redis;

import com.sso.framework.redis.client.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

/**
 * Redis 分布式锁
 *
 * @author mennq
 */
@Slf4j
@Component
public class RedisLock {

	private static final Long ONE = 1L;
	private static final String DEFAULT_LOCK_VALUE = "1";
	private static final String OK = "OK";

	/**
	 * 释放锁 lua
	 */
	private static final String RELEASE_LUA = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

	@Autowired
	private JedisPool jedisPool;

	@Autowired
	private RedisClient redisClient;

	/**
	 * 尝试获取分布式锁
	 *
	 * @param lockKey    锁key
	 * @param requestId  (请求标识)加锁者标识-例如UUID
	 * @param expireTime 超期时间 毫秒数
	 * @return 是否获取锁成功
	 */
	public boolean tryGetLock(String lockKey, String requestId, int expireTime) {
		return redisClient.invoke(jedisPool, jedis -> {
					String result = jedis.set(lockKey, requestId, new SetParams().px(expireTime).nx());
					return OK.equals(result);
				}
		);
	}

	/**
	 * 尝试在等待时间范围内 , 获取锁
	 *
	 * @param lockKey    锁key
	 * @param waitTime   等待时间,单位:秒
	 * @param expireTime 锁定有效期,单位:秒
	 * @return 是否获取锁成功
	 */
	public boolean tryGetLock(String lockKey, String requestId, int waitTime, int expireTime) {
		int preTime = 20;
		int waitLimit = waitTime * 1000 / preTime;
		int waitNum = 0;

		for (; waitNum < waitLimit; waitNum++) {
			if (this.tryGetLock(lockKey, requestId, expireTime)) {
				return true;
			} else {
				try {
					Thread.sleep(preTime);
				} catch (InterruptedException e) {
					log.error("[ tryGetLock ] >> Exception ", e);
				}
			}
		}
		return false;
	}

	/**
	 * 尝试获取分布式锁
	 *
	 * @param lockKey    锁key
	 * @param expireTime 超期时间 毫秒数
	 * @return 是否获取成功
	 */
	public boolean tryGetLock(String lockKey, int expireTime) {
		return tryGetLock(lockKey, DEFAULT_LOCK_VALUE, expireTime);
	}

	/**
	 * 释放分布式锁
	 *
	 * @param lockKey   锁key
	 * @param requestId (请求标识)加锁者标识，必须与加锁时一致-才可以
	 * @return 是否释放成功
	 */
	public boolean releaseLock(String lockKey, String requestId) {
		return redisClient.invoke(jedisPool, jedis -> {
					Object result = jedis.eval(RELEASE_LUA, Collections.singletonList(lockKey), Collections.singletonList(requestId));
					return ONE.equals(result);
				}
		);
	}
}
