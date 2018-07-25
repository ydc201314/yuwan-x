package com.yuwan.controller.item.message;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuwan.manager.service.ItemDescService;
import com.yuwan.manager.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ItemMessageListener implements MessageListener {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public void onMessage(Message message) {
		// 判断是否是TextMessage
		if (message instanceof TextMessage) {
			// 强转
			TextMessage textMessage = (TextMessage) message;

			try {
				// 获取消息
				String json = textMessage.getText();

				// 解析json
				if (StringUtils.isNotBlank(json)) {
					// 直接解析json
					JsonNode jsonNode = MAPPER.readTree(json);

					// 获取操作符
					String type = jsonNode.get("type").asText();

					// 获取itemId
					long itemId = jsonNode.get("itemId").asLong();

					// 判断是否是新增
					if ("save".equals(type)) {
						// 根据itemId生成静态页
						this.genHtml(itemId);
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Value("${YUWAN_ITEM_HTML}")
	private String YUWAN_ITEM_HTML;

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemDescService itemDescService;

	// 静态化商品页面方法
	private void genHtml(long itemId) throws Exception {
		// 获取核心对象
		Configuration cfg = this.freeMarkerConfigurer.getConfiguration();

		// 获取模板
		Template template = cfg.getTemplate("item.ftl");

		// 声明模型数据
		Map<String, Object> root = new HashMap<>();

		// 设置数据
		// 设置商品数据item.taotao.com
		root.put("item", this.itemService.queryById(itemId));
		// 设置商品详情数据
		root.put("itemDesc", this.itemDescService.queryById(itemId));

		// 获取输出对象
		Writer out = new FileWriter(new File(this.YUWAN_ITEM_HTML + itemId + ".html"));

		// 输出静态页面
		template.process(root, out);

	}

}
