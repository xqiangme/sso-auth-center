/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.framework.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
 *
 * @author menq
 */
public interface RedisCache {

	/* ------------------ string  类型相关操作--------------------------------*/

	/**
	 * 添加数据到redis
	 * 注意：设置默认过期时间  30 分钟
	 *
	 * @param key
	 * @param value
	 */
	Boolean set(String key, Object value);

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
	Boolean set(String key, Object value, int duration, TimeUnit timeUnit);

	/**
	 * 添加数据到redis
	 * 并设置永不过期
	 * 注：一般使用较少，数据过大时尽量不要使用
	 * 从 Redis 2.6.12 版本开始， SET 在设置操作成功完成时，返回 OK
	 *
	 * @param key
	 * @param value
	 */
	Boolean putNeverExpires(String key, Object value);

	/**
	 * 根据key 获取值
	 *
	 * @param key
	 * @param clazz 类class
	 * @return 类对象
	 */
	<T> T get(String key, Class<T> clazz);

	/**
	 * 根据key集合批量获取
	 *
	 * @param keys   key集合
	 * @param classz 序列化对象
	 * @return 类对象
	 */
	<T> List<T> mget(List<String> keys, Class<T> classz);

	/**
	 * 根据key 获取值
	 * 返回 key 的值，如果 key 不存在时，返回 nil。
	 * 如果 key 不是字符串类型，那么返回一个错误。
	 *
	 * @param key
	 * @return String
	 */
	String get(String key);

	/**
	 * 根据key 获取值
	 *
	 * @param key
	 * @param clazz 集合泛型对象
	 * @return 集合对象
	 */
	<T> List<T> getList(String key, Class<T> clazz);

	/**
	 * 将key 的值设为 value ,当且仅当 key 不存在
	 * 默认值是时间戳 默认有效期是 5 分钟
	 *
	 * @param key
	 * @return 设置成功返回 true 失败返回false
	 */
	Boolean setNx(String key);

	/**
	 * 将key 的值设为 value ,当且仅当 key 不存在
	 * 注意！！：默认有效期是 5 分钟
	 *
	 * @param key
	 * @param value 自定义值
	 * @return 设置成功返回 true 失败返回false
	 */
	Boolean setNx(String key, Object value);

	/**
	 * 将key 的值设为 value ,当且仅当 key 不存在
	 * 默认值是时间戳
	 *
	 * @param key
	 * @param seconds 自定义过期时间秒数
	 * @return 设置成功返回 true 失败返回false
	 */
	Boolean setNx(String key, int seconds);

	/**
	 * 将key 的值设为 value ,当且仅当 key 不存在
	 * 默认时间单位是秒
	 *
	 * @param key
	 * @param value   自定义 value
	 * @param seconds 自定义过期时间秒数
	 * @return 设置成功返回 true 失败返回false
	 */
	Boolean setNx(String key, Object value, int seconds);

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
	Boolean setNx(String key, Object value, int duration, TimeUnit timeUnit);

	/**
	 * 设置指定 key 的值，并返回 key 的旧值
	 * 返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 null
	 * 注意！！：默认有效期为 5分钟
	 *
	 * @param key
	 * @return String
	 */
	String getSet(String key, String value);

	/**
	 * 设置指定 key 的值，并返回 key 的旧值
	 * 返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 null
	 *
	 * @param key
	 * @return string key 的旧值
	 */
	String getSet(String key, String value, int duration, TimeUnit timeUnit);

	/**
	 * 用于获取指定 key 所储存的字符串值的长度
	 * 当 key 储存的不是字符串值时，返回一个错误
	 * 当 key 不存在时，返回 0
	 *
	 * @param key
	 */
	Long getStrLen(String key);

	/**
	 * key 中储存的数字值增一 (默认增量+1)
	 *
	 * @param key
	 * @return 执行命令之后 key 的值。
	 */
	Long incr(String key);

	/**
	 * key 中储存的数字值增一 （自定义增量值 ）
	 *
	 * @param key
	 * @param value 自定义增量值
	 * @return 执行命令之后 key 的值。
	 */
	Long incrBy(String key, long value);

