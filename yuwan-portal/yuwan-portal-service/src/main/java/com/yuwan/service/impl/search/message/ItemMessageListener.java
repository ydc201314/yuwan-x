package com.yuwan.service.impl.search.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuwan.service.search.SearchService;

public class ItemMessageListener implements MessageListener {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Autowired
	private SearchService searchService;

	@Override
	public void onMessage(Message message) {
		// 判断消息类型是否是TextMessage
		if (message instanceof TextMessage) {
			// 强转消息类型
			TextMessage textMessage = (TextMessage) message;

			try {
				// 获取消息内容
				String json = textMessage.getText();

				// 解析消息
				if (StringUtils.isNotBlank(json)) {
					// 使用Jackson工具类的直接解析json数据的方式
					JsonNode jsonNode = MAPPER.readTree(json);

					// {type:save,itemId:123}
					// 操作符
					String type = jsonNode.get("type").asText();

					// 商品id
					long itemId = jsonNode.get("itemId").asLong();

					// 如果是新增或者更新，执行添加商品到索引库的逻辑
					if ("save".equals(type)) {
						// 如果是新增则添加数据
						this.searchService.saveItem(itemId);
					}

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
