package com.dasinong.app.entity;

import java.util.List;

public class CropNumberList extends BaseEntity {
	private List<CropNumber> list;

	public class CropNumber {
		private String name;
		private String id;

		public String getName() {
			return name;
		}

		public String getId() {
			return id;
		}
	}

	public List<CropNumber> getList() {
		return list;
	}

}
