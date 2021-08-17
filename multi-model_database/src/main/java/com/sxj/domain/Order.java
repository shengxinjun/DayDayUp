package com.sxj.domain;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class Order {

	@JSONField(name="OrderId")
	private String id;
	@JSONField(name="PersonId")
	private String personId;
	@JSONField(name="OrderDate")
	private String orderDate;
	@JSONField(name="TotalPrice")
	private String totalPrice;
	@JSONField(name="Orderline")
	private List<OrderItem> items;
	
	
	/**
	 * ArangoDB专用字段 
	 * @param items
	 */
	private String _id;

	private String _key;
	private String person_id;
	private String order_date;
	private String total_price;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public List<OrderItem> getItems() {
		return items;
	}
	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String get_key() {
		return _key;
	}
	public void set_key(String _key) {
		this._key = _key;
	}
	public String getPerson_id() {
		return person_id;
	}
	public void setPerson_id(String person_id) {
		this.person_id = person_id;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	public String getTotal_price() {
		return total_price;
	}
	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}
	
	
	
}
