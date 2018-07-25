package com.yuwan.manager.service;

import com.yuwan.common.pojo.EasyUIResult;
import com.yuwan.manager.pojo.Item;


public interface ItemService extends BaseService<Item> {
    /**
     * @Description:保存商品
     * @Date:12:48 2018/6/7
     * @Params:[item]
     */
    public void save(Item item, String desc);
    /**
     * 分页查询商品信息
     *
     * @param page
     * @param rows
     * @return
     * @return EasyUIResult<Item>
     */
    public EasyUIResult<Item> queryItemList(Integer page, Integer rows);
}
