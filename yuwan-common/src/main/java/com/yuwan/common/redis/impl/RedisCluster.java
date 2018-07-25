package com.yuwan.common.redis.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.yuwan.common.redis.RedisUtils;

import redis.clients.jedis.JedisCluster;

public class RedisCluster implements RedisUtils {

	@Autowired
	private JedisCluster jedisCluster;

	@Override
	public void set(String key, String value) {
		this.jedisCluster.set(key, value);
	}

	@Override
	public void set(String key, String value, Integer seconds) {
		this.jedisCluster.set(key, value);
		this.jedisCluster.expire(key, seconds);
	}

	@Override
	public String get(String key) {
		String result = this.jedisCluster.get(key);
		return result;
	}

	@Override
	public void delete(String key) {
		this.jedisCluster.del(key);
	}

	@Override
	public void expire(String key, Integer seconds) {
		this.jedisCluster.expire(key, seconds);
	}

	@Override
	public Long incr(String key) {
		Long count = this.jedisCluster.incr(key);
		return count;
	}

}
