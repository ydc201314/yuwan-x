package com.yuwan.manager.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuwan.common.pojo.EasyUIResult;
import com.yuwan.manager.pojo.Item;
import com.yuwan.manager.pojo.ItemDesc;
import com.yuwan.manager.service.ItemDescService;
import com.yuwan.manager.service.ItemService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl extends BaseServiceImpl<Item> implements ItemService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination destination;
    @Autowired
    @Qualifier("itemMapper")
    private Mapper<Item> mapper;

    public Mapper<Item> getMapper() {
        return mapper;
    }

    @Autowired
    private ItemDescService itemDescService;

    @Override
    public void save(Item item, String desc) {
        item.setStatus(1);
        super.save(item);
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(item.getId());
        this.itemDescService.save(itemDesc);
        // 使用ActiveMQ发送消息，消息尽量简洁，发送操作符和商品ID
        this.sendMQ("save",item.getId());
    }

    @Override
    public EasyUIResult<Item> queryItemList(Integer page, Integer rows) {
        // 设置分页数据
        PageHelper.startPage(page, rows);
        List<Item> list = super.queryListByWhere(null);
        // 获取分页的详细数据
        PageInfo<Item> pageInfo = new PageInfo<Item>(list);

        // 封装返回对象
        EasyUIResult<Item> easyUIResult = new EasyUIResult<>();
        easyUIResult.setRows(list);
        easyUIResult.setTotal(pageInfo.getTotal());
        return easyUIResult;
    }

    // 通用发送消息方法，第一个参数是操作符，第二个参数是商品id
    private void sendMQ(final String type, final Long itemId) {
        this.jmsTemplate.send(destination, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                // 声明消息
                TextMessage textMessage = new ActiveMQTextMessage();

                // 构建消息内容
                // 使用json格式的数据封装需要传递的消息
                Map<String, Object> map = new HashMap<>();
                // 操作符
                map.put("type", type);
                // 商品id
                map.put("itemId", itemId);

                try {
                    // 把map转为接送格式的数据
                    String json = MAPPER.writeValueAsString(map);
                    // 设置消息内容
                    textMessage.setText(json);
                    System.out.println("发送的消息是 : " + json);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return textMessage;
            }
        });
    }

}
