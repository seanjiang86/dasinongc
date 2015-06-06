package com.dasinong.app.entity;

import java.util.List;

public class VillageInfoList extends BaseEntity {

	private List<VillageInfo> list;

	public List<VillageInfo> getList() {
		return list;
	}

	class VillageInfo {

		private String name;
		private String id;

		public String getName() {
			return name;
		}

		public String getId() {
			return id;
		}
	}
}
