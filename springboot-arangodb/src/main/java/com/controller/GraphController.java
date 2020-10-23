package com.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arangodb.ArangoCursor;
import com.arangodb.entity.BaseDocument;
import com.util.ArangoUtil;
import com.util.JsonUtil;
@RequestMapping("/graph")
@RestController
public class GraphController {
	
	@RequestMapping("/query")
	String query(){
		ArangoUtil arangoUtil = new ArangoUtil();
		arangoUtil.initArango("www.shengxinjun.top", 8529, "root", "root");
		arangoUtil.initDbs("example");
		Map<String, Object> params= new HashMap<>();
		params.put("name", "Ned");
		String query="for c in Characters filter c.name==@name for v in 1..1 inbound c ChildOf return v";
		ArangoCursor<BaseDocument>  doc = arangoUtil.query(0, query, params);
		List<BaseDocument> list = doc.asListRemaining();
		List<String> strs=new ArrayList<String>();
		for(BaseDocument b:list){
			strs.add(b.getAttribute("name").toString());
		}
		String json = JsonUtil.list2Json(strs);
		return json;
	}
	@RequestMapping("/insert")
	String insert(){
		ArangoUtil arangoUtil = new ArangoUtil();
		arangoUtil.initArango("www.shengxinjun.top", 8529, "root", "root");
		arangoUtil.initDbs("example");
		BaseDocument doc = new BaseDocument();
		doc.addAttribute("name", "Jon");
		doc.addAttribute("surname", "Snow");
		doc.addAttribute("alive",true);
		doc.addAttribute("age", 16);
		List<String> list =new ArrayList<>();
		list.add("A");
		list.add("B");
		list.add("C");
		list.add("F");
		doc.addAttribute("traits",list);
		doc.addAttribute("season", 1);
		Map<String, Object> params= new HashMap<>();
		params.put("name1", "Jon");
		params.put("name2", "Ned");
		String insertRelation="let childId=FIRST(for c in Characters filter c.name==@name1 return c._id) let parentId=FIRST(for c in Characters filter c.name==@name2 return c._id) FILTER parentId != null AND childId != null INSERT { _from: childId, _to: parentId } INTO ChildOf RETURN NEW";
		try {
			arangoUtil.insert(0, "Characters", doc);
			arangoUtil.query(0, insertRelation, params);
			return "success";
		} catch (Exception e) {
			return "fail";
		}
	}
	@RequestMapping("/delete")
	String delete(){
		ArangoUtil arangoUtil = new ArangoUtil();
		arangoUtil.initArango("www.shengxinjun.top", 8529, "root", "root");
		arangoUtil.initDbs("example");
		Map<String, Object> params= new HashMap<>();
		params.put("name", "Jon");
		params.put("name2", "Ned");
		String query="for c in Characters filter c.name==@name remove c in Characters";
		String relation="let childId=FIRST(for c in Characters filter c.name==@name return c._id) let parentId=FIRST(for c in Characters filter c.name==@name2 return c._id) FILTER parentId != null AND childId != null for c in ChildOf filter c._from==childId filter c._to==parentId remove c in ChildOf";
		try {
			arangoUtil.query(0, relation, params);
			params.remove("name2");
			arangoUtil.query(0, query, params);
			return "success";
		} catch (Exception e) {
			return "fail";
		}
		
	}
	
}
