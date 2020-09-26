package com.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.util.JdbcUtil;

@RequestMapping("customer")
@RestController
public class CustomerController {

	@RequestMapping("query")
	String query(){
		List<Map<String, String>> lists = new ArrayList<>();
		Connection conn=null;
		Statement stat=null;
		ResultSet rs=null;
		try {
			conn=JdbcUtil.getConn();
			stat=conn.createStatement();
			rs=stat.executeQuery("select from customer");
			while(rs.next()){
				Map<String, String> map = new HashMap<>();
				map.put("id", rs.getString("id"));
				map.put("name", rs.getString("name"));
				map.put("age", rs.getString("age"));
				lists.add(map);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			JdbcUtil.close(rs, stat, conn);
		}
		return lists.toString();
	}
	@RequestMapping("insert")
	Integer insert(Integer id,String name,Integer age){
		Integer result = null;
		Connection conn=null;
		Statement stat=null;
		ResultSet rs=null;
		try {
			conn=JdbcUtil.getConn();
			stat=conn.createStatement();
			result=stat.executeUpdate("insert into customer set id="+id+",name='"+name+"',age="+age);
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			JdbcUtil.close(rs, stat, conn);
		}
		return result;
	}
	
	@RequestMapping("delete")
	Integer delete(Integer id){
		Integer result = null;
		Connection conn=null;
		Statement stat=null;
		ResultSet rs=null;
		try {
			conn=JdbcUtil.getConn();
			stat=conn.createStatement();
			result=stat.executeUpdate("delete from customer where id="+id);
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			JdbcUtil.close(rs, stat, conn);
		}
		return result;
	}
	
	@RequestMapping("update")
	Integer update(Integer id,String name,Integer age){
		Integer result = null;
		Connection conn=null;
		Statement stat=null;
		ResultSet rs=null;
		try {
			conn=JdbcUtil.getConn();
			stat=conn.createStatement();
			result=stat.executeUpdate("update customer set name='"+name+"',age="+age+" where id="+id);
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			JdbcUtil.close(rs, stat, conn);
		}
		return result;
	}
}
