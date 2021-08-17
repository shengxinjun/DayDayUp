package com.sxj.graphData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Values;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sxj.service.OrderService;
import com.sxj.util.DateUtil;
import com.sxj.util.Neo4jDBUtil;
public class Neo4jController {

	private static Logger log = Logger.getLogger(Neo4jController.class);
	public void startInterface(){
		try {
			insertGraph();
			queryGraph();
			updateGraph();
			deleteGraph();
		} catch (IOException e) {
			log.error("--------------------------异常::"+e+"-----------------------------------");
			log.error("--------------------------Neo4j执行CRUD发生异常-----------------------------------");
		}
	}
	
	public void insertGraph() throws IOException {
		File file = new File("/home/shengxinjun/Unibench_Multi-model_data/Customer/person_0_0.csv");
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "GB2312");
		BufferedReader reader=new BufferedReader(isr);
		Session session = Neo4jDBUtil.getSession();
		List<String> personId = new ArrayList<String>();
		String endTimeString = "";
		String line = null;
		int count = 0;
		// 一次读取一行
		while ((line = reader.readLine()) != null && count <= 100000) {
			line = filterSqlString(line);
			String item[] = line.split("\\|");
			session.run(
					"CREATE (a:person {id: {id},firstName: {firstName},lastName: {lastName},gender: {gender},birthday: {birthday},creationDate: {creationDate}, locationIP: {locationIP}, browserUsed: {browserUsed}, place: {place}})",
					Values.parameters("id", item[0], "firstName", item[1],"lastName", item[2],"gender", item[3],"birthday", item[4],"creationDate", item[5],"locationIP", item[6],"browserUsed", item[7],"place", item[8]));
			personId.add(item[0]);
		}
		
		//从插入关系开始计时
		String startTimeString = DateUtil.currentDateTime();
		//定义停止符号
		boolean flag=true;
		for(String id1:personId){
			if(!flag)break;
			for(String id2:personId){
				if(id1==id2)continue;
				count++;
				session.run(
						"match(p1:person{id:{personId1}}),(p2:person{id:{personId2}})  create (p1)-[:person_know_person{relationName:'know'}]->(p2)",
						Values.parameters("personId1", id1, "personId2", id2));
				if(count%100000==0){
					endTimeString=DateUtil.currentDateTime();
					String msg="插入"+count+"条数据用时：" + DateUtil.subtractMinute(startTimeString, endTimeString)+" 分钟,开始时间："+startTimeString+",结束时间："+endTimeString;
					
					session.run(
							"CREATE (a:logger {message: {msg}})",
							Values.parameters("msg", msg));
					if(count==500000)flag=false;
				}
			}
		}
		
	}

	public void queryGraph() throws IOException {
		Session session = Neo4jDBUtil.getSession();
		
		String endTimeString = "";
		int count = 0;
		File file = new File("/home/shengxinjun/Unibench_Multi-model_data/Customer/person_0_0.csv");
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "GB2312");
		BufferedReader reader=new BufferedReader(isr);
		String line="";
		List<String> personId=new ArrayList<>();
		while ((line = reader.readLine()) != null ) {
			line = filterSqlString(line);
			String item[] = line.split("\\|");
			personId.add(item[0]);
		}
		//从插入关系开始计时
		String startTimeString = DateUtil.currentDateTime();
		//定义停止符号
		boolean flag=true;
		for(String id1:personId){
			if(!flag)break;
			for(String id2:personId){
				if(id1==id2)continue;
				count++;
				StatementResult result = session.run(
						"match(p1:person)-[r]->(p2:person) where p1.id={personId} return p2",
						Values.parameters("personId", id2));
				if(count%100000==0){
					endTimeString=DateUtil.currentDateTime();
					String msg="查找"+count+"条数据用时：" + DateUtil.subtractMinute(startTimeString, endTimeString)+" 分钟,开始时间："+startTimeString+",结束时间："+endTimeString;
					
					session.run(
							"CREATE (a:logger {message: {msg}})",
							Values.parameters("msg", msg));
					if(count==500000)flag=false;
				}
			}
		}
	}
	public void updateGraph() throws IOException {
		Session session = Neo4jDBUtil.getSession();
		
		String endTimeString = "";
		int count = 0;
		File file = new File("/home/shengxinjun/Unibench_Multi-model_data/Customer/person_0_0.csv");
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "GB2312");
		BufferedReader reader=new BufferedReader(isr);
		String line="";
		List<String> personId=new ArrayList<>();
		while ((line = reader.readLine()) != null ) {
			line = filterSqlString(line);
			String item[] = line.split("\\|");
			personId.add(item[0]);
		}
		//从插入关系开始计时
		String startTimeString = DateUtil.currentDateTime();
		//定义停止符号
		boolean flag=true;
		for(String id1:personId){
			if(!flag)break;
			for(String id2:personId){
				if(id1==id2)continue;
				count++;
				StatementResult result = session.run(
						"match(p1:person)-[r]->(p2:person) where p1.id={personId} set p2.lastName='test'",
						Values.parameters("personId", id2));

				if(count%100000==0){
					endTimeString=DateUtil.currentDateTime();
					String msg="更新"+count+"条数据用时：" + DateUtil.subtractMinute(startTimeString, endTimeString)+" 分钟,开始时间："+startTimeString+",结束时间："+endTimeString;
					
					session.run(
							"CREATE (a:logger {message: {msg}})",
							Values.parameters("msg", msg));
					if(count==500000)flag=false;
				}
			}
		}
	}
	public void deleteGraph() throws IOException {
		Session session = Neo4jDBUtil.getSession();
		
		String endTimeString = "";
		int count = 0;
		File file = new File("/home/shengxinjun/Unibench_Multi-model_data/Customer/person_0_0.csv");
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "GB2312");
		BufferedReader reader=new BufferedReader(isr);
		String line="";
		List<String> personId=new ArrayList<>();
		while ((line = reader.readLine()) != null ) {
			line = filterSqlString(line);
			String item[] = line.split("\\|");
			personId.add(item[0]);
		}
		//从插入关系开始计时
		String startTimeString = DateUtil.currentDateTime();
		//定义停止符号
		boolean flag=true;
		for(String id1:personId){
			if(!flag)break;
			for(String id2:personId){
				if(id1==id2)continue;
				count++;
				session.run(
						"match(p1:person{id:{personId1}})-[r]->(p2:person{id:{personId2}}) delete r",
						Values.parameters("personId1", id1, "personId2", id2));
				
				if(count%100000==0){
					endTimeString=DateUtil.currentDateTime();
					String msg="删除"+count+"条数据用时：" + DateUtil.subtractMinute(startTimeString, endTimeString)+" 分钟,开始时间："+startTimeString+",结束时间："+endTimeString;
					
					session.run(
							"CREATE (a:logger {message: {msg}})",
							Values.parameters("msg", msg));
					if(count==500000)flag=false;
				}
			}
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
