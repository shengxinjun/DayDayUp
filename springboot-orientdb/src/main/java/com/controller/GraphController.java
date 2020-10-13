package com.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.util.JdbcUtil;
import com.util.JsonUtil;
import com.util.Result;

@RequestMapping("/graph")
@RestController
public class GraphController {

	@RequestMapping("/findWorker")
	String findWorker(){
		List<String> list=new ArrayList<>();
		Connection conn=null;
		Statement stat=null;
		ResultSet rs=null;
		try {
			conn=JdbcUtil.getConn();
			stat=conn.createStatement();
			rs=stat.executeQuery("match{class:company,where:(name='Lianjia')}.inE('Employee').outV(){as:person} return person.name");

			while(rs.next()){
				list.add(rs.getString("person.name"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			JdbcUtil.close(rs, stat, conn);
		}
		
		return JsonUtil.list2Json(list);
	}
	
	
	@RequestMapping("/addWorker")
	String addWorker(){
		Result result = new Result();
		Connection conn=null;
		Statement stat=null;
		ResultSet rs=null;
		try {
			conn=JdbcUtil.getConn();
			stat=conn.createStatement();
			stat.execute("create vertex preson set name='测试'");
			stat.execute("create edge employee from (select from preson where name='测试') to (select from company where name='Lianjia')  ");
			result.setCode(1);
			result.setMessage("add successfully");
		} catch (Exception e) {
			result.setCode(500);
			result.setMessage(e.toString());
			// TODO: handle exception
		}finally {
			JdbcUtil.close(rs, stat, conn);
		}
		
		return JsonUtil.object2Json(result);
	}
	
	@RequestMapping("/deleteWorker")
	String deleteWorker(){
		Result result = new Result();
		Connection conn=null;
		Statement stat=null;
		ResultSet rs=null;
		try {
			conn=JdbcUtil.getConn();
			stat=conn.createStatement();
			stat.execute("delete edge employee from (select @rid from preson where name='测试') to (select @rid from company where name='Lianjia')");
			stat.execute("delete vertex preson where name='测试' ");
			result.setCode(1);
			result.setMessage("delete successfully");
		} catch (Exception e) {
			result.setCode(500);
			result.setMessage(e.toString());
			// TODO: handle exception
		}finally {
			JdbcUtil.close(rs, stat, conn);
		}
		
		return JsonUtil.object2Json(result);
	}
	
}
