package com.lab2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.Document;
import org.springframework.util.StringUtils;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.util.DateUtil;

public class ImportDataController {
	

	public void importDocument() {

		// 连接到 mongodb 服务
		MongoClient mongoClient = new MongoClient("121.196.54.227", 27017);
		// 连接到数据库
		MongoDatabase mongoDatabase = mongoClient.getDatabase("sheng");
		System.out.println("Connect to database successfully");

		MongoCollection<Document> collection = mongoDatabase.getCollection("A_share_data");
		
		
		try {
			int count = 0;
			String startTimeString="";
			String endTimeString="";
			BufferedReader reader = new BufferedReader(new FileReader("/usr/local/sxj/szag.csv"));// 换成你的文件名

			//BufferedReader reader = new BufferedReader(
			//		new FileReader("D:\\初次读研请多指教\\数据集\\上证A股\\szag.csv"));// 换成你的文件名
			String line = null;
			List<Record> list = new ArrayList<>();
			while ((line = reader.readLine()) != null&&count<=1000000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				Record record = new Record();
				record.set_id(item[24]);
				record.setCode(item[0]);
				record.setName(item[1]);
				record.setDate(item[2]);
				record.setLast_price(item[3]);
				record.setStart_price(item[4]);
				record.setMax_price(item[5]);
				record.setMin_price(item[6]);
				record.setEnd_price(item[7]);
				record.setTransaction_amount(item[8]);
				record.setTransaction_money(item[9]);
				record.setUp_and_down(item[10]);
				record.setUp_and_down_range(item[11]);
				record.setAverage_price(item[12]);
				record.setTurnover_rate(item[13]);
				record.setA_market_value(item[14]);
				record.setB_market_value(item[15]);
				record.setAll_market_value(item[16]);
				record.setA_market_amount(item[17]);
				record.setB_market_amount(item[18]);
				record.setAll_market_amount(item[19]);
				record.setSyl(item[20]);
				record.setSjl(item[21]);
				record.setSxl(item[22]);
				record.setSxl2(item[23]);
				list.add(record);
				
			}
			count=0;
			startTimeString=DateUtil.currentDateTime();
			long startTime = System.currentTimeMillis();
			for(Record r :list){
				Document document = new Document("_id", r.get_id()).append("code", r.getCode()).append("name", r.getName())
						.append("date", r.getDate()).append("last_price", r.getLast_price()).append("start_price", r.getStart_price())
						.append("max_price", r.getMax_price()).append("min_price", r.getMin_price())
						.append("end_price", r.getEnd_price()).append("transaction_amount", r.getTransaction_amount())
						.append("transaction_money", r.getTransaction_money()).append("up_and_down", r.getUp_and_down())
						.append("up_and_down_range", r.getUp_and_down_range()).append("average_price", r.getAverage_price())
						.append("turnover_rate", r.getTurnover_rate()).append("a_market_value", r.getA_market_value())
						.append("b_market_value", r.getB_market_value()).append("all_market_value", r.getAll_market_value())
						.append("a_market_amount", r.getA_market_amount()).append("b_market_amount", r.getB_market_amount())
						.append("all_market_amount", r.getAll_market_amount()).append("syl", r.getSyl()).append("sjl", r.getSjl())
						.append("sxl", r.getSxl()).append("sxl2", r.getSxl2());
				collection.insertOne(document);
				if (++count % 10000 == 0)
					System.out.println("正在插入第" + count + "条数据！");
				if(count==500000){
					long endTime = System.currentTimeMillis();
					String msg="插入500000条数据用时：" + (endTime - startTime)+" ms";
					collection = mongoDatabase.getCollection("logger");
					Document loggerDoc = new Document("message", msg).append("create_time", new Date());
					collection.insertOne(loggerDoc);
					collection = mongoDatabase.getCollection("A_share_data");
				}
			}
			endTimeString=DateUtil.currentDateTime();
			long endTime = System.currentTimeMillis();
			String msg="插入1000000条数据用时：" + (endTime - startTime)+" ms,开始时间："+startTimeString+",结束时间："+endTimeString+"'";
			
			collection = mongoDatabase.getCollection("logger");
			Document loggerDoc = new Document("message", msg).append("create_time", new Date());
			collection.insertOne(loggerDoc);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
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
