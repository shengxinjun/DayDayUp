package com.lab1;

import java.io.BufferedReader;
import java.io.FileReader;

import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.util.StringUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class VisitDataController {
	public static void main(String[] args) {
		visitDocument();
	}

	static void visitDocument() {

		// 连接到 mongodb 服务
		MongoClient mongoClient = new MongoClient("www.shengxinjun.top", 27017);

		// 连接到数据库
		MongoDatabase mongoDatabase = mongoClient.getDatabase("sheng");
		System.out.println("Connect to database successfully");

		MongoCollection<Document> collection = mongoDatabase.getCollection("test");
		System.out.println("集合 test 选择成功");

		try {
			int count = 0;
			BufferedReader reader = new BufferedReader(
					new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Product\\Product.csv"));// 换成你的文件名
			String line = null;
			long startTime = System.currentTimeMillis();
			while ((line = reader.readLine()) != null) {
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				BasicDBObject doc = new BasicDBObject();
				doc.put("id", item[0]);
				FindIterable<Document> it = collection.find(doc);
				MongoCursor<Document> mongoCursor = it.iterator();
				while (mongoCursor.hasNext()) {
					System.out.println(mongoCursor.next());
				}
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
			long endTime = System.currentTimeMillis();
			System.out.println("程序运行时间： " + (endTime - startTime) + "ms");

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
