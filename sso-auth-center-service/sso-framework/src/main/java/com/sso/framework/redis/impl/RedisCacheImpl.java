/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.framework.redis.impl;

import com.alibaba.fastjson.JSONArray;
import com.sso.common.utils.JsonSerializer;
import com.sso.framework.redis.RedisCache;
import com.sso.framework.redis.client.RedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * jedis 操作redis 方封装
 * <p>
 * 大致内容如下，常用的方法
 * 1.string 相关操作
 * 2.set
 * 3.list
 * 4.hash
 * 5.zset
 * 6.管道查询Pipeline
 * 7.发布订阅
 */
@Component
public class RedisCacheImpl implements RedisCache {

	private static final int ZERO = 0;
	private static final int FIVE = 5;
	private static final int THIRTY = 30;
	private static final String OK = "OK";
	private static final Long LONG_ONE = 1L;
	private static final Long LONG_ZERO = 0L;
	/**
	 * 默认失效时间毫秒 5 分钟
	 */
	private static final long DEFAULT_CACHE_MILLIS = getMillis(TimeUnit.MINUTES, FIVE);

	@Autowired
	private JedisPool jedisPool;
	@Autowired
	private RedisClient redisClient;


	/* ------------------ string  类型相关操作--------------------------------*/

	/**
	 * 添加数据到redis
	 * 设置默认过期时间  30 分钟
	 *
	 * @param key
	 * @param value
	 */
	@Override
	public Boolean set(String key, Object value) {
		return set(key, value, THIRTY, TimeUnit.MINUTES);
	}

	/**
	 * 添加数据到redis
	 * 自定义过期时间
	 * 注：从 Redis 2.6.12 版本开始， SET 在设置操作成功完成时，返回 OK
	 *
	 * @param key
	 * @param value
	 * @param duration 时间量
	 * @param timeUnit 时间单位枚举
	 */
	@Override
	public Boolean set(String key, Object value, int duration, TimeUnit timeUnit) {
		validateParam(key, value);
		String result = redisClient.invoke(jedisPool, (jedis) -> {
					String srtResult = jedis.set(key.getBytes(), JsonSerializer.serialize(value));
					if (duration <= ZERO) {
						//默认5 分钟
						jedis.pexpire(key.getBytes(), DEFAULT_CACHE_MILLIS);
					} else {
						//时间转换成毫秒
						long millis = getMillis(timeUnit, duration);
						jedis.pexpire(key.getBytes(), millis);
					}
					return srtResult;
				}

		);
		//是否成功
		return this.isStringEquals(OK, result);
	}

	/**
	 * 添加数据到redis
	 * 并设置永不过期
	 * 注：一般使用较少，数据过大时尽量不要使用
	 * 从 Redis 2.6.12 版本开始， SET 在设置操作成功完成时，返回 OK
	 *
	 * @param key
	 * @param value
	 */
	@Override
	public Boolean putNeverExpires(String key, Object value) {
		validateParam(key, value);
		String result = redisClient.invoke(jedisPool, (jedis) -> {
					return jedis.set(key.getBytes(), JsonSerializer.serialize(value));
				}
		);
		//是否成功
		return this.isStringEquals(OK, result);
	}

	/**
	 * 根据key 获取值
	 *
	 * @param key
	 * @param clazz 类class
	 * @return 类对象
	 */
	@Override
	public <T> T get(String key, Class<T> clazz) {
		validateKeyParam(key);
		byte[] result = redisClient.invoke(jedisPool, (jedis) -> jedis.get(key.getBytes()));
		if (result == null) {
			return null;
		}
		return JsonSerializer.deserialize(result, clazz);
	}

	/**
	 * 根据key集合批量获取
	 *
	 * @param keys   key集合
	 * @param classz 序列化对象
	 * @return 类对象
	 */
	@Override
	public <T> List<T> mget(List<String> keys, Class<T> classz) {
		if (CollectionUtils.isEmpty(keys)) {
			return new ArrayList<>(0);
		}
		String[] strings = new String[keys.size()];
		keys.toArray(strings);
		List<T> result = redisClient.invoke(jedisPool, jedis -> {
			List<String> list = jedis.mget(strings);
			List<T> resultList = new ArrayList<>();
			if (CollectionUtils.isEmpty(list)) {
				return new ArrayList<T>();
			}
			for (String str : list) {
				T t = JsonSerializer.deserialize(str.getBytes(), classz);
				resultList.add(t);
			}
			return resultList;
		});
		return result;
	}


	/**
	 * 根据key 获取值
	 * 返回 key 的值，如果 key 不存在时，返回 nil。
	 * 如果 key 不是字符串类型，那么返回一个错误。
	 *
	 * @param key
	 * @return String
	 */
	@Override
	public String get(String key) {
		return this.get(key, String.class);
	}

	/**
	 * 根据key 获取值
	 *
	 * @param key
	 * @param clazz 集合泛型对象
	 * @return 集合对象
	 */
	@Override
	public <T> List<T> getList(String key, Class<T> clazz) {
		String str = this.get(key, String.class);
		return JSONArray.parseArray(str, clazz);
	}

	/**
	 * 将key 的值设为 value ,当且仅当 key 不存在
	 * 默认值是时间戳 默认有效期是 5 分钟
	 *
	 * @param key
	 * @return 设置成功返回 true 失败返回false
	 */
	@Override
	public Boolean setNx(String key) {
		return this.setNx(key, System.currentTimeMillis(), FIVE, TimeUnit.MINUTES);
	}

	/**
	 * 将key 的值设为 value ,当且仅当 key 不存在
	 * 默认有效期是 5 分钟
	 *
	 * @param key
	 * @param value 自定义值
	 * @return 设置成功返回 true 失败返回false
	 */
	@Override
	public Boolean setNx(String key, Object value) {
		return this.setNx(key, value, FIVE, TimeUnit.MINUTES);
	}

