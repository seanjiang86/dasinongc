package com.dasinong.app.entity;

import java.util.List;

public class CPProductEntity extends BaseEntity {
	public CPProduct data;

	public class CPProduct {
		public String activeIngredient;
		public String crop;
		public String disease;
		public String guideline;
		public int id;
		public String manufacturer;
		public String method;
		public String name;
		public String registrationId;
		public String tip;
		public String type;
		public String volumn;
		public List<UseDirection> useDirections;
		public String telephone;
	}
	
	public class UseDirection{
		public String useCrop;
		public String useDisease;
		public String useVolumn;
		public String useMethod;
	}
}
