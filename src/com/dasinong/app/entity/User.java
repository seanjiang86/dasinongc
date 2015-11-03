package com.dasinong.app.entity;

public class User {

	private String userName;
	private boolean authenticated;
	private String userId;
	private String cellPhone;
	private String pictureId;
	private String telephone;
	private String address;
	private String[] fields;
	private String[] monitorLocationId;
	private String qqtoken;
	private String weixintoken;
	private String refcode;
	private int refuid;
	private String channel;
	private int institutionId;
	
	public int getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getRefcode() {
		return refcode;
	}

	public void setRefcode(String refcode) {
		this.refcode = refcode;
	}

	public int getRefuid() {
		return refuid;
	}

	public void setRefuid(int refuid) {
		this.refuid = refuid;
	}

	public String getQqtoken() {
		return qqtoken;
	}

	public void setQqtoken(String qqtoken) {
		this.qqtoken = qqtoken;
	}

	public String getWeixintoken() {
		return weixintoken;
	}

	public void setWeixintoken(String weixintoken) {
		this.weixintoken = weixintoken;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPictureId() {
		return pictureId;
	}

	public void setPictureId(String pictureId) {
		this.pictureId = pictureId;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public String[] getMonitorLocationId() {
		return monitorLocationId;
	}

	public void setMonitorLocationId(String[] monitorLocationId) {
		this.monitorLocationId = monitorLocationId;
	}

}
