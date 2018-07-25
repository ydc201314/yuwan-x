package com.yuwan.service.impl.cart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuwan.service.cart.CartService;
import com.yuwan.common.redis.RedisUtils;
import com.yuwan.manager.dao.ItemMapper;
import com.yuwan.manager.pojo.Cart;
import com.yuwan.manager.pojo.Item;

/**
 * @discription 购物车服务层实现类
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年9月20日上午11:36:08
 * @version 1.0.0
 */
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private RedisUtils redisUtils;

	@Autowired
	private ItemMapper itemMapper;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Value("${YUWAN_CART_KEY}")
	private String YUWAN_CART_KEY;

	@Override
	public void addItemByCart(Long userId, Long itemId, Integer num) {
		// 根据userId查询购物车数据
		List<Cart> list = this.queryCartByUserId(userId);

		Cart cart = null;
		// 遍历购物车，判断商品是否存在于购物车中
		for (Cart c : list) {
			if (c.getItemId().longValue() == itemId.longValue()) {
				// 商品存在，则进行复制
				cart = c;
				// 跳出循环
				break;
			}
		}

		if (cart != null) {
			// 如果商品存在，则商品数量相加
			cart.setNum(cart.getNum() + num);
			cart.setUpdated(new Date());

		} else {
			// 根据商品id查询MySql数据库,获取商品数据
			Item item = this.itemMapper.selectByPrimaryKey(itemId);

			// 如果商品不存在，则直接添加商品到购物车中
			cart = new Cart();
			cart.setItemId(itemId);
			cart.setUserId(userId);
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

			// 把新加的购物车放到购物车集合中
			list.add(cart);

		}

		try {
			// 把修改后的购物车放到redis中
			this.redisUtils.set(this.YUWAN_CART_KEY + userId, MAPPER.writeValueAsString(list));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public List<Cart> queryCartByUserId(Long userId) {
		// 使用RedisUtils查询redis数据库，获取用户的购物车数据
		// redis中存放的是json格式的数据
		// 保存用户的购物车的key需要设置前缀，方便管理和维护。key使用的是用户id
		String json = this.redisUtils.get(this.YUWAN_CART_KEY + userId);

		try {
			// 判断查询结果不为空
			if (StringUtils.isNotBlank(json)) {
				// 解析json格式的数据,需要把json格式的数据转为List
				List<Cart> list = MAPPER.readValue(json,
						MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));

				// 返回购物车数据
				return list;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 如果查询的结果为空，或者解析出现异常，则返回返回空集合，不能返回null
		return new ArrayList<Cart>();
	}

	public static void main(String[] args) {
		// List<Object> list = null;
		// List<Object> list = new ArrayList<>();
		//
		// for (Object object : list) {
		// System.out.println(1);
		// }
		// System.out.println(2);

		Integer a = 1000;
		Integer aa = 1000;
		Long b = 1000l;
		Long bb = 1000l;

		System.out.println(a.intValue() == aa.intValue());
		System.out.println(b.longValue() == bb.longValue());

	}

	@Override
	public void updateItemByCart(Long userId, Long itemId, Integer num) {
		// 根据用户id从redis中查询购物车
		List<Cart> list = this.queryCartByUserId(userId);

		// 遍历购物车，找到需要修改的商品
		for (Cart cart : list) {
			if (cart.getItemId().longValue() == itemId.longValue()) {
				// 如果找到，则设置num商品数量
				cart.setNum(num);

				try {
					// 把修改好的购物车数据保存到redis中
					this.redisUtils.set(this.YUWAN_CART_KEY + userId, MAPPER.writeValueAsString(list));
				} catch (Exception e) {
					e.printStackTrace();
				}

				// 跳出循环
				break;
			}

		}

		// 如果没有找到商品，则神马都不干
	}

	@Override
	public void deleteItemByCart(Long userId, Long itemId) {
		// 根据用户id查询购物车
		List<Cart> list = this.queryCartByUserId(userId);

		// 遍历查询结果，判断商品是否存在
		for (Cart cart : list) {
			if (cart.getItemId().longValue() == itemId.longValue()) {
				// 如果商品存在，则执行删除
				list.remove(cart);

				try {
					// 把修改后的购物车保存到redis中
					this.redisUtils.set(this.YUWAN_CART_KEY + userId, MAPPER.writeValueAsString(list));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 跳出循环
				break;
			}
		}

	}

}
