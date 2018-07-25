package com.yuwan.service.impl.order;

import java.util.Date;
import java.util.List;

import com.github.abel533.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yuwan.common.redis.RedisUtils;
import com.yuwan.manager.dao.OrderItemMapper;
import com.yuwan.manager.dao.OrderShippingMapper;
import com.yuwan.manager.pojo.Order;
import com.yuwan.manager.pojo.OrderItem;
import com.yuwan.manager.pojo.OrderShipping;
import com.yuwan.service.order.OrderService;

/**
 * @discription 订单系统服务层实现类
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年9月25日下午2:31:24
 * @version 1.0.0
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Value("${YUWAN_ORDER_KEY}")
	private String YUWAN_ORDER_KEY;

	@Autowired
	private RedisUtils redisUtils;

	@Autowired
	@Qualifier("orderMapper")
	private Mapper<Order> orderMapper;

	@Autowired
	private OrderItemMapper orderItemMapper;

	@Autowired
	private OrderShippingMapper orderShippingMapper;

	public String saveOrder(Order order) {
		// 创建订单id,使用用户id加redis的唯一数
		String orderId = "" + order.getUserId() + this.redisUtils.incr(this.YUWAN_ORDER_KEY);

		// 存放订单数据
		order.setOrderId(orderId);
		order.setStatus(1);
		order.setCreateTime(new Date());

		this.orderMapper.insert(order);

		// 订单商品数据
		// 获取订单商品数据
		List<OrderItem> orderItems = order.getOrderItems();

		// 遍历，执行保存
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId);
			this.orderItemMapper.insert(orderItem);
		}

		// 存放订单物流数据
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId);

		this.orderShippingMapper.insert(orderShipping);

		return orderId;
	}

	public Order queryOrderByOrderId(String orderId) {
		// 根据订单id查询订单
		Order order = this.orderMapper.selectByPrimaryKey(orderId);

		// 根据订单id查询订单商品
		// 声明查询条件
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderId(orderId);
		List<OrderItem> orderItems = this.orderItemMapper.select(orderItem);

		// 根据订单id查询订单物流
		OrderShipping orderShipping = this.orderShippingMapper.selectByPrimaryKey(orderId);

		// 把订单商品和订单物流设置到订单中
		order.setOrderItems(orderItems);
		order.setOrderShipping(orderShipping);

		return order;
	}
}
