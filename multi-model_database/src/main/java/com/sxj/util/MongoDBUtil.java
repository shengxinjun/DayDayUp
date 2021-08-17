package com.sxj.util;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBUtil {

	static MongoClient mongoClient;
	static MongoDatabase mongoDatabase;

	static {
		mongoClient = new MongoClient("120.55.63.242", 27017);
		mongoDatabase = mongoClient.getDatabase("sheng");
	}

	public static MongoDatabase getDB() {
		return mongoDatabase;
	}
}
