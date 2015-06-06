package com.dasinong.app.net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpParams;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.utils.Logger;
import com.dasinong.app.utils.StringHelper;

import android.text.TextUtils;

/**
 * @ClassName NetConfig
 * @author linmu
 * @Decription
 */
public class NetConfig {

	public static final String BASE_URL = "http://115.29.111.179/ploughHelper/";

	private static final String KEY_REQUEST = "UHTN90SPOLKIRT6131NM0SEWGLPALczmf";

	private static final String FLAG = "=";

	public static class SubUrl {
		/** 注册 */
		public static final String REGISTER_BY_PASSWORD = "regUser";
		/** 登录 */
		public static final String LOGIN_BY_PASSWORD = "login";
		/** 验证码注册登录 */
		public static final String LOGIN_REGISTER = "authRegLog";
		/** 检测用户是否已注册 */
		public static final String CHECK_USER = "checkUser";
	}

	public static String getRequestUrl(String subUrl) {
		return NetConfig.BASE_URL + subUrl;
	}

	public static class Params {
		public static final String token = "token";
		public static final String stamp = "stamp";
		public static final String sign = "sign";
		public static final String username = "username";
		public static final String password = "password";
		public static final String cellphone = "cellphone";
		public static final String address = "address";
	}

	public static class ResponseCode {
		public static final String OK = "200";
		public static final String CODE_100 = "100";
//		public static final String NG = "NO";
	}

	/**
	 * @param userName
	 * @param password
	 * @param cellPhone
	 * @param address
	 * @return 注册
	 */
	public static Map<String, String> getRegisterParams(String userName, String password, String cellPhone, String address) {
		Map<String, String> paramsMap = getBaseParams(false, getTokenParams(Params.username, userName),
				getTokenParams(Params.password, password), getTokenParams(Params.cellphone, cellPhone),
				getTokenParams(Params.address, address));
		paramsMap.put(Params.username, userName);
		paramsMap.put(Params.password, password);
		paramsMap.put(Params.cellphone, cellPhone);
		paramsMap.put(Params.address, address);
		return paramsMap;
	}

	public static Map<String, String> getRegisterLoginParams(String cellphone) {
		Map<String, String> paramsMap = getBaseParams(false, getTokenParams(Params.cellphone, cellphone));
		paramsMap.put(Params.cellphone, cellphone);
		return paramsMap;
	}
	
	public static Map<String, String> getLoginParams(String userName,String password) {
		Map<String, String> paramsMap = getBaseParams(false, getTokenParams(Params.username, userName)
				, getTokenParams(Params.password, password));
		paramsMap.put(Params.username, userName);
		paramsMap.put(Params.cellphone, userName);
		paramsMap.put(Params.password, password);
		return paramsMap;
	}
	
	public static Map<String, String> getCheckUserParams(String cellPhone) {
		Map<String, String> paramsMap = getBaseParams(false, getTokenParams(Params.cellphone, cellPhone));
		paramsMap.put(Params.cellphone, cellPhone);
		return paramsMap;
	}

	private static String getCheckToken(String stampToken, boolean isNeedAuthToken, String authToken, String... strs) {
		String[] array = null;
		if (isNeedAuthToken) {
			array = new String[strs.length + 2];
			array[strs.length + 1] = authToken;
		} else {
			array = new String[strs.length + 1];
		}
		array[strs.length] = stampToken;
		for (int i = 0; i < strs.length; i++) {
			array[i] = strs[i];
		}
		Arrays.sort(array);
		String checkToken = "";
		for (String s : array) {
			checkToken = checkToken + s.substring(s.indexOf(FLAG) + 1);
		}

		checkToken = checkToken + KEY_REQUEST;
		Logger.d1("checkToken", checkToken);
		checkToken = StringHelper.toMD5(checkToken);
		Logger.d1("checkToken-toMD5", checkToken);
		return checkToken;
	}

	public static Map<String, String> getBaseParams(boolean isNeedAuthToken, String... strs) {

		Map<String, String> paramsMap = new HashMap<String, String>();
		String token = null;
		if (isNeedAuthToken) {
			token = AccountManager.getAuthToken(DsnApplication.getContext());
			paramsMap.put(Params.token, token);
		} else {
			token = null;
		}
		String stamp = System.currentTimeMillis() + "";
		String sign = getCheckToken(getTokenParams(Params.stamp, stamp), isNeedAuthToken, getTokenParams(Params.token, token),
				strs);

		// paramsMap.put(Params.stamp, stamp);
		// paramsMap.put(Params.sign, sign);
		return paramsMap;
	}
	public static Map<String, String> getBaseParams(boolean isNeedAuthToken, Map<String,String> params) {

		String[] strs = new String[params.size()];
		int index = 0;
		for(Map.Entry<String,String> entry: params.entrySet()){
			strs[index] = getTokenParams(entry.getKey(),entry.getValue());
		}

		Map<String, String> paramsMap = new HashMap<String, String>();
		String token = null;
		if (isNeedAuthToken) {
			token = AccountManager.getAuthToken(DsnApplication.getContext());
			paramsMap.put(Params.token, token);
		} else {
			token = null;
		}
		String stamp = System.currentTimeMillis() + "";
		String sign = getCheckToken(getTokenParams(Params.stamp, stamp), isNeedAuthToken, getTokenParams(Params.token, token),
				strs);

		// paramsMap.put(Params.stamp, stamp);
		// paramsMap.put(Params.sign, sign);
		return paramsMap;
	}

	public static Map<String, String> getDefaultParams() {
		Map<String, String> paramsMap = getBaseParams(false);
		return paramsMap;
	}

	public static Map<String, String> getAuthDefaultParams() {
		Map<String, String> paramsMap = getBaseParams(true);
		return paramsMap;
	}

	private static String getTokenParams(String name, String value) {
		return name + FLAG + value;
	}

}
