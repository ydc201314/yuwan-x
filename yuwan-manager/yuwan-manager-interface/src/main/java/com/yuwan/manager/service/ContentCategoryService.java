package com.yuwan.manager.service;

import com.yuwan.manager.pojo.ContentCategory;

import java.util.List;

public interface ContentCategoryService extends BaseService<ContentCategory> {

    /**
     * 后台管理中内容管理树列表的初始化，根据parentID查询内容分类
     *
     * @param parentId
     * @return List<ContentCategory>
     */
    List<ContentCategory> queryContentCategoryByParentId(Long parentId);

    /**
     * 新增内容分类
     *
     * @param contentCategory
     * @return ContentCategory
     */
    ContentCategory addContentCategory(ContentCategory contentCategory);

    /**
     * 删除内容分类
     *
     * @param parentId
     * @param id       void
     */
    void deleteContentCategoryById(Long parentId, Long id);
}
