package com.sxj.lab2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.sxj.util.DateUtil;

public class QueryController {
	// MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
    final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    final String DB_URL = "jdbc:mysql://121.196.54.227:3306/hhu?characterEncoding=utf-8";


   // 数据库的用户名与密码，需要根据自己的设置
    final String USER = "spp";
    final String PASS = "`Ho,.hai2020Sci";
 
    public void doQuery() throws FileNotFoundException {
    	BufferedReader reader = new BufferedReader(
				new FileReader("/usr/local/sxj/US_Accidents_June20.csv"));// 换成你的文件名

		String startTimeString="";
		String endTimeString="";
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
			List<String> list = new ArrayList<String>();
			while ((line = reader.readLine()) != null&&count<=1000000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				list.add(item[0]);
			}
			count=0;
			startTimeString=DateUtil.currentDateTime();
			long startTime = System.currentTimeMillis();
			for(String str:list) {
				String sql = "select * from us_accident where  id ='"+str+"'";
				rs = stmt.executeQuery(sql);
				if (++count % 10000 == 0)
					System.out.println("正在查询第" + count + "条数据！");
				if(count==500000){
					long endTime = System.currentTimeMillis();
					String sql1="insert into logger set message='查找500000条数据用时：" + (endTime - startTime)+" ms'";
		            stmt.execute(sql1);
		        }
			}
			

			endTimeString=DateUtil.currentDateTime();
			long endTime = System.currentTimeMillis();
			String sql1="insert into logger set message='查找1000000条数据用时：" + (endTime - startTime)+" ms,开始时间："+startTimeString+",结束时间："+endTimeString+"'";
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

	public String filterSqlString(String sqlStr) {
		if (StringUtils.isEmpty(sqlStr)) {
			return sqlStr;
		}
		sqlStr = sqlStr.replace("'", "");
		sqlStr = sqlStr.replace("\\", "");
		return sqlStr;
	}


}
