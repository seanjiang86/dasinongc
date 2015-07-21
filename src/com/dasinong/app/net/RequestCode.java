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
	 * 发送在田里时的经纬度信息
	 */
	public static final int SEARCH_LOCATION = CHECK_USER + 1;
	/**
	 * 获取品种名列表
	 */
	public static final int GET_VARIETY_LIST = SEARCH_LOCATION + 1;
	/**
	 * 获取村子的信息列表
	 */
	public static final int GET_LOCATION = GET_VARIETY_LIST + 1;
	/**
	 * 搜索附近用户
	 */
	public static final int SEARCH_NEAR_USER = GET_LOCATION + 1;
	/**
	 * 获取全部任务
	 */
	public static final int GET_ALL_TASK = SEARCH_NEAR_USER + 1;
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
	public static final int SMS_SUBSCRIBE = UPLOAD_PHONE_AUTH_STATE + 1;
	public static final int GET_SUBSCRIBE_LIST = SMS_SUBSCRIBE + 1;
	public static final int RESET_PWSSWORD = GET_SUBSCRIBE_LIST + 1;
	public static final int UPDATE_PWSSWORD = RESET_PWSSWORD + 1;
	public static final int DELETE_SMS_SUBSCRIBE = UPDATE_PWSSWORD + 1;
	public static final int MODIFI_SMS_SUBSCRIBE = DELETE_SMS_SUBSCRIBE + 1;
	public static final int SMS_SUBSCRIBE_DETAIL = MODIFI_SMS_SUBSCRIBE + 1;
	/**
	 * 创建田地
	 */
	public static final int CREATE_FIELD = SMS_SUBSCRIBE_DETAIL + 1;
	/**
	 * 搜索百科
	 */
	public static final int SEARCH_WORD = CREATE_FIELD + 1;
	/**
	 * 请求验证码
	 */
	public static final int REQUEST_SECURITY_CODE = SEARCH_WORD + 1;
	public static final int LOGIN_WITH_SECCODE = REQUEST_SECURITY_CODE + 1;
	public static final int IS_PWSS_SET = LOGIN_WITH_SECCODE + 1;
	public static final int GET_STEPS = IS_PWSS_SET + 1;
	/**
	 * 获取指定的病虫草害详情
	 */
	public static final int GET_PET_DIS_SPEC_DETIAL = GET_STEPS + 1;
	/**
	 * 获取指定病虫草害解决方案所需药物
	 */
	public static final int  GET_PET_SOLU= GET_PET_DIS_SPEC_DETIAL + 1;
	public static final int  PETDIS_BYTYPE= GET_PET_SOLU + 1;
	public static final int  CPPRODUCT_BYMODEL= PETDIS_BYTYPE + 1;
}
