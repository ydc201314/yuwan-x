package com.yuwan.common.redis.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.yuwan.common.redis.RedisUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisPool implements RedisUtils {

	@Autowired
	private JedisPool jedisPool;

	@Override
	public void set(String key, String value) {
		// 获取jedis连接对象
		Jedis jedis = this.jedisPool.getResource();

		jedis.set(key, value);

		// 释放资源，其实就是把连接还给连接池，必须执行
		jedis.close();

	}

	@Override
	public void set(String key, String value, Integer seconds) {
		// 获取jedis连接对象
		Jedis jedis = this.jedisPool.getResource();

		jedis.set(key, value);
		jedis.expire(key, seconds);

		// 释放资源，其实就是把连接还给连接池，必须执行
		jedis.close();

	}

	@Override
	public String get(String key) {
		// 获取jedis连接对象
		Jedis jedis = this.jedisPool.getResource();

		String result = jedis.get(key);

		// 释放资源，其实就是把连接还给连接池，必须执行
		jedis.close();

		return result;
	}

	@Override
	public void delete(String key) {
		// 获取jedis连接对象
		Jedis jedis = this.jedisPool.getResource();

		jedis.del(key);

		// 释放资源，其实就是把连接还给连接池，必须执行
		jedis.close();

	}

	@Override
	public void expire(String key, Integer seconds) {
		// 获取jedis连接对象
		Jedis jedis = this.jedisPool.getResource();

		jedis.expire(key, seconds);

		// 释放资源，其实就是把连接还给连接池，必须执行
		jedis.close();

	}

	@Override
	public Long incr(String key) {
		// 获取jedis连接对象
		Jedis jedis = this.jedisPool.getResource();

		Long count = jedis.incr(key);

		// 释放资源，其实就是把连接还给连接池，必须执行
		jedis.close();

		return count;
	}

}
