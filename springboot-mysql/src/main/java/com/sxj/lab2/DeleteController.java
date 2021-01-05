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

public class DeleteController {
	// MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://shengxinjun.top:3306/hhu";
 
    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "sxj";
    static final String PASS = "sxj";
 
    public void doDelete() throws FileNotFoundException {
    	BufferedReader reader = new BufferedReader(
				new FileReader("/opt/mysql/US_Accidents_June20.csv"));// 换成你的文件名
		
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
			while ((line = reader.readLine()) != null) {
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				String sql = "delete from  us_accident where id ='"+item[0]+"'";

				stmt.execute(sql);
				if(count==548576){
					long endTime = System.currentTimeMillis();
					String sql1="insert into logger set message='删除548576条数据用时：" + (endTime - startTime)+" ms'";
		            stmt.execute(sql1);
		        }
				if (++count % 10000 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
			

			long endTime = System.currentTimeMillis();
			String sql1="insert into logger set message='删除1048576条数据用时：" + (endTime - startTime)+" ms'";
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

	public String filterSqlString(String sqlStr) {
		if (StringUtils.isEmpty(sqlStr)) {
			return sqlStr;
		}
		sqlStr = sqlStr.replace("'", "");
		sqlStr = sqlStr.replace("\\", "");
		return sqlStr;
	}


}
