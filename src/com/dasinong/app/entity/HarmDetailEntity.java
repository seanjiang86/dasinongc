package com.dasinong.app.entity;

import java.io.Serializable;
import java.util.List;

import com.dasinong.app.database.disaster.domain.PetSolu;

public class HarmDetailEntity extends BaseEntity {
	public HarmDetail data;

	public static class HarmDetail {
		public HarmInfo petDisSpec;
		public List<Solutions> petSolutions;
	}

	public static class HarmInfo {
		public String alias;
		public String form;
		public String habbit;
		public int id;
		public String imagePath;
		public String petDisSpecName;
		public String rule;
		public String sympton;
		public int severity;
	}
	
	public static class Solutions implements Serializable{
		public boolean isCPSolu;
		public boolean isRemedy;
		public int petDisSpecId;
		public String petSoluDes;
		public int petSoluId;
		public String providedBy;
		public int rank;
		public String subStageId;
		public String snapshotCP;
	}
}
