package com.sxj.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Value;

public class MysqlDBUtil {

	private static String DB_URL="jdbc:mysql://120.55.63.242:3306/hhu?characterEncoding=utf-8";

	private static String USER="root";

	private static String PASS="root";

	private static String JDBC_DRIVER="com.mysql.cj.jdbc.Driver";
	
	private static Connection conn;
	
	private static Statement stmt;

	static {
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Statement getStatement(){
		return stmt;
	}
}
