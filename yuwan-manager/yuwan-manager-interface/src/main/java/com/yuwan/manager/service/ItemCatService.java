package com.yuwan.manager.service;

import java.util.List;

import com.yuwan.manager.pojo.ItemCat;

public interface ItemCatService extends BaseService<ItemCat> {

	/**
	 * 根据类目ID查询商品类目
	 *
	 * @param parentId
	 * @return List<ItemCat>
	 */
	List<ItemCat> queryItemCatByParentId(Long parentId);

	/**
	 * 分页查询商品类目
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	// List<ItemCat> queryItemCatByPage(Integer page, Integer rows);
}
