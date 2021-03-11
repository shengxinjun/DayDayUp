package com.lab2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.util.DateUtil;
import com.util.JdbcUtil;

public class UpdateController {

	public void updateRelationData() {

		String startTimeString="";
		String endTimeString="";
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConn();
			stat = conn.createStatement();
			int count = 0;
			BufferedReader reader = new BufferedReader(
					new FileReader("/usr/local/sxj/US_Accidents_June20.csv"));
			String line = null;
			List<String> accidents = new ArrayList<String>();
			while ((line = reader.readLine()) != null && count <= 1000000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split(",");
				accidents.add(item[0]);
			}
			count = 0;
			startTimeString=DateUtil.currentDateTime();
			long startTime = System.currentTimeMillis();
			for (String id : accidents) {
				count++;
				String sql = "update  accident set source='test'  where id='" + id + "'";
				stat.execute(sql);
				if (count % 10000 == 0) {
					System.out.println("正在执行第：" + count + "条数据");
				}
				if (count == 500000) {
					long endTime = System.currentTimeMillis();
					String msg = "更新50w条关系型数据总共用时： " + (endTime - startTime) + "ms";
					stat.execute("insert into  logger set msg='" + msg + "'");
				}
			}

			long endTime = System.currentTimeMillis();
			endTimeString=DateUtil.currentDateTime();
			String msg = "更新100w条关系型数据总共用时： " + (endTime - startTime) + "ms,开始时间："+startTimeString+",结束时间："+endTimeString;
			stat.execute("insert into  logger set msg='" + msg + "'");

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			JdbcUtil.close(rs, stat, conn);
		}

	}

	public void updateDocumentData() {

		String startTimeString="";
		String endTimeString="";
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConn();
			stat = conn.createStatement();
			int count = 0;
			BufferedReader reader = new BufferedReader(new FileReader("/usr/local/sxj/szag.csv"));
			String line = null;
			List<String> records = new ArrayList<>();
			while ((line = reader.readLine()) != null && count <= 1000000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split(",");
				records.add(item[24]);
			}
			count = 0;
			startTimeString=DateUtil.currentDateTime();
			long startTime = System.currentTimeMillis();
			for (String id : records) {
				count++;
				String sql = "update record set b_market_amount='test' where id='" + id + "'";
				stat.execute(sql);
				if (count % 10000 == 0) {
					System.out.println("正在执行第：" + count + "条数据");
				}
				if (count == 500000) {
					long endTime = System.currentTimeMillis();
					String msg = "更新50w条文档型数据总共用时： " + (endTime - startTime) + "ms";
					stat.execute("insert into  logger set msg='" + msg + "'");
				}
			}

			long endTime = System.currentTimeMillis();
			endTimeString=DateUtil.currentDateTime();
			String msg = "更新100w条文档型数据总共用时： " + (endTime - startTime) + "ms,开始时间："+startTimeString+",结束时间："+endTimeString;
			stat.execute("insert into  logger set msg='" + msg + "'");

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			JdbcUtil.close(rs, stat, conn);
		}

	}

	public void updateGraphData() {

		String startTimeString="";
		String endTimeString="";
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConn();
			stat = conn.createStatement();
			int count = 0;
			// 将数据读入list集合中
			String line = null;
			BufferedReader reader = new BufferedReader(new FileReader("/usr/local/sxj/post_hasCreator_person_0_0.csv"));
			List<PostPersonRelation> relations = new ArrayList<>();
			while ((line = reader.readLine()) != null && count++ <= 45025) {
				line = filterSqlString(line);
				String item[] = line.split("\\|");
				PostPersonRelation relation = new PostPersonRelation();
				relation.setPostId(item[0]);
				relation.setPersonId(item[1]);
				relations.add(relation);
			}
			reader.close();
			count = 0;
			startTimeString=DateUtil.currentDateTime();
			long startTime = System.currentTimeMillis();
			for (PostPersonRelation relation : relations) {
				count++;
				if (count % 10000 == 0) {
					System.out.println("正在更新第：" + count + "条数据");
				}
				String sql = "update person set place='test' where id = (select in().id from post where id='"+relation.getPostId()+"')";
				stat.execute(sql);
			}
			for (PostPersonRelation relation : relations) {
				count++;
				if (count % 10000 == 0) {
					System.out.println("正在更新第：" + count + "条数据");
				}
				String sql = "update person set place='test' where id = (select in().id from post where id='"+relation.getPostId()+"')";
				stat.execute(sql);
			}
			relations=null;
			long endTime = System.currentTimeMillis();
			endTimeString=DateUtil.currentDateTime();
			String msg = "更新10w条图数据总共用时： " + (endTime - startTime) + "ms,开始时间："+startTimeString+",结束时间："+endTimeString;
			stat.execute("insert into  logger set msg='" + msg + "'");

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
