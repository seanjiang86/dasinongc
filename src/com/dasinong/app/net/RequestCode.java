package com.dasinong.app.net;

public final class RequestCode {
	
	/**
	 * 注册
	 */
	public static final int REGISTER_BY_PASSWORD = 0;
	/**
	 * 登录
	 */
	public static final int LOGIN_BY_PASSWORD = REGISTER_BY_PASSWORD + 1;
	/**
	 * 验证码注册登录
	 */
	public static final int LOGIN_REGISTER = LOGIN_BY_PASSWORD + 1;
	/**
	 * 检测用户是否已注册
	 */
	public static final int CHECK_USER = LOGIN_REGISTER + 1;
	
}
