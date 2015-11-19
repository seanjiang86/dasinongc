package com.dasinong.app.components.net;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.dasinong.app.BuildConfig;
import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.components.domain.BaseResponse;
import com.dasinong.app.components.domain.WeatherEntity;
import com.dasinong.app.components.net.NetError.NetErrorStatus;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.net.NetConfig.Params;
import com.dasinong.app.ui.RegisterPhoneActivity;
import com.dasinong.app.ui.fragment.HomeFragment;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.utils.AppInfoUtils;
import com.dasinong.app.utils.DeviceHelper;
import com.dasinong.app.utils.FieldUtils;
import com.dasinong.app.utils.StringHelper;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuningning on 15/6/13.
 */
public class VolleyManager {

	private static final String TAG = "VolleyManager";
	private volatile static VolleyManager instance;
	private RequestQueue mRequestQueue;

	private Context mContext;
	private volatile Boolean isInit;

	private VolleyManager() {
		isInit = false;
	}

	public static VolleyManager getInstance() {

		if (instance == null) {
			synchronized (VolleyManager.class) {
				if (instance == null) {
					instance = new VolleyManager();
				}
			}
		}

		return instance;
	}

	public void init(Context context) {

		if (isInit) {
			return;
		} else {

			synchronized (isInit) {
				if (!isInit) {
					mContext = context.getApplicationContext();
					mRequestQueue = createRequestQueue();

				}
			}

		}
	}

	public <T extends BaseResponse> Request addGetRequestWithNoCache(final int requestCode, String url, Object param, final Class<T> clazz,
			final INetRequest netRequest) {

		return addGetRequest(false, requestCode, url, param, clazz, netRequest);

	}

	public <T extends BaseResponse> Request addGetRequestWithCache(final int requestCode, String url, Object param, final Class<T> clazz,
			final INetRequest netRequest) {

		return addGetRequest(true, requestCode, url, param, clazz, netRequest);

	}

	public <T extends BaseResponse> Request addPostRequest(int requestCode, String url, Object param, Class<? extends BaseResponse> clazz,
			final INetRequest netReqeust) {
		final WeakReference<INetRequest> weakReference = new WeakReference<INetRequest>(netReqeust);
		final Response.Listener<T> successListener = createSuccessListener(requestCode, weakReference);

		final Response.ErrorListener errorListener = createErrorListener(requestCode, weakReference);
		HashMap<String, String> map = FieldUtils.convertToHashMap(param);

		String product = android.os.Build.PRODUCT;
		String deviceId = DeviceHelper.getDeviceId(mContext);
		String accessToken = AccountManager.getAuthToken(mContext);

		int version = AppInfoUtils.getVersionCode(DsnApplication.getContext());

		if (version <= 0) {
			version = 1;
		}

		map.put(Params.token, accessToken);
		map.put(Params.deviceType, product);
		map.put(Params.deviceId, deviceId);
		map.put(Params.appId, DsnApplication.APP_ID);
		map.put(Params.version, String.valueOf(version));

		DEBUG(map.toString());
		final GsonRequest<T> request = new GsonRequest(url, map, clazz, successListener, errorListener);

		request.setShouldCache(false);

		mRequestQueue.add(request);
		return request;

	}

	private RequestQueue createRequestQueue() {
		File cacheDir = new File(mContext.getCacheDir(), "network");

		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}

		String userAgent = "";

		try {
			String network = mContext.getPackageName();
			PackageInfo queue = mContext.getPackageManager().getPackageInfo(network, 0);
			userAgent = network + "/" + queue.versionCode;
		} catch (PackageManager.NameNotFoundException var6) {

		}
		HttpStack stack;
		if (Build.VERSION.SDK_INT >= 9) {
			stack = new HurlStack();
		} else {
			stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
		}

