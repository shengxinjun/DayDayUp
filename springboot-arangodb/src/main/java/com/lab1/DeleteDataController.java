package com.lab1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.util.ArangoUtil;

public class DeleteDataController {
	public static void main(String[] args) {
		deleteDocument();
	}
	static void deleteDocument(){



		ArangoUtil arangoUtil = new ArangoUtil();
		arangoUtil.initArango("www.shengxinjun.top", 8529, "root", "root");
		arangoUtil.initDbs("example");
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Product\\Product.csv"));// 换成你的文件名
			
			String line = null;
			long startTime = System.currentTimeMillis();
			long endTime = System.currentTimeMillis();
			while ((line = reader.readLine()) != null) {
				line = filterSqlString(line);
				Map<String, Object> params= new HashMap<>();
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				params.put("name", item[0]);
				String insertRelation="for c in Product filter c.name==@name remove c in Product";
				arangoUtil.query(0, insertRelation, params);
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
			endTime = System.currentTimeMillis();
			System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	
		
	}
	
	static void deleteGraph(){


		ArangoUtil arangoUtil = new ArangoUtil();
		arangoUtil.initArango("www.shengxinjun.top", 8529, "root", "root");
		arangoUtil.initDbs("example");
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Product\\Product.csv"));// 换成你的文件名
			BufferedReader reader2 = new BufferedReader(
					new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Vendor\\Vendor.csv"));// 换成你的文件名
			BufferedReader reader3 = new BufferedReader(
					new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Product\\BrandByProduct.csv"));// 换成你的文件名
			
			String line = null;
			long startTime = System.currentTimeMillis();
			while ((line = reader3.readLine()) != null) {
				line = filterSqlString(line);
				Map<String, Object> params= new HashMap<>();
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				params.put("name", item[1]);
				params.put("name2", item[0]);
				String relation="let pId=FIRST(for c in Product filter c.name==@name return c._id) let vId=FIRST(for c in Product filter c.name==@name2 return c._id) FILTER pId != null AND vId != null for c in BrandByProduct filter c._from==pId filter c._to==vId remove c in BrandByProduct";
				arangoUtil.query(0, relation, params);
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
			long endTime = System.currentTimeMillis();
			while ((line = reader.readLine()) != null) {
				line = filterSqlString(line);
				Map<String, Object> params= new HashMap<>();
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				params.put("name", item[0]);
				String insertRelation="for c in Product filter c.name==@name remove c in Product";
				arangoUtil.query(0, insertRelation, params);
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
			while ((line = reader2.readLine()) != null) {
				line = filterSqlString(line);
				Map<String, Object> params= new HashMap<>();
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				params.put("name", item[0]);
				String insertRelation="for c in Vendor filter c.name==@name remove c in Vendor";
				arangoUtil.query(0, insertRelation, params);
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
			endTime = System.currentTimeMillis();
			System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
