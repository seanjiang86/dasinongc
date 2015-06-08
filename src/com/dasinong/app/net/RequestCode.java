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
	/**
	 * 发送不在田里时的经纬度信息
	 */
	public static final int SEND_NO_IN_LOCATION = CHECK_USER + 1;
	/**
	 * 发送在田里时的经纬度信息
	 */
	public static final int SEND_IN_LOCATION = SEND_NO_IN_LOCATION + 1;
	/**
	 * 获取植物种类列表
	 */
	public static final int GET_CROP = SEND_IN_LOCATION + 1;
	/**
	 * 获取品种名列表
	 */
	public static final int GET_CROP_NAME = GET_CROP + 1;
	/**
	 * 获取品种编号列表
	 */
	public static final int GET_CROP_NUMBER = GET_CROP_NAME + 1;
	/**
	 * 获取村子的信息列表
	 */
	public static final int GET_VILLAGE_INFO = GET_CROP_NUMBER + 1;
	
}
