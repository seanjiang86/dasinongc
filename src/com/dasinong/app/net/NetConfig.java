package com.dasinong.app.net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.text.TextUtils;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.utils.Logger;
import com.dasinong.app.utils.StringHelper;

/**
 * @ClassName NetConfig
 * @author linmu
 * @Decription
 */
public class NetConfig {

//	public static final String BASE_URL = "http://115.29.111.179/ploughHelper/";
	public static final String BASE_URL = "http://182.254.129.101:8080/ploughHelper/";
	public static final String IMAGE_URL = "http://182.254.129.101:8080/avater/";

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
		/** 发送在田地时的经纬度信息 */
		public static final String SEARCH_LOCATION = "searchLocation";
		/** 获取品种名列表 */
		public static final String GET_VARIETY_LIST = "getVarietyList";
		/*获取首页的的task*/
		public static final String GET_HOME_TASK ="home";
		/**测土的详情页*/
		public static final String GET_SOIL_DETAIL ="";
		/**提交测土信息*/
		public static final String GET_SOIL_POST ="";
		/**获取最后一级的地理信息*/
		public static final String GET_LOCATION ="getLocation";
		/**获取附近用户*/
		public static final String SEARCH_NEAR_USER ="searchNearUser";
		/**获取全部任务 */
		public static final String GET_All_TASK ="getAllTask";
		/**个人信息 */
		public static final String GET_MY_INFO ="loadUserProfile";
		/**上传个人信息 */
		public static final String UPLOAD_MY_INFO ="updateProfile";
		/**提交任务状态 */
		public static final String UPLOAD_MY_TASK ="updateTask";
		/**提交手机验证状态 */
		public static final String UPLOAD_PHONE_AUTH_STATE ="isAuth";
		/**短信订阅 */
		public static final String SMS_SUBSCRIBE ="insertSubScribeList";
		/**短信订阅列表 */
		public static final String GET_SUBSCRIBE_LIST ="getSubScribeLists";
		/**修改密码*/
		public static final String RESET_PWSSWORD ="updatePassword";
		/**刪除短信訂閱*/
		public static final String DELETE_SMS_SUBSCRIBE ="deleteSubScribeList";
		/**更改短信訂閱*/
		public static final String MODIFI_SMS_SUBSCRIBE ="updateSubScribeList";
		/**获取短信订阅详情*/
		public static final String SMS_SUBSCRIBE_DETAIL ="loadSubScribeList";
		/**创建田地*/
		public static final String CREATE_FIELD ="createField";
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
		public static final String userName = "userName";
		public static final String address = "address";
		public static final String latitude = "lat";
		public static final String longitude = "lon";
		public static final String mprovince = "province";
		public static final String mcity = "city";
		public static final String mdistrict = "country";
		public static final String cropName = "cropName";
		public static final String province = "province";
		public static final String city = "city";
		public static final String county = "country";
		public static final String district = "district";
		public static final String locationId = "locationId";
		public static final String varietyId = "varietyId";
		public static final String fieldId = "fieldId";
		public static final String telephone = "telephone";
		public static final String pictureId = "pictureId";
		public static final String taskIds = "taskIds";
		public static final String taskStatuss = "taskStatuss";
		public static final String targetName = "targetName";
		public static final String country = "country";
		public static final String area = "area";
		public static final String cropId = "cropId";
		public static final String isAgriWeather = "isAgriWeather";
		public static final String isNatAlter = "isNatAlter";
		public static final String isRiceHelper = "isRiceHelper";
		public static final String oPassword = "oPassword";
		public static final String nPassword = "nPassword";
		public static final String id = "id";
		public static final String isActive = "isActive";
		public static final String seedingortransplant = "seedingortransplant";
		public static final String startDate = "startDate";
		public static final String currentStageId = "currentStageId";
		public static final String yield = "yield";
		
	}
	
	public static class ResponseCode {
		public static final String OK = "200";
		public static final String CODE_100 = "100";
		// public static final String NG = "NO";
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

	private static String getCheckToken(String stampToken,boolean isNeedAuthToken, String authToken, String... strs) {
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
	
	private static String getSignToken(Map<String,String> paramsMap) {
		StringBuilder builder = null;
		Set<String> keySet = paramsMap.keySet();
        List<String> keyList = new ArrayList<String>();
        for (String key : keySet) {
            keyList.add(key);
        }
        Collections.sort(keyList, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }           
        });
        builder.append(KEY_REQUEST);
        for (String key : keyList) {
        	builder.append(key).append(paramsMap.get(key));
        }
        Logger.d1("checkToken", builder.toString());
		String sign = StringHelper.toMD5(builder.toString());
		Logger.d1("checkToken-toMD5", sign);
		return sign;
	}
	
	public static Map<String, String> getBaseParams(boolean isNeedAuthToken, String... strs) {

		Map<String, String> paramsMap = new HashMap<String, String>();
		String token = null;
		if (isNeedAuthToken) {
			token = AccountManager.getAuthToken(DsnApplication.getContext());
//			paramsMap.put(Params.token, token);
		} else {
			token = null;
		}
		String stamp = System.currentTimeMillis() + "";
		String sign = getCheckToken(getTokenParams(Params.stamp, stamp), isNeedAuthToken, getTokenParams(Params.token, token), strs);

		// paramsMap.put(Params.stamp, stamp);
		// paramsMap.put(Params.sign, sign);
		return paramsMap;
	}
	public static Map<String, String> getBaseParams(boolean isNeedAuthToken, Map<String,String> paramsMap) {

		
		String token = null;
		if (isNeedAuthToken) {
			token = AccountManager.getAuthToken(DsnApplication.getContext());
			paramsMap.put(Params.token, token);
		} else {
		}
		
//		String stamp = System.currentTimeMillis() + "";
//		paramsMap.put(Params.stamp, stamp);
//		String sign = getCheckToken(strs);
//		paramsMap.put(Params.sign, getSignToken(paramsMap));
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

	public static Map<String, String> getSearchLocationParams(String latitude, String longitude,String mprovince,String mcity,String mdistrict) {
		Map<String, String> paramsMap = getBaseParams(false, getTokenParams(Params.latitude, latitude), getTokenParams(Params.longitude, longitude), getTokenParams(Params.mprovince, mprovince), getTokenParams(Params.mcity, mcity), getTokenParams(Params.mdistrict, mdistrict));
		paramsMap.put(Params.latitude, latitude);
		paramsMap.put(Params.longitude, longitude);
		paramsMap.put(Params.mprovince, mprovince);
		paramsMap.put(Params.mcity, mcity);
		paramsMap.put(Params.mdistrict, mdistrict);
		return paramsMap;
	}
	public static Map<String, String> getGetVarietyListParams(String cropName,String locationId) {
		Map<String, String> paramsMap = getBaseParams(false,getTokenParams(Params.cropName , cropName),getTokenParams(Params.locationId , locationId));
		paramsMap.put(Params.cropName, cropName);
		paramsMap.put(Params.locationId, locationId);
		return paramsMap;
	}
	public static Map<String, String> getGetLocationParams(String province,String city,String county,String district) {
		Map<String, String> paramsMap = getBaseParams(false, getTokenParams(Params.province, province), getTokenParams(Params.city, city),getTokenParams(Params.county, county),getTokenParams(Params.district, district));
		paramsMap.put(Params.province, province);
		paramsMap.put(Params.city, city);
		paramsMap.put(Params.county, county);
		paramsMap.put(Params.district, district);
		return paramsMap;
	}
	public static Map<String, String> getSearchNearUserParams(String latitude, String longitude) {
		Map<String, String> paramsMap = getBaseParams(false, getTokenParams(Params.latitude, latitude), getTokenParams(Params.longitude, longitude));
		paramsMap.put(Params.latitude, latitude);
		paramsMap.put(Params.longitude, longitude);
		return paramsMap;
	}
	public static Map<String, String> getAllTaskParams(String fieldId) {
		Map<String, String> paramsMap = getBaseParams(false, getTokenParams(Params.fieldId, fieldId));
		paramsMap.put(Params.fieldId, fieldId);
		return paramsMap;
	}
	public static Map<String, String> getUploadInfoParams(String userName,
			String cellphone,String password,String address,String telephone) {
		Map<String, String> paramsMap = getBaseParams(false, 
				getTokenParams(Params.username, userName),
				getTokenParams(Params.cellphone, cellphone),
				getTokenParams(Params.address, address),
				getTokenParams(Params.password, password),
				getTokenParams(Params.telephone, telephone)
				);
		paramsMap.put(Params.username, userName);
		paramsMap.put(Params.cellphone, cellphone);
		paramsMap.put(Params.password, password);
		paramsMap.put(Params.address, address);
		paramsMap.put(Params.telephone, telephone);
		return paramsMap;
	}
	
	public static Map<String, String> getUploadTaskParams(String fieldId,String taskIds,String taskStatuss) {
		Map<String, String> paramsMap = getBaseParams(false, 
				getTokenParams(Params.fieldId, fieldId),
				getTokenParams(Params.taskIds, taskIds),
				getTokenParams(Params.taskStatuss, taskStatuss)
				);
		paramsMap.put(Params.fieldId, fieldId);
		paramsMap.put(Params.taskIds, taskIds);
		paramsMap.put(Params.taskStatuss, taskStatuss);
		return paramsMap;
	}
	
	public static Map<String, String> getSmsSubParams(String id,
			String targetName,String cellphone,String province
			,String city,String country,String district
			,String area,String cropId,Boolean isAgriWeather
			,Boolean isNatAlter,Boolean isRiceHelper) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		if(!TextUtils.isEmpty(id)){
			paramsMap.put(Params.id, id);
		}
		paramsMap.put(Params.targetName, targetName);
		paramsMap.put(Params.cellphone, cellphone);
		paramsMap.put(Params.province, province);
		paramsMap.put(Params.city, city);
		paramsMap.put(Params.country, country);
		paramsMap.put(Params.district, district);
		paramsMap.put(Params.area, area);
		paramsMap.put(Params.cropId, cropId);
		paramsMap.put(Params.isAgriWeather, String.valueOf(isAgriWeather));
		paramsMap.put(Params.isNatAlter, String.valueOf(isNatAlter));
		paramsMap.put(Params.isRiceHelper, String.valueOf(isRiceHelper));
		return getBaseParams(false, paramsMap);
	}
	
	public static Map<String, String> getResetPwdParams(String oPassword,String nPassword) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.oPassword, oPassword);
		paramsMap.put(Params.nPassword, nPassword);
		return getBaseParams(false, paramsMap);
	}
	
	public static Map<String, String> getDeleteSmsSubParams(String id) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.id, id);
		return getBaseParams(false, paramsMap);
	}
	
	public static Map<String, String> getCreateFieldParams(String isActive,String seedingortransplant,String area,String startDate,String locationId,String varietyId,String currentStageId,String yield) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put(Params.isActive, isActive);
		paramsMap.put(Params.seedingortransplant, seedingortransplant);
		paramsMap.put(Params.area, area);
		paramsMap.put(Params.startDate, startDate);
		paramsMap.put(Params.locationId, locationId);
		paramsMap.put(Params.varietyId, varietyId);
		paramsMap.put(Params.currentStageId, currentStageId);
		paramsMap.put(Params.yield, yield);
		return getBaseParams(false, paramsMap);
	}
}

