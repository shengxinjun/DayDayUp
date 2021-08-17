package com.sxj.domain;

import org.springframework.util.StringUtils;

public class Product {
	private String id;
	private String title;
	private String price;
	private String imgUrl;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Product stringToProduct(String str){
		str = filterSqlString(str);
		String item[] = str.split(",");
		Product product = new Product();
		product.setId(item[0]);
		product.setTitle(item[1]);
		product.setPrice(item[2]);
		product.setImgUrl(item[3]);
		return product;
	}
	public String filterSqlString(String sqlStr) {
		if (StringUtils.isEmpty(sqlStr)) {
			return sqlStr;
		}
		sqlStr = sqlStr.replace("'", "");
		sqlStr = sqlStr.replace("\\", "");
		return sqlStr;
	}
}
