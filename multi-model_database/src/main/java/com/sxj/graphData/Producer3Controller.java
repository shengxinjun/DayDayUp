package com.sxj.graphData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sxj.domain.BrandByProduct;
import com.sxj.domain.Person;
import com.sxj.domain.Relation;
import com.sxj.domain.Product;
import com.sxj.domain.Vendor;

@Controller
@RequestMapping("/producer3")
public class Producer3Controller {
	
	
	@ResponseBody
	@RequestMapping("/person")
	public void readerPersonCSV() throws IOException, SQLException {
		Properties props = new Properties();
		//kafka集群
		props.put("bootstrap.servers", "121.196.54.227:9092");
		//应答级别
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		//重试次数
		props.put("retries", 0);
		//批量大小
		props.put("batch.size", 16384);
		//提交延时
		props.put("linger.ms", 1);
		//缓存
		props.put("buffer.memory", 33554432);
		//KV的序列化类
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		BufferedReader reader = new BufferedReader(
				new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Customer\\person_0_0.csv"));// 换成你的文件名
		String line="";
		Person p = new Person();
		Producer<String, String> producer = new KafkaProducer<>(props);
		while ((line = reader.readLine()) != null) {
			line = filterSqlString(line);
			String item[] = line.split(",");
			p.setId(item[0]);
			p.setFirstName(item[1]);
			p.setLastName(item[2]);
			p.setGender(item[3]);
			line = (String) JSON.toJSON(p);
			producer.send(new ProducerRecord<String, String>("person", line));
		}
		reader.close();
		
	}
	@ResponseBody
	@RequestMapping("/personKnowperson")
	public void readerPersonRelationCSV() throws IOException, SQLException {
		Properties props = new Properties();
		//kafka集群
		props.put("bootstrap.servers", "121.196.54.227:9092");
		//应答级别
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		//重试次数
		props.put("retries", 0);
		//批量大小
		props.put("batch.size", 16384);
		//提交延时
		props.put("linger.ms", 1);
		//缓存
		props.put("buffer.memory", 33554432);
		//KV的序列化类
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		BufferedReader reader = new BufferedReader(
				new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Vendor\\Vendor.csv"));// 换成你的文件名
		String line="";
		Relation p= new Relation();
		Producer<String, String> producer = new KafkaProducer<>(props);
		while ((line = reader.readLine()) != null) {
			line = filterSqlString(line);
			String item[] = line.split(",");
			p.setPerson1(item[0]);
			p.setPerson2(item[1]);
			line = (String) JSON.toJSON(p);
			producer.send(new ProducerRecord<String, String>("personknowperson", line));
		}
		reader.close();
		
	}
	
	public  String filterSqlString(String sqlStr) {
		if (StringUtils.isEmpty(sqlStr)) {
			return sqlStr;
		}
		sqlStr = sqlStr.replace("'", "");
		sqlStr = sqlStr.replace("\\", "");
		return sqlStr;
	}
}
