package com.yuwan.portal.controller.cart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuwan.portal.controller.cart.service.CartCookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuwan.service.cart.CartService;
import com.yuwan.common.utils.CookieUtils;
import com.yuwan.manager.pojo.Cart;
import com.yuwan.manager.pojo.User;
import com.yuwan.service.sso.UserService;

/**
 * @discription
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年9月20日下午3:15:24
 * @version 1.0.0
 */
@Controller
@RequestMapping("cart")
public class CartController {

	@Value("${YUWAN_CART}")
	private String YUWAN_CART;

	@Autowired
	private UserService userService;

	@Autowired
	private CartService cartService;

	@Autowired
	private CartCookieService cartCookieService;

	// 把商品添加到购物车中
	// http://www.taotao.com/cart/679533.html?num=6
	/**
	 * 添加商品到购物车
	 *
	 * @param request
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "{itemId}", method = RequestMethod.GET)
	public String addItemByCart(HttpServletRequest request, HttpServletResponse response,
								@PathVariable("itemId") Long itemId, @RequestParam("num") Integer num) {
		// 获取用户登录信息
		// 获取cookie中的ticket
		String ticket = CookieUtils.getCookieValue(request, this.YUWAN_CART, true);
		// 使用单点登录服务，通过ticket查询用户信息
		User user = this.userService.queryUserByTicket(ticket);

		// 判断查询的用户数据是否为空
		if (user != null) {
			// 如果不为空，表示用户已登录
			// 调用购物车服务实现商品添加到购物车
			this.cartService.addItemByCart(user.getId(), itemId, num);
		} else {
			// 如果为空，表示用户未登录
			this.cartCookieService.addItemByCart(request, response, itemId, num);
		}

		return "redirect:/cart/show.html";
	}

	// 展示购物车详情页
	// http://www.taotao.com/cart/show.html
	/**
	 * 展示购物车详情页
	 *
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "show", method = RequestMethod.GET)
	public String show(Model model, HttpServletRequest request) {
		// 查询用户登录信息
		String ticket = CookieUtils.getCookieValue(request, this.YUWAN_CART, true);
		User user = this.userService.queryUserByTicket(ticket);

		// 声明购物车容器
		List<Cart> cartList = null;
		// 判断查询结果是否为空
		if (user != null) {
			// 如果不为空表示登录
			cartList = this.cartService.queryCartByUserId(user.getId());
		} else {
			// 如果为空表示未登录
			cartList = this.cartCookieService.queryCartByCookie(request);
		}

		// 把查询结果放到模型中，传递给前端展示
		model.addAttribute("cartList", cartList);

		return "cart";
	}

	// http://www.taotao.com/service/cart/update/num/1474391931/2
	// $.post("/service/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val()
	/**
	 * 修改购物车商品数量
	 *
	 * @param itemId
	 * @param num
	 */
	@RequestMapping(value = "update/num/{itemId}/{num}", method = RequestMethod.POST)
	@ResponseBody
	public void updateItemByCart(HttpServletRequest request, HttpServletResponse response,
								 @PathVariable("itemId") Long itemId, @PathVariable("num") Integer num) {
		// 查询用户登录信息
		String ticket = CookieUtils.getCookieValue(request, this.YUWAN_CART, true);
		User user = this.userService.queryUserByTicket(ticket);

		// 判断查询结果是否为空
		if (user != null) {
			// 如果不为空表示登录
			this.cartService.updateItemByCart(user.getId(), itemId, num);
		} else {
			// 如果为空表示未登录
			this.cartCookieService.updateItemByCart(request, response, itemId, num);
		}
	}

	// http://www.taotao.com/cart/delete/1474391931.html
	/**
	 * 删除购物车中的商品
	 *
	 * @param request
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
	public String deleteItemByCart(HttpServletRequest request, HttpServletResponse response,
								   @PathVariable("itemId") Long itemId) { // 查询用户登录信息
		// 查询用户登录信息
		String ticket = CookieUtils.getCookieValue(request, this.YUWAN_CART, true);
		User user = this.userService.queryUserByTicket(ticket);

		// 判断查询结果是否为空
		if (user != null) {
			// 如果不为空表示登录
			this.cartService.deleteItemByCart(user.getId(), itemId);
		} else {
			// 如果为空表示未登录
			this.cartCookieService.deleteItemByCart(request, response, itemId);
		}

		// 返回购物车详情页
		return "redirect:/cart/show.html";
	}
}
