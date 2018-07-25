package com.yuwan.controller.portal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuwan.service.cart.CartService;
import com.yuwan.manager.pojo.Cart;
import com.yuwan.manager.pojo.Order;
import com.yuwan.manager.pojo.User;
import com.yuwan.service.order.OrderService;
import com.yuwan.service.sso.UserService;

/**
 * @discription
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年9月25日上午9:42:14
 * @version 1.0.0
 */
@Controller
@RequestMapping("order")
public class OrderController {

	@Value("${YUWAN_TICKET}")
	private String YUWAN_TICKET;

	@Autowired
	private UserService userService;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	// http://www.taotao.com/order/create.html
	/**
	 * 跳转到订单结算页
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, HttpServletRequest request) {
		// 查询用户数据
		User user = (User) request.getAttribute("user");

		// 根据用户数据查询购物车
		List<Cart> carts = this.cartService.queryCartByUserId(user.getId());

		// 把用户的购物车中所有商品添加到订单中，把购物车的商品显示在页面中
		model.addAttribute("carts", carts);
		// }
		return "order-cart";
	}

	// type : "POST",
	// dataType : "json",
	// url : "/service/order/submit",
	// if(result.status == 200){
	// location.href = "/order/success.html?id="+result.data;
	/**
	 * 创建订单
	 * 
	 * @param request
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "submit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveOrder(HttpServletRequest request, Order order) {
		// 获取用户信息,拦截器把用户信息放到了request中，直接获取即可
		User user = (User) request.getAttribute("user");

		// 设置用户信息到订单中
		order.setUserId(user.getId());// 设置用户id
		order.setBuyerNick(user.getUsername());// 设置用户昵称，我们使用username

		// 调用订单服务保存订单到数据库中
		String orderId = this.orderService.saveOrder(order);

		// 判断如果订单创建成功，则返回订单id
		if (StringUtils.isNotBlank(orderId)) {
			// 创建订单成功，封装返回数据
			Map<String, Object> map = new HashMap<>();

			map.put("status", 200);
			map.put("data", orderId);
			return map;
		}

		// 如果创建失败,返回null
		return null;
	}

	// http://www.taotao.com/order/success.html?id=81
	/**
	 * 显示订单创建成功页
	 * 
	 * @param model
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "success", method = RequestMethod.GET)
	public String success(Model model, @RequestParam("id") String orderId) {
		// 通过订单服务，根据订单id查询订单
		Order order = this.orderService.queryOrderByOrderId(orderId);

		// 把订单数据放到模型中，传递给前台页面
		model.addAttribute("order", order);

		// 把当前时间往后推3天放到模型中，传递给前台页面，送达时间
		model.addAttribute("date", new DateTime().plusDays(3).toString("yyyy年MM月dd日  HH时mm分ss秒SSS毫秒"));

		return "success";
	}

}
