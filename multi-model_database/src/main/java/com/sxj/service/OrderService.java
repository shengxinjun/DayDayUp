package com.sxj.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arangodb.ArangoCursor;
import com.arangodb.entity.BaseDocument;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.sxj.domain.Order;
import com.sxj.domain.OrderItem;
import com.sxj.domain.Product;
import com.sxj.util.ArangoDBUtil;
import com.sxj.util.MongoDBUtil;
@Service
public class OrderService {

	private static Logger log = Logger.getLogger(OrderService.class);
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private ProductService productService;
	
	
	public void MongoDBInsertOrder(Order order) throws IOException {
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("order");
		Document document = new Document("_id", order.getId()).append("person_id", order.getPersonId()).append("order_date", order.getOrderDate())
				.append("total_price", order.getTotalPrice());
		collection.insertOne(document);
		if(order.getItems()!=null&&order.getItems().size()>0){
			collection = MongoDBUtil.getDB().getCollection("sub_order");
			List<Document> list = new ArrayList<>();
			for(OrderItem item:order.getItems()){
				Document doc = new Document();
				doc.append("order_id", order.getId()).append("product_id", item.getProductId());
				list.add(doc);
			}
			collection.insertMany(list);
		}
	}
	public FindIterable<Document> MongoDBQueryOrder(int id) throws IOException {
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("order");
		FindIterable<Document> it = collection.find(Filters.eq("_id",id));
		return it;
	}
	public void MongoDBUpdateOrder(int id) throws IOException {
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("order");
		collection.updateOne(new Document().append("_id", id), new Document().append("$set", new Document().append("total_price", "100")));
	}
	public void MongoDBDeleteOrder(String id) throws IOException {
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("order");
		collection.deleteOne(new Document().append("_id", id));
		collection = MongoDBUtil.getDB().getCollection("sub_order");
		collection.deleteMany(new Document().append("order_id", id));
	}
	public void ArangoDBInsertOrder(Order order){
		String aql = "insert {_key:@id,person_id:@person_id,order_date:@order_date,total_price:@total_price} in order";
		Map<String, Object> params = new HashMap<>();
		params.put("id", order.getId());
		params.put("person_id", order.getPersonId());
		params.put("order_date", order.getOrderDate());
		params.put("total_price", order.getTotalPrice());
		ArangoDBUtil.getArangoDatabase().query(aql, params, Order.class);
		if(order.getItems()!=null&&order.getItems().size()>0){
			JSONArray array = new JSONArray();
			for(OrderItem item:order.getItems()){
				JSONObject obj=new JSONObject();
				obj.put("order_id", order.getId());
				obj.put("product_id", item.getProductId());
				array.add(obj);
			}
			String SubOrderStr = array.toString();
			ArangoDBUtil.getArangoDatabase().collection("sub_order").importDocuments(SubOrderStr);
		}
	}
	public ArangoCursor<Order> ArangoDBQueryOrderById(String id){
		Map<String, Object> params= new HashMap<>();
		params.put("id", id);
		String aql="for c in order filter c._key==@id  return c";
		return ArangoDBUtil.getArangoDatabase().query(aql, params,  Order.class);
	}
	public void ArangoDBUpdateOrderById(String id){
		Map<String, Object> params= new HashMap<>();
		params.put("id", id);
		String aql="for c in order filter c._key==@id update c with{total_price:'test'} in order";
		ArangoDBUtil.getArangoDatabase().query(aql, params,  Order.class);
	}
	public void ArangoDBDeleteOrderById(String id){
		Map<String, Object> params= new HashMap<>();
		params.put("id", id);
		String aql="for c in order filter c._key==@id remove c in order";
		ArangoDBUtil.getArangoDatabase().query(aql, params,  Order.class);
		aql="for c in sub_order filter c.order_id==@id remove c in sub_order";
		ArangoDBUtil.getArangoDatabase().query(aql, params,  Order.class);
	}
	public void MongoDBDeleteAllOrder(){
		MongoCollection<Document> collection1 = MongoDBUtil.getDB().getCollection("order");
		collection1.drop();
		MongoCollection<Document> collection2 = MongoDBUtil.getDB().getCollection("sub_order");
		collection2.drop();
	}
	public void ArangoDBDeleteAllOrder(){
		String aql = "for c in order remove c in order";
		ArangoDBUtil.getArangoDatabase().query(aql, Order.class);
		aql = "for c in sub_order remove c in sub_order";
		ArangoDBUtil.getArangoDatabase().query(aql, Order.class);
		
	}
	/**
	 * 已知顾客名字，查询其支付的订单总额
	 * @param firstName
	 * @param lastName
	 */
	public List<Order> ArangoDBFindTotalPriceByPersonName(String firstName,String lastName){
		Map<String, Object> params= new HashMap<>();
		params.put("first_name", firstName);
		params.put("last_name", lastName);
		String aql="let person_ids =(for p in person filter p.first_name==@first_name and p.last_name==@last_name return p.id) for c in order filter c.person_id in person_ids return c";
		ArangoCursor<Order> order = ArangoDBUtil.getArangoDatabase().query(aql,params, Order.class);
		List<Order> list = order.asListRemaining();
		return list;
	}
	public List<Order> MongoDBFindOrderByPersonId(String personId) {
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("order");
		FindIterable<Document> it = collection.find(Filters.eq("person_id",personId));
		List<Order> list = new ArrayList<>();
		Iterator<Document> iterator = it.iterator();
		while(iterator.hasNext()){
			Order order=documentToOrder(iterator.next());
			list.add(order);
		}
		return list;
	}
	public List<String> MongoDBFindProductIdsByPersonId(String personId){
		List<Order> orders=MongoDBFindOrderByPersonId(personId);
		List<String> orderIds = new ArrayList<>();
		orders.stream().forEach(e->orderIds.add(e.getId()));
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("sub_order");
		FindIterable<Document> it = collection.find(Filters.in("order_id",orderIds));
		List<String> list = new ArrayList<>();
		Set set = new HashSet<>();
		Iterator<Document> iterator = it.iterator();
		while (iterator.hasNext()){
			set.add(iterator.next().getString("product_id"));
		}
		list.addAll(set);
		return list;
	}
	Order documentToOrder(Document doc){
		Order order = new Order();
		order.setId(doc.getString("_id"));
		order.setTotalPrice(doc.getString("total_price"));
		order.setPersonId(doc.getString("person_id"));
		order.setOrderDate(doc.getString("order_date"));
		return order;
	}
	OrderItem documentToOrderItem(Document doc){
		OrderItem item = new OrderItem();
		item.setId(doc.getString("_id"));
		item.setOrderId(doc.getString("order_id"));
		item.setProductId(doc.getString("product_id"));
		return item;
	}
	public List<Order> MongoDBFindOrdersByProductId(String id) {
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("sub_order");
		FindIterable<Document> it = collection.find(Filters.eq("product_id",id));
		Iterator<Document> iterator = it.iterator();
		List<String> ids = new ArrayList<>();
		while(iterator.hasNext()){
			ids.add(iterator.next().getString("order_id"));
		}
		collection = MongoDBUtil.getDB().getCollection("order");
		it = collection.find(Filters.in("_id",ids));
		iterator = it.iterator();
		List<Order> list = new ArrayList<>();
		while(iterator.hasNext()){
			list.add(documentToOrder(iterator.next()));
		}
		return list;
	}
	public List<Order> MongoDBFindOrdersByProductIds(List<String> productIds) {
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("sub_order");
		FindIterable<Document> it = collection.find(Filters.in("product_id",productIds));
		Iterator<Document> iterator = it.iterator();
		Set<String> set = new HashSet<>();
		List<String> ids = new ArrayList<>();
		while(iterator.hasNext()){
			set.add(iterator.next().getString("order_id"));
		}
		ids.addAll(set);
		collection = MongoDBUtil.getDB().getCollection("order");
		it = collection.find(Filters.in("_id",ids));
		iterator = it.iterator();
		List<Order> list = new ArrayList<>();
		while(iterator.hasNext()){
			list.add(documentToOrder(iterator.next()));
		}
		return list;
	}
	public List<Order> MongoDBFindOrderDetailByPersonIds(List<String> personIds) {
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("order");
		FindIterable<Document> it = collection.find(Filters.in("person_id",personIds));
		Iterator<Document> iterator = it.iterator();
		List<Order> list = new ArrayList<>();
		while(iterator.hasNext()){
			list.add(documentToOrder(iterator.next()));
		}
		for(Order order:list){
			order.setItems(MongoDBFindOrderItemsByOrderId(order.getId()));;
		}
		return list;
	}
	public List<OrderItem> MongoDBFindOrderItemsByOrderId(String orderId){
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("sub_order");
		FindIterable<Document> it = collection.find(Filters.eq("order_id",orderId));
		Iterator<Document> iterator = it.iterator();
		List<OrderItem> items = new ArrayList<>();
		while(iterator.hasNext()){
			items.add(documentToOrderItem(iterator.next()));
		}
		return items;
	}
	public List<String> MongoDBFindProductIdsByPersonIds(List<String> personIds) {
		List<String> orderIds = MongoDBFindOrderIdsByPersonIds(personIds);
		List<String> productIds = MongoDBFindProductIdsByOrderIds(orderIds);
		return productIds;
	}
	public List<String> MongoDBFindProductIdsByOrderIds(List<String> orderIds){
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("sub_order");
		FindIterable<Document> it = collection.find(Filters.in("order_id",orderIds));
		Iterator<Document> iterator = it.iterator();
		HashSet<String> set=new HashSet<>();
		while(iterator.hasNext()){
			set.add(iterator.next().getString("product_id"));
		}
		List<String> productIds	= new ArrayList<>();
		set.stream().forEach(e->productIds.add(e));
		return productIds;
	}
	public List<String> MongoDBFindOrderIdsByPersonIds(List<String> personIds){
		MongoCollection<Document> collection = MongoDBUtil.getDB().getCollection("order");
		FindIterable<Document> it = collection.find(Filters.in("person_id",personIds));
		Iterator<Document> iterator = it.iterator();
		List<String> orderIds=new ArrayList<>();
		while(iterator.hasNext()){
			orderIds.add(iterator.next().getString("_id"));
		}
		return orderIds;
	}
	
	private List<OrderItem> ArangoDBFindOrderItemByOrderId(String orderId) {
		String aql="for c in sub_order filter c.order_id ==@order_id return c";
		Map<String, Object> params= new HashMap<>();
		params.put("order_id", orderId);
		ArangoCursor<OrderItem> order = ArangoDBUtil.getArangoDatabase().query(aql,params, OrderItem.class);
		List<OrderItem> orderItems = order.asListRemaining();
		return orderItems;
	}
	public List<String> ArangoDBFindPersonIdsByProductIds(List<String> productIds) {
		String[] arrayIds=new String[productIds.size()];
		String aql="let order_ids = (for c in sub_order filter c.product_id in @productIds return c.order_id) for o in order filter o._key in order_ids return o.person_id";
		Map<String, Object> params= new HashMap<>();
		params.put("productIds", productIds.toArray(arrayIds));
		ArangoCursor<String> personIds = ArangoDBUtil.getArangoDatabase().query(aql,params, String.class);
		return personIds.asListRemaining();
	}
}
