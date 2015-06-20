package com.dasinong.app.entity;
/**
 * @author: yusonglin
 * @类   说   明:	
 * @version 1.0
 * @创建时间：2015-6-20 下午12:02:58
 * 
 */
public class SmsSubscribeItem {
	private String targetName;
	private String id;
	private String cellphone;
	private String area;
	private String cropId;
	private String ownerId;
	private String province;
	private String city;
	private String district;
	private String country;
	private boolean isAgriWeather;
	private boolean isNatAler;
	private boolean isRiceHelper;

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCropId() {
		return cropId;
	}

	public void setCropId(String cropId) {
		this.cropId = cropId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isAgriWeather() {
		return isAgriWeather;
	}

	public void setAgriWeather(boolean isAgriWeather) {
		this.isAgriWeather = isAgriWeather;
	}

	public boolean isNatAler() {
		return isNatAler;
	}

	public void setNatAler(boolean isNatAler) {
		this.isNatAler = isNatAler;
	}

	public boolean isRiceHelper() {
		return isRiceHelper;
	}

	public void setRiceHelper(boolean isRiceHelper) {
		this.isRiceHelper = isRiceHelper;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
