package com.sxj.domain;

import com.alibaba.fastjson.annotation.JSONField;

public class OrderItem {
	
	private String id;
	private String orderId;
	
	/**
	 * ArangoDB专用
	 */
	private String order_id;
	private String product_id;
	

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	@JSONField(name="asin")
	private String productId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	
}
