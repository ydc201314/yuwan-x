package com.yuwan.manager.service;

import com.yuwan.common.pojo.EasyUIResult;
import com.yuwan.manager.pojo.Content;

public interface ContentService extends BaseService<Content> {

    /**
     * 分页查询内容信息
     *
     * @param page
     * @param rows
     * @param categoryId
     * @return EasyUIResult<Content>
     */
    EasyUIResult<Content> queryContentByPage(Integer page, Integer rows, Long categoryId);

    /**
     * 门户首页大广告信息查询
     *
     * @param categoryId
     * @return String
     */
    String queryAD(Long categoryId);
}
