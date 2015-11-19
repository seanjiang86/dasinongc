package com.dasinong.app.entity;

import java.io.Serializable;
import java.util.List;

public class PetDisSpecEntity extends BaseEntity {

	public PetDisSpec data;

	public class PetDisSpec {
		public String alias;
		public String form;
		public String habbit;
		public int id;
		public List<String> imagesPath;
		public String petDisSpecName;
		public String rule;
		public int severity;
		public List<Solution> solutions;
		public String sympton;
		public String type;

		public class Solution implements Serializable{
			public boolean isCPSolu;
			public boolean isRemedy;
			public int petDisSpecId;
			public String petSoluDes;
			public int petSoluId;
			public String providedBy;
			public int rank;
			public String snapshotCP;
			public String subStageId;

		}

		public List<Solution> petSoluList;
		public List<Solution> petPreventList;

	}

}
