package com.sxj.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sxj.domain.Order;
import com.sxj.domain.OrderItem;
import com.sxj.service.FileReaderService;
import com.sxj.service.LoggerService;
import com.sxj.service.OrderService;
import com.sxj.util.DateUtil;
import com.sxj.util.MysqlDBUtil;
@RequestMapping("/test")
@Controller
public class TestController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private LoggerService loggerService;

	@Autowired
	private FileReaderService fileReaderService;
	
	@RequestMapping("/t")
	public void test(){
		System.out.println("test");
	}
	
	public void importTest() throws Exception{
		
		BufferedReader reader=fileReaderService.readerOrderJson();
		String line = null;
		int count = 0;
		List<Order> list = new ArrayList<>();
		
		// 一次读取一行
		while ((line = reader.readLine()) != null && count <= 100000) {
			// json转对象
			Order order = JSON.parseObject(line, Order.class);
			// 设置orderitem的外键
			for (OrderItem item : order.getItems()) {
				item.setOrderId(order.getId());
			}
			list.add(order);
			count++;
		}
		String startTimeString  = DateUtil.currentDateTime();
		String endTimeString = "";
		for(Order order:list){
			orderService.MongoDBInsertOrder(order);
		}
		endTimeString = DateUtil.currentDateTime();
		loggerService.MongoDBLogger(startTimeString, endTimeString, list.size(), 1,"单线程");
		for(Order order:list){
			orderService.MongoDBDeleteOrder(order.getId());
		}
		Thread.sleep(10000);
		ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 150, 200, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
		startTimeString  = DateUtil.currentDateTime();
		for(Order order:list){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						orderService.MongoDBInsertOrder(order);
						System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
					             executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		executor.shutdown();
		while(!executor.isTerminated()){
			System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
		             executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
		}
		endTimeString = DateUtil.currentDateTime();
		loggerService.MongoDBLogger(startTimeString, endTimeString, list.size(), 1,"多线程");
		
		
	}
}
