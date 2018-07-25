package com.yuwan.manager.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuwan.common.pojo.EasyUIResult;
import com.yuwan.common.redis.RedisUtils;
import com.yuwan.manager.pojo.Content;
import com.yuwan.manager.pojo.ContentCategory;
import com.yuwan.manager.service.ContentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ContentServiceImpl extends BaseServiceImpl<Content> implements ContentService {

    @Autowired
    @Qualifier("contentMapper")
    private Mapper<Content> mapper;

    public Mapper<Content> getMapper() {
        return mapper;
    }
    @Autowired
    private RedisUtils redisUtils;

    @Value("${YUWAN_PORTAL_AD}")
    private String YUWAN_PORTAL_AD;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public EasyUIResult<Content> queryContentByPage(Integer page, Integer rows, Long categoryId) {
        PageHelper.startPage(page, rows);

        Content content = new Content();
        content.setCategoryId(categoryId);

        List<Content> list = super.queryListByWhere(content);

        // 封装返回数据
        EasyUIResult<Content> easyUIResult = new EasyUIResult<>();

        PageInfo<Content> pageInfo = new PageInfo<>(list);

        easyUIResult.setTotal(pageInfo.getTotal());
        easyUIResult.setRows(list);

        return easyUIResult;
    }

    @Override
    public String queryAD(Long categoryId) {
        // 1. 从缓存中查询
        // 为了很好的管理和维护redis，需要redis的key是有意义的
        try {
            String redisJson = this.redisUtils.get(YUWAN_PORTAL_AD);
            // 判断是否为空，如果不为空表示命中了，直接返回
            if (StringUtils.isNotBlank(redisJson)) {
                return redisJson;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // 2. 如果没有命中，执行原有逻辑，查MySQL
        // 从数据库查询大广告需要的数据，其实就是查询CategoryId为31的数据
        Content param = new Content();
        param.setCategoryId(categoryId);
        List<Content> list = super.queryListByWhere(param);

        // 遍历内容，把内容封装到List<Map>中，根据前端数据格式进行封装
        // 声明容器存放内容
        List<Map<String, Object>> result = new ArrayList<>();
        for (Content content : list) {
            Map<String, Object> map = new HashMap<>();

            map.put("srcB", content.getPic());
            map.put("height", 240);
            map.put("alt", "");
            map.put("width", 670);
            map.put("src", content.getPic());
            map.put("widthB", 550);
            map.put("href", content.getUrl());
            map.put("heightB", 240);

            // 将封装好的map放到list中
            result.add(map);
        }

        String json = "";

        // 需要把List<Map>序列化为json格式数据,可以使用Jackson工具类
        try {
            json = MAPPER.writeValueAsString(result);
            // 3. 把查到的数据放到redis中
            this.redisUtils.set(this.YUWAN_PORTAL_AD, json, 60 * 60 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }
}
