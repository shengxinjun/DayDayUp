package com.lab2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.util.StringUtils;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ImportDataController {
	

	void importDocument() {

		// 连接到 mongodb 服务
		MongoClient mongoClient = new MongoClient("www.shengxinjun.top", 27017);
		// 连接到数据库
		MongoDatabase mongoDatabase = mongoClient.getDatabase("sheng");
		System.out.println("Connect to database successfully");

		MongoCollection<Document> collection = mongoDatabase.getCollection("A_share_data");

		try {
			int count = 0;
			BufferedReader reader = new BufferedReader(new FileReader("/opt/lab2/szag.csv"));// 换成你的文件名
			String line = null;
			long startTime = System.currentTimeMillis();
			while ((line = reader.readLine()) != null&&count<=1000000) {
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				Document document = new Document("id", count).append("code", item[0]).append("name", item[1])
						.append("date", item[2]).append("last_price", item[3]).append("start_price", item[4])
						.append("max_price", item[5]).append("min_price", item[6])
						.append("end_price", item[7]).append("transaction_amount", item[8])
						.append("transaction_money", item[9]).append("up_and_down", item[10])
						.append("up_and_down_range", item[11]).append("average_price", item[12])
						.append("turnover_rate", item[13]).append("a_market_value", item[14])
						.append("b_market_value", item[15]).append("all_market_value", item[16])
						.append("a_market_amount", item[17]).append("b_market_amount", item[18])
						.append("all_market_amount", item[19]).append("syl", item[20]).append("sjl", item[21])
						.append("sxl", item[22]).append("sxl2", item[23]);
				collection.insertOne(document);
				if(count==500000){
					long endTime = System.currentTimeMillis();
					String msg="insert into logger set message='插入500000条数据用时：" + (endTime - startTime)+" ms'";
					collection = mongoDatabase.getCollection("logger");
					Document loggerDoc = new Document("message", msg).append("create_time", new Date());
					collection.insertOne(loggerDoc);
					collection = mongoDatabase.getCollection("A_share_data");
				}
				if (++count % 10000 == 0)
					System.out.println("正在插入第" + count + "条数据！");
			}
			long endTime = System.currentTimeMillis();
			String msg="insert into logger set message='插入1000000条数据用时：" + (endTime - startTime)+" ms'";
			collection = mongoDatabase.getCollection("logger");
			Document loggerDoc = new Document("message", msg).append("create_time", new Date());
			collection.insertOne(loggerDoc);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public static String filterSqlString(String sqlStr) {
		if (StringUtils.isEmpty(sqlStr)) {
			return sqlStr;
		}
		sqlStr = sqlStr.replace("'", "");
		sqlStr = sqlStr.replace("\\", "");
		return sqlStr;
	}
}
