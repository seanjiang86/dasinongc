package com.dasinong.app.entity;

import com.dasinong.app.net.NetConfig;

public class BaseEntity {

	private String respCode;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;

	}

	public boolean isOk() {
		if (respCode != null && respCode.equals(NetConfig.ResponseCode.OK)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isCode(String code) {
		if (respCode != null && respCode.equals(code)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isAuthTokenInvalid() {
		if ("100".equals(respCode)) {
			return true;
		} else {
			return false;
		}
	}

}
