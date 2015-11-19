package com.dasinong.app.entity;

public class LoginRegEntity extends BaseEntity {

	private User data;
	
	private String accessToken;

	public User getData() {
		return data;
	}

	public void setData(User data) {
		this.data = data;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
