package com.sxj.domain;

public class Relation {
	private String id;
	private String person1;
	private String person2;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPerson1() {
		return person1;
	}
	public void setPerson1(String person1) {
		this.person1 = person1;
	}
	public String getPerson2() {
		return person2;
	}
	public void setPerson2(String person2) {
		this.person2 = person2;
	}
	public Relation strToRelation(String str){
		String item[] = str.split("\\|");
		Relation relation = new Relation();
		relation.setPerson1(item[0]);
		relation.setPerson2(item[1]);
		return relation;
	}
}
