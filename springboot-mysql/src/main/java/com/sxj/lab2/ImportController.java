package com.sxj.lab2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.util.StringUtils;


public class ImportController {
	// MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
     final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
     final String DB_URL = "jdbc:mysql://shengxinjun.top:3306/hhu";
 
    // 数据库的用户名与密码，需要根据自己的设置
     final String USER = "sxj";
     final String PASS = "sxj";
 
    public  void doImport() throws FileNotFoundException {
    	BufferedReader reader = new BufferedReader(
				new FileReader("/opt/lab2/US_Accidents_June20.csv"));// 换成你的文件名
		
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
        
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            ResultSet rs = null;
            
            String line = null;
			int count = 0;
			long startTime = System.currentTimeMillis();
			while ((line = reader.readLine()) != null&&count<=1000000) {
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				String sql = "insert into us_accident set id ='"+item[0]+"',source='"+item[1]+"',start_time='"+item[4]+"',end_time='"+item[5]+"',start_lat='"+item[6]+"',start_lng='"+item[7]+"',street='"+item[13]+"',city='"+item[15]+"',country='"+item[16]+"',state='"+item[17]+"',airport_code='"+item[21]+"',weather='"+item[22]+"',temperature='"+item[23]+"',wind='"+item[24]+"'";

				stmt.execute(sql);
				if(count==500000){
					long endTime = System.currentTimeMillis();
					String sql1="insert into logger set message='插入500000条数据用时：" + (endTime - startTime)+" ms'";
		            stmt.execute(sql1);
					
				}
				if (++count % 10000 == 0)
					System.out.println("正在插入第" + count + "条数据！");
				
			}
			

			long endTime = System.currentTimeMillis();
			String sql1="insert into logger set message='插入1000000条数据用时：" + (endTime - startTime)+" ms'";
            stmt.execute(sql1);
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }

	public  String filterSqlString(String sqlStr) {
		if (StringUtils.isEmpty(sqlStr)) {
			return sqlStr;
		}
		sqlStr = sqlStr.replace("'", "");
		sqlStr = sqlStr.replace("\\", "");
		return sqlStr;
	}
}
