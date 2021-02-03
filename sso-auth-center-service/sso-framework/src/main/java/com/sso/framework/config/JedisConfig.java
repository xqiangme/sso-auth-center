/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.framework.config;

import com.sso.framework.config.property.RedisConfigProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;


/**
 * jedis 配置类
 *
 * @author 程序员小强iang
 * @version JedisConfig.java, v 1.0
 */
@Slf4j
@Configuration
public class JedisConfig extends CachingConfigurerSupport {

	@Resource
	private RedisConfigProperty redisConfigProperty;

	/**
	 * 连接池配置
	 */
	@Bean(name = "jedisPoolConfig")
	@ConfigurationProperties(prefix = "spring.redis.pool-config")
	public JedisPoolConfig getRedisConfig() {
		return new JedisPoolConfig();
	}

	/**
	 * jedis 连接池
	 */
	@Bean(name = "jedisPool")
	public JedisPool jedisPool(@Qualifier(value = "jedisPoolConfig") final JedisPoolConfig jedisPoolConfig) {
		log.info("Jedis Pool build start ");
		String host = redisConfigProperty.getHost();
		int timeout = redisConfigProperty.getTimeout();
		int port = redisConfigProperty.getPort();
		String password = redisConfigProperty.getPassword();
		int database = redisConfigProperty.getDatabase();
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
		log.info("Jedis Pool build success  host = {} , port = {} ", host, port);
		return jedisPool;
	}
}
