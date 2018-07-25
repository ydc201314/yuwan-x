package com.yuwan.service.impl.sso;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuwan.common.redis.RedisUtils;
import com.yuwan.manager.dao.UserMapper;
import com.yuwan.manager.pojo.User;
import com.yuwan.service.sso.UserService;

/**
 * @discription user服务层实现类
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年8月23日上午11:06:03
 * @version 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

	@Value("${SSO_TICKET_INCR}")
	private String SSO_TICKET_INCR;

	@Autowired
	private UserMapper userMapper;

	@Override
	public Boolean check(String param, Integer type) {
		// 声明查询条件
		User user = new User();

		// 设置条件
		switch (type) {
		case 1:
			user.setUsername(param);
			break;
		case 2:
			user.setPhone(param);
			break;
		case 3:
			user.setEmail(param);
			break;

		default:
			break;
		}

		// 判断查询的结果是否为0，如果为0表示数据可用
		// 如果可用返回true 如果不可用返回false
		return userMapper.selectCount(user) == 0;
	}

	@Autowired
	private RedisUtils redisUtils;

	@Value("${SSO_TICKET_KEY}")
	private String SSO_TICKET_KEY;

	// ObjectMapper工具类的作用：1.对象转json；2.json转对象；3.直接解析json
	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public User queryUserByTicket(String ticket) {
		// 从redis中查询用户登录数据
		// 每个用户生成的ticket是不一样的，为了redis的数据管理和维护方便，需要给key设置前缀
		String json = this.redisUtils.get(this.SSO_TICKET_KEY + ticket);

		// 判断返回的json不为空
		try {
			if (StringUtils.isNotBlank(json)) {
				// 如果用户执行查询，表示用户是活动，重置用户的登录时间，这里设置登录时间为1小时
				this.redisUtils.expire(this.SSO_TICKET_KEY + ticket, 60 * 60);

				// 解析返回的数据
				User user = MAPPER.readValue(json, User.class);
				return user;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 如果没有查到数据，获取解析数据异常，返回null
		return null;
	}

	@Override
	public void doRegister(User user) {
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());

		// 密码需要进行md5的加密
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));

		this.userMapper.insert(user);
	}

	@Override
	public String doLogin(User user) {
		// 使用Mapper从MySQL数据库中查询用户
		// 给密码加密，可以进行查找
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		// 查询
		User result = this.userMapper.selectOne(user);

		try {
			// 判断是否找到，如果不为null，代表用户登录成功
			if (result != null) {
				// 生成唯一key，就ticket
				// 生成唯一数ticket,可以使用redis的唯一数+用户id
				String ticket = "" + this.redisUtils.incr(this.SSO_TICKET_INCR) + result.getId();

				// 需要给key加前缀，方便管理
				this.redisUtils.set(this.SSO_TICKET_KEY + ticket, MAPPER.writeValueAsString(result), 60 * 60);

				// 返回ticket
				return ticket;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 如果为空，表示没有找到用户，代表登录失败，返回null
		return null;
	}

}
