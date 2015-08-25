package com.dasinong.app.entity;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AllTaskEntity extends BaseEntity {
	public TreeMap<Integer, List<TaskDetialEntity>> data;

	public class TaskDetialEntity {
		public int fieldId;
		public String stageName;
		public int subStageId;
		public String subStageName;
		public int taskId;
		public int taskSpecId;
		public String taskSpecName;
		public boolean taskStatus;
	}

}
