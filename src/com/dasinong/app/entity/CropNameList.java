package com.dasinong.app.entity;

import java.util.List;

public class CropNameList extends BaseEntity {
	private List<CropName> list;

	public class CropName {
		private String name;
		private String id;

		public String getName() {
			return name;
		}

		public String getId() {
			return id;
		}
	}

	public List<CropName> getList() {
		return list;
	}

}
