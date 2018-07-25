package com.yuwan.controller.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.yuwan.common.utils.CookieUtils;
import com.yuwan.manager.pojo.User;
import com.yuwan.service.sso.UserService;

public class OrderInterceptor implements HandlerInterceptor {

	@Value("${YUWAN_TICKET}")
	private String YUWAN_TICKET;

	@Autowired
	private UserService userService;

	// 进入controller方法之前执行
	// 权限控制，登录过滤
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 从cookie中查询ticket
		String ticket = CookieUtils.getCookieValue(request, this.YUWAN_TICKET, true);

		// 判断ticket是否为空
		if (StringUtils.isBlank(ticket)) {
			// 如果为空表示用户压根就没登陆过
			// 获取用户原来的登录信息
			String url = request.getRequestURL().toString();
			// 跳转到登陆页面，携带原来的访问地址
			response.sendRedirect("http://www.taotao.com/page/login.html?url=" + url);
			// 不能放行
			return false;
		}

		// 不为空，使用ticket从单点登录查询登录信息
		User user = this.userService.queryUserByTicket(ticket);

		// 判断查询结果是否为空
		if (user == null) {
			// 如果为空，表示用户登录超时
			// 获取用户原来的登录信息
			String url = request.getRequestURL().toString();
			// 跳转到登陆页面，携带原来的访问地址
			response.sendRedirect("http://www.taotao.com/page/login.html?url=" + url);
			// 不能放行
			return false;
		}

		// 如果用户查询到，需要放到request中，其他需要使用，直接从request中获取即可
		request.setAttribute("user", user);

		// 不为空，表示用户登录成功，放行
		return true;
	}

	// 进入controller方法之后，没有返回ModelAndView之前执行
	// 把需要在每个页面上公共展示的数据，在这里进行设置
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	// 执行完成controller方法后，返回ModelAndView之后执行
	// 可以在这里统一处理异常 ，也可以进行日志管理
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
