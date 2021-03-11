package com.sxj.lab2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Values;
import org.springframework.util.StringUtils;

import com.sxj.util.DateUtil;

public class UpdateController {

	public void doUpdate() {

		/**
		 * 1.连接数据库 2.将数据读入list集合中 3.执行更新操作 4.关闭数据库连接
		 * 
		 */
		// 连接数据库
		Driver driver = GraphDatabase.driver("bolt://121.196.54.227:7687", AuthTokens.basic("neo4j", "root"));
		Session session = driver.session();

		try {
			// 将数据读入list集合中
			String startTimeString="";
			String endTimeString="";
			int count = 0;
			BufferedReader reader = new BufferedReader(new FileReader("/usr/local/sxj/person_0_0.csv"));
			String line = null;
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
			reader = new BufferedReader(new FileReader("/usr/local/sxj/post_0_0.csv"));
			List<Post> posts = new ArrayList<>();
			while ((line = reader.readLine()) != null && count++ <= 45025) {
				line = filterSqlString(line);
				String item[] = line.split("\\|");
				Post post = new Post();
				post.setId(item[0]);
				post.setImageFile(item[1]);
				post.setCreationDate(item[2]);
				post.setLocationIP(item[3]);
				post.setBrowserUsed(item[4]);
				post.setLanguage(item[5]);
				post.setContent(item[6]);
				post.setLength(item[7]);
				posts.add(post);
			}
			count = 0;
			reader = new BufferedReader(new FileReader("/usr/local/sxj/post_hasCreator_person_0_0.csv"));
			List<PostPersonRelation> relations = new ArrayList<>();
			while ((line = reader.readLine()) != null && count++ <= 45025) {
				line = filterSqlString(line);
				String item[] = line.split("\\|");
				PostPersonRelation relation = new PostPersonRelation();
				relation.setPostId(item[0]);
				relation.setPersonId(item[1]);
				relations.add(relation);
			}
			// 将list集合中的数据插入数据库中

			startTimeString=DateUtil.currentDateTime();
			long startTime = System.currentTimeMillis();

			for (PostPersonRelation relation : relations) {

				StatementResult result = session.run(
						"match(pe:Person)-[r]->(po:Post) where po.id={postId} set pe.place='test'",
						Values.parameters("personId", relation.getPersonId(), "postId", relation.getPostId()));

			}
			for (PostPersonRelation relation : relations) {

				StatementResult result = session.run(
						"match(pe:Person)-[r]->(po:Post) where pe.id={personId} and po.id={postId}  set po.length='test'",
						Values.parameters("personId", relation.getPersonId(), "postId", relation.getPostId()));

			}
			endTimeString=DateUtil.currentDateTime();
			long endTime = System.currentTimeMillis();
			String msg = "更新10w条数据用时：" + (endTime - startTime) + " ms,开始时间："+startTimeString+",结束时间："+endTimeString;
			
			session.run("CREATE (a:logger {message: {msg}})", Values.parameters("msg", msg));

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 关闭数据库连接
		session.close();
		driver.close();

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
