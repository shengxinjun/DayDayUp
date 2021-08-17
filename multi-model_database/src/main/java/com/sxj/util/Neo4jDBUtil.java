package com.sxj.util;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

public class Neo4jDBUtil {

	static Driver driver;
	static Session session;
	static {
		driver = GraphDatabase.driver("bolt://120.55.63.242:7687", AuthTokens.basic("neo4j", "123456"));
		session = driver.session();
	}

	public static Session getSession() {
		return session;
	}
}
