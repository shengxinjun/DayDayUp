package com.sxj.multiModelData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sxj.domain.BrandByProduct;
import com.sxj.domain.Order;
import com.sxj.domain.OrderItem;
import com.sxj.domain.Person;
import com.sxj.domain.Relation;
import com.sxj.domain.Product;
import com.sxj.domain.Vendor;
import com.sxj.util.DateUtil;
import com.sxj.util.OrientDBUtil;

@RequestMapping("/consumer4")
@Controller
public class Consumer4Controller {
	
	int count=0;
	boolean flag=true;
	String startTimeString=DateUtil.currentDateTime();
	String endTimeString="";
	long startTime = System.currentTimeMillis();
	long endTime = System.currentTimeMillis();
	
	@ResponseBody
	@RequestMapping("/insert")
	public void consumeFromKafka() throws SQLException{
		Properties props = new Properties();
        //kafka集群
        props.put( "bootstrap.servers", "121.196.54.227:9092");
        //消费者组id
        props.put( "group.id","test23");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        //设置自动提交offset
        //props.put("enable.auto.commit",true);
        //提交延时
        //props.put("auto.commit.interval.ms", 1000);
        //KV的反序列化
        props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("order","product","vendor","brandByProduct","person","personknowperson"));
        Date date1 = new Date();
        while (flag){
        	if(DateUtil.subtract(date1, new Date())>10){
        		flag=false;
        	}
            ConsumerRecords<String, String> records = consumer.poll(100);
            for(ConsumerRecord<String, String> record:records){
            	date1=new Date();
            	if(record.topic().equals("order")){
            		insertOrder(record.value());
            	}else if(record.topic().equals("product")){
            		insertProduct(record.value());
            	}else if(record.topic().equals("vendor")){
            		insertVendor(record.value());
            	}else if(record.topic().equals("brandByProduct")){
            		insertBrandByProduct(record.value());
            	}else{
            		insertPerson(record.value());
            	}
                
            }
        }
        consumer.subscribe(Arrays.asList("personknowperson"));
        flag=true;
        while(flag){
        	if(DateUtil.subtract(date1, new Date())>10){
        		flag=false;
        	}
            ConsumerRecords<String, String> records = consumer.poll(100);
            for(ConsumerRecord<String, String> record:records){
            	date1=new Date();
            	insertPersonKnowPerson(record.value());
            }
        }
	}
	public void insertPersonKnowPerson(String json){
		Statement stmt = OrientDBUtil.getStatement();
		Relation p = JSON.parseObject(json, Relation.class);
		String sql="";
		//插入product
		sql = "create edge personknowperson from (select from person where id='"
				+ p.getPerson1() + "') to (select from person where id='" +p.getPerson2()+ "')  ";
		try {
			stmt.execute(sql);
			count++;
			if(count%10000==0){
				endTimeString = DateUtil.currentDateTime();
				sql="insert into logger set message='插入"+count+"条数据用时：" + (endTime - startTime)+" ms,开始时间："+startTimeString+",结束时间："+endTimeString+"'";
				stmt.execute(sql);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	public void insertPerson(String json){
		Statement stmt = OrientDBUtil.getStatement();
		Person p = JSON.parseObject(json, Person.class);
		String sql="";
		//插入product
		sql="insert into person (id,firstName,lastName,gender) values ('"+p.getId()+"','"+p.getFirstName()+"','"+p.getLastName()+"','"+p.getGender()+"')";
		try {
			stmt.execute(sql);
			count++;
			if(count%10000==0){
				endTimeString = DateUtil.currentDateTime();
				sql="insert into logger set message='插入"+count+"条数据用时：" + (endTime - startTime)+" ms,开始时间："+startTimeString+",结束时间："+endTimeString+"'";
				stmt.execute(sql);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void insertBrandByProduct(String json){

		Statement stmt = OrientDBUtil.getStatement();
		BrandByProduct b = JSON.parseObject(json, BrandByProduct.class);
		String sql="";
		//插入product
		sql="insert into brandByProduct (vendorId,productId) values ('"+b.getVendorId()+"','"+b.getProductId()+"')";
		try {
			stmt.execute(sql);
			count++;
			if(count%10000==0){
				endTimeString = DateUtil.currentDateTime();
				sql="insert into logger set message='插入"+count+"条数据用时：" + (endTime - startTime)+" ms,开始时间："+startTimeString+",结束时间："+endTimeString+"'";
				stmt.execute(sql);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	
	}
	public void insertVendor(String json){

		Statement stmt = OrientDBUtil.getStatement();
		Vendor v = JSON.parseObject(json, Vendor.class);
		String sql="";
		//插入product
		sql="insert into vendor (id,country,industry) values ('"+v.getId()+"','"+v.getCountry()+"','"+v.getIndustry()+"')";
		try {
			stmt.execute(sql);
			count++;
			if(count%10000==0){
				endTimeString = DateUtil.currentDateTime();
				sql="insert into logger set message='插入"+count+"条数据用时：" + (endTime - startTime)+" ms,开始时间："+startTimeString+",结束时间："+endTimeString+"'";
				stmt.execute(sql);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	public void insertProduct(String json){
		Statement stmt = OrientDBUtil.getStatement();
		Product p = JSON.parseObject(json, Product.class);
		String sql="";
		//插入product
		sql="insert into product (id,title,price,imgUrl) values ('"+p.getId()+"','"+p.getTitle()+"','"+p.getPrice()+"','"+p.getImgUrl()+"')";
		try {
			stmt.execute(sql);
			count++;
			if(count%10000==0){
				endTimeString = DateUtil.currentDateTime();
				sql="insert into logger set message='插入"+count+"条数据用时：" + (endTime - startTime)+" ms,开始时间："+startTimeString+",结束时间："+endTimeString+"'";
				stmt.execute(sql);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void insertOrder(String json) throws SQLException{

		Statement stmt = OrientDBUtil.getStatement();
		Order order = JSON.parseObject(json, Order.class);
		for(OrderItem item:order.getItems()){
			item.setOrderId(order.getId());
			item.setId(UUID.randomUUID().toString());
		}
		String sql="";
		//插入order
		sql="insert into orders (id,person_id,order_date,total_price) values ('"+order.getId()+"','"+order.getPersonId()+"','"+order.getOrderDate()+"','"+order.getTotalPrice()+"')";
		System.out.print(sql);
		try {
			stmt.execute(sql);
			count++;
			if(count%10000==0){
				endTimeString = DateUtil.currentDateTime();
				sql="insert into logger set message='插入"+count+"条数据用时：" + (endTime - startTime)+" ms,开始时间："+startTimeString+",结束时间："+endTimeString+"'";
				stmt.execute(sql);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//插入orderitem
		for(OrderItem item:order.getItems()){
			sql="insert into order_item (id,product_id,order_id) values ('"+item.getId()+"','"+item.getProductId()+"','"+item.getOrderId()+"')";
			System.out.print(sql);
			try {
				stmt.execute(sql);
				count++;
				if(count%10000==0){
					endTimeString = DateUtil.currentDateTime();
					sql="insert into logger set message='插入"+count+"条数据用时：" + (endTime - startTime)+" ms,开始时间："+startTimeString+",结束时间："+endTimeString+"'";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
