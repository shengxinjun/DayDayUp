package com.sxj.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sxj.domain.Order;
import com.sxj.domain.Person;
import com.sxj.domain.Product;
import com.sxj.domain.Relation;
import com.sxj.service.FileReaderService;
import com.sxj.service.LoggerService;
import com.sxj.service.OrderService;
import com.sxj.service.PersonService;
import com.sxj.service.ProductService;
import com.sxj.util.DateUtil;

public class MultiThreadController {



	private static Logger log = Logger.getLogger(MultiThreadController.class);
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private LoggerService loggerService;

	@Autowired
	private FileReaderService fileReaderService;

	
	public void workload(){

		delete();
		int[] threadNum={5,10,20,40,80,160};
		for(int i=0;i<6;i++){
			insert(threadNum[i]);
			delete();
		}
	}
	private void delete(){
		productService.MongoDBDeleteAllProduct();
		productService.ArangoDBDeleteAllProduct();
		orderService.ArangoDBDeleteAllOrder();
		orderService.MongoDBDeleteAllOrder();
		personService.Neo4jDeleteAllPerson();
		personService.ArangoDBDeleteAllPerson();
	}

	/**
	 * 
	 */
	private void insert(int threadNum) {
		BufferedReader reader = fileReaderService.readerProductCSV();
		String line="";
		List<Product> list1 = new ArrayList<>();
		List<Order> list2= new ArrayList<>();;
		List<Person> list3= new ArrayList<>();;
		List<Relation> list4= new ArrayList<>();;
		try {
			while ((line = reader.readLine()) != null) {
				Product product=new Product().stringToProduct(line);
				list1.add(product);
			}
			reader = fileReaderService.readerOrderJson();
			list2 = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				Order order = JSON.parseObject(line, Order.class);
				list2.add(order);
			}
			reader = fileReaderService.readerPersonCSV();
			list3 = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				Person person = new Person().stringToPerson(line);
				list3.add(person);
			}
			reader = fileReaderService.readerRelationCSV();
			list4 = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				Relation relation = new Relation().strToRelation(line);
				list4.add(relation);
			}
		} catch (IOException e) {
			log.info(this.getClass()+":"+e.toString());
		}
		log.info("insert(),threadNum="+threadNum+",list1.size()="+list1.size()+",list2.size()="+list2.size());
		mongoDBInsertDocument(list1, list2,threadNum);
		neo4jInsertGraph(list3, list4,threadNum);
		
		arangoDBInsertDocument(list1, list2,threadNum);
		arangoDBInsertGraph(list3, list4,threadNum);
	}
	/**
	 * @param list3
	 * @param list4
	 */
	private void arangoDBInsertGraph(List<Person> list3, List<Relation> list4,int threadNum) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(threadNum, threadNum, 200, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
		String start = DateUtil.currentDateTime();
		for(Person person:list3){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					personService.ArangoDBInsertPerson(person);
				}
			});
		}
		for(Relation relation:list4){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					personService.ArangoDBInsertRelations(relation.getPerson1(), relation.getPerson2());
				}
			});
		}
		executor.shutdown();
		while(!executor.isTerminated()){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
		             executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
		}
		String end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end, 0, 1, "负载类型：C6，线程数:"+threadNum);
	}

	/**
	 * @param list1
	 * @param list2
	 * @return
	 */
	private String arangoDBInsertDocument(List<Product> list1, List<Order> list2,int threadNum) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(threadNum, threadNum, 200, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
		String start = DateUtil.currentDateTime();
		for(Product product:list1){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					productService.ArangoDBInsertProduct(product);
				}
			});
		}
		for(Order order:list2){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					orderService.ArangoDBInsertOrder(order);
				}
			});
		}
		executor.shutdown();
		while(!executor.isTerminated()){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
		             executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
		}
		String end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end, 0, 1, "负载：C5，线程数："+threadNum);
		return start;
	}

	/**
	 * @param list3
	 * @param list4
	 */
	private void neo4jInsertGraph(List<Person> list3, List<Relation> list4,int threadNum) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(threadNum, threadNum, 200, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
		String start = DateUtil.currentDateTime();
		for(Person person:list3){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					personService.Neo4jInsertPerson(person);					
				}
			});
		}
		for(Relation relation:list4){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					personService.Neo4jInsertRelation(relation.getPerson1(), relation.getPerson2());
				}
			});
		}
		executor.shutdown();
		while(!executor.isTerminated()){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
		             executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
		}
		String end = DateUtil.currentDateTime();
		loggerService.Neo4jLogger(start, end, 0, 1, "负载：C6，线程数："+threadNum);
	}

	/**
	 * @param list1
	 * @param list2
	 * @return
	 */
	private void mongoDBInsertDocument(List<Product> list1, List<Order> list2,int threadNum) {
		log.info("mongoDBInsertDocument,list1.size="+list1.size()+",list2.size="+list2.size()+",threadNum="+threadNum);
		ThreadPoolExecutor executor = new ThreadPoolExecutor(threadNum, threadNum, 200, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
		String start = DateUtil.currentDateTime();
		for(Product product:list1){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						productService.MongoDBInsertProduct(product);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		
		for(Order order:list2){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						orderService.MongoDBInsertOrder(order);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		executor.shutdown();
		while(!executor.isTerminated()){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
		             executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
		}
		String end = DateUtil.currentDateTime();
		loggerService.MongoDBLogger(start, end, 0, 1, "负载类型：C5，线程数:"+threadNum);
		log.info("exit");
	}
	/**
	 * @param list3
	 * @param list4
	 */
	private void arangoDBDeleteGraph(List<Person> list3, List<Relation> list4) {
		log.info("arangoDBDeleteGraph");
		for(Relation relation:list4){
			personService.ArangoDBDeleteRelation(relation.getPerson1(), relation.getPerson2());
		}
		for(Person person:list3){
			personService.ArangoDBDeletePerson(person.getId());
		}
	}

	/**
	 * @param list1
	 * @param list2
	 */
	private void arangoDBDeleteDocument(List<Product> list1, List<Order> list2) {
		log.info("arangoDBDeleteDocument");
		for(Product product:list1){
			productService.ArangoDBDeleteProductById(product.getId());
		}
		for(Order order:list2){
			orderService.ArangoDBDeleteOrderById(order.getId());
		}
	}

	/**
	 * @param list3
	 * @param list4
	 */
	private void neo4jDeleteGraph(List<Person> list3, List<Relation> list4) {
		log.info("neo4jDeleteGraph");
		for(Relation relation:list4){
			try {
				personService.Neo4jDeleteKnows(relation.getPerson1(), relation.getPerson2());
			} catch (IOException e) {
				log.error("Neo4j delete Relation occur error xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			}
		}
		for(Person person:list3){
			personService.Neo4jDeletePerson(person.getId());
		}
	}

	/**
	 * @param list1
	 * @param list2
	 */
	private void mongoDBDeleteDocument(List<Product> list1, List<Order> list2) {
		log.info("mongoDBDeleteDocument");
		for(Product product:list1){
			try {
				productService.MongoDBDeleteProduct(product.getId());
			} catch (IOException e) {
				log.error("MongoDB delete Product occur error xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			}
		}
		for(Order order:list2){
			try {
				orderService.MongoDBDeleteOrder(order.getId());
			} catch (IOException e) {
				log.error("MongoDB delete Order occur error xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			}
		}

	}
}
