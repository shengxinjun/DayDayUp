package com.lab2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.util.DateUtil;
import com.util.JdbcUtil;

public class DeleteController {
	public  void deleteRelationData(){

		String startTimeString="";
		String endTimeString="";
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConn();
			stat = conn.createStatement();
			int count = 0;
			BufferedReader reader = new BufferedReader(
					new FileReader("/usr/local/sxj/US_Accidents_June20.csv"));// 换成你的文件名
			String line = null;
			List<Accident> accidents = new ArrayList<Accident>();
			while ((line = reader.readLine()) != null&&count<=1000000) {
				count++;
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				Accident acc = new Accident();
				acc.setId(item[0]);
				acc.setSource(item[1]);
				acc.setStart_time(item[4]);
				acc.setEnd_time(item[5]);
				acc.setStart_lat(item[6]);
				acc.setStart_lng(item[7]);
				acc.setStreet(item[13]);
				acc.setCity(item[15]);
				acc.setCountry(item[16]);
				acc.setState(item[17]);
				acc.setAirport_code(item[21]);
				acc.setWeather(item[22]);
				acc.setTemperature(item[23]);
				acc.setWind(item[24]);
				accidents.add(acc);
			}
			count=0;
			startTimeString=DateUtil.currentDateTime();
			long startTime = System.currentTimeMillis();
			for(Accident acc:accidents){
				count++;
				String sql = "delete from   accident  where id='" + acc.getId() +  "'";
				stat.execute(sql);
				if(count%10000==0){
					System.out.println("正在执行第："+count+"条数据");
				}
				if(count==500000){
					long endTime = System.currentTimeMillis();
					String msg = "删除50w条关系型数据总共用时： " + (endTime - startTime) + "ms";
					stat.execute("insert into  logger set msg='"+msg+"'");
				}
			}

			long endTime = System.currentTimeMillis();
			endTimeString=DateUtil.currentDateTime();
			String msg = "删除100w条关系型数据总共用时： " + (endTime - startTime) + "ms,开始时间："+startTimeString+",结束时间："+endTimeString;
			stat.execute("insert into  logger set msg='"+msg+"'");

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			JdbcUtil.close(rs, stat, conn);
		}
	
	}
	public void deleteDocumentData(){

		String startTimeString="";
		String endTimeString="";
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConn();
			stat = conn.createStatement();
			int count = 0;
			BufferedReader reader = new BufferedReader(
					new FileReader("/usr/local/sxj/szag.csv"));// 换成你的文件名
			String line = null;
			List<Record> records = new ArrayList<>();
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
				records.add(record);
				
			}
			count=0;
			startTimeString=DateUtil.currentDateTime();
			long startTime = System.currentTimeMillis();
			for(Record rec:records){
				count++;
				String sql = "delete from record  where id='" + rec.get_id() + "'";
				stat.execute(sql);
				if(count%10000==0){
					System.out.println("正在执行第："+count+"条数据");
				}
				if(count==500000){
					long endTime = System.currentTimeMillis();
					String msg = "删除50w条文档型数据总共用时： " + (endTime - startTime) + "ms";
					stat.execute("insert into  logger set msg='"+msg+"'");
				}
			}

			long endTime = System.currentTimeMillis();
			endTimeString=DateUtil.currentDateTime();
			String msg = "删除100w条文档型数据总共用时： " + (endTime - startTime) + "ms,开始时间："+startTimeString+",结束时间："+endTimeString;
			
			stat.execute("insert into  logger set msg='"+msg+"'");

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			JdbcUtil.close(rs, stat, conn);
		}
	
	
	}
	
	public void deleteGraphData(){

		String startTimeString="";
		String endTimeString="";
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConn();
			stat = conn.createStatement();
			int count = 0;
			//将数据读入list集合中
			BufferedReader reader = new BufferedReader(new FileReader("/usr/local/sxj/post_hasCreator_person_0_0.csv"));
			String line = null;
			List<PostPersonRelation> relations = new ArrayList<>();
			while ((line = reader.readLine()) != null&&count++<=45025) {
				line = filterSqlString(line);
				String item[] = line.split("\\|");
				PostPersonRelation relation = new PostPersonRelation();
				relation.setPostId(item[0]);
				relation.setPersonId(item[1]);
				relations.add(relation);
			}

			long totalTime=0;
			startTimeString=DateUtil.currentDateTime();
			long startTime = System.currentTimeMillis();
			for(PostPersonRelation relation:relations){
				String sql = "delete edge post_person_relation from (select from person where id='"+relation.getPersonId()+"') to (select from post where id='"+relation.getPostId()+"')  ";
				stat.execute(sql);
			}
			long endTime = System.currentTimeMillis();
			totalTime+=endTime-startTime;
			relations=null;
			
			reader = new BufferedReader(new FileReader("/usr/local/sxj/person_0_0.csv"));
			List<Person> persons = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				line = filterSqlString(line);
				Person person = new Person();
				String item[] = line.split("\\|");
				person.setId(item[0]);
				person.setFirstName(item[1]);
				person.setLastName(item[2]);
				person.setGender(item[3]);
				person.setBirthday(item[4]);
				person.setCreationDate(item[5]);
				person.setLocationIP(item[6]);
				person.setBrowserUsed(item[7]);
				person.setPlace(item[8]);
				persons.add(person);
			}
			
			startTime = System.currentTimeMillis();
			for(Person pe:persons){
				String sql = "delete vertex from  person  where id='" + pe.getId() +  "'";
				stat.execute(sql);
			}
			endTime = System.currentTimeMillis();
			totalTime+=endTime-startTime;
			persons=null;
			
			reader = new BufferedReader(new FileReader("/usr/local/sxj/post_0_0.csv"));
			List<Post> posts = new ArrayList<>();
			count=0;
			while ((line = reader.readLine()) != null&&count++<=45025) {
				line = filterSqlString(line);
				String item[] = line.split("\\|");
				Post post = new Post();
				post.setId(item[0]);
				posts.add(post);
			}
			
			startTime = System.currentTimeMillis();
			for(Post po:posts){
				String sql = "delete vertex from  post  where id='" + po.getId() +"'";
				stat.execute(sql);
			}
			endTime = System.currentTimeMillis();
			totalTime+=endTime-startTime;
			posts=null;

			endTimeString=DateUtil.currentDateTime();
			String msg = "删除10w条图数据总共用时： " + totalTime + "ms,开始时间："+startTimeString+",结束时间："+endTimeString;
			
			stat.execute("insert into  logger set msg='"+msg+"'");

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			JdbcUtil.close(rs, stat, conn);
		}
	
	
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
