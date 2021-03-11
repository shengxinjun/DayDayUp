package com.sxj.lab2;

import org.neo4j.driver.v1.Values;

public class Post {

	private String id;
	private String imageFile;
	private String creationDate;
	private String locationIP;
	private String browserUsed;
	private String language;
	private String content;
	private String length;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImageFile() {
		return imageFile;
	}
	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getLocationIP() {
		return locationIP;
	}
	public void setLocationIP(String locationIP) {
		this.locationIP = locationIP;
	}
	public String getBrowserUsed() {
		return browserUsed;
	}
	public void setBrowserUsed(String browserUsed) {
		this.browserUsed = browserUsed;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	
}
