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
//	userName=testadmin3&password=11111111&cellPhone=12112345678&address=beijing
	public void register(Context context,String userName,String password, String cellPhone,String address,Class<? extends BaseEntity> clazz, RequestListener callBack) {
		 Map<String, String> params = NetConfig.getRegisterParams(userName, password,cellPhone,address);
		 new NetRequest(context).request(params, SubUrl.LOGIN, callBack,clazz);
	}
}
