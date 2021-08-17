package com.sxj.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.Values;
import org.neo4j.driver.v1.summary.ResultSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.arangodb.ArangoCursor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxj.domain.Order;
import com.sxj.domain.Person;
import com.sxj.domain.Product;
import com.sxj.util.ArangoDBUtil;
import com.sxj.util.Neo4jDBUtil;



@Service
public class PersonService {
	@Autowired
	private ProductService productService;
	
	public void Neo4jInsertPerson(Person person) {
		Neo4jDBUtil.getSession()
				.run("CREATE (a:person {id: {id},firstName: {firstName},lastName: {lastName},gender: {gender},birthday: {birthday},creationDate: {creationDate}})",
						Values.parameters("id", person.getId(), "firstName", person.getFirstName(), "lastName",
								person.getLastName(), "gender", person.getGender(), "creationDate",
								person.getCreationDate(),"birthday",
								person.getBirthday()));
	}

	public void Neo4jInsertRelation(String personId1, String personId2) {
		Neo4jDBUtil.getSession().run(
				"match(p:person{id:{personId1}}),(pp:person{id:{personId2}})  create (p)-[:relation{relationName:'know'}]->(pp)",
				Values.parameters("personId1", personId1, "personId2", personId2));
	}

	/**
	 * 根据personId查询他的朋友
	 * 
	 * @param session
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public List<Person> Neo4jQueryPersonFriends(String id) throws IOException {
		StatementResult result = Neo4jDBUtil.getSession().run("match(q:person)-[r]->(p:person) where q.id={personId} return return p.birthday,p.firstName,p.lastName,p.gender,p.id,p.creationDate",
				Values.parameters("personId", id));
		List<Person> persons = new ArrayList<>();
		while(result.hasNext()){
			Map<String, Object> map = result.next().asMap();
			Person person = mapToPerson(map);
			persons.add(person);
		}
		return persons;
	}

	public void Neo4jUpdatePersonFriends(String id) throws IOException {
		Neo4jDBUtil.getSession().run("match(p:person)-[r]->(pp:person) where p.id={personId} set pp.lastName='test'",
				Values.parameters("personId", id));
	}

	public void Neo4jDeleteKnows(String personId1, String personId2) throws IOException {
		Neo4jDBUtil.getSession().run("match(p:person{id:{personId1}})-[r]->(pp:person{id:{personId2}}) delete r",
				Values.parameters("personId1", personId1, "personId2", personId2));
	}

	public void Neo4jDeletePerson(String id) {
		Neo4jDBUtil.getSession().run("match(p:person{id:{personId}}) delete p", Values.parameters("personId", id));
	}

	public void ArangoDBInsertPerson(Person person) {
		String aql = "insert {id:@id,first_name:@first_name,last_name:@last_name,gender:@gender,birthday:@birthday,create_date:@create_date} in person";
		Map<String, Object> params = new HashMap<>();
		params.put("id", person.getId());
		params.put("first_name", person.getFirstName());
		params.put("last_name", person.getLastName());
		params.put("gender", person.getGender());
		params.put("birthday", person.getBirthday());
		params.put("create_date", person.getCreationDate());
		ArangoDBUtil.getArangoDatabase().query(aql, params, Person.class);
	}

	public void ArangoDBInsertRelations(String id1, String id2) {
		Map<String, Object> params = new HashMap<>();
		params.put("id1", id1);
		params.put("id2", id2);
		String aql = "let from=FIRST(for c in person filter c.id==@id1 return c._id) let to=FIRST(for c in person filter c.id==@id2 return c._id) FILTER from != null AND to != null INSERT { _from: from, _to: to } INTO relation RETURN NEW";
		ArangoDBUtil.getArangoDatabase().query(aql, params, Person.class);
	}

	public List<String> ArangoDBQueryFriendsById(String id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		String aql = "for c in person filter c.id==@id for v in 1..1 outbound c relation return v.id";
		ArangoCursor<String> persons = ArangoDBUtil.getArangoDatabase().query(aql, params, String.class);
		List<String> list = persons.asListRemaining();
		return list;
	}

	public void ArangoDBUpdateFriendsById(String id) {
		String aql = "for c in person filter c.id==@id for v in 1..1 outbound c relation update v with{place:'test'} in person";
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		ArangoDBUtil.getArangoDatabase().query(aql, params, Person.class);
	}

	public void ArangoDBDeleteRelation(String id1, String id2) {
		Map<String, Object> params = new HashMap<>();
		params.put("id1", id1);
		params.put("id2", id2);
		String aql = "let f=FIRST(for c in person filter c.id==@id1 return c._id) let t=FIRST(for c in person filter c.id==@id2 return c._id) FILTER f != null AND t != null for c in relation filter c._from==f filter c._to==t remove c in relation";
		ArangoDBUtil.getArangoDatabase().query(aql, params, Person.class);
	}

	public void ArangoDBDeletePerson(String id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		String aql = "for c in person filter c.id==@id remove c in person";
		ArangoDBUtil.getArangoDatabase().query(aql, params, Person.class);
	}
	public void ArangoDBDeleteAllPerson(){
		String aql = "for c in relation remove c in relation";
		ArangoDBUtil.getArangoDatabase().query(aql, Person.class);
		aql = "for c in person remove c in person";
		ArangoDBUtil.getArangoDatabase().query(aql, Person.class);
	}
	public void Neo4jDeleteAllPerson(){
		Neo4jDBUtil.getSession().run("MATCH p=()-[r:relation]->() delete p");
		Neo4jDBUtil.getSession().run("match(p:person) delete p");
	}
	public Person Neo4jFindPersonByName(String firstName,String lastName){
		StatementResult result = Neo4jDBUtil.getSession().run("match(p:person) where p.firstName={firstName} and p.lastName={lastName} return p.birthday,p.firstName,p.lastName,p.gender,p.id,p.creationDate",
				Values.parameters("firstName", firstName,"lastName",lastName));
		
		if(result.hasNext()){
			Map<String, Object> map = result.next().asMap();
			Person person = mapToPerson(map);
			return person;
		}
		return null;
		
	}
	public Person ArangoDBFindPersonByName(String firstname,String lastName){
		Map<String, Object> params = new HashMap<>();
		params.put("first_name", firstname);
		params.put("last_name", lastName);
		String aql = "for c in person filter c.first_name==@first_name and c.last_name==@last_name return c";
		ArangoCursor<Person> persons = ArangoDBUtil.getArangoDatabase().query(aql, params, Person.class);
		List<Person> list = persons.asListRemaining();
		if(list!=null)
			return list.get(0);
		else
			return null;
	}
	/**
	 * 已知商品名字，查询购买过该商品的顾客信息
	 * @param name
	 */
	public List<Person> ArangoDBFindPersonByProductName(String name){
		Map<String, Object> params = new HashMap<>();
		params.put("title", name);
		String aql = "let person_ids = (let order_ids = (let product_id = (for c in product filter c.title==@title return c._key) for s in sub_order filter s.product_id in product_id return s.order_id) for o in order filter o._key in order_ids return o.person_id) for p in person filter p.id in person_ids return p";
		ArangoCursor<Person> person = ArangoDBUtil.getArangoDatabase().query(aql, params, Person.class);
		return person.asListRemaining();
	}

