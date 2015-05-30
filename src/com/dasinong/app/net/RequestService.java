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

	public void register(Context context,String phone,String stype, Class<? extends BaseEntity> clazz, RequestListener callBack) {
		 Map<String, String> params = NetConfig.getMobileCode(phone, stype);
		 new NetRequest(context).requestPost(params, SubUrl.LOGIN, callBack,clazz);
	}
}
