package com.sxj.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.v1.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.sxj.domain.Order;
import com.sxj.domain.OrderItem;
import com.sxj.domain.Person;
import com.sxj.domain.Product;
import com.sxj.service.FileReaderService;
import com.sxj.service.LoggerService;
import com.sxj.service.OrderService;
import com.sxj.util.DateUtil;

@Component
public class CountController {
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private LoggerService loggerService;

	@Autowired
	private FileReaderService fileReaderService;

	public void count() throws IOException{
		BufferedReader reader = fileReaderService.readerOrderJson();
		String line = null;
		int count = 0;
		List<Order> list = new ArrayList<>();
		// 一次读取一行
		while ((line = reader.readLine()) != null) {
			// json转对象
			Order order = JSON.parseObject(line, Order.class);
			// 设置orderitem的外键
			for (OrderItem item : order.getItems()) {
				item.setOrderId(order.getId());
			}
			list.add(order);
			count=count+order.getItems().size();
		}
		String startTimeString  = DateUtil.currentDateTime();
		String endTimeString = DateUtil.currentDateTime();
		String remark="order:"+list.size()+";sub_order:"+count;
		loggerService.MongoDBLogger(startTimeString, endTimeString, count, 1, remark);
		
		reader=fileReaderService.readerPersonCSV();
		List<Person> persons = new ArrayList<>();
		while ((line = reader.readLine()) != null) {
			Person person = new Person();
			person = person.stringToPerson(line);
			persons.add(person);
		}
		remark="person:"+persons.size();
		loggerService.MongoDBLogger(startTimeString, endTimeString, count, 1, remark);
		
		reader=fileReaderService.readerRelationCSV();
		count=0;
		while ((line = reader.readLine()) != null) {
			count++;
		}
		remark="relation:"+count;
		loggerService.MongoDBLogger(startTimeString, endTimeString, count, 1, remark);
		
		
		reader = fileReaderService.readerProductCSV();
		List<Product> products = new ArrayList<>();
		while ((line = reader.readLine()) != null) {
			Product product = new Product();
			product = product.stringToProduct(line);
			products.add(product);
		}
		remark="product:"+products.size();
		loggerService.MongoDBLogger(startTimeString, endTimeString, count, 1, remark);
		
	}
	
}
