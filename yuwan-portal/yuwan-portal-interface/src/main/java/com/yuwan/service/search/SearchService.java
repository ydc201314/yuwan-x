package com.yuwan.service.search;

import com.yuwan.common.pojo.EasyUIResult;
import com.yuwan.manager.pojo.Item;

/**
 * @discription 所搜服务层接口
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年8月28日下午3:49:00
 * @version 1.0.0
 */
public interface SearchService {

	/**
	 * 根据关键词搜索
	 *
	 * @param query
	 * @param page
	 * @param rows
	 * @return
	 * @return EasyUIResult<Item>
	 */
	EasyUIResult<Item> search(String query, Integer page, Integer rows);

	/**
	 * 根据商品ID更新索引库
	 *
	 * @param itemId 
	 * @return void
	 */
	void saveItem(long itemId);

}