	/**
	 * 为 key 中所储存的值加上指定的浮点数增量值
	 * 如果 key 不存在，那么 incrbyfloat 会先将 key 的值设为 0 ，再执行加法操作
	 *
	 * @param key
	 * @param value 增量值
	 * @return 执行命令之后 key 的值
	 */
	Double incrByFloat(String key, Double value);

	/**
	 * 将 key 中储存的数字值减一
	 *
	 * @param key
	 * @return 执行命令之后 key 的值
	 */
	Long decr(String key);

	/**
	 * 将 key 中储存的数字值减自定义减量
	 *
	 * @param key
	 * @param value 自定义减量值
	 * @return 执行命令之后 key 的值。
	 */
	Long decrBy(String key, long value);

	/**
	 * 用于为指定的 key 追加值
	 *
	 * @param key
	 * @param value
	 * @return 追加指定值之后， key 中字符串的长度
	 */
	Long append(String key, Object value);


	/* ------------------ key  相关操作--------------------------------*/

	/**
	 * 检查给定 key 是否存在
	 *
	 * @param key
	 * @return 是否存在
	 */
	Boolean exists(String key);

	/**
	 * 用于设置 key 的过期时间，
	 * key 过期后将不再可用。单位以秒计
	 *
	 * @param key
	 * @param seconds
	 * @return 是否设置成功
	 */
	Boolean expire(String key, int seconds);

	/**
	 * 用于设置 key 的过期时间，key 过期后将不再可用
	 * 设置成功返回 1
	 * 当 key 不存在或者不能为 key 设置过期时间时返回
	 *
	 * @param key
	 * @param duration 时间量与单位一起使用
	 * @param timeUnit 单位枚举类
	 */
	Boolean expire(String key, int duration, TimeUnit timeUnit);

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
	Long getExpiresTtl(String key);

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
	Long getExpiresPttl(String key);

	/**
	 * 移除 key 的过期时间，key 将持久保持。
	 * 当过期时间移除成功时，返回 1
	 * 如果 key 不存在或 key 没有设置过期时间，返回 0
	 *
	 * @param key
	 */
	Boolean persist(String key);

	/**
	 * 根据key 获取存储类型
	 *
	 * @param key
	 * @return 返回 key 的数据类型
	 */
	String getType(String key);

	/**
	 * 用于删除已存在的键。不存在的 key 会被忽略
	 * 被删除 key 的数量
	 *
	 * @param key
	 */
	Long del(String key);

	/* ------------------ set  类型相关操作--------------------------------*/

	/**
	 * 向集合添加元素
	 * 被添加到集合中的新元素的数量，不包括被忽略的元素。
	 *
	 * @param key
	 */
	Long sAdd(String key, Object value);

	/**
	 * 移除集合中元素
	 * 被成功移除的元素的数量，不包括被忽略的元素。
	 *
	 * @param key
	 */
	Long sRem(String key, Object value);

	/**
	 * 获取集合的成员数
	 * 被成功移除的元素的数量，不包括被忽略的元素。
	 *
	 * @param key
	 * @return 集合的数量。 当集合 key 不存在时，返回 0 。
	 */
	Long sCard(String key);

	/**
	 * 判断 value 元素是否是集合 key 的成员
	 * 如果成员元素是集合的成员，返回 1 。 如果成员元素不是集合的成员，或 key 不存在，返回 0 。
	 *
	 * @param key
	 */
	Boolean sisMember(String key, Object value);

	/**
	 * 返回集合中的所有成员
	 *
	 * @param key
	 */
	<T> Set<T> sMembers(String key, Class<T> clazz);

	/**
	 * 返回集合随机count个值
	 *
	 * @param key
	 */
	<T> Set<T> sRandMember(String key, Class<T> clazz, int count);

	/**
	 * 返回给定集合的交集
	 *
	 * @param keys
	 */
	<T> Set<T> sinter(Set<String> keys, Class<T> clazz);

	/**
	 * 返回给定集合的并集
	 *
	 * @param keys
	 */
	<T> Set<T> sunion(Set<String> keys, Class<T> clazz);

	/**
	 * 返回给定集合的差集
	 *
	 * @param keys
	 */
	<T> Set<T> sDiff(List<String> keys, Class<T> clazz);

	/**
	 * 移除并返回集合中的一个随机元素
	 *
	 * @param key
	 */
	<T> T sPop(String key, Class<T> clazz);

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
	Long lRpushObject(String key, Object... value);

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
	Long lRem(String key, Object value);

