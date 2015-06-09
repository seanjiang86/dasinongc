package com.dasinong.app.entity;

public class User {

	private String userId;
	private String cellPhone;
	private String userName;
	private String password;
	private String address;
	private int[] fields;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int[] getFields() {
		return fields;
	}

	public void setFields(int[] fields) {
		this.fields = fields;
	}

}