		BasicNetwork network = new BasicNetwork(stack);
		RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network);

		queue.start();
		return queue;

	}

	private <T extends BaseResponse> Request addGetRequest(boolean needCache, final int requestCode, String url, Object param, final Class<T> clazz,
			final INetRequest netReqeust) {
		final String finalUrl = createUrl(url, param);

		final WeakReference<INetRequest> weakReference = new WeakReference<INetRequest>(netReqeust);
		final Response.Listener<T> successListener = createSuccessListener(requestCode, weakReference);

		final Response.ErrorListener errorListener = createErrorListener(requestCode, weakReference);

		final GsonRequest<T> request = new GsonRequest(finalUrl, clazz, successListener, errorListener);

		request.setShouldCache(needCache);
		mRequestQueue.add(request);

		return request;

	}

	private String buildUrl(String url, Object param) {

		StringBuilder urlWithParm = new StringBuilder(url);
		if (param != null) {
			String positionParam = encodeParameters(FieldUtils.convertToHashMap(param));

			urlWithParm.append(positionParam);
		}
		return urlWithParm.toString();
	}

	private String encodeParameters(Map<String, String> params) {
		StringBuilder encodedParams = new StringBuilder("?");

		String deviceType = android.os.Build.PRODUCT;
		String deviceId = DeviceHelper.getDeviceId(mContext);
		String accessToken = AccountManager.getAuthToken(mContext);
		int version = AppInfoUtils.getVersionCode(mContext);

		params.put(Params.token, accessToken);
		params.put(Params.deviceId, deviceId);
		params.put(Params.deviceType, deviceType);
		params.put(Params.appId, DsnApplication.APP_ID);
		params.put(Params.version, String.valueOf(version));

		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				encodedParams.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
				encodedParams.append('=');
				encodedParams.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				encodedParams.append('&');
				
				
				// TODO MING : 替换构造链接方式
//				Uri.Builder builder = new Uri.Builder();
//				builder.appendQueryParameter(entry.getKey(), entry.getValue());
			}

			return encodedParams.toString();
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException("Encoding not supported: " + "UTF-8", uee);
		}
	}

	private String createUrl(String url, Object param) {
		if (param != null) {
			url = buildUrl(url, param);
		}

		return url;
	}

	private Response.ErrorListener createErrorListener(final int requestcode, final WeakReference<INetRequest> weakReference) {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				INetRequest tem = weakReference.get();

				if (error != null &&  error.networkResponse != null &&  (error.networkResponse.statusCode == 401 || error.networkResponse.statusCode == 403) && requestcode == 130) {
					Toast.makeText(mContext, mContext.getResources().getString(R.string.longin_info_outtime), Toast.LENGTH_SHORT).show();
					AccountManager.logout(mContext);
					Intent intent = new Intent(mContext, RegisterPhoneActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
					NetError netError = new NetError(NetError.TOKENOUTTIME_ERROR);
					tem.onTaskFailedSuccess(requestcode, netError);
					return;
				}

				if (tem != null) {
					NetError netError;

					if (error instanceof TimeoutError) {
						netError = new NetError(NetError.NET_TIMEOUT);

					} else if (error instanceof ParseError) {
						netError = new NetError(NetError.PARSE_ERROR);

					} else if (error instanceof AuthFailureError) {
						netError = new NetError(NetError.NET_AUTHFAILURE);

					} else if (error instanceof TimeoutError) {
						netError = new NetError(NetError.NET_TIMEOUT);

					} else if (error instanceof NetworkError) {
						netError = new NetError(NetError.NET_ERROR);

					} else if (error instanceof ServerError) {
						netError = new NetError(NetError.SERVER_ERROR);

					} else {
						netError = new NetError(NetError.UNKNOW_ERROR);
					}

					tem.onTaskFailedSuccess(requestcode, netError);
				}

			}
		};
	}

	private <T extends BaseResponse> Response.Listener<T> createSuccessListener(final int requestCode, final WeakReference<INetRequest> weakReference) {
		return new Response.Listener<T>() {
			@Override
			public void onResponse(T response) {

				INetRequest tem = weakReference.get();
				if (tem != null) {
					tem.onTaskSuccess(requestCode, response);
				}
			}

		};
	}

	private Cache.Entry getCache(Request request) {

		if (request != null && !TextUtils.isEmpty(request.getUrl())) {
			return mRequestQueue.getCache().get(request.getUrl());
		}

		return null;

	}

	public <T> T getCacheDomain(Request request, Class<T> clazz) {

		Cache.Entry entry = getCache(request);

		if (entry != null) {

			String result = new String(entry.data).trim();

			try {

				JsonReader reader = new JsonReader(new StringReader(result));
				reader.setLenient(true);

				return new Gson().fromJson(reader, clazz);
			} catch (Exception e) {
				DEBUG("cache json parse" + request.getUrl());
				e.printStackTrace();

			}

		}
		return null;
	}

	private void DEBUG(String msg) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, msg);
		}
	}
}
