package com.yuwan.manager.service.impl;

import com.github.abel533.mapper.Mapper;
import com.yuwan.manager.pojo.ContentCategory;
import com.yuwan.manager.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentCategoryServiceImpl extends BaseServiceImpl<ContentCategory> implements ContentCategoryService {

    @Autowired
    @Qualifier("contentCategoryMapper")
    private Mapper<ContentCategory> mapper;
    public Mapper<ContentCategory> getMapper(){
       return mapper;
    }

    public List<ContentCategory> queryContentCategoryByParentId(Long parentId) {
        // 设置查询条件
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setParentId(parentId);

        // 查询
        List<ContentCategory> list = super.queryListByWhere(contentCategory);
        return list;

    }

    public ContentCategory addContentCategory(ContentCategory contentCategory) {
        // 保存内容分类
        contentCategory.setIsParent(false);
        contentCategory.setStatus(1);
        super.save(contentCategory);

        // 需要判断父节点的isParent 是否为true，如果不为true，修改为true
        // 查询父节点
        ContentCategory parent = super.queryById(contentCategory.getParentId());
        // 判断父节点isParent是否为false
        if (!parent.getIsParent()) {
            // 修改父节点，保存
            parent.setIsParent(true);
            super.updateByIdSelective(parent);
        }

        return contentCategory;

    }
    @Override
    public void deleteContentCategoryById(Long parentId, Long id) {
        // 获取所有需要删除的节点id
        List<Object> ids = new ArrayList<>();
        // 把自己放进入
        ids.add(id);
        // 使用递归的方式获取节点的所有子节点
        this.getIds(ids, id);

        // 删除节点
        super.deleteByIds(ids);

        // 判断是否存在兄弟节点，如果没有兄弟节点，修改父节点的isParent为false
        ContentCategory param = new ContentCategory();
        param.setParentId(parentId);
        Integer count = super.queryCountByWhere(param);
        // 如果为0，没有兄弟节点，修改父节点的isParent为false
        if (count == 0) {
            ContentCategory parent = new ContentCategory();
            parent.setId(parentId);
            parent.setIsParent(false);

            super.updateByIdSelective(parent);
        }

    }

    // 递归获取id这个节点的所有的子节点
    private void getIds(List<Object> ids, Long parentId) {
        // 根据parentId查询子节点
        ContentCategory param = new ContentCategory();
        param.setParentId(parentId);
        // 查询子节点
        List<ContentCategory> list = super.queryListByWhere(param);

        // 判断查询结果，其实就是判断是否有子节点
        // 直接使用高级for循环，就不用判断
        for (ContentCategory contentCategory : list) {
            // 如果进入循环，表示有子节点，把子节点id放到ids中
            ids.add(contentCategory.getId());
            // 递归调用，获取子节点的子节点
            this.getIds(ids, contentCategory.getId());
        }
    }
}
