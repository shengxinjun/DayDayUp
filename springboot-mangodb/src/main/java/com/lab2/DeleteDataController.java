package com.lab2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;

import org.bson.Document;
import org.springframework.util.StringUtils;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DeleteDataController {
	
	void deleteDocument(){

		// 连接到 mongodb 服务
		MongoClient mongoClient = new MongoClient("www.shengxinjun.top", 27017);

		// 连接到数据库
		MongoDatabase mongoDatabase = mongoClient.getDatabase("sheng");
		System.out.println("Connect to database successfully");

		MongoCollection<Document> collection = mongoDatabase.getCollection("A_share_data");
		System.out.println("集合 test 选择成功");

		try {
			int count = 0;
			BufferedReader reader = new BufferedReader(new FileReader("/opt/lab2/szag.csv"));// 换成你的文件名
			String line = null;
			long startTime = System.currentTimeMillis();
			while ((line = reader.readLine()) != null&&count<=1000000) {
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				collection.deleteOne(new Document().append("code", item[0]).append("date", item[2]));
				if(count==500000){
					long endTime = System.currentTimeMillis();
					String msg="insert into logger set message='删除500000条数据用时：" + (endTime - startTime)+" ms'";
					collection = mongoDatabase.getCollection("logger");
					Document loggerDoc = new Document("message", msg).append("create_time", new Date());
					collection.insertOne(loggerDoc);
					collection = mongoDatabase.getCollection("A_share_data");
				}
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
			long endTime = System.currentTimeMillis();
			String msg="insert into logger set message='删除1000000条数据用时：" + (endTime - startTime)+" ms'";
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
