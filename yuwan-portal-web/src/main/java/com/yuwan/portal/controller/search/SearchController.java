package com.yuwan.portal.controller.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yuwan.common.pojo.EasyUIResult;
import com.yuwan.manager.pojo.Item;
import com.yuwan.service.search.SearchService;

/**
 * @discription 所搜系统表现层
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年8月28日下午5:04:09
 * @version 1.0.0
 */
@Controller
@RequestMapping("search")
public class SearchController {

	@Autowired
	private SearchService searchService;
	@Value("${YUWAN_SEARCH_ROWS}")
	private Integer YUWAN_SEARCH_ROWS;

	/**
	 * 搜索商品
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String search(Model model, @RequestParam("q") String query,
			@RequestParam(value = "page", defaultValue = "1") Integer page) {
		// 解决乱码问题
		try {
			query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		EasyUIResult<Item> easyUIResult = this.searchService.search(query, page, this.YUWAN_SEARCH_ROWS);
		// 把数据放到模型中，页面展示
		// 搜索关键词
		model.addAttribute("query", query);

		// 搜索结果集list
		model.addAttribute("itemList", easyUIResult.getRows());

		// 当前页码数
		model.addAttribute("page", page);

		// 总页数,查询到的数据总数和每页显示条数进行计算
		long total = easyUIResult.getTotal();
		// long pages = total % 16 == 0 ? total / 16 : (total / 16) + 1;
		long pages = (total + this.YUWAN_SEARCH_ROWS - 1) / this.YUWAN_SEARCH_ROWS;
		model.addAttribute("totalPages", pages);

		return "search";
	}
}
