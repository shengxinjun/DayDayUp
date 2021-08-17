package com.sxj.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arangodb.ArangoCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.sxj.domain.Order;
import com.sxj.domain.OrderItem;
import com.sxj.domain.Person;
import com.sxj.domain.Product;
import com.sxj.util.ArangoDBUtil;
import com.sxj.util.MongoDBUtil;

@Service
public class ProductService {
	
	private static Logger log = Logger.getLogger(ProductService.class);
	
	@Autowired
	private OrderService orderService;
	
	public void MongoDBInsertProduct(Product product) throws IOException {
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("product");
		Document document = new Document("_id", product.getId()).append("title", product.getTitle()).append("price", product.getPrice())
				.append("imgUrl", product.getImgUrl());
		collection.insertOne(document);
	}

	public FindIterable<Document> MongoDBQueryProduct(String id) throws IOException {
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("product");
		FindIterable<Document> it = collection.find(Filters.eq("_id",id));
		return it;
	}
	public void MongoDBUpdateProduct(String id) throws IOException {
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("product");
		collection.updateOne(new Document().append("_id", id), new Document().append("$set", new Document().append("price", "100")));
	}
	public void MongoDBDeleteProduct(String id) throws IOException {
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("product");
		collection.deleteOne(new Document().append("_id", id));
	}
	
	public void ArangoDBInsertProduct(Product product){
		String aql = "insert {_key:@id,title:@title,price:@price,imgUrl:@imgUrl} in product";
		Map<String, Object> params = new HashMap<>();
		params.put("id", product.getId());
		params.put("title", product.getTitle());
		params.put("price", product.getPrice());
		params.put("imgUrl", product.getImgUrl());
		ArangoDBUtil.getArangoDatabase().query(aql, params, Product.class);
	}
	public ArangoCursor<Product> ArangoDBQueryProductById(String id){
		Map<String, Object> params= new HashMap<>();
		params.put("id", id);
		String aql="for c in product filter c._key==@id  return c";
		return ArangoDBUtil.getArangoDatabase().query(aql, params,  Product.class);
	}
	public void ArangoDBUpdateProductById(String id){
		Map<String, Object> params= new HashMap<>();
		params.put("id", id);
		String aql="for c in product filter c._key==@id update c with{price:'100'} in product";
		ArangoDBUtil.getArangoDatabase().query(aql, params,  Product.class);
	}
	public void ArangoDBDeleteProductById(String id){
		Map<String, Object> params= new HashMap<>();
		params.put("id", id);
		String aql="for c in product filter c._key==@id remove c in product";
		ArangoDBUtil.getArangoDatabase().query(aql, params,  Product.class);
	}
	public void MongoDBDeleteAllProduct(){
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("product");
		collection.drop();
	}
	public void ArangoDBDeleteAllProduct(){
		String aql = "for c in product remove c in product";
		ArangoDBUtil.getArangoDatabase().query(aql, Product.class);
	}

	/**
	 * 已知顾客主键，查询其购买过哪些商品
	 * @param id
	 */
	public List<String> ArangoDBFindProductListByPersonId(String id){
		Map<String, Object> params= new HashMap<>();
		params.put("person_id", id);
		String aql="let order_ids = (for c in order filter c.person_id==@person_id return c._key) for s in sub_order filter s.order_id in order_ids return s.product_id";
		ArangoCursor<String> productIds = ArangoDBUtil.getArangoDatabase().query(aql, params,  String.class);
		List<String> list = productIds.asListRemaining();
		return list;
	}

	public List<Product> MongoDBFindProductByIds(List<String> productIds) {
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("product");
		FindIterable<Document> it = collection.find(Filters.in("_id",productIds));
		Iterator<Document> iterator = it.iterator();
		List<Product> list = new ArrayList<>();
		while(iterator.hasNext()){
			Document doc = iterator.next();
			Product product = documentToProduct(doc);
			list.add(product);
		}
		return list;
	}
	Product documentToProduct(Document doc){
		Product product = new Product();
		product.setId(doc.getString("_id"));
		product.setTitle(doc.getString("title"));
		product.setImgUrl(doc.getString("imgUrl"));
		product.setPrice(doc.getString("price"));
		return product;
	}
	public Product MongoDBFindProductByTitle(String title) {
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("product");
		FindIterable<Document> it = collection.find(Filters.eq("title",title));
		Iterator<Document> iterator = it.iterator();
		if(iterator.hasNext()){
			Document doc = iterator.next();
			Product product = documentToProduct(doc);
			return product;
		}else{
			return null;
		}
		
	}
	public List<Product> ArangoDBFindFemaleOtherProductByProductTitle(String title) {
		String aql="let product_idss =(let order_idss=(let person_idss = (let person_ids =(let order_ids = (let product_id = (for p in product filter p.title==@title return p._key) for s in sub_order filter s.product_id in product_id return s.order_id) for o in order filter o._key in order_ids return o.person_id) for c in person filter c.id in person_ids and c.gender=='female' return c.id) for oo in order filter oo.person_id in person_idss return oo._key) for ss in sub_order filter ss.order_id in order_idss return ss.product_id) for pp in product filter pp._key in product_idss return pp";
		Map<String, Object> params= new HashMap<>();
		params.put("title", title);
		ArangoCursor<Product> product = ArangoDBUtil.getArangoDatabase().query(aql,params, Product.class);
		List<Product> products = product.asListRemaining();
		return products;
	}

	public List<Product> MongoDBFindProductByPersonIds(List<String> persons) {
		List<String> orderIds=orderService.MongoDBFindOrderIdsByPersonIds(persons);
		List<String> productIds = orderService.MongoDBFindProductIdsByOrderIds(orderIds);
		return MongoDBFindProductByIds(productIds);
		
		
	}

	public List<String> ArangoDBFindFriendsProductListByPersonId(String personId) {
		Map<String, Object> params= new HashMap<>();
		params.put("personId", personId);
		String aql="let product_ids =(let order_ids = (let friends_ids =(for c in person filter c.id==@personId for v in 1..1 outbound c relation return v.id) for c in order filter c.person_id in friends_ids return c._key) for s in sub_order filter s.order_id in order_ids return s.product_id) for p in product filter p._key in product_ids return p._key";
		ArangoCursor<String> productIds = ArangoDBUtil.getArangoDatabase().query(aql, params,  String.class);
		List<String> list = productIds.asListRemaining();
		return list;
	}
}
