package com.dasinong.app.entity;

import java.util.List;

public class ChangeStageEntity extends BaseEntity {
	
	public Data data;
	
	public class Data{
		public boolean active;
		public String cropName;
		public int currentStageID;
		public int dayToHarvest;
		public int fieldId;
		public String fieldName;
		public int locationId;
		public int monitorLocationId;
		public List natdisws;
		public List petdisspecws;
		public List petdisws;
		public boolean sprayable;
		public long startDate;
		public List taskws;
		public int userId;
		public int varietyId;
		public boolean workable;
		public int yield;
	}
}
