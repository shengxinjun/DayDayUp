package com.sxj.graphData;

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
import org.bson.Document;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Values;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sxj.domain.BrandByProduct;
import com.sxj.domain.Order;
import com.sxj.domain.OrderItem;
import com.sxj.domain.Person;
import com.sxj.domain.Relation;
import com.sxj.domain.Product;
import com.sxj.domain.Vendor;
import com.sxj.util.DateUtil;
import com.sxj.util.MongoDBUtil;
import com.sxj.util.MysqlDBUtil;
import com.sxj.util.Neo4jDBUtil;

@RequestMapping("/consumer3")
@Controller
public class Consumer3Controller {

	int count = 0;
	Session session = Neo4jDBUtil.getSession();
	String startTimeString = DateUtil.currentDateTime();
	String endTimeString = "";
	long startTime = System.currentTimeMillis();
	long endTime ;

	@ResponseBody
	@RequestMapping("/insert")
	public void consumePerson() throws SQLException {
		Properties props = new Properties();
		// kafka集群
		props.put("bootstrap.servers", "121.196.54.227:9092");
		// 消费者组id
		props.put("group.id", "test23");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		// 设置自动提交offset
		// props.put("enable.auto.commit",true);
		// 提交延时
		// props.put("auto.commit.interval.ms", 1000);
		// KV的反序列化
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList("person","personknowperson"));
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(100);
			for (ConsumerRecord<String, String> record : records) {
				count++;
				if(count%10000==0){
					endTime= System.currentTimeMillis();
					endTimeString = DateUtil.currentDateTime();
					String msg = "插入"+count+"条数据用时：" + (endTime - startTime) + " ms,开始时间：" + startTimeString + ",结束时间："
							+ endTimeString + "'";
					session.run(
							"CREATE (a:logger {message: {msg}})",
							Values.parameters("msg", msg));
				}
				if(record.topic().equals("person")){
					insertPerson(record.value());
				}else{
					insertPersonKnowPerson(record.value());
				}
				
			}
		}
	}
	public void insertPersonKnowPerson(String json){
		Relation p = JSON.parseObject(json,Relation.class);
		session.run(
				"match(p1:Person{id:{personId1}}),(p2:Person{id:{personId2}})  create (p1)-[:personknowperson{relationName:'know'}]->(p2)",
				Values.parameters("personId1", p.getPerson1(), "personId2", p.getPerson2()));
		
	}

	public void insertPerson(String json) throws SQLException {
		Person p = JSON.parseObject(json,Person.class);
		session.run(
				"CREATE (a:Person {id: {id},firstName: {firstName},lastName: {lastName},gender: {gender}})",
				Values.parameters("id", p.getId(), "firstName", p.getFirstName(),"lastName", p.getLastName(),"gender", p.getGender()));
	
	}

}
