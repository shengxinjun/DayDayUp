package com.sxj.relationalData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/producer1")
public class Producer1Controller {
	
	
	public static void main(String[] args) throws IOException {

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
				new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Order\\Order.json"));// 换成你的文件名
		String line="";
		int count=0;
		Producer<String, String> producer = new KafkaProducer<>(props);
		while ((line = reader.readLine()) != null &&count<100000) {
			producer.send(new ProducerRecord<String, String>("order", line));
			count++;
			if(count%10000==0)
				System.out.println("count="+count);
		}
		reader.close();
		
	
	}
	
	@ResponseBody
	@RequestMapping("/insert")
	public void readerOrderJson() throws IOException, SQLException {
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
				new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Order\\Order.json"));// 换成你的文件名
		String line="";
		int count=0;
		Producer<String, String> producer = new KafkaProducer<>(props);
		while ((line = reader.readLine()) != null &&count<100000) {
			producer.send(new ProducerRecord<String, String>("order", line));
			count++;
		}
		reader.close();
		
	} 
}
