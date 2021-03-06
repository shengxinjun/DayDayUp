package com.lab2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.util.StringUtils;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.util.DateUtil;

public class UpdateDataController {
	
	void updateDocument(){

		// 连接到 mongodb 服务
		MongoClient mongoClient = new MongoClient("121.196.54.227", 27017);

		// 连接到数据库
		MongoDatabase mongoDatabase = mongoClient.getDatabase("sheng");
		System.out.println("Connect to database successfully");

		MongoCollection<Document> collection = mongoDatabase.getCollection("A_share_data");

		try {
			String startTimeString="";
			String endTimeString="";
			int count = 0;
			BufferedReader reader = new BufferedReader(new FileReader("/usr/local/sxj/szag.csv"));
			String line = null;
			List<String> list = new ArrayList<String>();
			while ((line = reader.readLine()) != null&&count<=1000000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				list.add(item[24]);
			}
			startTimeString=DateUtil.currentDateTime();
			long startTime = System.currentTimeMillis();
			for(String s:list){
				collection.updateOne(new Document().append("_id", s), new Document().append("$set", new Document().append("b_market_amount", "test")));

				if (++count % 10000 == 0)
					System.out.println("正在执行第" + count + "条数据！");
				if(count==500000){
					long endTime = System.currentTimeMillis();
					String msg="更新500000条数据用时：" + (endTime - startTime)+" ms";
					collection = mongoDatabase.getCollection("logger");
					Document loggerDoc = new Document("message", msg).append("create_time", new Date());
					collection.insertOne(loggerDoc);
					collection = mongoDatabase.getCollection("A_share_data");
				}
			}
			endTimeString=DateUtil.currentDateTime();
			long endTime = System.currentTimeMillis();
			String msg="更新1000000条数据用时：" + (endTime - startTime)+" ms,开始时间："+startTimeString+",结束时间："+endTimeString+"'";
			
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
