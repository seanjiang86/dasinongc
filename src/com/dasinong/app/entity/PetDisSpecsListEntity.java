package com.dasinong.app.entity;

import java.io.Serializable;
import java.util.List;

public class PetDisSpecsListEntity extends BaseEntity implements Serializable {
	public List<PetDisSpecs> data;

	public static class PetDisSpecs implements Serializable {
		public int petDisSpecId;
		public String petDisSpecName;
		public String petDisSpecNamePY;
		public String sympthon;
		public String thumbnailId;
		public String type;
	}
}
