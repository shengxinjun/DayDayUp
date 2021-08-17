package com.sxj.documentData;

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
import com.sxj.domain.Product;
import com.sxj.domain.Vendor;

@Controller
@RequestMapping("/producer2")
public class Producer2Controller {
	
	
	@ResponseBody
	@RequestMapping("/product")
	public void readerProductCSV() throws IOException, SQLException {
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
				new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Product\\Product.csv"));// 换成你的文件名
		String line="";
		Product p = new Product();
		Producer<String, String> producer = new KafkaProducer<>(props);
		while ((line = reader.readLine()) != null) {
			line = filterSqlString(line);
			String item[] = line.split(",");
			p.setId(item[0]);
			p.setTitle(item[1]);
			p.setPrice(item[2]);
			p.setImgUrl(item[3]);
			line = (String) JSON.toJSON(p);
			producer.send(new ProducerRecord<String, String>("product", line));
		}
		reader.close();
		
	}
	@ResponseBody
	@RequestMapping("/vendor")
	public void readerVendorCSV() throws IOException, SQLException {
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
		Vendor v = new Vendor();
		Producer<String, String> producer = new KafkaProducer<>(props);
		while ((line = reader.readLine()) != null) {
			line = filterSqlString(line);
			String item[] = line.split(",");
			v.setId(item[0]);
			v.setCountry(item[1]);
			v.setIndustry(item[2]);
			line = (String) JSON.toJSON(v);
			producer.send(new ProducerRecord<String, String>("vendor", line));
		}
		reader.close();
		
	}
	@ResponseBody
	@RequestMapping("/relation")
	public void readerRelationCSV() throws IOException, SQLException {
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
				new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Product\\BrandByProduct.csv"));// 换成你的文件名
		String line="";
		BrandByProduct b = new BrandByProduct();
		Producer<String, String> producer = new KafkaProducer<>(props);
		while ((line = reader.readLine()) != null) {
			line = filterSqlString(line);
			String item[] = line.split(",");
			b.setVendorId(item[0]);
			b.setProductId(item[1]);
			line = (String) JSON.toJSON(b);
			producer.send(new ProducerRecord<String, String>("brandByProduct", line));
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
