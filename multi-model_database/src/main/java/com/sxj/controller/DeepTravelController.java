package com.sxj.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sxj.domain.Order;
import com.sxj.domain.Person;
import com.sxj.domain.Product;
import com.sxj.service.FileReaderService;
import com.sxj.service.LoggerService;
import com.sxj.service.OrderService;
import com.sxj.service.PersonService;
import com.sxj.service.ProductService;
import com.sxj.util.DateUtil;

@Component
public class DeepTravelController {



	private static Logger log = Logger.getLogger(DeepTravelController.class);
	
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
	
	@Scheduled(cron="0 35 19 4 * ?  ")
	public void travel(){
		//读顾客
		BufferedReader reader = fileReaderService.readerPersonCSV();
		ArrayList<Person> list = new ArrayList<>();
		String line="";
		int count=0;
		try {
			while ((line = reader.readLine()) != null&&count<1000) {
				count++;
				Person person = new Person().stringToPerson(line);
				list.add(person);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//读商品
		count=0;
		reader = fileReaderService.readerProductCSV();
		ArrayList<Product> list2 = new ArrayList<>();
		try {
			while ((line = reader.readLine()) != null&&count<5000) {
				Product product = new Product().stringToProduct(line);
				list2.add(product);
				count++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String start = DateUtil.currentDateTime();
		for(Person person:list){
			MultiDatabaseCombinationQ4(person);
		}
		String end = DateUtil.currentDateTime();
		loggerService.MongoDBLogger(start, end,5000, 2, "负载类型：R4，多数据库组合");
		
		start = DateUtil.currentDateTime();
		for(Person person:list){
			MultiModelDatabaseQ4(person);
		}
		end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end,5000, 2, "负载类型：R4，多模数据库");

		start = DateUtil.currentDateTime();
		for(Person person:list){
			MultiDatabaseCombinationQ5(person);
		}
		end = DateUtil.currentDateTime();
		loggerService.MongoDBLogger(start, end,5000, 2, "负载类型：R5，多数据库组合");

		start = DateUtil.currentDateTime();
		for(Person person:list){
			MultiModelDatabaseQ5(person);
		}
		end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end,5000, 2, "负载类型：R5，多模数据库");
		
		start = DateUtil.currentDateTime();
		for(Product product:list2){
			List<Person> persons2 =MultiDatabaseCombinationQ6(product);
		}
		end = DateUtil.currentDateTime();
		loggerService.MongoDBLogger(start, end,5000, 2, "负载类型：R6，多数据库组合");
		
		start = DateUtil.currentDateTime();
		for(Product product:list2){
			List<Person> persons = MultiModelDatabaseQ6(product);
		}
		end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end,5000, 2, "负载类型：R6，多模数据库");
		
		start = DateUtil.currentDateTime();
		for(Person p :list){
			List<String> personIds2 =MultiDatabaseCombinationQ7(p);
		}
		end = DateUtil.currentDateTime();
		loggerService.MongoDBLogger(start, end,5000, 2, "负载类型：R7，多数据库组合");

		start = DateUtil.currentDateTime();
		for(Person p :list){
			List<String> personIds =MultiModelDatabaseQ7(p);
		}
		end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end,5000, 2, "负载类型：R7，多模数据库");
		

		start = DateUtil.currentDateTime();
		for(Product product:list2){
			List<Product> products2 = MultiDatabaseCombinationQ8(product);
		}
		end = DateUtil.currentDateTime();
		loggerService.MongoDBLogger(start, end,5000, 2, "负载类型：R8，多数据库组合");
		
		start = DateUtil.currentDateTime();
		for(Product product:list2){
			List<Product> products = MultiModelDatabaseQ8(product);
		}
		end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end,5000, 2, "负载类型：R8，多模数据库");
		
		start = DateUtil.currentDateTime();
		for(Person person:list){
			boolean flag2 = MultiDatabaseCombinationQ9(person);
			
		}
		end = DateUtil.currentDateTime();
		loggerService.MongoDBLogger(start, end,5000, 2, "负载类型：R9，多数据库组合");
		
