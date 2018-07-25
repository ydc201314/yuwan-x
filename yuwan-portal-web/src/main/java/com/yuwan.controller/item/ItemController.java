package com.yuwan.controller.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yuwan.manager.pojo.Item;
import com.yuwan.manager.pojo.ItemDesc;
import com.yuwan.manager.service.ItemDescService;
import com.yuwan.manager.service.ItemService;

/**
 * @discription
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年9月15日上午11:25:13
 * @version 1.0.0
 */
@Controller
@RequestMapping("item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemDescService itemDescService;

	/**
	 * 商品详情展示
	 * 
	 * @param model
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "{itemId}", method = RequestMethod.GET)
	public String queryItemByItemId(Model model, @PathVariable("itemId") Long itemId) {

		// 根据商品服务查询商品数据
		Item item = this.itemService.queryById(itemId);
		// 根据商品服务查询商品描述数据
		ItemDesc itemDesc = this.itemDescService.queryById(itemId);

		// 把商品数据放到模型中，传递给前台页面
		model.addAttribute("item", item);
		// 把商品描述数据放到模型中，传递给前台页面
		model.addAttribute("itemDesc", itemDesc);

		// 返回商品详情页的视图
		return "item";

	}
}
