package com.yuwan.manager.service.impl;

import java.util.List;

import com.github.abel533.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.yuwan.manager.dao.ItemCatMapper;
import com.yuwan.manager.pojo.ItemCat;
import com.yuwan.manager.service.ItemCatService;

@Service
public class ItemCatServiceImpl extends BaseServiceImpl<ItemCat> implements ItemCatService {

    @Autowired
    @Qualifier("itemCatMapper")
    private Mapper<ItemCat> mapper;

    public Mapper<ItemCat> getMapper() {
        return mapper;
    }

    @Override
    public List<ItemCat> queryItemCatByParentId(Long parentId) {
        // 设置查询条件
        ItemCat itemCat = new ItemCat();
        itemCat.setParentId(parentId);
        List<ItemCat> list = super.queryListByWhere(itemCat);
        return list;
    }

	/*@Autowired
	private ItemCatMapper itemCatMapper;

	@Override
	public List<ItemCat> queryItemCatByPage(Integer page, Integer rows) {
		// 使用分页插件设置分页条件
		PageHelper.startPage(page, rows);

		// 查询
		List<ItemCat> list = this.itemCatMapper.select(null);
		return list;
	}*/

}
