package com.sxj.relationalData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Statement;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sxj.domain.Order;
import com.sxj.domain.OrderItem;
import com.sxj.service.OrderService;
import com.sxj.util.DateUtil;
import com.sxj.util.MysqlDBUtil;
public class MysqlController {

	private static Logger log = Logger.getLogger(MysqlController.class);
	public void startInterface(){
		try {
			insertOrder();
			queryOrder();
			updateOrder();
			deleteOrder();
		} catch (Exception e) {
			log.error("--------------------------异常::"+e+"-----------------------------------");
			log.error("--------------------------MySQL执行CRUD发生异常-----------------------------------");
		}
	}
	
	public void insertOrder() throws IOException {
		File file = new File("/home/shengxinjun/Unibench_Multi-model_data/Order/Order.json");
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "GB2312");
		BufferedReader reader=new BufferedReader(isr);
		Statement stmt = MysqlDBUtil.getStatement();
		String startTimeString  = DateUtil.currentDateTime();
		String endTimeString = "";
		String line = null;
		int count = 0;
		// 一次读取一行
		while ((line = reader.readLine()) != null && count <= 510000) {
			// json转对象
			Order order = JSON.parseObject(line, Order.class);
			//order.setId(count);
			// 设置orderitem的外键
			for (OrderItem item : order.getItems()) {
				item.setOrderId(order.getId());
			}
			String sql = "";
			// 插入order
			sql = "insert into orders (id,person_id,order_date,total_price) values ('" + order.getId() + "','"
					+ order.getPersonId() + "','" + order.getOrderDate() + "','" + order.getTotalPrice() + "')";
			try {
				stmt.execute(sql);
				count++;
				if (count % 100000 == 0) {
					endTimeString = DateUtil.currentDateTime();
					sql = "insert into logger set message='插入" + count + "条数据用时：" + DateUtil.subtractMinute(startTimeString, endTimeString) + " 分钟,开始时间："
							+ startTimeString + ",结束时间：" + endTimeString + "'";
					stmt.execute(sql);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 插入orderitem
			for (OrderItem item : order.getItems()) {
				sql = "insert into order_item (product_id,order_id) values ('" + item.getProductId() + "','" + item.getOrderId() + "')";
				try {
					stmt.execute(sql);
					count++;
					if (count % 100000 == 0) {
						endTimeString = DateUtil.currentDateTime();
						sql = "insert into logger set message='插入" + count + "条数据用时：" + DateUtil.subtractMinute(startTimeString, endTimeString) + " 分钟,开始时间："
								+ startTimeString + ",结束时间：" + endTimeString + "'";
						stmt.execute(sql);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void queryOrder() throws IOException {
		Statement stmt = MysqlDBUtil.getStatement();
		String startTimeString = DateUtil.currentDateTime();
		String endTimeString = "";
		String line = null;
		int count = 0;
		// 一次读取一行
		while ( count <= 510000) {
			count++;
			String sql = "";
			sql = "select * from orders where id = (select order_id from order_item where id='"+count+"')";
			try {
				stmt.execute(sql);
				count++;
				if (count % 100000 == 0) {
					endTimeString = DateUtil.currentDateTime();
					sql = "insert into logger set message='查询" + count + "条数据用时：" + DateUtil.subtractMinute(startTimeString, endTimeString) + " 分钟,开始时间："
							+ startTimeString + ",结束时间：" + endTimeString + "'";
					stmt.execute(sql);
				}
			} catch (Exception e) {
				log.error("--------------------------异常::"+e+"-----------------------------------");
				log.error("--------------------------异常SQL:"+sql+"-----------------------------------");
			}
			
		}

	}
	public void updateOrder() throws IOException {
		Statement stmt = MysqlDBUtil.getStatement();
		String startTimeString = DateUtil.currentDateTime();
		String endTimeString = "";
		String line = null;
		int count = 0;
		// 一次读取一行
		while (count <= 510000) {
			count++;
			
			String sql = "";
			sql = "update order set total_price='1000' where id=(select from order_item where id="+count+")";
			try {
				stmt.executeUpdate(sql);
				count++;
				if (count % 100000 == 0) {
					endTimeString = DateUtil.currentDateTime();
					sql = "insert into logger set message='插入" + count + "条数据用时：" + DateUtil.subtractMinute(startTimeString, endTimeString) + " 分钟,开始时间："
							+ startTimeString + ",结束时间：" + endTimeString + "'";
					stmt.execute(sql);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
	public void deleteOrder() throws IOException {
		Statement stmt = MysqlDBUtil.getStatement();
		String startTimeString = DateUtil.currentDateTime();
		String endTimeString = "";
		int count = 0;
		// 一次读取一行
		while (count <= 510000) {
			count++;
			String sql = "";
			sql = "delete from order_item where order_id="+count;
			try {
				stmt.execute(sql);
				count++;
				if (count % 100000 == 0) {
					endTimeString = DateUtil.currentDateTime();
					sql = "insert into logger set message='插入" + count + "条数据用时：" + DateUtil.subtractMinute(startTimeString, endTimeString) + " 分钟,开始时间："
							+ startTimeString + ",结束时间：" + endTimeString + "'";
					stmt.execute(sql);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
