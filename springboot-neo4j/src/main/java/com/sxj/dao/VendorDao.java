package com.sxj.dao;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.sxj.entity.Vendor;

@Repository
public interface VendorDao extends Neo4jRepository<Vendor, Long> {
	
	
	@Query("match(n:Vendor) where n.name={0} return n")
	public Vendor findVendorByName(String name);

	@Query("match(n:Vendor) where n.name={0} delete n")
	public void deleteVendorByName(String name);
	
	@Query("match(p:Product)-[]->(v:Vendor) where p.name={0} return v limit 1 ")
	public Vendor findVendorByProduct(String name);
	
	@Query("match(p:Product{name:{0}})-[]->(v)  set v.title = 'test' ")
	public Vendor updateVendorOnlyForTest(String name);
}
