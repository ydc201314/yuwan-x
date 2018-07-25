package com.yuwan.controller.cart.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuwan.common.utils.CookieUtils;
import com.yuwan.manager.pojo.Cart;
import com.yuwan.manager.pojo.Item;
import com.yuwan.manager.service.ItemService;

@Service
public class CartCookieService {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Value("${YUWAN_CART}")
	private String YUWAN_CART;

	@Autowired
	private ItemService itemService;

	/**
	 * 新增商品到cookie中的购物车
	 * 
	 * @param request
	 * @param response
	 * @param itemId
	 * @param num
	 */
	public void addItemByCart(HttpServletRequest request, HttpServletResponse response, Long itemId, Integer num) {
		// 从cookie查询购物车
		List<Cart> list = this.queryCartByCookie(request);

		Cart cart = null;
		// 遍历购物车商品是否存在
		for (Cart c : list) {
			if (c.getItemId().longValue() == itemId.longValue()) {
				cart = c;
				break;
			}
		}

		if (cart != null) {
			// 如果存在则商品数量相加
			cart.setNum(cart.getNum() + num);
			cart.setUpdated(new Date());

		} else {
			// 根据商品id查询商品数据
			Item item = this.itemService.queryById(itemId);

			// 如果不存在则商品新增到购物车
			cart = new Cart();
			cart.setItemId(itemId);
			// if (StringUtils.isNotBlank(item.getImage())) {
			if (item.getImages() != null) {
				// 判断图片不为空，则设置
				cart.setItemImage(item.getImages()[0]);
			} else {
				cart.setItemImage(null);
			}
			cart.setItemPrice(item.getPrice());
			cart.setItemTitle(item.getTitle());
			cart.setNum(num);
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());

			// 把新增的商品放到购物车中
			list.add(cart);

		}

		try {
			// 保存修改后的购物车到cookie中
			CookieUtils.setCookie(request, response, this.YUWAN_CART, MAPPER.writeValueAsString(list), 60 * 60 * 24 * 7,
					true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 查询cookie中的购物车
	 * 
	 * @param request
	 * @return
	 */
	public List<Cart> queryCartByCookie(HttpServletRequest request) {
		// 从cookie中获取购物车数据，查询到的购物车的json数据
		String json = CookieUtils.getCookieValue(request, this.YUWAN_CART, true);

		try {
			// 判断json是否为非空
			if (StringUtils.isNotBlank(json)) {
				// 解析json数据
				List<Cart> list = MAPPER.readValue(json,
						MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));

				// 返回数据
				return list;

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 如果查询到的数据是空，或者解析异常，则返回空集合，不能返回null
		return new ArrayList<Cart>();
	}

	/**
	 * 修改cookie中的购物车数量
	 * 
	 * @param request
	 * @param response
	 * @param itemId
	 * @param num
	 */
	public void updateItemByCart(HttpServletRequest request, HttpServletResponse response, Long itemId, Integer num) {
		// 查询购物车
		List<Cart> list = this.queryCartByCookie(request);

		// 遍历购物车
		for (Cart cart : list) {
			// 判断商品是否存在
			if (cart.getItemId().longValue() == itemId.longValue()) {
				// 如果存在则执行修改
				cart.setNum(num);
				cart.setUpdated(new Date());

				// 把修改后的购物车保存到cookie中
				try {
					CookieUtils.setCookie(request, response, this.YUWAN_CART, MAPPER.writeValueAsString(list),
							60 * 60 * 24 * 7, true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 跳出循环
				break;
			}

		}

	}

	/**
	 * 删除购物车中的商品
	 * 
	 * @param request
	 * @param response
	 * @param itemId
	 */
	public void deleteItemByCart(HttpServletRequest request, HttpServletResponse response, Long itemId) {
		// 查询
		List<Cart> list = this.queryCartByCookie(request);

		// 遍历
		for (Cart cart : list) {
			// 判断是否存在
			if (cart.getItemId().longValue() == itemId.longValue()) {
				// 删除
				list.remove(cart);

				// 保存购物车到cookie
				try {
					CookieUtils.setCookie(request, response, this.YUWAN_CART, MAPPER.writeValueAsString(list),
							60 * 60 * 24 * 7, true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			}

		}

	}

}
