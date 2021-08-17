package com.sxj.documentData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.bson.Document;
import org.springframework.util.StringUtils;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sxj.util.DateUtil;
import com.sxj.util.MongoDBUtil;

public class MongoDBController {
	public void insertDocument() throws IOException{

		MongoDatabase db = MongoDBUtil.getDB();
		MongoCollection<Document> collection = db.getCollection("product");
		String startTimeString = DateUtil.currentDateTime();
		String endTimeString = "";
		String line = null;
		int count = 1;
		for(int i=0;i<51;i++){
			File file = new File("/home/shengxinjun/Unibench_Multi-model_data/Product/Product.csv");
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "GB2312");
			BufferedReader reader=new BufferedReader(isr);
			
			// 一次读取一行
			while ((line = reader.readLine()) != null) {
				line = filterSqlString(line);
				
			}
			reader.close();
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
