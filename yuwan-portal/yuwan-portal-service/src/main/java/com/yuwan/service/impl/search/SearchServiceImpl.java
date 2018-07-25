package com.yuwan.service.impl.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuwan.common.pojo.EasyUIResult;
import com.yuwan.manager.dao.ItemMapper;
import com.yuwan.manager.pojo.Item;
import com.yuwan.service.search.SearchService;

/**
 * @discription 所搜服务层实现类
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年8月28日下午3:51:13
 * @version 1.0.0
 */
@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private HttpSolrServer httpSolrServer;

	@Autowired
	private ItemMapper itemMapper;

	@Override
	public EasyUIResult<Item> search(String query, Integer page, Integer rows) {
		// 封装查询对象
		SolrQuery solrQuery = new SolrQuery();

		// 设置查询语句
		if (StringUtils.isNotBlank(query)) {
			solrQuery.setQuery("item_title:" + query + " AND item_status:1");
		} else {
			solrQuery.setQuery("item_status:1");
		}

		// 设置分页
		solrQuery.setStart((page - 1) * rows);
		solrQuery.setRows(rows);

		// 高亮
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<font color='red'>");
		solrQuery.setHighlightSimplePost("</font>");

		// 声明返回结果对象taoResult
		EasyUIResult<Item> taoResult = new EasyUIResult<>();

		try {
			// 执行查询
			QueryResponse response = this.httpSolrServer.query(solrQuery);
			SolrDocumentList results = response.getResults();

			// 获取高亮数据
			Map<String, Map<String, List<String>>> map = response.getHighlighting();

			// 解析结果集
			// 声明存放商品的集合
			List<Item> list = new ArrayList<>();
			for (SolrDocument solrDocument : results) {
				Item item = new Item();

				// 解析Document
				// 商品id
				item.setId(Long.parseLong(solrDocument.get("id").toString()));

				// 获取高亮的数据
				List<String> hlist = map.get(solrDocument.get("id").toString()).get("item_title");

				// 商品title
				if (hlist != null && hlist.size() > 0) {
					item.setTitle(hlist.get(0));
				} else {
					item.setTitle(solrDocument.get("item_title").toString());
				}

				// 商品图片image
				item.setImage(solrDocument.get("item_image").toString());

				// 商品价格price
				item.setPrice(Long.parseLong(solrDocument.get("item_price").toString()));

				// 商品cid
				item.setCid(Long.parseLong(solrDocument.get("item_cid").toString()));

				// 把封装好的商品数据放到集合中
				list.add(item);

			}

			// 封装返回数据taoResult
			// 设置结果集
			taoResult.setRows(list);
			// 设置查询的数据总条数
			taoResult.setTotal(results.getNumFound());

			return taoResult;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 如果查询有异常，就返回一个空的结果
		return taoResult;

	}

	@Override
	public void saveItem(long itemId) {

		// 1. 调用商品服务，根据商品id获取商品信息
		Item item = this.itemMapper.selectByPrimaryKey(itemId);

		// 2. 根据获取到的数据，保存索引库
		SolrInputDocument document = new SolrInputDocument();
		// 商品id
		document.setField("id", item.getId().toString());
		// 商品标题
		document.setField("item_title", item.getTitle());
		// 商品价格
		document.setField("item_price", item.getPrice());
		// 商品图片
		document.setField("item_image", item.getImage());
		// 商品类目id
		document.setField("item_cid", item.getCid());
		// 商品状态
		document.setField("item_status", item.getStatus());

		try {
			// 保存到索引库中
			this.httpSolrServer.add(document);
			this.httpSolrServer.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
