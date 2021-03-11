package com.lab1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.util.StringUtils;

import com.util.JdbcUtil;

public class ImportDataController {


	 void importGraph() {

		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConn();
			stat = conn.createStatement();
			int count = 0;
			BufferedReader reader = new BufferedReader(
					new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Product\\Product.csv"));// 换成你的文件名
			BufferedReader reader2 = new BufferedReader(
					new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Vendor\\Vendor.csv"));// 换成你的文件名
			BufferedReader reader3 = new BufferedReader(
					new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Product\\BrandByProduct.csv"));// 换成你的文件名
			
			String line = null;
			long startTime = System.currentTimeMillis();
			/*while ((line = reader.readLine()) != null) {
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				stat.execute("create vertex Production  set id='" + item[0] + "',title='" + item[1] + "',price='" + item[2]
						+ "',imgUrl='" + item[3] + "'");
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
			System.out.println("------------Production ending-------------");
			while ((line = reader2.readLine()) != null) {
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				stat.execute("create vertex Vendor  set name='" + item[0] + "',country='" + item[1] + "',industry='" + item[2]+  "'");
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}*/
			System.out.println("------------Vendor ending-------------");
			while ((line = reader3.readLine()) != null) {
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				stat.execute("create edge BrandByProduct from (select from Production where id='"+item[1]+"') to (select from Vendor where name='"+item[0]+"')  ");
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
			long endTime = System.currentTimeMillis();
			System.out.println("程序运行时间： " + (endTime - startTime) + "ms");

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			JdbcUtil.close(rs, stat, conn);
		}
	}

	 void importDocument() {

		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConn();
			stat = conn.createStatement();
			int count = 0;
			BufferedReader reader = new BufferedReader(
					new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Product\\Product.csv"));// 换成你的文件名
			String line = null;
			long startTime = System.currentTimeMillis();
			while ((line = reader.readLine()) != null) {
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				stat.execute("insert into Product set id='" + item[0] + "',title='" + item[1] + "',price='" + item[2]
						+ "',imgUrl='" + item[3] + "'");
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
			long endTime = System.currentTimeMillis();
			System.out.println("程序运行时间： " + (endTime - startTime) + "ms");

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			JdbcUtil.close(rs, stat, conn);
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
