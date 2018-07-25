package com.yuwan.service.order;

import com.yuwan.manager.pojo.Order;

/**
 * @discription
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年9月25日下午2:27:41
 * @version 1.0.0
 */
public interface OrderService {

	/**
	 * 创建订单
	 * 
	 * @param order
	 * @return
	 */
	String saveOrder(Order order);

	/**
	 * 根据订单id查询订单
	 * 
	 * @param orderId
	 * @return
	 */
	Order queryOrderByOrderId(String orderId);

}
