package com.dasinong.app.entity;

/**
 * @author: yusonglin
 * @�� ˵ ��:
 * @version 1.0
 * @����ʱ�䣺2015-6-5 ����11:05:48
 * 
 */
// sb.append("\nAddr"+location.getCountry()+"  "+location.getCity()+"  "+location.getProvince()+"  "+location.getStreet()+
// "  "+location.getDistrict()+"  "+location.getNetworkLocationType()+"  "+location.getFloor());

// sb.append("\nlatitude : ");
// sb.append(location.getLatitude());
// sb.append("\nlontitude : ");
// sb.append(location.getLongitude());

public class LocationResult {

	private double latitude;
	private double longitude;

	private String country;
	private String province;
	private String city;
	private String district;
	private String street;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

}
