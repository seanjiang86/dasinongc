package com.dasinong.app.entity;

public class SearchItem {

	private String id;
	private String name;
	private String source;

	private boolean isType = false;

	public boolean isType() {
		return isType;
	}

	public void setType(boolean isType) {
		this.isType = isType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}