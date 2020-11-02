package com.lab1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.arangodb.entity.BaseDocument;
import com.util.ArangoUtil;

public class ImportDataController {

	public static void main(String[] args) {
		importData();
	}

	static void importData() {

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
			while ((line = reader.readLine()) != null) {
				line = filterSqlString(line);
				Map<String, Object> params= new HashMap<>();
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				params.put("name", item[0]);
				params.put("title", item[1]);
				params.put("price", item[2]);
				params.put("imgUrl", item[3]);
				String insertRelation="insert {name:@name,title:@title,price:@price,imgUrl:@imgUrl} in Product";
				arangoUtil.query(0, insertRelation, params);
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
			long endTime = System.currentTimeMillis();
			System.out.println("文档插入完成，程序运行时间： " + (endTime - startTime) + "ms");
			while ((line = reader2.readLine()) != null) {
				line = filterSqlString(line);
				Map<String, Object> params= new HashMap<>();
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				params.put("name", item[0]);
				params.put("country", item[1]);
				params.put("industry", item[2]);
				String insertRelation="insert {name:@name,country:@country,industry:@industry} in Vendor";
				arangoUtil.query(0, insertRelation, params);
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
			while ((line = reader3.readLine()) != null) {
				line = filterSqlString(line);
				Map<String, Object> params= new HashMap<>();
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				params.put("vname", item[0]);
				params.put("pname", item[1]);
				String insertRelation="let productId=FIRST(for c in Product filter c.name==@pname return c._id) let vendorId=FIRST(for c in Vendor filter c.name==@vname return c._id) FILTER productId != null AND vendorId != null INSERT { _from: productId, _to: vendorId } INTO BrandByProduct RETURN NEW";
				arangoUtil.query(0, insertRelation, params);
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
			endTime = System.currentTimeMillis();
			System.out.println("图插入完成，程序运行时间： " + (endTime - startTime) + "ms");
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
