package com.home.liuhao.zceshi;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.home.liuhao.system.po.SysUser;

public class RedisCeShi {

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Resource
	RedisTemplate<String, SysUser> redisTemplate;

	/**
	 * redis存储dome
	 */
	public void test1() {
		SysUser u = new SysUser();
		u.setUserName("测试");
		u.setId(UUID.randomUUID().toString());
		redisTemplate.opsForValue().set(u.getId(), u);
		SysUser su = (SysUser) redisTemplate.opsForValue().get(u.getId());
		System.out.println(su.toString());
		stringRedisTemplate.opsForValue().set("msg", "你好");
		String msg = stringRedisTemplate.opsForValue().get("msg");
		System.out.println(msg);

	}

}
