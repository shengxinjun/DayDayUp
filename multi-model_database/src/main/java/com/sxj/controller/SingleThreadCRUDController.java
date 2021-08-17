package com.sxj.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class SingleThreadCRUDController {
	

	private static Logger log = Logger.getLogger(SingleThreadCRUDController.class);
	
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
	
	public void crud(){
		insert();
		/*query();
		update();
		delete();*/
	}
	public void insert(){
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
		arangoDBInsertDocument(list1, list2);
		
		arangoDBInsertGraph(list3, list4);
		mongoDBInsertDocument(list1, list2);
		
		neo4jInsertGraph(list3, list4);
		
	}
	public void query(){
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
		
		//多数据库组合
		mongoDBQueryDocument(list1);
		
		neo4jQueryGraph(list4);
		
		//多模数据库
		arangoDBQueryDocument(list1);
		
		arangoDBQueryGraph(list4);
	}
	public void update(){

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
		
		//多数据库组合
		mongoDBUpdateDocument(list1);
		neo4jUpdateGraph(list4);
		
		//多模数据库
		arangoDBUpdateDocument(list1);
		arangoDBUpdateGraph(list4);
	
	}
	public void delete(){
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
		
		//多数据库组合
		mongoDBDeleteDocument(list1, list2);

		neo4jDeleteGraph(list3, list4);
		
		//多模数据库
		arangoDBDeleteDocument(list1, list2);
		arangoDBDeleteGraph(list3, list4);
	}
	/**
	 * @param list3
	 * @param list4
	 */
	private void arangoDBInsertGraph(List<Person> list3, List<Relation> list4) {
		String start = DateUtil.currentDateTime();
		for(Person person:list3){
			personService.ArangoDBInsertPerson(person);
		}
		for(Relation relation:list4){
			personService.ArangoDBInsertRelations(relation.getPerson1(), relation.getPerson2());
		}
		String end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end, 0, 1, "C6");
	}

	/**
	 * @param list1
	 * @param list2
	 * @return
	 */
	private String arangoDBInsertDocument(List<Product> list1, List<Order> list2) {
		String start = DateUtil.currentDateTime();
		for(Product product:list1){
			productService.ArangoDBInsertProduct(product);
		}
		for(Order order:list2){
			orderService.ArangoDBInsertOrder(order);
		}
		String end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end, 0, 1, "C5");
		return start;
	}

	/**
	 * @param list3
	 * @param list4
	 */
	private void neo4jInsertGraph(List<Person> list3, List<Relation> list4) {
		String start = DateUtil.currentDateTime();
		for(Person person:list3){
			personService.Neo4jInsertPerson(person);
		}
		for(Relation relation:list4){
			personService.Neo4jInsertRelation(relation.getPerson1(), relation.getPerson2());
		}
		String end = DateUtil.currentDateTime();
		loggerService.Neo4jLogger(start, end, 0, 1, "C6");
	}

	/**
	 * @param list1
	 * @param list2
	 * @return
	 */
	private void mongoDBInsertDocument(List<Product> list1, List<Order> list2) {
		log.info("enter");
		String start = DateUtil.currentDateTime();
		for(Product product:list1){
			try {
				productService.MongoDBInsertProduct(product);
			} catch (IOException e) {
				log.error("MongoDB insert Product occur error xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			}
		}
		
		for(Order order:list2){
			try {
				orderService.MongoDBInsertOrder(order);
			} catch (IOException e) {
				log.error("MongoDB insert Order occur error xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			}
		}

		String end = DateUtil.currentDateTime();
		loggerService.MongoDBLogger(start, end, 0, 1, "C5");
		log.info("exit");
	}
	
	
	/**
	 * @param list4
	 */
	private void arangoDBQueryGraph(List<Relation> list4) {
		log.info("enter");
		String start = DateUtil.currentDateTime();
		for(Relation relation:list4){
			personService.ArangoDBQueryFriendsById(relation.getPerson1());
		}
		String end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end, 0, 2, "R2");
		log.info("exit");
	}

	/**
	 * @param list1
	 * @return
	 */
	private void arangoDBQueryDocument(List<Product> list1) {
		log.info("enter");
		String start = DateUtil.currentDateTime();
		for(Product product:list1){
			productService.ArangoDBQueryProductById(product.getId());
		}
		String end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end, 0, 2, "R3");
		log.info("exit");
	}

	/**
	 * @param list4
	 */
	private void neo4jQueryGraph(List<Relation> list4) {
		log.info("enter");
		String start = DateUtil.currentDateTime();
		for(Relation relation:list4){
			try {
				personService.Neo4jQueryPersonFriends(relation.getPerson1());
			} catch (IOException e) {
				log.error("Neo4j insert Relation occur error xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			}
		}
		String end = DateUtil.currentDateTime();
		loggerService.Neo4jLogger(start, end, 0, 2, "R2");
		log.info("exit");
	}

	/**
	 * @param list1
	 * @return
	 */
	private void mongoDBQueryDocument(List<Product> list1) {
		log.info("enter");
		String start = DateUtil.currentDateTime();
		for(Product product:list1){
			try {
				productService.MongoDBQueryProduct(product.getId());
			} catch (IOException e) {
				log.error("MongoDB insert Product occur error xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			}
		}
		String end = DateUtil.currentDateTime();
		loggerService.MongoDBLogger(start, end, 0, 2, "R3");
		log.info("exit");
	}
	
	
	/**
	 * @param list4
	 */
	private void arangoDBUpdateGraph(List<Relation> list4) {
		log.info("enter");
		String start = DateUtil.currentDateTime();
		for(Relation relation:list4){
			personService.ArangoDBUpdateFriendsById(relation.getPerson1());
		}
		String end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end, 0, 3, "U2");
		log.info("exit");
	}

	/**
	 * @param list1
	 * @return
	 */
	private void arangoDBUpdateDocument(List<Product> list1) {
		log.info("enter");
		String start = DateUtil.currentDateTime();
		for(Product product:list1){
			productService.ArangoDBUpdateProductById(product.getId());
		}
		String end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end, 0, 3, "U3");
		log.info("exit");
	}

	/**
	 * @param list4
	 */
	private void neo4jUpdateGraph(List<Relation> list4) {
		log.info("enter");
		String start = DateUtil.currentDateTime();
		for(Relation relation:list4){
			try {
				personService.Neo4jUpdatePersonFriends(relation.getPerson1());
			} catch (IOException e) {
				log.error("Neo4j update Relation occur error xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			}
		}
		
		String end = DateUtil.currentDateTime();
		loggerService.Neo4jLogger(start, end, 0, 3, "U2");
		log.info("exit");
	}

	/**
	 * @param list1
	 * @return
	 */
	private void mongoDBUpdateDocument(List<Product> list1) {
		log.info("enter");
		String start = DateUtil.currentDateTime();
		for(Product product:list1){
			try {
				productService.MongoDBUpdateProduct(product.getId());
			} catch (IOException e) {
				log.error("MongoDB update Product occur error xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			}
		}
		String end = DateUtil.currentDateTime();
		loggerService.MongoDBLogger(start, end, 0, 3, "U3");
		log.info("exit");
	}
	

	/**
	 * @param list3
	 * @param list4
	 */
	private void arangoDBDeleteGraph(List<Person> list3, List<Relation> list4) {
		log.info("enter");
		String start = DateUtil.currentDateTime();
		for(Relation relation:list4){
			personService.ArangoDBDeleteRelation(relation.getPerson1(), relation.getPerson2());
		}
		for(Person person:list3){
			personService.ArangoDBDeletePerson(person.getId());
		}
		String end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end, 0, 4, "D6");
		log.info("exit");
	}

	/**
	 * @param list1
	 * @param list2
	 */
	private void arangoDBDeleteDocument(List<Product> list1, List<Order> list2) {
		log.info("enter");
		String start = DateUtil.currentDateTime();
		for(Product product:list1){
			productService.ArangoDBDeleteProductById(product.getId());
		}
		for(Order order:list2){
			orderService.ArangoDBDeleteOrderById(order.getId());
		}
		String end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end, 0, 4, "D5");
		log.info("exit");
	}

	/**
	 * @param list3
	 * @param list4
	 */
	private void neo4jDeleteGraph(List<Person> list3, List<Relation> list4) {
		log.info("enter");
		String start = DateUtil.currentDateTime();
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
		String end = DateUtil.currentDateTime();
		loggerService.Neo4jLogger(start, end, 0, 4, "D6");
		log.info("exit");
	}

	/**
	 * @param list1
	 * @param list2
	 */
	private void mongoDBDeleteDocument(List<Product> list1, List<Order> list2) {
		log.info("enter");
		String start = DateUtil.currentDateTime();
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

		String end = DateUtil.currentDateTime();
		loggerService.MongoDBLogger(start, end, 0, 4, "D5");
		log.info("exit");
	}
}
