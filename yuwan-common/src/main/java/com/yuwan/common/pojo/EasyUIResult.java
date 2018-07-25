package com.yuwan.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @discription 返回easyUI中datagrid所需要的列表数据
 * @author ydc 猜猜我是谁
 * @date 创建时间：2017年8月18日上午10:49:55
 * @version 1.0.0
 */
public class EasyUIResult<T> implements Serializable {

	private long total;
	private List<T> rows;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