	/**
	 * 将key 的值设为 value ,当且仅当 key 不存在
	 * 默认值是时间戳
	 *
	 * @param key
	 * @param seconds 自定义过期时间秒数
	 * @return 设置成功返回 true 失败返回false
	 */
	@Override
	public Boolean setNx(String key, int seconds) {
		return this.setNx(key, System.currentTimeMillis(), seconds, TimeUnit.SECONDS);
	}

	/**
	 * 将key 的值设为 value ,当且仅当 key 不存在
	 * 默认时间单位是秒
	 *
	 * @param key
	 * @param value   自定义 value
	 * @param seconds 自定义过期时间秒数
	 * @return 设置成功返回 true 失败返回false
	 */
	@Override
	public Boolean setNx(String key, Object value, int seconds) {
		return this.setNx(key, value, seconds, TimeUnit.SECONDS);
	}

	/**
	 * 将key 的值设为 value ,当且仅当 key 不存在
	 * 注：常用与分布式锁
	 *
	 * @param key
	 * @param value
	 * @param duration 时间量
	 * @param timeUnit 时间单位枚举
	 * @return 设置成功返回 true 失败返回false
	 */
	@Override
	public Boolean setNx(String key, Object value, int duration, TimeUnit timeUnit) {
		validateParam(key, value);
		return redisClient.invoke(jedisPool, (jedis) -> {
					long result = jedis.setnx(key.getBytes(), JsonSerializer.serialize(value));
					if (result >= 1) {
						if (duration <= ZERO) {
							//默认5 分钟
							jedis.pexpire(key.getBytes(), DEFAULT_CACHE_MILLIS);
							return true;
						} else {
							//时间转换成毫秒
							long millis = getMillis(timeUnit, duration);
							jedis.pexpire(key.getBytes(), millis);
							return true;
						}
					} else {
						return false;
					}
				}
		);
	}

	/**
	 * 设置指定 key 的值，并返回 key 的旧值
	 * 返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 null
	 * 注：默认有效期为 5分钟
	 *
	 * @param key
	 * @return String
	 */
	@Override
	public String getSet(String key, String value) {
		return this.getSet(key, value, FIVE, TimeUnit.MINUTES);
	}

	/**
	 * 设置指定 key 的值，并返回 key 的旧值
	 * 返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 null
	 *
	 * @param key
	 * @return string key 的旧值
	 */
	@Override
	public String getSet(String key, String value, int duration, TimeUnit timeUnit) {
		validateParam(key, value);
		return redisClient.invoke(jedisPool, (jedis) -> {
					String result = jedis.getSet(key, value);
					if (duration <= ZERO) {
						//设置默认过期时间 5 分钟
						jedis.pexpire(key.getBytes(), DEFAULT_CACHE_MILLIS);
						return result;
					} else {
						//时间转换成毫秒
						long millis = getMillis(timeUnit, duration);
						jedis.pexpire(key.getBytes(), millis);
						return result;
					}
				}
		);
	}

