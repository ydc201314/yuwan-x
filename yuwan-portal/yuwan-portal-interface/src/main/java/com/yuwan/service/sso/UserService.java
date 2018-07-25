package com.yuwan.service.sso;

import com.yuwan.manager.pojo.User;

/**
 * @discription user服务层接口
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年8月23日上午11:01:57
 * @version 1.0.0
 */
public interface UserService {

	/**
	 * 校验数据是否可用
	 * 
	 * @param param
	 * @param type
	 * @return
	 */
	Boolean check(String param, Integer type);

	/**
	 * 根据ticket查询用户
	 * 
	 * @param ticket
	 * @return
	 */
	User queryUserByTicket(String ticket);

	/**
	 * 用户注册
	 * 
	 * @param user
	 */
	void doRegister(User user);

	/**
	 * 用户登录
	 * 
	 * @param user
	 * @return
	 */
	String doLogin(User user);
}
