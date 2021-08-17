package com.sxj.domain;

import org.springframework.util.StringUtils;

public class Person {
	private String id;
	private String firstName;
	private String lastName;
	private String gender;
	private String birthday;
	private String creationDate;
	
	/**
	 * ArangoDB专用字段
	 * @return
	 */
	private String first_name;
	private String last_name;
	private String create_date;
	
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Person stringToPerson(String str){
		str = filterSqlString(str);
		String item[] = str.split("\\|");
		Person person = new Person();
		person.setId(item[0]);
		person.setFirstName(item[1]);
		person.setLastName(item[2]);
		person.setGender(item[3]);
		person.setBirthday(item[4]);
		person.setCreationDate(item[5]);
		return person;
	}
	public String filterSqlString(String sqlStr) {
		if (StringUtils.isEmpty(sqlStr)) {
			return sqlStr;
		}
		sqlStr = sqlStr.replace("'", "");
		sqlStr = sqlStr.replace("\\", "");
		return sqlStr;
	}
}
