package com.sxj.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import com.orientechnologies.orient.jdbc.OrientJdbcDriver;
public class OrientDBUtil {

	static Properties info;
	static Connection conn;
	static Statement stmt;

	static {
		try {
			Class.forName(OrientJdbcDriver.class.getName());
			info = new Properties();
			info.put("user", "root");
			info.put("password", "123456");
			conn=DriverManager.getConnection("jdbc:orient:remote:120.55.63.242/sheng", info);
			stmt = conn.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Statement getStatement() {
		return stmt;
	}
}