	public List<Person> Neo4jFindPersonsByOrders(List<Order> orders) {
		String ids="";
		for(Order order:orders){
			if(ids.equals("")){
				ids=ids+"'"+order.getPersonId()+"'";
			}else{
				ids=ids+",'"+order.getPersonId()+"'";
			}
		}
		StatementResult result = Neo4jDBUtil.getSession().run("match(p:person) where p.id in ["+ids+"] return p.birthday,p.firstName,p.lastName,p.gender,p.id,p.creationDate");
		List<Person> list = new ArrayList<>();
		while(result.hasNext()){
			Map<String, Object> map = result.next().asMap();
			Person person = mapToPerson(map);
			list.add(person);
		}
		return list;
	}
	
	public List<String> Neo4jFindFemalePersonIdsByOrders(List<Order> orders) {
		String ids="";
		for(Order order:orders){
			if(ids.equals("")){
				ids=ids+"'"+order.getPersonId()+"'";
			}else{
				ids=ids+",'"+order.getPersonId()+"'";
			}
		}
		StatementResult result = Neo4jDBUtil.getSession().run("match(p:person) where p.gender='female' and p.id in ["+ids+"] return p.id");
		List<String> list = new ArrayList<>();
		while(result.hasNext()){
			Map<String, Object> map = result.next().asMap();
			list.add(map.get("p.id").toString());
		}
		return list;
	}

	/**
	 * @param map
	 * @return
	 */
	private Person mapToPerson(Map<String, Object> map) {
		Person person = new Person();
		for(String key:map.keySet()){
			switch(key){
			case "p.id":
				person.setId(map.get(key).toString());
				break;
			case "p.birthday":
				person.setBirthday(map.get(key).toString());
				break;
			case "p.firstName":
				person.setFirstName(map.get(key).toString());
				break;
			case "p.lastName":
				person.setLastName(map.get(key).toString());
				break;
			case "p.gender":
				person.setGender(map.get(key).toString());
				break;
			case "p.creationDate":
				person.setCreationDate(map.get(key).toString());
				break;
			}
		}
		return person;
	}

	public List<String> ArangoDBFindPersonBuySomeThingByPersonName(String first_name, String last_name) {
		Person person = ArangoDBFindPersonByName(first_name, last_name);
		if(person==null||StringUtils.isEmpty(person.getId()))
			return new ArrayList<>();
		String aql="let person_idss=(let order_idss=(let product_ids =(let order_ids = (for o in order filter o.person_id==@personId return o._key) for s in sub_order filter s.order_id in order_ids return s.product_id) for ss in sub_order filter ss.product_id in product_ids return ss.order_id) for oo in order filter oo._key in order_idss return oo.person_id) for pp in person filter pp.id in person_idss  return pp.id";
		Map<String, Object> params = new HashMap<>();
		params.put("personId", person.getId());
		ArangoCursor<String> productIds = ArangoDBUtil.getArangoDatabase().query(aql, params,  String.class);
		List<String> list = productIds.asListRemaining();
		list.remove(person.getId());
		return list;
	}
	public List<String> Neo4jQueryPersonFriendIds(String id) throws IOException {
		StatementResult result = Neo4jDBUtil.getSession().run("match(q:person)-[r]->(p:person) where q.id={personId} return p.id",
				Values.parameters("personId", id));
		List<String> persons = new ArrayList<>();
		while(result.hasNext()){
			Map<String, Object> map = result.next().asMap();
			persons.add(map.get("p.id").toString());
		}
		return persons;
	}
}
