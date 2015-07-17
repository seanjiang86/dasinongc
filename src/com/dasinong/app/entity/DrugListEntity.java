package com.dasinong.app.entity;

import java.util.List;

import com.dasinong.app.entity.HarmDetialEntity.Solutions;

public class DrugListEntity extends BaseEntity {
	public Data data;
	
	public static class Data{
		public Solutions petSolutions;
		public List<Drug> cPProducts;
	}
	
	public static class Drug{
		public String activeIngredient;
		public String disease;
		public String guideline;
		public int id;
		public String manufacturer;
		public String name;
		public String registrationId;
		public String tip;
		public String type;
		public String volumn;
	}
}
