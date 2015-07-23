package com.dasinong.app.entity;

import java.util.List;

public class SearchTypeResultEntity extends BaseEntity {

	private List<SearchItem> data;

	public List<SearchItem> getData() {
		return data;
	}

	public void setData(List<SearchItem> data) {
		this.data = data;
	}

}
