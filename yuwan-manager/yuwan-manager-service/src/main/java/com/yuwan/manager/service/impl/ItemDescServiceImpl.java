package com.yuwan.manager.service.impl;

import com.github.abel533.mapper.Mapper;
import com.yuwan.manager.pojo.ItemDesc;
import com.yuwan.manager.service.ItemDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ItemDescServiceImpl extends BaseServiceImpl<ItemDesc> implements ItemDescService {

    @Autowired
    @Qualifier("itemDescMapper")
    private Mapper<ItemDesc> mapper;

    public Mapper<ItemDesc> getMapper() {
        return mapper;
    }
}
