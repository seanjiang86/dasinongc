package com.dasinong.app.entity;

public class VarietyEntity extends BaseEntity {
	public Variety data;

	public class Variety {
		public String characteristics;
		public int id;
		public String owner;
		public String registrationId;
		public String subId;
		public String suitableArea;
		public String varietyName;
		public String yieldPerformance;
	}
}
