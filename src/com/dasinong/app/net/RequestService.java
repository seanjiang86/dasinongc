package com.dasinong.app.net;

import java.util.Map;

import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetConfig.SubUrl;
import com.dasinong.app.net.NetRequest.RequestListener;

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
}