	/**
	 * 用于获取指定 key 所储存的字符串值的长度
	 * 当 key 储存的不是字符串值时，返回一个错误
	 * 当 key 不存在时，返回 0
	 *
	 * @param key
	 */
	@Override
	public Long getStrLen(String key) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) -> jedis.strlen(key.getBytes()));
	}

	/**
	 * key 中储存的数字值增一 (默认增量+1)
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作
	 * 注：
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内
	 *
	 * @param key
	 * @return 执行命令之后 key 的值。
	 */
	@Override
	public Long incr(String key) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) -> jedis.incr(key.getBytes()));
	}

	/**
	 * key 中储存的数字值增一 （自定义增量值 ）
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作
	 * 注：
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内
	 *
	 * @param key
	 * @param value 自定义增量值
	 * @return 执行命令之后 key 的值。
	 */
	@Override
	public Long incrBy(String key, long value) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) -> jedis.incrBy(key.getBytes(), value));
	}

	/**
	 * 为 key 中所储存的值加上指定的浮点数增量值
	 * 如果 key 不存在，那么 incrbyfloat 会先将 key 的值设为 0 ，再执行加法操作
	 *
	 * @param key
	 * @param value 增量值
	 * @return 执行命令之后 key 的值
	 */
	@Override
	public Double incrByFloat(String key, Double value) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) -> jedis.incrByFloat(key.getBytes(), value));
	}

	/**
	 * 将 key 中储存的数字值减一
	 * <p>
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内
	 *
	 * @param key
	 * @return 执行命令之后 key 的值
	 */
	@Override
	public Long decr(String key) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) -> jedis.decr(key.getBytes()));
	}

	/**
	 * 将 key 中储存的数字值减自定义减量
	 * <p>
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内
	 *
	 * @param key
	 * @param value 自定义减量值
	 * @return 执行命令之后 key 的值。
	 */
	@Override
	public Long decrBy(String key, long value) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) -> jedis.decrBy(key.getBytes(), value));
	}

	/**
	 * 用于为指定的 key 追加值
	 * <p>
	 * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value
	 * 追加到 key 原来的值的末尾。
	 * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value
	 * 就像执行 SET key value 一样
	 *
	 * @param key
	 * @param value
	 * @return 追加指定值之后， key 中字符串的长度
	 */
	@Override
	public Long append(String key, Object value) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) -> jedis.append(key.getBytes(), JsonSerializer.serialize(value)));
	}


	/* ------------------ key  相关操作--------------------------------*/

	/**
	 * 检查给定 key 是否存在
	 *
	 * @param key
	 * @return 是否存在
	 */
	@Override
	public Boolean exists(String key) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) -> jedis.exists(key.getBytes()));
	}

	/**
	 * 用于设置 key 的过期时间，
	 * key 过期后将不再可用。单位以秒计
	 *
	 * @param key
	 * @param seconds
	 * @return 是否设置成功
	 */
	@Override
	public Boolean expire(String key, int seconds) {
		return this.expire(key, seconds, TimeUnit.SECONDS);
	}

	/**
	 * 用于设置 key 的过期时间，key 过期后将不再可用
	 * 设置成功返回 1
	 * 当 key 不存在或者不能为 key 设置过期时间时返回 0
	 * <p>
	 * 时间枚举介绍
	 * TimeUnit.DAYS          //天
	 * TimeUnit.HOURS         //小时
	 * TimeUnit.MINUTES       //分钟
	 * TimeUnit.SECONDS       //秒
	 * TimeUnit.MILLISECONDS  //毫秒
	 * TimeUnit.NANOSECONDS   //毫微秒
	 * TimeUnit.MICROSECONDS  //微秒
	 *
	 * @param key
	 * @param duration 时间量与单位一起使用
	 * @param timeUnit 单位枚举类
	 * @return
	 */
	@Override
	public Boolean expire(String key, int duration, TimeUnit timeUnit) {
		validateKeyParam(key);
		//时间转换成毫秒
		long millis = getMillis(timeUnit, duration);
		Long lResult = redisClient.invoke(jedisPool, (jedis) -> jedis.pexpire(key.getBytes(), millis));
		return this.isLongEquals(LONG_ONE, lResult);
	}

	/**
	 * 根据key 获取过期时间秒数
	 * 不存在时返回负数
	 *
	 * @param key
	 * @return 剩余过期时间秒数
	 * 当 key 不存在时，返回 -2 。
	 * 当 key 存在但没有设置剩余生存时间时，返回 -1
	 * 否则，以秒为单位，返回 key 的剩余生存时间
	 */
	@Override
	public Long getExpiresTtl(String key) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) -> jedis.ttl(key.getBytes()));
	}

	/**
	 * 根据key 获取过期时间毫秒数
	 * 不存在时返回负数
	 *
	 * @param key
	 * @return 剩余过期时间毫秒数
	 * 当 key 不存在时，返回 -2
	 * 当 key 存在但没有设置剩余生存时间时，返回 -1
	 * 否则，以毫秒为单位，返回 key 的剩余生存时间
	 */
	@Override
	public Long getExpiresPttl(String key) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) -> jedis.pttl(key.getBytes()));
	}

	/**
	 * 移除 key 的过期时间，key 将持久保持。
	 * 当过期时间移除成功时，返回 1
	 * 如果 key 不存在或 key 没有设置过期时间，返回 0
	 *
	 * @param key
	 */
	@Override
	public Boolean persist(String key) {
		validateKeyParam(key);
		Long lResult = redisClient.invoke(jedisPool, (jedis) -> jedis.persist(key.getBytes()));
		return this.isLongEquals(LONG_ONE, lResult);
	}

	/**
	 * 根据key 获取存储类型
	 *
	 * @param key
	 * @return 返回 key 的数据类型
	 * 数据类型有：
	 * none (key不存在)
	 * string (字符串)
	 * list (列表)
	 * set (集合)
	 * zset (有序集)
	 * hash (哈希表)
	 */
	@Override
	public String getType(String key) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) -> jedis.type(key.getBytes()));
	}

	/**
	 * 用于删除已存在的键。不存在的 key 会被忽略
	 * 被删除 key 的数量
	 *
	 * @param key
	 */
	@Override
	public Long del(String key) {
		validateKeyParam(key);
		if (exists(key)) {
			return redisClient.invoke(jedisPool, (jedis) -> jedis.del(key.getBytes()));
		}
		return LONG_ZERO;
	}

	/**
	 * 查找所有符合给定模式( pattern)的 key 。
	 * 谨慎使用(存在性能问题)
	 * 会引发Redis锁，并且增加Redis的CPU占用
	 *
	 * @param pattern
	 * @return 符合给定模式的 key 列表 (Array)。
	 */
	public List<String> findKeys(String pattern) {
		Assert.hasText(pattern, "查找规则不能为空");
		return redisClient.invoke(jedisPool, jedis -> {
			Set<String> sets = jedis.keys(("*" + pattern + "*"));
			if (sets != null) {
				List<String> list = new ArrayList<>(sets.size());
				list.addAll(sets);
				return list;
			}
			return null;
		});
	}


	/* ------------------ set  类型相关操作--------------------------------*/

	/**
	 * 向集合添加元素
	 * 被添加到集合中的新元素的数量，不包括被忽略的元素。
	 *
	 * @param key
	 */
	@Override
	public Long sAdd(String key, Object value) {
		validateParam(key, value);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.sadd(key.getBytes(), JsonSerializer.serialize(value));
		});
	}


	/**
	 * 移除集合中元素
	 * 被成功移除的元素的数量，不包括被忽略的元素。
	 *
	 * @param key
	 */
	@Override
	public Long sRem(String key, Object value) {
		validateParam(key, value);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.srem(key.getBytes(), JsonSerializer.serialize(value));
		});
	}

	/**
	 * 获取集合的成员数
	 * 被成功移除的元素的数量，不包括被忽略的元素。
	 *
	 * @param key
	 * @return 集合的数量。 当集合 key 不存在时，返回 0 。
	 */
	@Override
	public Long sCard(String key) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.scard(key.getBytes());
		});
	}

	/**
	 * 判断 value 元素是否是集合 key 的成员
	 * 如果成员元素是集合的成员，返回 1 。 如果成员元素不是集合的成员，或 key 不存在，返回 0 。
	 *
	 * @param key
	 */
	@Override
	public Boolean sisMember(String key, Object value) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.sismember(key.getBytes(), JsonSerializer.serialize(value));
		});
	}

	/**
	 * 返回集合中的所有成员
	 *
	 * @param key
	 */
	@Override
	public <T> Set<T> sMembers(String key, Class<T> clazz) {
		validateKeyParam(key);
		Set<T> result = redisClient.invoke(jedisPool, jedis -> {
			Set<byte[]> setResult = jedis.smembers(key.getBytes());
			if (CollectionUtils.isEmpty(setResult)) {
				return new HashSet<T>(ZERO);
			}
			Set<T> set = new HashSet<>(setResult.size());
			for (byte[] bytes : setResult) {
				T t = JsonSerializer.deserialize(bytes, clazz);
				set.add(t);
			}
			return set;
		});
		return result;
	}

	/**
	 * 返回集合随机count个值
	 *
	 * @param key
	 */
	@Override
	public <T> Set<T> sRandMember(String key, Class<T> clazz, int count) {
		validateKeyParam(key);
		Set<T> result = redisClient.invoke(jedisPool, jedis -> {
			List<byte[]> setResult = jedis.srandmember(key.getBytes(), count);
			if (CollectionUtils.isEmpty(setResult)) {
				return new HashSet<T>(ZERO);
			}
			Set<T> set = new HashSet<>(setResult.size());
			for (byte[] bytes : setResult) {
				T t = JsonSerializer.deserialize(bytes, clazz);
				set.add(t);
			}
			return set;
		});
		return result;
	}


	/**
	 * 返回给定集合的交集
	 *
	 * @param keys
	 */
	@Override
	public <T> Set<T> sinter(Set<String> keys, Class<T> clazz) {
		if (CollectionUtils.isEmpty(keys)) {
			return new HashSet<T>(ZERO);
		}
		String[] strKeys = new String[keys.size()];
		keys.toArray(strKeys);
		return redisClient.invoke(jedisPool, jedis -> {
			Set<String> setResult = jedis.sinter(strKeys);
			if (CollectionUtils.isEmpty(setResult)) {
				return new HashSet<T>(ZERO);
			}
			Set<T> set = new HashSet<>(setResult.size());
			for (String str : setResult) {
				T t = JsonSerializer.deserialize(str.getBytes(), clazz);
				set.add(t);
			}
			return set;
		});
	}

	/**
	 * 返回给定集合的并集
	 *
	 * @param keys
	 */
	@Override
	public <T> Set<T> sunion(Set<String> keys, Class<T> clazz) {
		if (CollectionUtils.isEmpty(keys)) {
			return new HashSet<T>(ZERO);
		}
		String[] strKeys = new String[keys.size()];
		keys.toArray(strKeys);
		return redisClient.invoke(jedisPool, jedis -> {
			Set<String> setResult = jedis.sunion(strKeys);
			if (CollectionUtils.isEmpty(setResult)) {
				return new HashSet<T>(ZERO);
			}
			Set<T> set = new HashSet<>(setResult.size());
			for (String str : setResult) {
				T t = JsonSerializer.deserialize(str.getBytes(), clazz);
				set.add(t);
			}
			return set;
		});
	}

	/**
	 * 返回给定集合的差集
	 *
	 * @param keys
	 */
	@Override
	public <T> Set<T> sDiff(List<String> keys, Class<T> clazz) {
		if (CollectionUtils.isEmpty(keys)) {
			return new HashSet<T>(ZERO);
		}
		String[] strKeys = new String[keys.size()];
		keys.toArray(strKeys);
		return redisClient.invoke(jedisPool, jedis -> {
			Set<String> setResult = jedis.sdiff(strKeys);
			if (CollectionUtils.isEmpty(setResult)) {
				return new HashSet<T>(ZERO);
			}
			Set<T> set = new HashSet<>(setResult.size());
			for (String str : setResult) {
				T t = JsonSerializer.deserialize(str.getBytes(), clazz);
				set.add(t);
			}
			return set;
		});
	}

	/**
	 * 移除并返回集合中的一个随机元素
	 *
	 * @param key
	 */
	@Override
	public <T> T sPop(String key, Class<T> clazz) {
		validateKeyParam(key);
		byte[] value = redisClient.invoke(jedisPool, (jedis) -> jedis.spop(key.getBytes()));
		if (value != null) {
			return JsonSerializer.deserialize(value, clazz);
		}
		return null;
	}



	/* ------------------ list  相关操作--------------------------------*/

	/**
	 * Redis Rpush 命令用于将一个或多个值插入到列表的尾部(最右边)。
	 * <p>
	 * 如果列表不存在，一个空列表会被创建并执行 RPUSH 操作。 当列表存在但不是列表类型时，返回一个错误。
	 * <p>
	 * 注意：在 Redis 2.4 版本以前的 RPUSH 命令，都只接受单个 value 值。
	 *
	 * @param key
	 * @return 列表的长度。
	 */
	@Override
	public Long lRpushObject(String key, Object... value) {
		validateParam(key, value);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.rpush(key.getBytes(), JsonSerializer.serialize(value));
		});
	}


	/**
	 * 移除列表元素
	 * 移除列表中与参数 VALUE 相等的元素
	 *
	 * @param key
	 * @return 被移除元素的数量。 列表不存在时返回 0
	 * 根据count值,从列表中删除所有value相等的项
	 * (1) count>0 , 删除从左到右,最多count个value相等的项
	 * (2) count<0 ,删除从右到左,最多count绝对值个value相等的项
	 * (3) count=0 ,删除所有value相等的项
	 */
	@Override
	public Long lRem(String key, Object value) {
		validateParam(key, value);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.lrem(key.getBytes(), ZERO, JsonSerializer.serialize(value));
		});
	}


	/**
	 * 将一个或多个值插入到列表头部
	 * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作
	 * 当 key 存在但不是列表类型时，返回一个错误
	 * 注意：在Redis 2.4版本以前的 lpush 命令，都只接受单个 value 值
	 *
	 * @param key
	 * @return 列表的长度。
	 */
	@Override
	public Long lPush(String key, Object value) {
		validateParam(key, value);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.lpush(key.getBytes(), JsonSerializer.serialize(value));
		});
	}

	/**
	 * 通过索引来设置元素的值。
	 * 当索引参数超出范围，或对一个空列表进行 LSET 时，返回一个错误。
	 * 操作成功返回 ok ，否则返回错误信息。
	 *
	 * @param key
	 * @return 操作是否成功
	 */
	@Override
	public Boolean lset(String key, long index, Object value) {
		validateParam(key, value);
		return redisClient.invoke(jedisPool, jedis -> {
			String result = jedis.lset(key.getBytes(), index, JsonSerializer.serialize(value));
			return this.isStringEquals(OK, result);
		});
	}

	/**
	 * 将一个或多个值插入到列表尾部
	 * 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作
	 * 当 key 存在但不是列表类型时，返回一个错误
	 *
	 * @param key
	 * @return 列表的长度。
	 */
	@Override
	public Long rpush(String key, Object value) {
		validateParam(key, value);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.rpush(key.getBytes(), JsonSerializer.serialize(value));
		});
	}

	/**
	 * 对一个列表进行修剪(trim)
	 * 让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除
	 *
	 * @param key
	 * @param start 保留区间开始下标
	 * @param end   保留区间结束下标
	 * @return 是否成功
	 */
	@Override
	public Boolean lTrim(String key, long start, long end) {
		validateKeyParam(key);
		String result = redisClient.invoke(jedisPool, jedis -> {
			return jedis.ltrim(key.getBytes(), start, end);
		});
		//是否成功
		return this.isStringEquals(OK, result);
	}

	/**
	 * 用于返回列表的长度
	 * 如果列表 key 不存在，则 key 被解释为一个空列表，
	 * 返回 0 。 如果 key 不是列表类型，返回一个错误
	 *
	 * @param key
	 * @return list 集大小
	 */
	@Override
	public long lLen(String key) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.llen(key.getBytes());
		});
	}


	/**
	 * 通过索引获取列表中的元素
	 * 如果指定索引值不在列表的区间范围内，返回 null
	 * 使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 *
	 * @param key
	 * @param index 集合索引
	 * @return 元素信息
	 */
	@Override
	public <T> T lIndex(String key, int index, Class<T> clazz) {
		validateParam(key, clazz);
		T obj = redisClient.invoke(jedisPool, jedis -> {
			byte[] result = jedis.lindex(key.getBytes(), index);
			if (result == null) {
				return null;
			}
			return JsonSerializer.deserialize(result, clazz);
		});
		return obj;
	}

	/**
	 * 返回列表中指定区间内的字符串 元素
	 * 区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素
	 * 可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
	 *
	 * @param key
	 * @param start 开始位置
	 * @param end   结束位置
	 * @return 相应对象集合
	 */
	@Override
	public List<String> lRange(String key, int start, int end) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, jedis -> {
			List<String> strList = jedis.lrange(key, start, end);
			if (CollectionUtils.isEmpty(strList)) {
				return new ArrayList<>(ZERO);
			}
			return strList;
		});

	}


	/**
	 * 返回列表中指定区间内的元素
	 * 区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素
	 * 可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
	 *
	 * @param key
	 * @param clazz
	 * @param start 开始位置
	 * @param end   结束位置
	 * @return 相应对象集合
	 */
	@Override
	public <T> List<T> lRange(String key, Class<T> clazz, int start, int end) {
		validateKeyParam(key);
		List<T> result = redisClient.invoke(jedisPool, jedis -> {
			List<byte[]> set = jedis.lrange(key.getBytes(), start, end);
			List<T> list = new ArrayList<>(set.size());
			if (CollectionUtils.isEmpty(set)) {
				return new ArrayList<T>(ZERO);
			}
			for (byte[] bytes : set) {
				T t = JsonSerializer.deserialize(bytes, clazz);
				list.add(t);
			}
			return list;
		});
		return result;
	}

	/**
	 * 移除并返回列表的第一个元素
	 *
	 * @param key
	 * @return 列表的第一个元素。 当列表 key 不存在时，返回 null
	 */
	@Override
	public <T> T lPop(String key, Class<T> clazz) {
		validateParam(key, clazz);
		return redisClient.invoke(jedisPool, jedis -> {
			byte[] result = jedis.lpop(key.getBytes());
			if (result == null) {
				return null;
			}
			return JsonSerializer.deserialize(result, clazz);
		});
	}

	/**
	 * 移除并返回列表的最后一个元素
	 *
	 * @param key
	 * @return 列表的最后一个元素。 当列表不存在时，返回 null
	 */
	@Override
	public <T> T rPop(String key, Class<T> clazz) {
		validateParam(key, clazz);
		return redisClient.invoke(jedisPool, jedis -> {
			byte[] result = jedis.rpop(key.getBytes());
			if (result == null) {
				return null;
			}
			return JsonSerializer.deserialize(result, clazz);
		});
	}


	/* ------------------ hash  相关操作--------------------------------*/

	/**
	 * 用于查看哈希表的指定字段是否存在
	 *
	 * @param key
	 * @param field
	 */
	@Override
	public Boolean hexists(String key, String field) {
		validateParam(key, field);
		return redisClient.invoke(jedisPool, (jedis) -> jedis.hexists(key.getBytes(), field.getBytes()));
	}

	/**
	 * 用于为哈希表中的字段赋值
	 * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作
	 * 如果字段已经存在于哈希表中，旧值将被覆盖
	 *
	 * @param key
	 * @param field
	 * @param value 值
	 *              注:仅在field不存在，并成功添加时返回 true,
	 *              field已经存在，会更新内容，但添加状态是false
	 */
	@Override
	public Boolean hSet(String key, String field, Object value) {
		validateKeyParam(key);
		Long result = redisClient.invoke(jedisPool, (jedis) ->
				jedis.hset(key.getBytes(), field.getBytes(),
						JsonSerializer.serialize(value))
		);
		return this.isLongEquals(LONG_ONE, result);
	}

	@Override
	public Boolean hSet(String key, String field, Object value, int duration, TimeUnit timeUnit) {
		validateKeyParam(key);
		Long result = redisClient.invoke(jedisPool, (jedis) -> {
					Long hashResult = jedis.hset(key.getBytes(), field.getBytes(),
							JsonSerializer.serialize(value));
					jedis.pexpire(key.getBytes(), getMillis(timeUnit, duration));
					return hashResult;
				}
		);
		return this.isLongEquals(LONG_ONE, result);
	}


	/**
	 * 用于为哈希表中不存在的的字段赋值
	 * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作
	 * 如果字段已经存在于哈希表中，操作无效
	 * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令
	 * 设置成功，返回 1 。 如果给定字段已经存在且没有操作被执行，返回 0 。
	 * 注意版本 >= 2.0.0
	 *
	 * @param key
	 * @param field
	 * @param value 值
	 * @return 是否成功
	 */
	@Override
	public Boolean hSetNx(String key, String field, Object value) {
		validateKeyParam(key);
		Long result = redisClient.invoke(jedisPool, (jedis) ->
				jedis.hsetnx(key.getBytes(), field.getBytes(),
						JsonSerializer.serialize(value)));
		return this.isLongEquals(LONG_ONE, result);
	}


	/**
	 * key 中HASH储存的数字值增一 （自定义增量值 ）
	 *
	 * @param key
	 * @param field 属性名称
	 * @return 执行命令之后 key 的值。
	 */
	@Override
	public Long hashIncr(String key, String field) {
		return this.hashIncrBy(key, field, 1L);
	}

	/**
	 * key 中HASH储存的数字值增一 （自定义增量值 ）
	 *
	 * @param key
	 * @param value 自定义增量值
	 * @return 执行命令之后 key 的值。
	 */
	@Override
	public Long hashIncrBy(String key, String field, long value) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) ->
				jedis.hincrBy(key.getBytes(), field.getBytes(), value));
	}

	/**
	 * key 中HASH储存的数字值增一 （自定义增量值 ）
	 *
	 * @param key
	 * @param value 自定义增量值
	 * @return 执行命令之后 key 的值。
	 */
	@Override
	public Double hashIncrByFloat(String key, String field, Double value) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) ->
				jedis.hincrByFloat(key.getBytes(), field.getBytes(), value));
	}

	/**
	 * 获取存储在哈希表中指定字段的值
	 *
	 * @param key
	 * @param field
	 * @return 返回给定字段的值。如果给定的字段或 key 不存在时，返回 null
	 */
	@Override
	public <T> T hGet(String key, String field, Class<T> clazz) {
		validateKeyParam(key);
		byte[] value = redisClient.invoke(jedisPool, (jedis) -> jedis.hget(key.getBytes(), field.getBytes()));
		if (value != null) {
			return JsonSerializer.deserialize(value, clazz);
		}
		return null;
	}

	/**
	 * 根据key集合批量获取
	 *
	 * @param key    key集合
	 * @param fields field集合
	 * @param classz 序列化对象
	 * @return 类对象
	 */
	@Override
	public <T> List<T> hmget(String key, List<String> fields, Class<T> classz) {
		if (CollectionUtils.isEmpty(fields)) {
			return new ArrayList<>(0);
		}
		String[] strings = new String[fields.size()];
		fields.toArray(strings);

		List<T> result = redisClient.invoke(jedisPool, jedis -> {
			List<String> list = jedis.hmget(key, strings);

			List<T> resultList = new ArrayList<>();
			if (CollectionUtils.isEmpty(list)) {
				return new ArrayList<T>();
			}
			for (String strValue : list) {
				if (StringUtils.isNoneBlank(strValue)) {
					T t = JsonSerializer.deserialize(strValue.getBytes(), classz);
					resultList.add(t);
				}
			}

			return resultList;
		});
		return result;
	}


	/**
	 * 用于删除哈希表 key 中的个指定字段
	 *
	 * @param key
	 * @param field
	 * @return 被成功删除字段的数量，不包括被忽略的字段
	 */
	@Override
	public Long hdel(String key, String field) {
		validateParam(key, field);
		return redisClient.invoke(jedisPool, (jedis) -> jedis.hdel(key.getBytes(), field.getBytes()));
	}


	/**
	 * 获取hash 大小
	 *
	 * @param key
	 */
	@Override
	public Long hlen(String key) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) -> jedis.hlen(key.getBytes()));
	}

	/**
	 * 获取在哈希表中指定 key 的所有字段和值
	 *
	 * @param key
	 * @return 以列表形式返回哈希表的字段及字段值。 若 key 不存在，返回空列表。
	 */
	@Override
	public <T> Map<String, T> hgetAll(String key, Class<T> clazz) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) -> {
			Map<byte[], byte[]> map = jedis.hgetAll(key.getBytes());
			Map<String, T> resultMap = new HashMap<>(map.size());
			if (map != null) {
				for (Map.Entry<byte[], byte[]> item : map.entrySet()) {
					byte[] newKey = item.getKey();
					T newValue = JsonSerializer.deserialize(item.getValue(), clazz);
					resultMap.put(new String(newKey), newValue);
				}
				return resultMap;
			}
			return null;
		});
	}

	/**
	 * 获取在哈希表中指定 key 的所有field的值
	 *
	 * @param key
	 * @return 若 key 不存在，返回空列表。
	 */
	@Override
	public <T> List<T> hvals(String key, Class<T> clazz) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) -> {
			List<byte[]> byteList = jedis.hvals(key.getBytes());
			List<T> list = new ArrayList<>(byteList.size());
			if (CollectionUtils.isEmpty(byteList)) {
				return new ArrayList<T>(ZERO);
			}
			for (byte[] bytes : byteList) {
				T t = JsonSerializer.deserialize(bytes, clazz);
				list.add(t);
			}
			return list;
		});
	}

	/**
	 * 返回hash key 对应所有field
	 *
	 * @param key
	 * @return 当 key 不存在时，返回一个空表。
	 */
	@Override
	public Set<String> hkeys(String key) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, (jedis) -> {
			return jedis.hkeys(key);
		});
	}

	/**
	 * 获取Hash下所有数据
	 *
	 * @param hashKey
	 */
	@Override
	public Map<String, String> hgetall(String hashKey) {
		Map<String, String> result = redisClient.invoke(jedisPool, jedis -> jedis.hgetAll(hashKey));
		return result;
	}



	/* ------------------ zset  相关操作--------------------------------*/


	/**
	 * 添加元素到有序集合,有序集合是按照元素的score进行排列
	 * 注意： 在 Redis 2.4 版本以前， ZADD 每次只能添加一个元素
	 * 当 key 存在但不是有序集类型时，返回一个错误。
	 *
	 * @param key
	 * @param obj
	 * @param score 分值
	 * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员
	 */
	@Override
	public Long zAdd(String key, Object obj, double score) {
		validateParam(key, obj);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.zadd(key.getBytes(), score, JsonSerializer.serialize(obj));
		});
	}

	/**
	 * 根据分值批量添加
	 */
	@Override
	public void zAddWithMapScore(String key, Map<byte[], Double> members) {
		redisClient.invoke(jedisPool, jedis -> {
			jedis.zadd(key.getBytes(), members);
			return null;
		});
	}

	/**
	 * 根据key 计算集合中元素的数量
	 *
	 * @param key
	 * @return 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0
	 */
	@Override
	public long zCard(String key) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.zcard(key.getBytes());
		});
	}

	/**
	 * 根据key 计算在有序集合中指定区间分数的成员数
	 *
	 * @param key
	 * @param minScore 最小排序分值
	 * @param maxScore 最大排序分值
	 * @return 分数值在 min 和 max 之间的成员的数量。
	 */
	@Override
	public Long zCount(String key, double minScore, double maxScore) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.zcount(key.getBytes(), minScore, maxScore);
		});
	}

	/**
	 * 返回有序集中，指定区间内的成员 -> 从小到大
	 * 其中成员的位置按分数值递增(从小到大)来排序
	 * <p>
	 * 具有相同分数值的成员按字典序来排列
	 * 注意：下标参数0 为起始
	 * 负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推
	 *
	 * @param key
	 * @param clazz
	 * @param start 开始位置
	 * @param end   结束位置
	 * @return 相应对象集合
	 */
	@Override
	public <T> List<T> zRange(String key, Class<T> clazz, int start, int end) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, jedis -> {
			Set<byte[]> set = jedis.zrange(key.getBytes(), start, end);
			List<T> list = new ArrayList<>(set.size());
			if (CollectionUtils.isEmpty(set)) {
				return new ArrayList<T>(ZERO);
			}
			for (byte[] bytes : set) {
				T t = JsonSerializer.deserialize(bytes, clazz);
				list.add(t);
			}
			return list;
		});
	}

	/**
	 * 返回有序集中，指定区间内的成员 -> 从大到小
	 * 其中成员的位置按分数值递增(从大到小)来排序
	 *
	 * @param key
	 * @param clazz
	 * @param start 开始位置
	 * @param end   结束位置
	 * @return 指定区间内，带有分数值的有序集成员的列表。
	 */
	@Override
	public <T> List<T> zRevRange(String key, Class<T> clazz, int start, int end) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, jedis -> {
			Set<byte[]> set = jedis.zrevrange(key.getBytes(), start, end);
			List<T> list = new ArrayList<>(set.size());
			if (CollectionUtils.isEmpty(set)) {
				return new ArrayList<T>(ZERO);
			}
			for (byte[] bytes : set) {
				T t = JsonSerializer.deserialize(bytes, clazz);
				list.add(t);
			}
			return list;
		});
	}

	/**
	 * 通过分数返回有序集合指定区间内的成员 -> 从小到大
	 * 有序集成员按分数值递增(从小到大)次序排列
	 *
	 * @param key
	 * @param clazz
	 * @param minScore 最小分数
	 * @param maxScore 最大分数
	 * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
	 */
	@Override
	public <T> List<T> zRangeByScore(String key, Class<T> clazz, double minScore, double maxScore) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, jedis -> {
			Set<byte[]> set = jedis.zrangeByScore(key.getBytes(), minScore, maxScore);
			List<T> list = new ArrayList<>(set.size());
			if (CollectionUtils.isEmpty(set)) {
				return new ArrayList<T>(ZERO);
			}
			for (byte[] bytes : set) {
				T t = JsonSerializer.deserialize(bytes, clazz);
				list.add(t);
			}
			return list;
		});
	}

	/**
	 * 通过分数返回有序集合指定区间内的成员 -> 从大到小
	 * 有序集成员按分数值递增(从大到小)次序排列
	 *
	 * @param key
	 * @param clazz
	 * @param minScore 最小分数
	 * @param maxScore 最大分数
	 * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
	 */
	@Override
	public <T> List<T> zRevRangeByScore(String key, Class<T> clazz, double minScore, double maxScore) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, jedis -> {
			Set<byte[]> set = jedis.zrevrangeByScore(key.getBytes(), maxScore, minScore);
			List<T> list = new ArrayList<>(set.size());
			if (CollectionUtils.isEmpty(set)) {
				return new ArrayList<T>(ZERO);
			}
			for (byte[] bytes : set) {
				T t = JsonSerializer.deserialize(bytes, clazz);
				list.add(t);
			}
			return list;
		});
	}


	/**
	 * 返回有序集中指定成员的排名
	 * 按分数值递增(从小到大)顺序排列
	 * 排名以 0 为底，也就是说， 分数值最小的成员排名为 0
	 *
	 * @param key
	 * @param obj 成员对象
	 * @return 如果成员是有序集 key 的成员，返回 member 的排名。
	 * 如果成员不是有序集 key 的成员，返回空
	 */
	@Override
	public Long zRank(String key, Object obj) {
		validateParam(key, obj);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.zrank(key.getBytes(), JsonSerializer.serialize(obj));
		});
	}

	/**
	 * 返回有序集中指定成员的排名
	 * 分数值递减(从大到小)排序
	 * 排名以 0 为底，也就是说， 分数值最大的成员排名为 0
	 *
	 * @param key
	 * @param obj 成员对象
	 * @return 如果成员是有序集 key 的成员，返回 member 的排名。
	 * 如果成员不是有序集 key 的成员，返回空
	 */
	@Override
	public Long zRevRank(String key, Object obj) {
		validateParam(key, obj);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.zrevrank(key.getBytes(), JsonSerializer.serialize(obj));
		});
	}

	/**
	 * 移除有序集合中的个成员
	 * 名称为key 的有序集合中的元素 obj
	 *
	 * @param key
	 * @param obj 元素
	 * @return 被成功移除的成员的数量，不包括被忽略的成员
	 */
	@Override
	public Long zRem(String key, Object obj) {
		validateParam(key, obj);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.zrem(key.getBytes(), JsonSerializer.serialize(obj));
		});
	}

	/**
	 * 移除有序集合中给定的排名区间的所有成员
	 * 从排序小的开始删除
	 *
	 * @param start 开始位置 下标 0 开始
	 * @param end   结束位置
	 * @return 被移除成员的数量
	 */
	@Override
	public Long zRemRangeByRank(String key, int start, int end) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.zremrangeByRank(key.getBytes(), start, end);
		});
	}

	/**
	 * 移除有序集合中给定的分数区间的所有成员
	 * 从排序小的开始删除
	 *
	 * @param startScore 开始分值
	 * @param endScore   结束分值
	 * @return 被移除成员的数量
	 */
	@Override
	public Long zRemRangeByScore(String key, double startScore, double endScore) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.zremrangeByScore(key.getBytes(), startScore, endScore);
		});
	}

	/**
	 * 返回有序集中，成员的分数值
	 * 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 null
	 *
	 * @param key
	 * @param obj 成员对象
	 * @return 如果成员是有序集 key 的成员，返回 member 的排名
	 * 如果成员不是有序集 key 的成员，返回空
	 */
	@Override
	public Double zScore(String key, Object obj) {
		validateParam(key, obj);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.zscore(key.getBytes(), JsonSerializer.serialize(obj));
		});
	}


	@Override
	public Double zIncrBy(String key, Object obj, double score) {
		validateParam(key, obj);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.zincrby(key.getBytes(), score, JsonSerializer.serialize(obj));
		});
	}


	/* ------------------ 管道批量查询 - Pipeline -相关操作 --------------------------------*/


	/**
	 * 通过分值批量查询
	 *
	 * @param key
	 * @param tClass
	 * @param starts
	 * @param <T>
	 */
	@Override
	public <T> List<T> zRevRangePipeline(String key, Class<T> tClass, List<Integer> starts) {
		List<T> result = redisClient.invoke(jedisPool, jedis -> {
			Pipeline pipeline = jedis.pipelined();

			List<Response<Set<byte[]>>> responseList = new ArrayList<>();
			for (Integer start : starts) {
				Response<Set<byte[]>> response = pipeline.zrevrange(key.getBytes(), start, start);
				responseList.add(response);
			}
			pipeline.sync();
			if (CollectionUtils.isEmpty(responseList)) {
				return new ArrayList<T>();
			}
			List<T> list = new ArrayList<>(responseList.size());
			for (Response<Set<byte[]>> response : responseList) {
				for (byte[] bytes : response.get()) {
					T t = JsonSerializer.deserialize(bytes, tClass);
					list.add(t);
				}
			}
			return list;
		});
		return result;
	}

	/**
	 * 通过多个key 批量查询
	 */
	@Override
	public List<Map<String, String>> hGetAllPipeline(List<String> hashKeys) {
		List<Map<String, String>> result = redisClient.invoke(jedisPool, jedis -> {
			Pipeline pipeline = jedis.pipelined();

			List<Response<Map<String, String>>> responseList = new ArrayList<>();
			for (String hashKey : hashKeys) {
				responseList.add(pipeline.hgetAll(hashKey));
			}
			pipeline.sync();
			if (CollectionUtils.isEmpty(responseList)) {
				return new ArrayList<>();
			}
			List<Map<String, String>> list = new ArrayList<>();
			for (Response<Map<String, String>> response : responseList) {
				list.add(response.get());
			}
			return list;
		});
		return result;
	}

	@Override
	public List<String> hmGetAllPipeline(List<String> hashKeys, String fieldKey) {
		List<String> result = redisClient.invoke(jedisPool, jedis -> {
			Pipeline pipeline = jedis.pipelined();

			List<Response<List<String>>> responseList = new ArrayList<>();
			for (String hashKey : hashKeys) {
				responseList.add(pipeline.hmget(hashKey, fieldKey));
			}
			pipeline.sync();
			if (CollectionUtils.isEmpty(responseList)) {
				return new ArrayList<>();
			}
			List<String> list = new ArrayList<>();
			for (Response<List<String>> response : responseList) {
				if (null != response.get().get(0)) {
					list.add(response.get().get(0));
				}
			}
			return list;
		});
		return result;
	}


	/* ------------------ 发布订阅 -相关操作 --------------------------------*/

	@Override
	public Long publish(String key, Object value) {
		validateParam(key, value);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.publish(key.getBytes(), JsonSerializer.serialize(value));
		});
	}



	/* ------------------ 其它私有方法 -相关操作 --------------------------------*/

	/**
	 * 校验参数
	 */
	private void validateParam(String key, Object value) {
		this.validateKeyParam(key);
		Assert.notNull(value, "value不能为空");
		Assert.isInstanceOf(Object.class, value, "value没有实现Object接口，无法序列化");
	}

	/**
	 * 校验参数
	 */
	private void validateKeyParam(String key) {
		Assert.hasText(key, "key不能为空");
		Assert.notNull(jedisPool, "jedis连接初始化失败");
	}

	/**
	 * 判断long值是否相同
	 */
	private Boolean isLongEquals(Long valueOne, Long valueTwo) {
		if ((null != valueOne) && (null != valueTwo)) {
			return valueOne.intValue() == valueTwo.intValue();
		}
		return false;
	}

	/**
	 * 判断string值是否相同
	 */
	private Boolean isStringEquals(String strOne, String strTwo) {
		if ((StringUtils.isNoneBlank(strOne)) && (StringUtils.isNoneBlank(strTwo))) {
			return strOne.equals(strTwo);
		}
		return false;
	}

	public synchronized Jedis getRedis() {
		return jedisPool.getResource();
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * 时间秒数计算
	 *
	 * @param timeUnit 单位枚举
	 * @param duration 时间量
	 * @return 秒数
	 */
	public static int getSeconds(TimeUnit timeUnit, int duration) {
		return (int) timeUnit.toSeconds(duration);
	}

	/**
	 * 时间毫秒数计算
	 *
	 * @param timeUnit 单位枚举
	 * @param duration 时间量
	 * @return 毫秒数
	 */
	public static long getMillis(TimeUnit timeUnit, int duration) {
		return timeUnit.toMillis(duration);
	}

}
