package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.orientechnologies.orient.jdbc.OrientJdbcDriver;

public class JdbcUtil {
	
	
	
	public static Connection getConn() throws SQLException, ClassNotFoundException{
		Class.forName(OrientJdbcDriver.class.getName());
		Properties info = new Properties(); 
	      info.put("user", "root"); 
	      info.put("password", "123456"); 
		return DriverManager.getConnection("jdbc:orient:remote:121.196.54.227/sheng",info);
	}
	public static void close(ResultSet rs,Statement stat,Connection conn){
		
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(stat!=null){
			try {
				stat.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
