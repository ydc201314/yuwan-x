package com.yuwan.manager.controller;

import com.yuwan.common.pojo.EasyUIResult;
import com.yuwan.manager.pojo.Item;
import com.yuwan.manager.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    /**
     * @Description: 添加商品
     * @Date:9:25 2018/6/14
     * @Params:[item, desc]
     */
    public void saveItem(Item item, String desc) {
        this.itemService.save(item, desc);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public EasyUIResult<Item> queryItemList(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "rows", defaultValue = "30") Integer rows) {
        EasyUIResult<Item> easyUIResult = this.itemService.queryItemList(page, rows);
        return easyUIResult;
    }
}
