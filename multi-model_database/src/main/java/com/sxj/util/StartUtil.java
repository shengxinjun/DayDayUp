package com.sxj.util;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sxj.graphData.Neo4jController;
import com.sxj.relationalData.MysqlController;
import com.sxj.service.OrderService;
public class StartUtil {
	

	private static Logger log = Logger.getLogger(StartUtil.class);
	public void startMethod(){

		try {
			MysqlController mc = new MysqlController();
			mc.startInterface();
		} catch (Exception e) {
			log.error("--------------------------异常::"+e+"-----------------------------------");
			log.error("--------------------------MySQL执行CRUD发生异常-----------------------------------");
		}
		
		try {
			/*MongoDBController mc = new MongoDBController();
			mc.startInterface();*/
		} catch (Exception e) {
			log.error("--------------------------异常::"+e+"-----------------------------------");
			log.error("--------------------------MongoDB执行CRUD发生异常-----------------------------------");
		}
		try {
			Neo4jController nc = new Neo4jController();
			nc.startInterface();
		} catch (Exception e) {
			log.error("--------------------------异常::"+e+"-----------------------------------");
			log.error("--------------------------Neo4j执行CRUD发生异常-----------------------------------");
		}
	}
}
