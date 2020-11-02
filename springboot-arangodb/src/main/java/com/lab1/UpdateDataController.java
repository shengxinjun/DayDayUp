package com.lab1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.arangodb.ArangoCursor;
import com.arangodb.entity.BaseDocument;
import com.util.ArangoUtil;

public class UpdateDataController {
	public static void main(String[] args) {
		updateGraph();
	}
	
	static void updateGraph(){


			ArangoUtil arangoUtil = new ArangoUtil();
			arangoUtil.initArango("www.shengxinjun.top", 8529, "root", "root");
			arangoUtil.initDbs("example");
			int count = 0;
			try {
				BufferedReader reader = new BufferedReader(
						new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Product\\Product.csv"));// 换成你的文件名
				
				String line = null;
				long startTime = System.currentTimeMillis();
				while ((line = reader.readLine()) != null) {
					line = filterSqlString(line);
					Map<String, Object> params= new HashMap<>();
					String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
					params.put("name", item[0]);
					String insertRelation="for c in Product filter c.name==@name for v in 1..1 outbound c BrandByProduct return v";
					ArangoCursor<BaseDocument> t = arangoUtil.query(0, insertRelation, params);
					if (++count % 100 == 0)
						System.out.println("正在执行第" + count + "条数据！");
				}
				long endTime = System.currentTimeMillis();
				System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
			} catch (IOException e) {
				System.out.println(e.toString());
			}

		
	}
	
	static void updateDocument(){

		ArangoUtil arangoUtil = new ArangoUtil();
		arangoUtil.initArango("www.shengxinjun.top", 8529, "root", "root");
		arangoUtil.initDbs("example");
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Product\\Product.csv"));// 换成你的文件名
			
			String line = null;
			long startTime = System.currentTimeMillis();
			while ((line = reader.readLine()) != null) {
				line = filterSqlString(line);
				Map<String, Object> params= new HashMap<>();
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				params.put("name", item[0]);
				String insertRelation="for c in Product filter c.name==@name update c with{title:'test'} in Product";
				ArangoCursor<BaseDocument> t = arangoUtil.query(0, insertRelation, params);
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
			long endTime = System.currentTimeMillis();
			System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
		} catch (IOException e) {
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
