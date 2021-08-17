package com.sxj.documentData;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sxj.domain.BrandByProduct;
import com.sxj.domain.Order;
import com.sxj.domain.OrderItem;
import com.sxj.domain.Product;
import com.sxj.domain.Vendor;
import com.sxj.util.DateUtil;
import com.sxj.util.MongoDBUtil;
import com.sxj.util.MysqlDBUtil;

@RequestMapping("/consumer2")
@Controller
public class Consumer2Controller {

	int count = 0;
	MongoDatabase db = MongoDBUtil.getDB();
	MongoCollection<Document> connect = db.getCollection("logger");
	String startTimeString = DateUtil.currentDateTime();
	String endTimeString = "";
	long startTime = System.currentTimeMillis();
	long endTime ;

	@ResponseBody
	@RequestMapping("/insert")
	public void consumeProduct() throws SQLException {
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
		consumer.subscribe(Arrays.asList("product","vendor","brandByProduct"));
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(100);
			for (ConsumerRecord<String, String> record : records) {
				count++;
				if(count%10000==0){
					endTime= System.currentTimeMillis();
					endTimeString = DateUtil.currentDateTime();
					String msg = "插入"+count+"条数据用时：" + (endTime - startTime) + " ms,开始时间：" + startTimeString + ",结束时间："
							+ endTimeString + "'";
					connect = db.getCollection("logger");
					Document loggerDoc = new Document("message", msg).append("create_time", new Date());
					connect.insertOne(loggerDoc);
				}
				if(record.topic().equals("product")){
					insertProduct(record.value());
				}else if(record.topic().equals("vendor")){
					insertVendor(record.value());
				}else{
					insertBrandByProduct(record.value());
				}
				
			}
		}
	}
	public void insertBrandByProduct(String json){
		connect = db.getCollection("brandByProduct");
		BrandByProduct b = JSON.parseObject(json,BrandByProduct.class);
		Document document = new Document("vendorId", b.getVendorId()).append("productId",b.getProductId());
		connect.insertOne(document);
	}
	public void insertVendor(String json){
		connect = db.getCollection("vendor");
		Vendor v = JSON.parseObject(json,Vendor.class);
		Document document = new Document("id", v.getId()).append("country", v.getCountry()).append("industry", v.getIndustry());
		connect.insertOne(document);
	}

	public void insertProduct(String json) throws SQLException {
		connect = db.getCollection("product");
		Product p = JSON.parseObject(json, Product.class);
		Document document = new Document("id", p.getId()).append("title", p.getTitle()).append("price", p.getPrice())
				.append("imgUrl", p.getImgUrl());
		connect.insertOne(document);
	}

}
