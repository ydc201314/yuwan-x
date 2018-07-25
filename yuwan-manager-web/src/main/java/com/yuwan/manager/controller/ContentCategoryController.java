package com.yuwan.manager.controller;

import com.yuwan.manager.pojo.ContentCategory;
import com.yuwan.manager.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 终极实验,少写点bug
 */
@Controller
@RequestMapping("content/category")
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;
    /**
     * 查询内容分类
     *
     * @param parentId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<ContentCategory> queryContentCategoryByPrentId(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        // 调用服务查询
        List<ContentCategory> list = this.contentCategoryService.queryContentCategoryByParentId(parentId);
        return list;
    }
    /**
     * 新增分类
     *
     * @param contentCategory
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public ContentCategory addContentCategoty(ContentCategory contentCategory) {
        // 调用服务保存
        ContentCategory result = this.contentCategoryService.addContentCategory(contentCategory);
        return result;

    }
    /**
     * 更新内容分类
     *
     * @param contentCategory
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String updateContentCategoryById(ContentCategory contentCategory) {
        // 调用服务更新
        this.contentCategoryService.updateByIdSelective(contentCategory);

        return "200";

    }

    /**
     * 删除内容分类
     * @param parentId
     * @param id
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteContentCategoryById(Long parentId, Long id) {
        // 调用服务删除
        this.contentCategoryService.deleteContentCategoryById(parentId, id);
        return "200";

    }

}
