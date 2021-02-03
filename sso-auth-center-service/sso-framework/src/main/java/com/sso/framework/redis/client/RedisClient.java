package com.sso.framework.redis.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;

/**
 * redis client 工具
 *
 * @author 程序员小强
 */
@Component
public class RedisClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisClient.class);

	public <T> T invoke(JedisPool pool, RedisClientInvoker<T> clients) {
		T obj = null;
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			obj = clients.invoke(jedis);
		} catch (JedisException | IOException ex) {
			LOGGER.error(ex.getMessage(), ex);
		} finally {
			if (jedis != null) {
				if (jedis.isConnected()) {
					jedis.close();
				}
			}
		}
		return obj;
	}
}
