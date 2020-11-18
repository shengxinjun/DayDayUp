package com.sxj.dao;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.sxj.entity.Relation;

@Repository
public interface RelationDao extends Neo4jRepository<Relation, Long>{
	
	@Query("match(p:Product{name:{1}}),(v:Vendor{name:{0}})  create (p)-[:relation{relationName:'belong'}]->(v)")
	public Relation saveRelation(String vname,String pname);
	
	@Query("MATCH (p:Product)-[r]->(v:Vendor) where p.name={1} and v.name={0} DELETE r")
	public Relation deleteRelation(String vname,String pname);
	
}
