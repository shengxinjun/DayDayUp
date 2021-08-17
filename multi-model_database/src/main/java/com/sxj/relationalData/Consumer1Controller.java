package com.sxj.relationalData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
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
import com.sxj.domain.Order;
import com.sxj.domain.OrderItem;
import com.sxj.util.DateUtil;
import com.sxj.util.MysqlDBUtil;

@RequestMapping("/consumer1")
@Controller
public class Consumer1Controller {

	static volatile int count = 0;

	static String startTimeString = DateUtil.currentDateTime();
	static String endTimeString = "";
	static long startTime = System.currentTimeMillis();
	static long endTime = System.currentTimeMillis();
	public static void main(String[] args) throws SQLException {

		Properties props = new Properties();
		// kafka集群
		props.put("bootstrap.servers", "121.196.54.227:9092");
		// 消费者组id
		props.put("group.id", "test240");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		// 设置自动提交offset
		// props.put("enable.auto.commit",true);
		// 提交延时
		// props.put("auto.commit.interval.ms", 1000);
		// KV的反序列化
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList("order"));
		while (true) {

			ConsumerRecords<String, String> records = consumer.poll(100);
			int type=3;
			for (ConsumerRecord<String, String> record : records) {
				if(type==1)
					insertOrder(record.value());
				else if(type==2)
					queryOrder(record.value());
				else if(type==3)
					updateOrder(record.value());
				else
					deleteOrder(record.value());
			}
		}
	}

	@ResponseBody
	@RequestMapping("/insert")
	public void consumeFromKafka(int type) throws SQLException {

		  
		Properties props = new Properties();
		// kafka集群
		props.put("bootstrap.servers", "121.196.54.227:9092");
		// 消费者组id
		props.put("group.id", "test241");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		// 设置自动提交offset
		// props.put("enable.auto.commit",true);
		// 提交延时
		// props.put("auto.commit.interval.ms", 1000);
		// KV的反序列化
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList("order"));
		while (true) {

			ConsumerRecords<String, String> records = consumer.poll(100);
			for (ConsumerRecord<String, String> record : records) {
				if(type==1)
					insertOrder(record.value());
				else if(type==2)
					queryOrder(record.value());
				else if(type==3)
					updateOrder(record.value());
				else
					deleteOrder(record.value());
			}
		}
	}

	private static  void deleteOrder(String json) {

		Statement stmt = MysqlDBUtil.getStatement();
		Order order = JSON.parseObject(json, Order.class);
		
		String sql = "";
		// 插入order
		sql = "delete from order_item where order_id='"+order.getId()+"'";
		try {
			stmt.execute(sql);
			count++;
			if (count % 10000 == 0) {
				endTimeString = DateUtil.currentDateTime();
				sql = "insert into logger set message='更新" + count + "条数据用时：" + (endTime - startTime) + " ms,开始时间："
						+ startTimeString + ",结束时间：" + endTimeString + "'";
				stmt.execute(sql);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql = "delete from orders where id='"+order.getId()+"'";
		try {
			ResultSet res =stmt.executeQuery(sql);
			while(res.next())
				count++;
			if (count % 10000 == 0) {
				endTimeString = DateUtil.currentDateTime();
				sql = "insert into logger set message='删除" + count + "条数据用时：" + (endTime - startTime) + " ms,开始时间："
						+ startTimeString + ",结束时间：" + endTimeString + "'";
				stmt.execute(sql);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static  void updateOrder(String json) {

		Statement stmt = MysqlDBUtil.getStatement();
		Order order = JSON.parseObject(json, Order.class);
		
		String sql = "";
		// 插入order
		sql = "update order_item set product_id ='u1101' where order_id='"+order.getId()+"'";
		try {
			int row =stmt.executeUpdate(sql);
			count=count+row;
			if (count % 10000 == 1) {
				endTimeString = DateUtil.currentDateTime();
				sql = "insert into logger set message='更新" + count + "条数据用时：" + (endTime - startTime) + " ms,开始时间："
						+ startTimeString + ",结束时间：" + endTimeString + "'";
				stmt.execute(sql);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static  void queryOrder(String json) {

		Statement stmt = MysqlDBUtil.getStatement();
		Order order = JSON.parseObject(json, Order.class);
		
		String sql = "";
		// 插入order
		sql = "select * from order_item where order_id='"+order.getId()+"'";
		try {
			ResultSet res =stmt.executeQuery(sql);
			count++;
			if (count % 10000 == 0) {
				endTimeString = DateUtil.currentDateTime();
				sql = "insert into logger set message='查询" + count + "条数据用时：" + (endTime - startTime) + " ms,开始时间："
						+ startTimeString + ",结束时间：" + endTimeString + "'";
				stmt.execute(sql);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static  void insertOrder(String json) throws SQLException {

		Statement stmt = MysqlDBUtil.getStatement();
		Order order = JSON.parseObject(json, Order.class);
		for (OrderItem item : order.getItems()) {
			item.setOrderId(order.getId());
			item.setId(UUID.randomUUID().toString());
		}
		String sql = "";
		// 插入order
		sql = "insert into orders (id,person_id,order_date,total_price) values ('" + order.getId() + "','"
				+ order.getPersonId() + "','" + order.getOrderDate() + "','" + order.getTotalPrice() + "')";
		System.out.print(sql);
		try {
			stmt.execute(sql);
			count++;
			if (count % 10000 == 0) {
				endTimeString = DateUtil.currentDateTime();
				sql = "insert into logger set message='插入" + count + "条数据用时：" + (endTime - startTime) + " ms,开始时间："
						+ startTimeString + ",结束时间：" + endTimeString + "'";
				stmt.execute(sql);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 插入orderitem
		for (OrderItem item : order.getItems()) {
			sql = "insert into order_item (id,product_id,order_id) values ('" + item.getId() + "','"
					+ item.getProductId() + "','" + item.getOrderId() + "')";
			System.out.print(sql);
			try {
				stmt.execute(sql);
				count++;
				if (count % 10000 == 0) {
					endTimeString = DateUtil.currentDateTime();
					sql = "insert into logger set message='插入" + count + "条数据用时：" + (endTime - startTime) + " ms,开始时间："
							+ startTimeString + ",结束时间：" + endTimeString + "'";
					stmt.execute(sql);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
