package com.dasinong.app.entity;

public class WXAccessTokenEntity extends BaseEntity{
	
//		"access_token":"ACCESS_TOKEN", 
//		"expires_in":7200, 
//		"refresh_token":"REFRESH_TOKEN",
//		"openid":"OPENID", 
//		"scope":"SCOPE",
//		"unionid":"o6_bmasdasdsad6_2sgVt7hMZOPfL"
	
//	{"errcode":40030,"errmsg":"invalid refresh_token"}
	
	public String access_token;
	public int expires_in;
	public String refresh_token;
	public String openid;
	public String scope;
	public String unionid;
	public String errcode;
	public String errmsg;
	
}