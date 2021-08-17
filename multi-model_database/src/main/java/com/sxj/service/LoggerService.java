package com.sxj.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.neo4j.driver.v1.Values;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.sxj.domain.OrderItem;
import com.sxj.domain.Person;
import com.sxj.util.ArangoDBUtil;
import com.sxj.util.DateUtil;
import com.sxj.util.MongoDBUtil;
import com.sxj.util.Neo4jDBUtil;
@Service
public class LoggerService {
	
	public void MongoDBLogger(String start,String end,int count,int type,String remark){
		int totalTime = DateUtil.subtractMinute(start, end);
		if(remark==null){
			remark="";
		}
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("logger");
		Document document = new Document("start_time", start).append("end_time", end).append("count", count).append("type", typeToStr(type)).append("total_time", totalTime).append("remark", remark);
		collection.insertOne(document);
	}
	public void Neo4jLogger(String start,String end,int count,int type,String remark){
		int totalTime = DateUtil.subtractMinute(start, end);
		if(remark==null){
			remark="";
		}
		Neo4jDBUtil.getSession()
		.run("CREATE (a:logger {start_time: {start},end_time: {end},count: {count},type: {type},total_time: {totalTime},remark:{remark}})",
				Values.parameters("start", start, "end", end, "count",count, "type", typeToStr(type), "totalTime",totalTime,"remark",remark));
	}
	public void ArangoDBLogger(String start,String end,int count,int type,String remark){
		int totalTime = DateUtil.subtractMinute(start, end);
		if(remark==null){
			remark="";
		}
		String aql = "insert {start_time:@start,end_time:@end,count:@count,type:@type,total_time:@totalTime,remark:@remark} in logger";
		Map<String, Object> params = new HashMap<>();
		params.put("start", start);
		params.put("end", end);
		params.put("count", count);
		params.put("type", typeToStr(type));
		params.put("totalTime", totalTime);
		params.put("remark", remark);
		ArangoDBUtil.getArangoDatabase().query(aql, params, Person.class);
	}
	
	String typeToStr(int type){
		String typeStr="";
		switch(type){
		case 1:
			typeStr="插入";
			break;
		case 2:
			typeStr="查询";
			break;
		case 3:
			typeStr="更新";
			break;
		case 4:
			typeStr="删除";
			break;
		}
		return typeStr;
	}
}
