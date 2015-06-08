package com.dasinong.app.entity;

import java.util.List;

public class CropList extends BaseEntity {
	private List<Crop> list;

	public class Crop {
		private String name;
		private String id;

		public String getName() {
			return name;
		}

		public String getId() {
			return id;
		}
	}

	public List<Crop> getList() {
		return list;
	}

}
