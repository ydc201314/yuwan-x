package com.yuwan.controller.portal;

import com.yuwan.manager.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @discription 跳转到首页
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年8月18日下午2:55:36
 * @version 1.0.0
 */
@Controller
@RequestMapping("index")
public class IndexController {

	@Autowired
	private ContentService contentService;

	@Value("${YUWAN_PORTAL_AD}")
	private Long YUWAN_PORTAL_AD;

	/**
	 * 门户首页展示
	 *
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String toIndex(Model model) {
		// 调用内容服务，查询大广告数据,大广告分类id为31
		String AD = this.contentService.queryAD(this.YUWAN_PORTAL_AD);

		// 把大广告轮播数据放到Model中，传递给前台页面
		model.addAttribute("AD", AD);

		return "index";
	}

}
