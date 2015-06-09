package com.dasinong.app.net;

import java.util.Map;

import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetConfig.SubUrl;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.utils.FieldUtils;

import android.content.Context;

public class RequestService {

	private volatile static RequestService mInstance;

	private RequestService() {
	}

	public static RequestService getInstance() {
		if (mInstance == null) {
			synchronized (RequestService.class) {
				if (mInstance == null) {
					mInstance = new RequestService();
				}
			}
		}
		return mInstance;
	}
	
	public void register(Context context,String userName,String password, String cellPhone,String address,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		 Map<String, String> params = NetConfig.getRegisterParams(userName, password,cellPhone,address);
		 new NetRequest(context).get(RequestCode.REGISTER_BY_PASSWORD,params, SubUrl.REGISTER_BY_PASSWORD, callBack,clazz);
	}
	public void authcodeLoginReg(Context context,String cellphone,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getRegisterLoginParams(cellphone);
		new NetRequest(context).get(RequestCode.LOGIN_REGISTER,params, SubUrl.LOGIN_REGISTER, callBack,clazz);
	}
	public void loginByPwd(Context context,String userName,String password,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getLoginParams(userName, password);
		new NetRequest(context).get(RequestCode.LOGIN_BY_PASSWORD,params, SubUrl.LOGIN_BY_PASSWORD, callBack,clazz);
	}
	public void checkUser(Context context,String cellPhone,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getCheckUserParams(cellPhone);
		new NetRequest(context).get(RequestCode.CHECK_USER,params, SubUrl.CHECK_USER, callBack,clazz);
	}
	public void sendNoInLocation(Context context,String latitude,String longitude,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getLocationInfoParams(latitude, longitude);
		new NetRequest(context).get(RequestCode.SEND_NO_IN_LOCATION,params, SubUrl.SEND_NO_IN_LOCATION, callBack,clazz);
	}
	public void sendInLocation(Context context,String latitude,String longitude,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getLocationInfoParams(latitude, longitude);
		new NetRequest(context).get(RequestCode.SEND_IN_LOCATION,params, SubUrl.SEND_IN_LOCATION, callBack,clazz);
	}
	public void getCropList(Context context,String locationId,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetCropListParams(locationId);
		new NetRequest(context).get(RequestCode.GET_CROP_LIST,params, SubUrl.GET_CROP_LIST, callBack,clazz);
	}
	public void getVarietyList(Context context,String cropId, String locationId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetVarietyListParams(cropId,locationId);
		new NetRequest(context).get(RequestCode.GET_VARIETY_LIST,params, SubUrl.GET_VARIETY_LIST, callBack,clazz);
	}
	public void getLocation(Context context,String province,String city,String country,String district, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetLocationParams(province,city,country,district);
		new NetRequest(context).get(RequestCode.GET_LOCATION,params, SubUrl.GET_LOCATION, callBack,clazz);
	}
	public void searchNearUser(Context context,String latitude,String longitude, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getSearchNearUserParams(latitude,longitude);
		new NetRequest(context).get(RequestCode.SEARCH_NEAR_USER,params, SubUrl.SEARCH_NEAR_USER, callBack,clazz);
	}
	public void getSubStage(Context context,String varietyId, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		Map<String, String> params = NetConfig.getGetSubStageParams(varietyId);
		new NetRequest(context).get(RequestCode.GET_SUB_STAGE,params, SubUrl.GET_SUB_STAGE, callBack,clazz);
	}
	
	public void sendRequestWithToken(Context context,Class<? extends BaseEntity> clazz,int requestCode,String url,Object param,RequestListener callBack) {
		Map<String,String> map = FieldUtils.convertToHashMap(param);
		Map<String, String> params = NetConfig.getBaseParams(true, map);
		new NetRequest(context).get(requestCode,params, url, callBack,clazz);
	}

	public void sendRequestWithOutToken(Context context,Class<? extends BaseEntity> clazz,int requestCode,String url,Object param,RequestListener callBack) {
		Map<String,String> map = FieldUtils.convertToHashMap(param);
		Map<String, String> params = NetConfig.getBaseParams(false,map);
		new NetRequest(context).get(requestCode,params, url, callBack,clazz);
	}
}
