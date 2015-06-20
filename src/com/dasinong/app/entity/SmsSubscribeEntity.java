package com.dasinong.app.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SmsSubscribeEntity extends BaseEntity {

	private Map<String, String> data;

	private List<SmsSubscribeItem> convertData = new ArrayList<SmsSubscribeItem>();

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public List<SmsSubscribeItem> getConvertData() {
		if (convertData.isEmpty()) {
			for (Map.Entry<String, String> entry : data.entrySet()) {
				SmsSubscribeItem item = new SmsSubscribeItem();
				item.setTargetName(entry.getKey());
				item.setId(entry.getValue());
				convertData.add(item);
			}
		}
		return convertData;
	}

}
