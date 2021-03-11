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
import org.neo4j.driver.v1.Values;
import org.springframework.util.StringUtils;

import com.sxj.util.DateUtil;

public class ImportController {
	


	/**
	 * 1.连接数据库
	 * 2.将数据读入list集合中
	 * 3.将list集合中的数据插入数据库中
	 * 4.关闭数据库连接
	 * 
	 */
	public void doImport() {
		//连接数据库内网  ip172.16.1.89
		Driver driver = GraphDatabase.driver("bolt://121.196.54.227:7687", AuthTokens.basic("neo4j", "root"));
		Session session = driver.session();
				
		try {
			//将数据读入list集合中
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
			while ((line = reader.readLine()) != null&&count++<=45025) {
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
			while ((line = reader.readLine()) != null&&count++<=45025) {
				line = filterSqlString(line);
				String item[] = line.split("\\|");
				PostPersonRelation relation = new PostPersonRelation();
				relation.setPostId(item[0]);
				relation.setPersonId(item[1]);
				relations.add(relation);
			}
			//将list集合中的数据插入数据库中

			startTimeString=DateUtil.currentDateTime();
			long startTime = System.currentTimeMillis();
			for(Person person:persons){
				session.run(
						"CREATE (a:Person {id: {id},firstName: {firstName},lastName: {lastName},gender: {gender},birthday: {birthday},creationDate: {creationDate}, locationIP: {locationIP}, browserUsed: {browserUsed}, place: {place}})",
						Values.parameters("id", person.getId(), "firstName", person.getFirstName(),"lastName", person.getLastName(),"gender", person.getGender(),"birthday", person.getBirthday(),"creationDate", person.getCreationDate(),"locationIP", person.getLocationIP(),"browserUsed", person.getBrowserUsed(),"place", person.getPlace()));
			}
			for(Post post:posts){
				session.run(
						"CREATE (a:Post {id: {id},imageFile: {imageFile},creationDate: {creationDate},locationIP: {locationIP},browserUsed: {browserUsed},language: {language}, content: {content}, length: {length}})",
						Values.parameters("id", post.getId(), "imageFile", post.getImageFile(),"creationDate", post.getCreationDate(),"locationIP", post.getLocationIP(),"browserUsed", post.getBrowserUsed(),"language", post.getLanguage(),"content", post.getContent(),"length", post.getLength()));
				
			}
			for(PostPersonRelation relation:relations){
				
				session.run(
						"match(pe:Person{id:{personId}}),(po:Post{id:{postId}})  create (pe)-[:post_person_relation{relationName:'create'}]->(po)",
						Values.parameters("personId", relation.getPersonId(), "postId", relation.getPostId()));
				
			}
			endTimeString=DateUtil.currentDateTime();
			long endTime = System.currentTimeMillis();
			String msg="插入10w条数据用时：" + (endTime - startTime)+" ms,开始时间："+startTimeString+",结束时间："+endTimeString;
			
			session.run(
					"CREATE (a:logger {message: {msg}})",
					Values.parameters("msg", msg));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//关闭数据库连接
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
