package com.yuwan.controller.portal;

import com.yuwan.common.utils.CookieUtils;
import com.yuwan.manager.pojo.User;
import com.yuwan.service.sso.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController {

	@Value("${YUWAN_TICKET}")
	private String YUWAN_TICKET;

	@Autowired
	private UserService userService;

	/**
	 * 用户注册
	 *
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "doRegister", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doRegister(User user) {
		// 调用单点登录服务，执行注册
		try {
			this.userService.doRegister(user);
			// 如果没有异常，表示用户注册成功
			// if(result.status == "200"){
			// 封装返回数据 map
			Map<String, Object> map = new HashMap<>();
			map.put("status", "200");

			// 返回封装的数据
			return map;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 如果有异常，则返回null
		return null;

	}

	/**
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "doLogin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doLogin(User user, HttpServletRequest request, HttpServletResponse response) {
		// 调用单点登录服务，用户登录
		String ticket = this.userService.doLogin(user);

		// 判断ticket不为空
		if (StringUtils.isNotBlank(ticket)) {
			// 把ticket放到cookie中
			CookieUtils.setCookie(request, response, this.YUWAN_TICKET, ticket, 60 * 60 * 24, true);

			// if (obj.status == 200) {
			// 登录成功，封装返回数据
			Map<String, Object> map = new HashMap<>();
			map.put("status", 200);

			// 返回数据
			return map;
		}

		// 如果ticket为空，表示用户登录失败
		return null;

	}

}
