package com.dasinong.app.entity;

import java.util.List;

public class AddFieldEntity extends BaseEntity {
	public FieldInfo data;
	
	public class FieldInfo {
		public boolean active;
		public int currentStageID;
		public int fieldId;
		public String fieldName;
		public int locationId;
		public int monitorLocationId;
		
		public List natdisws;
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
