package com.lab1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.util.StringUtils;

import com.util.JdbcUtil;

public class VisitDataController {

	
	 void visitGraph(){


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
				rs=stat.executeQuery("match{class:production,where:(id='"+item[0]+"')}.outE('brandByProduct').inV(){as:vender} return vender.name");
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

	 void visitDocument() {

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
				rs=stat.executeQuery("select from  Product where id='"+item[0]+"'");
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