		start = DateUtil.currentDateTime();
		for(Person person:list){
			boolean flag1 = MultiModelDatabaseQ9(person);
			
		}
		end = DateUtil.currentDateTime();
		loggerService.ArangoDBLogger(start, end,5000, 2, "负载类型：R9，多模数据库");
	}

	/**
	 * @param person
	 */
	private void MultiModelDatabaseQ4(Person person) {
		List<Order> orders = orderService.ArangoDBFindTotalPriceByPersonName(person.getFirstName(), person.getLastName());
	}

	/**
	 * @param person
	 * @return
	 */
	private void MultiDatabaseCombinationQ4(Person person) {
		person = personService.Neo4jFindPersonByName(person.getFirstName(), person.getLastName());
		List<Order> order = orderService.MongoDBFindOrderByPersonId(person.getId());
	}

	@RequestMapping("/travel5")
	public void travel5(){
		BufferedReader reader = fileReaderService.readerPersonCSV();
		ArrayList<Person> list = new ArrayList<>();
		String line="";
		try {
			while ((line = reader.readLine()) != null) {
				Person person = new Person().stringToPerson(line);
				list.add(person);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Person person:list){
			MultiModelDatabaseQ5(person);
			MultiDatabaseCombinationQ5(person);
		}
	}

	/**
	 * @param person
	 */
	private void MultiModelDatabaseQ5(Person person) {
		List<String> id = productService.ArangoDBFindProductListByPersonId(person.getId());
	}

	/**
	 * @param person
	 */
	private void MultiDatabaseCombinationQ5(Person person) {
		List<String> productIds = orderService.MongoDBFindProductIdsByPersonId(person.getId());
		if(!ObjectUtils.isEmpty(productIds)){
			List<Product> products = productService.MongoDBFindProductByIds(productIds);
		}
	}


	@RequestMapping("/travel6")
	public void travel6(){
		BufferedReader reader = fileReaderService.readerProductCSV();
		ArrayList<Product> list = new ArrayList<>();
		String line="";
		try {
			while ((line = reader.readLine()) != null) {
				Product product = new Product().stringToProduct(line);
				list.add(product);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Product product:list){
			List<Person> persons = MultiModelDatabaseQ6(product);
			List<Person> persons2 =MultiDatabaseCombinationQ6(product);
		}
	}

	/**
	 * @param product
	 */
	private List<Person> MultiModelDatabaseQ6(Product product) {
		List<Person> persons = personService.ArangoDBFindPersonByProductName(product.getTitle());
		return persons;
	}

	/**
	 * @param product
	 * @return
	 */
	private List<Person> MultiDatabaseCombinationQ6(Product product) {
		product = productService.MongoDBFindProductByTitle(product.getTitle());
		List<Order> orders = orderService.MongoDBFindOrdersByProductId(product.getId());
		List<Person> persons = personService.Neo4jFindPersonsByOrders(orders);
		return persons;
	}
	@RequestMapping("/travel7")
	public void travel7(){
		BufferedReader reader = fileReaderService.readerPersonCSV();
		ArrayList<Person> list = new ArrayList<>();
		String line="";
		try {
			while ((line = reader.readLine()) != null) {
				Person person = new Person().stringToPerson(line);
				list.add(person);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Person p :list){
			List<String> personIds =MultiModelDatabaseQ7(p);
			List<String> personIds2 =MultiDatabaseCombinationQ7(p);
		}
	}

	/**
	 * @param p
	 */
	private List<String> MultiModelDatabaseQ7(Person p) {
		List<String> personIds = personService.ArangoDBFindPersonBuySomeThingByPersonName(p.getFirstName(),p.getLastName());
		return personIds;
	}

	/**
	 * @param p
	 * @return
	 */
	private List<String> MultiDatabaseCombinationQ7(Person p) {
		List<String> personIds = new ArrayList<>();
		p = personService.Neo4jFindPersonByName(p.getFirstName(), p.getLastName());
		List<String> productIds = orderService.MongoDBFindProductIdsByPersonId(p.getId());
		List<Order> orders = orderService.MongoDBFindOrdersByProductIds(productIds);
		Set<String> set = new HashSet<>();
		for(Order order:orders){
			if(!order.getPersonId().equals(p.getId()))
				set.add(order.getPersonId());
		}
		personIds.addAll(set);
		return personIds;
	}
	
	@RequestMapping("/travel8")
	public void travel8(){
		BufferedReader reader = fileReaderService.readerProductCSV();
		ArrayList<Product> list = new ArrayList<>();
		String line="";
		try {
			while ((line = reader.readLine()) != null) {
				Product product = new Product().stringToProduct(line);
				list.add(product);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Product product:list){
			List<Product> products = MultiModelDatabaseQ8(product);
			List<Product> products2 = MultiDatabaseCombinationQ8(product);
		}
	}

	/**
	 * @param product
	 */
	private List<Product> MultiModelDatabaseQ8(Product product) {
		List<Product> products = productService.ArangoDBFindFemaleOtherProductByProductTitle(product.getTitle());
		return products;
	}

	/**
	 * @param product
	 * @return
	 */
	private List<Product> MultiDatabaseCombinationQ8(Product product) {
		product = productService.MongoDBFindProductByTitle(product.getTitle());
		List<Order> orders = orderService.MongoDBFindOrdersByProductId(product.getId());
		List<String> personIds = new ArrayList<>();


		List<String> persons = personService.Neo4jFindFemalePersonIdsByOrders(orders);
		List<Product> detail = productService.MongoDBFindProductByPersonIds(persons);
		return detail;
	}
	
	@RequestMapping("/travel9")
	public void travel9(){
		BufferedReader reader = fileReaderService.readerPersonCSV();
		ArrayList<Person> list = new ArrayList<>();
		String line="";
		try {
			while ((line = reader.readLine()) != null) {
				Person person = new Person().stringToPerson(line);
				list.add(person);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Person person:list){
			boolean flag1 = MultiModelDatabaseQ9(person);
			boolean flag2 = MultiDatabaseCombinationQ9(person);
			
		}
	}

	/**
	 * @param person
	 */
	private boolean MultiModelDatabaseQ9(Person person) {
		List<String> productIds = productService.ArangoDBFindProductListByPersonId(person.getId());
		List<String> productIds2 = productService.ArangoDBFindFriendsProductListByPersonId(person.getId());
		productIds.retainAll(productIds2);
		if(productIds.size()>0)
			return true;
		else
			return false;
	}

	/**
	 * @param person
	 */
	private boolean MultiDatabaseCombinationQ9(Person person) {
		List<String> productIds = orderService.MongoDBFindProductIdsByPersonId(person.getId());
		List<String> personIds = new ArrayList<>();
		try {
			personIds= personService.Neo4jQueryPersonFriendIds(person.getId());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> productIds2 = orderService.MongoDBFindProductIdsByPersonIds(personIds);
		productIds.retainAll(productIds2);
		if(productIds.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
}
