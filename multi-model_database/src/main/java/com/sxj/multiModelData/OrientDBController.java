package com.sxj.multiModelData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Statement;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.sxj.domain.Order;
import com.sxj.domain.OrderItem;
import com.sxj.util.DateUtil;
import com.sxj.util.OrientDBUtil;
public class OrientDBController {

	void startInterface(){
		/*insertOrder();
		queryOrder();
		updateOrder();
		deleteOrder();
		insertDocument();
		queryDocument();
		updateDocument();
		deleteDocument();*/
		insertGraph();
		queryGraph();
		updateGraph();
		deleteGraph();
	}
	
	public void insertOrder() throws IOException {
		BufferedReader reader = new BufferedReader(
				new FileReader("/usr/local/sxj/Unibench_Multi-model_data/Order/Order.json"));// 换成你的文件名
		
		Statement stmt = OrientDBUtil.getStatement();
		String startTimeString =DateUtil.currentDateTime();
		String endTimeString = "";
		String line = null;
		int count = 0;
		long startTime = System.currentTimeMillis();
		// 一次读取一行
		while ((line = reader.readLine()) != null && count <= 100000) {
			count++;
			// json转对象
			Order order = JSON.parseObject(line, Order.class);
			// 设置orderitem的主键和外键
			for (OrderItem item : order.getItems()) {
				item.setOrderId(order.getId());
				item.setId(UUID.randomUUID().toString());
			}
			String sql = "";
			// 插入order
			sql = "insert into orders (id,person_id,order_date,total_price) values ('" + order.getId() + "','"
					+ order.getPersonId() + "','" + order.getOrderDate() + "','" + order.getTotalPrice() + "')";
			System.out.println(sql);
			try {
				stmt.execute(sql);
				count++;
				if (count % 10000 == 0) {
					endTimeString = DateUtil.currentDateTime();
					long endTime = System.currentTimeMillis();
					sql = "insert into logger set message='插入" + count + "条关系型数据用时：" + (endTime - startTime) + " ms,开始时间："
							+ startTimeString + ",结束时间：" + endTimeString + "'";
					stmt.execute(sql);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 插入orderitem
			for (OrderItem item : order.getItems()) {
				sql = "insert into order_item (id,product_id,order_id) values ('" + item.getId() + "','"
						+ item.getProductId() + "','" + item.getOrderId() + "')";
				System.out.println(sql);
				try {
					stmt.execute(sql);
					count++;
					if (count % 10000 == 0) {
						endTimeString = DateUtil.currentDateTime();
						long endTime = System.currentTimeMillis();
						sql = "insert into logger set message='插入" + count + "条关系型数据用时：" + (endTime - startTime)
								+ " ms,开始时间：" + startTimeString + ",结束时间：" + endTimeString + "'";
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

		BufferedReader reader = new BufferedReader(
				new FileReader("/usr/local/sxj/Unibench_Multi-model_data/Order/Order.json"));// 换成你的文件名
		Statement stmt = OrientDBUtil.getStatement();
		String startTimeString = DateUtil.currentDateTime();
		String endTimeString = "";
		String line = null;
		int count = 0;
		long startTime = System.currentTimeMillis();
		// 一次读取一行
		while ((line = reader.readLine()) != null && count <= 100000) {
			count++;
			// json转对象
			Order order = JSON.parseObject(line, Order.class);
			
			String sql = "";
			sql = "select * from order_item where order_id='"+order.getId()+"'";
			System.out.println(sql);
			try {
				stmt.execute(sql);
				count++;
				if (count % 10000 == 0) {
					endTimeString = DateUtil.currentDateTime();
					long endTime = System.currentTimeMillis();
					sql = "insert into logger set message='查询" + count + "条关系型数据用时：" + (endTime - startTime) + " ms,开始时间："
							+ startTimeString + ",结束时间：" + endTimeString + "'";
					stmt.execute(sql);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
	public void updateOrder() throws IOException {
		BufferedReader reader = new BufferedReader(
				new FileReader("/usr/local/sxj/Unibench_Multi-model_data/Order/Order.json"));// 换成你的文件名
		Statement stmt = OrientDBUtil.getStatement();
		String startTimeString =DateUtil.currentDateTime();
		String endTimeString = "";
		String line = null;
		int count = 0;
		long startTime = System.currentTimeMillis();
		// 一次读取一行
		while ((line = reader.readLine()) != null && count <= 100000) {
			count++;
			// json转对象
			Order order = JSON.parseObject(line, Order.class);
			
			String sql = "";
			sql = "update order_item set product_id ='u1101' where order_id='"+order.getId()+"'";
			System.out.println(sql);
			try {
				stmt.executeUpdate(sql);
				count++;
				if (count % 10000 == 0) {
					endTimeString = DateUtil.currentDateTime();
					long endTime = System.currentTimeMillis();
					sql = "insert into logger set message='更新" + count + "条关系型数据用时：" + (endTime - startTime) + " ms,开始时间："
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
		BufferedReader reader = new BufferedReader(
				new FileReader("/usr/local/sxj/Unibench_Multi-model_data/Order/Order.json"));// 换成你的文件名
		Statement stmt = OrientDBUtil.getStatement();
		String startTimeString = DateUtil.currentDateTime();
		String endTimeString = "";
		String line = null;
		int count = 0;
		long startTime = System.currentTimeMillis();
		// 一次读取一行
		while ((line = reader.readLine()) != null && count <= 100000) {
			count++;
			// json转对象
			Order order = JSON.parseObject(line, Order.class);
			
			String sql = "";
			sql = "delete from order_item where order_id='"+order.getId()+"'";
			System.out.println(sql);
			try {
				stmt.execute(sql);
				count++;
				if (count % 10000 == 0) {
					endTimeString = DateUtil.currentDateTime();
					long endTime = System.currentTimeMillis();
					sql = "insert into logger set message='删除" + count + "条关系型数据用时：" + (endTime - startTime) + " ms,开始时间："
							+ startTimeString + ",结束时间：" + endTimeString + "'";
					stmt.execute(sql);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sql = "delete from orders where id='"+order.getId()+"'";
			System.out.println(sql);
			try {
				stmt.execute(sql);
				count++;
				if (count % 10000 == 0) {
					endTimeString = DateUtil.currentDateTime();
					long endTime = System.currentTimeMillis();
					sql = "insert into logger set message='删除" + count + "条数据用时：" + (endTime - startTime) + " ms,开始时间："
							+ startTimeString + ",结束时间：" + endTimeString + "'";
					stmt.execute(sql);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void insertDocument(){
		long startTime = System.currentTimeMillis();
		String startTimeString=DateUtil.currentDateTime();
		String endTimeString="";
		try {

			Statement stmt = OrientDBUtil.getStatement();
			int count = 0;

			BufferedReader reader = new BufferedReader(
					new FileReader("/usr/local/sxj/Unibench_Multi-model_data/Product/Product.csv"));// 换成你的文件名
			String line = null;
			while ((line = reader.readLine()) != null && count <= 100000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				String sql = "insert into product (id,title,price,img_url) values ('" + item[0] + "','"
						+ item[1] + "','" + item[2] + "','" + item[3] + "')";
				try {
					stmt.execute(sql);
					count++;
					if (count % 10000 == 0) {
						endTimeString = DateUtil.currentDateTime();
						long endTime = System.currentTimeMillis();
						sql = "insert into logger set message='插入" + count + "条文档型数据用时：" + (endTime - startTime) + " ms,开始时间："
								+ startTimeString + ",结束时间：" + endTimeString + "'";
						stmt.execute(sql);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			count = 0;
			reader = new BufferedReader(
					new FileReader("/usr/local/sxj/Unibench_Multi-model_data/Product/BrandByProduct.csv"));// 换成你的文件名
			while ((line = reader.readLine()) != null && count <= 100000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				String sql = "insert into brand_by_product (vendor_id,product_id) values ('" + item[0] + "','"
						+ item[1]+ "')";
				try {
					stmt.execute(sql);
					count++;
					if (count % 10000 == 0) {
						endTimeString = DateUtil.currentDateTime();
						long endTime = System.currentTimeMillis();
						sql = "insert into logger set message='插入" + count + "条文档型数据用时：" + (endTime - startTime) + " ms,开始时间："
								+ startTimeString + ",结束时间：" + endTimeString + "'";
						stmt.execute(sql);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			count = 0;

			reader = new BufferedReader(
					new FileReader("/usr/local/sxj/Unibench_Multi-model_data/Vendor/Vendor.csv"));// 换成你的文件名
			while ((line = reader.readLine()) != null && count <= 100000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				String sql = "insert into vendor (id,country,industry) values ('" + item[0] + "','"+ item[1] + "','"+ item[2]+ "')";
				try {
					stmt.execute(sql);
					count++;
					if (count % 10000 == 0) {
						endTimeString = DateUtil.currentDateTime();
						long endTime = System.currentTimeMillis();
						sql = "insert into logger set message='插入" + count + "条文档型数据用时：" + (endTime - startTime) + " ms,开始时间："
								+ startTimeString + ",结束时间：" + endTimeString + "'";
						stmt.execute(sql);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} 

	
	}

	public void queryDocument(){

		long startTime = System.currentTimeMillis();
		String startTimeString=DateUtil.currentDateTime();
		String endTimeString="";
		try {

			Statement stmt = OrientDBUtil.getStatement();
			int count = 0;

			BufferedReader reader = new BufferedReader(
					new FileReader("/usr/local/sxj/Unibench_Multi-model_data/Product/Product.csv"));// 换成你的文件名
			String line = null;
			while ((line = reader.readLine()) != null && count <= 100000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				String sql = "select * product where id='"+item[0]+"'";
				try {
					stmt.execute(sql);
					count++;
					if (count % 10000 == 0) {
						endTimeString = DateUtil.currentDateTime();
						long endTime = System.currentTimeMillis();
						sql = "insert into logger set message='查询" + count + "条文档型数据用时：" + (endTime - startTime) + " ms,开始时间："
								+ startTimeString + ",结束时间：" + endTimeString + "'";
						stmt.execute(sql);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} 

	
	
	}
	
	public void updateDocument(){


		long startTime = System.currentTimeMillis();
		String startTimeString=DateUtil.currentDateTime();
		String endTimeString="";
		try {

			Statement stmt = OrientDBUtil.getStatement();
			int count = 0;

			BufferedReader reader = new BufferedReader(
					new FileReader("/usr/local/sxj/Unibench_Multi-model_data/Product/Product.csv"));// 换成你的文件名
			String line = null;
			while ((line = reader.readLine()) != null && count <= 100000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				String sql = "update product set title='test' where id='"+item[0]+"'";
				try {
					stmt.execute(sql);
					count++;
					if (count % 10000 == 0) {
						endTimeString = DateUtil.currentDateTime();
						long endTime = System.currentTimeMillis();
						sql = "insert into logger set message='更新" + count + "条文档型数据用时：" + (endTime - startTime) + " ms,开始时间："
								+ startTimeString + ",结束时间：" + endTimeString + "'";
						stmt.execute(sql);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} 
	}
	
	public void deleteDocument(){
		long startTime = System.currentTimeMillis();
		String startTimeString=DateUtil.currentDateTime();
		String endTimeString="";
		try {

			Statement stmt = OrientDBUtil.getStatement();
			int count = 0;

			BufferedReader reader = new BufferedReader(
					new FileReader("/usr/local/sxj/Unibench_Multi-model_data/Product/Product.csv"));// 换成你的文件名
			String line = null;
			while ((line = reader.readLine()) != null && count <= 100000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				String sql = "delete from product where id='"+item[0]+"'";
				try {
					stmt.execute(sql);
					count++;
					if (count % 10000 == 0) {
						endTimeString = DateUtil.currentDateTime();
						long endTime = System.currentTimeMillis();
						sql = "insert into logger set message='删除" + count + "条文档型数据用时：" + (endTime - startTime) + " ms,开始时间："
								+ startTimeString + ",结束时间：" + endTimeString + "'";
						stmt.execute(sql);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} 
	}
	
	public void insertGraph(){

		long startTime = System.currentTimeMillis();
		String startTimeString=DateUtil.currentDateTime();
		String endTimeString="";
		try {

			Statement stmt = OrientDBUtil.getStatement();
			int count = 0;

			BufferedReader reader = new BufferedReader(new FileReader("/usr/local/sxj/Unibench_Multi-model_data/Customer/person_0_0.csv"));
			String line = null;
			while ((line = reader.readLine()) != null && count <= 100000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split("\\|");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				String sql = "create vertex person  set id='" + item[0] + "',first_name='" + item[1]
				+ "',last_name='" + item[2] + "',gender='" + item[3] + "',birthday='"
				+ item[4] + "',create_date='" + item[5] + "',locationIp='"
				+ item[6] + "',browserUsed='" + item[7] + "',place='" + item[8]
				+ "'";
				try {
					stmt.execute(sql);
					if (count % 10000 == 0) {
						endTimeString = DateUtil.currentDateTime();
						long endTime = System.currentTimeMillis();
						sql = "insert into logger set message='插入" + count + "条图数据用时：" + (endTime - startTime) + " ms,开始时间："
								+ startTimeString + ",结束时间：" + endTimeString + "'";
						stmt.execute(sql);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			reader = new BufferedReader(new FileReader("/usr/local/sxj/Unibench_Multi-model_data/SocialNetwork/person_knows_person_0_0.csv"));
			while ((line = reader.readLine()) != null && count <= 100000) {
				line = filterSqlString(line);
				String item[] = line.split("\\|");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				String sql = "create edge person_konw_person from (select from person where id='"
						+ item[0] + "') to (select from person where id='" + item[1] + "')  ";
				try {
					stmt.execute(sql);
					count++;
					if (count % 10000 == 0) {
						endTimeString = DateUtil.currentDateTime();
						long endTime = System.currentTimeMillis();
						sql = "insert into logger set message='插入" + count + "条图数据用时：" + (endTime - startTime) + " ms,开始时间："
								+ startTimeString + ",结束时间：" + endTimeString + "'";
						stmt.execute(sql);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} 

	
	
	}

	public void queryGraph(){
		long startTime = System.currentTimeMillis();
		String startTimeString=DateUtil.currentDateTime();
		String endTimeString="";
		try {

			Statement stmt = OrientDBUtil.getStatement();
			int count = 0;

			BufferedReader reader = new BufferedReader(new FileReader("/usr/local/sxj/Unibench_Multi-model_data/SocialNetwork/person_knows_person_0_0.csv"));
			String line = null;
			while ((line = reader.readLine()) != null && count <= 100000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split("\\|");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				String sql = "MATCH {as:v1,class:person,where:(id = '" + item[0]
				+ "')}-->{as:v2} RETURN  v2";
				try {
					stmt.execute(sql);
					if (count % 10000 == 0) {
						endTimeString = DateUtil.currentDateTime();
						long endTime = System.currentTimeMillis();
						sql = "insert into logger set message='查询" + count + "条图数据用时：" + (endTime - startTime) + " ms,开始时间："
								+ startTimeString + ",结束时间：" + endTimeString + "'";
						stmt.execute(sql);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} 
	}

	public void updateGraph(){
		long startTime = System.currentTimeMillis();
		String startTimeString=DateUtil.currentDateTime();
		String endTimeString="";
		try {

			Statement stmt = OrientDBUtil.getStatement();
			int count = 0;
			BufferedReader reader = new BufferedReader(new FileReader("/usr/local/sxj/Unibench_Multi-model_data/SocialNetwork/person_knows_person_0_0.csv"));
			
			String line = null;
			while ((line = reader.readLine()) != null && count <= 100000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split("\\|");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				String sql = "update person set place='test' where id = (select in().id from person where id='"+item[0]+"')";
				try {
					stmt.execute(sql);
					if (count % 10000 == 0) {
						endTimeString = DateUtil.currentDateTime();
						long endTime = System.currentTimeMillis();
						sql = "insert into logger set message='更新" + count + "条图数据用时：" + (endTime - startTime) + " ms,开始时间："
								+ startTimeString + ",结束时间：" + endTimeString + "'";
						stmt.execute(sql);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} 
	}

	public void deleteGraph(){
		long startTime = System.currentTimeMillis();
		String startTimeString=DateUtil.currentDateTime();
		String endTimeString="";
		try {

			Statement stmt = OrientDBUtil.getStatement();
			int count = 0;

			BufferedReader reader = new BufferedReader(new FileReader("/usr/local/sxj/Unibench_Multi-model_data/SocialNetwork/person_knows_person_0_0.csv"));
			String line = null;
			while ((line = reader.readLine()) != null && count <= 100000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split("\\|");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				String sql = "delete edge person_know_person from (select from person where id='"+item[0]+"') to (select from person where id='"+item[1]+"')  ";
				try {
					stmt.execute(sql);
					if (count % 10000 == 0) {
						endTimeString = DateUtil.currentDateTime();
						long endTime = System.currentTimeMillis();
						sql = "insert into logger set message='删除" + count + "条图数据用时：" + (endTime - startTime) + " ms,开始时间："
								+ startTimeString + ",结束时间：" + endTimeString + "'";
						stmt.execute(sql);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} 
	}
	
	public static String filterSqlString(String sqlStr) {
		if (StringUtils.isEmpty(sqlStr)) {
			return sqlStr;
		}
		sqlStr = sqlStr.replace("'", "");
		sqlStr = sqlStr.replace("\\", "");
		return sqlStr;
	}
}
