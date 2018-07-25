package com.yuwan.controller.sso;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuwan.manager.pojo.User;
import com.yuwan.service.sso.UserService;

/**
 * @discription user表现层
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年8月23日上午11:16:16
 * @version 1.0.0
 */
@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;

	// ObjectMapper工具类的作用：1.对象转json；2.json转对象；3.直接解析json
	private static final ObjectMapper MAPPER = new ObjectMapper();
	/**
	 * 校验数据是否可用
	 *
	 * @param request
	 * @param param
	 * @param type
	 * @return 
	 * @return ResponseEntity<String>
	 */
	@RequestMapping(value = "check/{param}/{type}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> check(HttpServletRequest request, @PathVariable("param") String param,
			@PathVariable("type") Integer type) {
		try {
			if (type > 3 || type < 1) {
				// 如果参数不为1、2、3，则返回400
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}

			String result = "";
			// 改造支持jsonp：1. 获取callback
			String callback = request.getParameter("callback");

			// 调用服务校验数据
			Boolean bool = this.userService.check(param, type);
			// 改造支持jsonp：2. 判断callback，如果callback存在，则执行包裹
			if (StringUtils.isNotBlank(callback)) {
				// 改造支持jsonp：3. 使用callback包裹返回的数据
				result = callback + "(" + bool + ")";
			} else {
				result = "" + bool;
			}

			// 200
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 根据ticket查询用户
	 *
	 * @param ticket
	 * @return 
	 * @return ResponseEntity<User>
	 */
	@RequestMapping(value = "{ticket}", method = RequestMethod.GET)
	public ResponseEntity<String> queryUserByTicket(HttpServletRequest request, @PathVariable("ticket") String ticket) {
		try {
			String result = "";
			// 调用用户服务，根据ticket查询用户数据
			User user = this.userService.queryUserByTicket(ticket);
			String jsonUser = MAPPER.writeValueAsString(user);

			String callback = request.getParameter("callback");
			if (StringUtils.isNotBlank(callback)) {
				// 改造支持jsonp：3. 使用callback包裹返回的数据
				result = callback + "(" + jsonUser + ")";
			} else {
				result = "" + jsonUser;
			}
			if (result != null) {
				return ResponseEntity.ok(result);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
