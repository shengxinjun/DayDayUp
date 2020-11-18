package com.sxj.dao;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.sxj.entity.Product;
@Repository
public interface ProductDao  extends Neo4jRepository<Product, Long> {
	@Query("match(n:Product) where n.name={0} return n")
	public Product findProductByName(String name);
}