	/**
	 * 将一个或多个值插入到列表头部
	 * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作
	 * 当 key 存在但不是列表类型时，返回一个错误
	 * 注意：在Redis 2.4版本以前的 lpush 命令，都只接受单个 value 值
	 *
	 * @param key
	 * @return 列表的长度。
	 */
	Long lPush(String key, Object value);

	/**
	 * 通过索引来设置元素的值。
	 * 当索引参数超出范围，或对一个空列表进行 LSET 时，返回一个错误。
	 * 操作成功返回 ok ，否则返回错误信息。
	 *
	 * @param key
	 * @return 操作是否成功
	 */
	Boolean lset(String key, long index, Object value);

	/**
	 * 将一个或多个值插入到列表尾部
	 * 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作
	 * 当 key 存在但不是列表类型时，返回一个错误
	 *
	 * @param key
	 * @return 列表的长度。
	 */
	Long rpush(String key, Object value);

	/**
	 * 对一个列表进行修剪(trim)
	 * 让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除
	 *
	 * @param key
	 * @param start 保留区间开始下标
	 * @param end   保留区间结束下标
	 * @return 是否成功
	 */
	Boolean lTrim(String key, long start, long end);

	/**
	 * 用于返回列表的长度
	 * 如果列表 key 不存在，则 key 被解释为一个空列表，
	 * 返回 0 。 如果 key 不是列表类型，返回一个错误
	 *
	 * @param key
	 * @return list 集大小
	 */
	long lLen(String key);


	/**
	 * 通过索引获取列表中的元素
	 * 如果指定索引值不在列表的区间范围内，返回 null
	 * 使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 *
	 * @param key
	 * @param index 集合索引
	 * @return 元素信息
	 */
	<T> T lIndex(String key, int index, Class<T> clazz);

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
	List<String> lRange(String key, int start, int end);


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
	<T> List<T> lRange(String key, Class<T> clazz, int start, int end);

	/**
	 * 移除并返回列表的第一个元素
	 *
	 * @param key
	 * @return 列表的第一个元素。 当列表 key 不存在时，返回 null
	 */
	<T> T lPop(String key, Class<T> clazz);

	<T> T rPop(String key, Class<T> clazz);


	/* ------------------ hash  相关操作--------------------------------*/


	/**
	 * 用于查看哈希表的指定字段是否存在
	 *
	 * @param key
	 * @param field
	 */
	Boolean hexists(String key, String field);

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
	Boolean hSet(String key, String field, Object value);

	Boolean hSet(String key, String field, Object value, int duration, TimeUnit timeUnit);

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
	Boolean hSetNx(String key, String field, Object value);


	/**
	 * key 中HASH储存的数字值增一 （自定义增量值 ）
	 *
	 * @param key
	 * @param field 属性名称
	 * @return 执行命令之后 key 的值。
	 */
	Long hashIncr(String key, String field);

	/**
	 * key 中HASH储存的数字值增一 （自定义增量值 ）
	 *
	 * @param key
	 * @param value 自定义增量值
	 * @return 执行命令之后 key 的值。
	 */
	Long hashIncrBy(String key, String field, long value);

	/**
	 * key 中HASH储存的数字值增一 （自定义增量值 ）
	 *
	 * @param key
	 * @param value 自定义增量值
	 * @return 执行命令之后 key 的值。
	 */
	Double hashIncrByFloat(String key, String field, Double value);

	/**
	 * 获取存储在哈希表中指定字段的值
	 *
	 * @param key
	 * @param field
	 * @return 返回给定字段的值。如果给定的字段或 key 不存在时，返回 null
	 */
	<T> T hGet(String key, String field, Class<T> clazz);

	/**
	 * 根据key集合批量获取
	 *
	 * @param key    key集合
	 * @param fields field集合
	 * @param classz 序列化对象
	 * @return 类对象
	 */
	<T> List<T> hmget(String key, List<String> fields, Class<T> classz);

	/**
	 * 用于删除哈希表 key 中的个指定字段
	 *
	 * @param key
	 * @param field
	 * @return 被成功删除字段的数量，不包括被忽略的字段
	 */
	Long hdel(String key, String field);

