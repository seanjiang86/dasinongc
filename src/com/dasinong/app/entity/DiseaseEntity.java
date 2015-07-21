package com.dasinong.app.entity;

public class DiseaseEntity {

	// "petDisSpecName": "凹缘菱纹叶蝉",
	// "petDisSpecId": 2399,
	// "petDisSpecNamePY": "aoyuanlingwenyechan",
	// "type": "虫害"

	private String petDisSpecName;
	private int petDisSpecId;
	private String petDisSpecNamePY;
	private String type;

	public String getPetDisSpecName() {
		return petDisSpecName;
	}

	public void setPetDisSpecName(String petDisSpecName) {
		this.petDisSpecName = petDisSpecName;
	}

	public int getPetDisSpecId() {
		return petDisSpecId;
	}

	public void setPetDisSpecId(int petDisSpecId) {
		this.petDisSpecId = petDisSpecId;
	}

	public String getPetDisSpecNamePY() {
		return petDisSpecNamePY;
	}

	public void setPetDisSpecNamePY(String petDisSpecNamePY) {
		this.petDisSpecNamePY = petDisSpecNamePY;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
