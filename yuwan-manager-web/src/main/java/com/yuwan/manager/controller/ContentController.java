package com.yuwan.manager.controller;

import com.yuwan.common.pojo.EasyUIResult;
import com.yuwan.manager.pojo.Content;
import com.yuwan.manager.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    /**
     * 新增内容
     *
     * @param content
     *            void
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void SaveContent(Content content) {
        // 调用服务保存
        this.contentService.save(content);

    }

    /**
     * 分页查询内容信息
     *
     * @param page
     * @param rows
     * @param categoryId
     * @return TaoResult<Content>
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public EasyUIResult<Content> queryContentByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                    @RequestParam(value = "rows", defaultValue = "20") Integer rows, Long categoryId) {
        EasyUIResult<Content> taoResult = this.contentService.queryContentByPage(page, rows, categoryId);
        return taoResult;
    }
}