	/**
	 * 获取hash 大小
	 *
	 * @param key
	 */
	Long hlen(String key);

	/**
	 * 获取在哈希表中指定 key 的所有字段和值
	 *
	 * @param key
	 * @return 以列表形式返回哈希表的字段及字段值。 若 key 不存在，返回空列表。
	 */
	<T> Map<String, T> hgetAll(String key, Class<T> clazz);

	/**
	 * 获取在哈希表中指定 key 的所有field的值
	 *
	 * @param key
	 * @return 若 key 不存在，返回空列表。
	 */
	<T> List<T> hvals(String key, Class<T> clazz);

	/**
	 * 返回hash key 对应所有field
	 *
	 * @param key
	 * @return 当 key 不存在时，返回一个空表。
	 */
	Set<String> hkeys(String key);

	/**
	 * 获取Hash下所有数据
	 *
	 * @param hashKey
	 */
	Map<String, String> hgetall(String hashKey);


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
	Long zAdd(String key, Object obj, double score);

	/**
	 * 根据分值批量添加
	 */
	void zAddWithMapScore(String key, Map<byte[], Double> members);

	/**
	 * 根据key 计算集合中元素的数量
	 *
	 * @param key
	 * @return 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0
	 */
	long zCard(String key);

	/**
	 * 根据key 计算在有序集合中指定区间分数的成员数
	 *
	 * @param key
	 * @param minScore 最小排序分值
	 * @param maxScore 最大排序分值
	 * @return 分数值在 min 和 max 之间的成员的数量。
	 */
	Long zCount(String key, double minScore, double maxScore);

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
	<T> List<T> zRange(String key, Class<T> clazz, int start, int end);

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
	<T> List<T> zRevRange(String key, Class<T> clazz, int start, int end);

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
	<T> List<T> zRangeByScore(String key, Class<T> clazz, double minScore, double maxScore);

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
	<T> List<T> zRevRangeByScore(String key, Class<T> clazz, double minScore, double maxScore);


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
	Long zRank(String key, Object obj);

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
	Long zRevRank(String key, Object obj);

	/**
	 * 移除有序集合中的个成员
	 * 名称为key 的有序集合中的元素 obj
	 *
	 * @param key
	 * @param obj 元素
	 * @return 被成功移除的成员的数量，不包括被忽略的成员
	 */
	Long zRem(String key, Object obj);

	/**
	 * 移除有序集合中给定的排名区间的所有成员
	 * 从排序小的开始删除
	 *
	 * @param start 开始位置 下标 0 开始
	 * @param end   结束位置
	 * @return 被移除成员的数量
	 */
	Long zRemRangeByRank(String key, int start, int end);

	/**
	 * 移除有序集合中给定的分数区间的所有成员
	 * 从排序小的开始删除
	 *
	 * @param startScore 开始分值
	 * @param endScore   结束分值
	 * @return 被移除成员的数量
	 */
	Long zRemRangeByScore(String key, double startScore, double endScore);

	/**
	 * 返回有序集中，成员的分数值
	 * 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 null
	 *
	 * @param key
	 * @param obj 成员对象
	 * @return 如果成员是有序集 key 的成员，返回 member 的排名
	 * 如果成员不是有序集 key 的成员，返回空
	 */
	Double zScore(String key, Object obj);

	Double zIncrBy(String key, Object obj, double score);


	/* ------------------ 管道批量查询 - Pipeline -相关操作 --------------------------------*/

	/**
	 * zset 数据通过分值批量查询
	 *
	 * @param key
	 * @param tClass
	 * @param starts
	 * @param <T>
	 */
	<T> List<T> zRevRangePipeline(String key, Class<T> tClass, List<Integer> starts);

	/**
	 * 通过多个key 批量查询
	 */
	List<Map<String, String>> hGetAllPipeline(List<String> hashKeys);

	/**
	 * hash 数据 通过多个key 以及 filed 查询
	 *
	 * @param hashKeys
	 * @param fieldKey
	 * @return
	 */
	List<String> hmGetAllPipeline(List<String> hashKeys, String fieldKey);

	/* ------------------ 发布订阅 -相关操作 --------------------------------*/

	/**
	 * 发布订阅
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	Long publish(String key, Object value);
}
