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
	public static final int SEARCH_LOCATION = SEND_NO_IN_LOCATION + 1;
	/**
	 * 获取植物种类列表
	 */
	public static final int GET_CROP_LIST = SEARCH_LOCATION + 1;
	/**
	 * 获取品种名列表
	 */
	public static final int GET_VARIETY_LIST = GET_CROP_LIST + 1;
	/**
	 * 获取村子的信息列表
	 */
	public static final int GET_LOCATION = GET_VARIETY_LIST + 1;
	/**
	 * 搜索附近用户
	 */
	public static final int SEARCH_NEAR_USER = GET_LOCATION + 1;
	/**
	 * 获取生长周期
	 */
	public static final int GET_SUB_STAGE = SEARCH_NEAR_USER + 1;
	/**
	 * 获取全部任务
	 */
	public static final int GET_ALL_TASK = GET_SUB_STAGE + 1;
	/**
	 * 获取个人信息
	 */
	public static final int GET_MY_INFO = GET_ALL_TASK + 1;
	/**
	 * 上传个人信息
	 */
	public static final int UPLOAD_MY_INFO = GET_MY_INFO + 1;
	/**
	 * 更新任务信息
	 */
	public static final int UPLOAD_MY_TASK = UPLOAD_MY_INFO + 1;
	public static final int UPLOAD_PHONE_AUTH_STATE = UPLOAD_MY_TASK + 1;
}
