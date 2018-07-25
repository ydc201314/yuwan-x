package com.yuwan.service.cart;

import java.util.List;

import com.yuwan.manager.pojo.Cart;

/**
 * @discription 购物车服务接口
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年9月20日上午9:53:33
 * @version 1.0.0
 */
public interface CartService {

	/**
	 * 把商品添加到redis中的购物车
	 * 
	 * @param userId
	 * @param itemId
	 * @param num
	 */
	void addItemByCart(Long userId, Long itemId, Integer num);

	/**
	 * 根据用户id，从redis中查询购物车数据
	 * 
	 * @param userId
	 * @return
	 */
	List<Cart> queryCartByUserId(Long userId);

	/**
	 * 修改购物车商品数量
	 * 
	 * @param userId
	 * @param itemId
	 * @param num
	 */
	void updateItemByCart(Long userId, Long itemId, Integer num);

	/**
	 * 删除购物车商品
	 * 
	 * @param id
	 * @param itemId
	 */
	void deleteItemByCart(Long id, Long itemId);

}
