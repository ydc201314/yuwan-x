package com.yuwan.common.redis;

/**
 * @discription redis接口
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年8月21日下午3:35:27
 * @version 1.0.0
 */
public interface RedisUtils {
	
	/**
	 * 保存一条记录
	 *
	 * @param key
	 * @param value 
	 * @return void
	 */
	public void set(String key, String value);

	/**
	 * 根据key查询
	 *
	 * @param key
	 * @return 
	 * @return String
	 */
	public String get(String key);

	/**
	 * 删除一条记录
	 *
	 * @param key 
	 * @return void
	 */
	public void delete(String key);

	/**
	 * 根据key设置生存时间
	 *
	 * @param key
	 * @param seconds 
	 * @return void
	 */
	public void expire(String key, Integer seconds);

	/**
	 * 保存并设置生存时间
	 *
	 * @param key
	 * @param value
	 * @param seconds 
	 * @return void
	 */
	public void set(String key, String value, Integer seconds);

	/**
	 * 保存并设置生存时间
	 *
	 * @param key
	 * @return 
	 * @return Long
	 */
	public Long incr(String key);

}
