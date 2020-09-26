package com.domain;

import com.orientechnologies.orient.core.record.impl.ODocument;

public class customer {
	
	private Integer id;
	private String name;
	private Integer age;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "customer [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
	public customer(Integer id, String name, Integer age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}
	public customer() {
		super();
		// TODO Auto-generated constructor stub
	}
	public static customer fromODocument(ODocument oDocument) {
		customer customers = new customer();
		customers.id = oDocument.field("id");
		customers.name = oDocument.field("name");
		customers.age = oDocument.field("age");

        return customers;
    }
	
}
