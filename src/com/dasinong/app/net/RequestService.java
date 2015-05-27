package com.dasinong.app.net;

import com.dasinong.app.entity.BaseEntity;
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

	public void register(Context context, String recommend, String loginPass, String mobile, String mobileCode,
			String checkToken, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		// Map<String, String> params = NetConfig.getMobileCode(phone,
		// stype)(recommend, loginPass, mobile, mobileCode, checkToken);
		// new NetRequest(context).requestPost(params, SubUrl.LOGIN, callBack,
		// clazz);
	}

}
