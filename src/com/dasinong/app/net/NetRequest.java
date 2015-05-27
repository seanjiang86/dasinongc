package com.dasinong.app.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Request.Priority;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dasinong.app.DsnApplication;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.utils.DeviceHelper;
import com.dasinong.app.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * @ClassName: NetRequestManager
 * @Description: 网络请求
 * @author Ysl
 */
public class NetRequest {

	private static final String tag = "NetRequest";
	private Context context;

	public NetRequest(Context context) {
		this.context = context;
	}

	private static Map<String, String> checkNull(Map<String, String> map) {
		if (map != null && map.size() >= 0) {
			Set<String> set = map.keySet();
			LinkedList<String> list = new LinkedList<String>();
			for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				String value = map.get(key);
				if (value == null || value.equals("null")) {
					list.add(key);
				}
			}
			for (String key : list) {
				map.remove(key);
			}
		}
		return map;
	}

	private static String encodeParameters(Map<String, String> params) {
		StringBuilder encodedParams = new StringBuilder("?");
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				encodedParams.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
				encodedParams.append('=');
				encodedParams.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				encodedParams.append('&');
			}
			return encodedParams.toString();
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException("Encoding not supported: " + "UTF-8", uee);
		}
	}

	public interface RequestListener {
		void onSuccess(BaseEntity resultData);

		void onFailed(Exception error, String msg);
	}

	// -----------------------

	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	public static final int ERROR_PASE_JSON = -1;
	public static final int ERROR_NO_NET = -2;
	public static final int ERROR_TIME_OUT = -3;

	public <T> void request(Map<String, String> map, String subUrl, final RequestListener callback,
			final Class<? extends BaseEntity> clazz) {

		if (!DeviceHelper.checkNetWork(DsnApplication.getContext())) {
			callback.onFailed(new Exception(), "无网络连接");
			return;
		}

		String url = NetConfig.getRequestUrl(subUrl);
		if (map != null) {
			checkNull(map);
			url = url + encodeParameters(map);
		}

		Logger.d1("NetRequest", url);

		get(url, clazz, null, callback, Priority.NORMAL, new DefaultRetryPolicy(10 * 1000, 0, 1));
	}

	public <T> void get(String url, final Class<? extends BaseEntity> clazz, final Map<String, String> header,
	/* final Map<String, String> map, */final RequestListener callback) {
		get(url, clazz, header, callback, Priority.NORMAL, new DefaultRetryPolicy(10 * 1000, 0, 1));
	}

	public <T> void get(String url, final Class<? extends BaseEntity> clazz, final Map<String, String> header,
	/* final Map<String, String> map, */final RequestListener callback, RetryPolicy retryPolicy) {
		get(url, clazz, header, callback, Priority.NORMAL, retryPolicy);
	}

	private <T> void get(String url, final Class<? extends BaseEntity> clazz, final Map<String, String> header,
			final RequestListener callback, final Priority priority, RetryPolicy retryPolicy) {
		StringRequest req = new StringRequest(Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				Logger.d1(tag, response);
				try {
					BaseEntity result = new Gson().fromJson(response, clazz);
					if(result == null){
						callback.onFailed(new NullPointerException(), "data:"+response);
						return;
					}
					
					
					//TODO token 失效  login
//					if ("1007".equals(result.getRespNo())) {
//						Intent intent = new Intent(context, LoginActivity.class);
//						// intent.putExtra(AccountManager.CHECK_LOGIN, true);
//						context.startActivity(intent);
//						AccountManager.logout(context);
//					}
					
					
					
					callback.onSuccess(result);
				} catch (Exception e) {
					callback.onFailed(e, e.getClass().toString());
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Logger.e1(tag, error);
				callback.onFailed(error, "");
			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				if (header != null) {
					checkNull(header);
					return header;
				} else {
					return Collections.emptyMap();
				}
			}

			@Override
			public Priority getPriority() {
				if (priority == Priority.LOW) {
					return Priority.LOW;
				} else if (priority == Priority.NORMAL) {
					return Priority.NORMAL;
				} else if (priority == Priority.IMMEDIATE) {
					return Priority.IMMEDIATE;
				} else if (priority == Priority.HIGH) {
					return Priority.HIGH;
				}
				return Priority.NORMAL;
			}
		};
		if (retryPolicy != null) {
			req.setRetryPolicy(retryPolicy);
		}
		Volley.newRequestQueue(context).add(req);
	}
	
	public <T> void requestPost(Map<String, String> map, String subUrl, final RequestListener callback,
			final Class<? extends BaseEntity> clazz) {

		if (!DeviceHelper.checkNetWork(DsnApplication.getContext())) {
			callback.onFailed(new Exception(), "无网络连接");
			return;
		}

		String url = NetConfig.getRequestUrl(subUrl);
//		if (map != null) {
//			checkNull(map);
//			url = url + encodeParameters(map);
//		}

		Logger.d1("NetRequest", url);

		post(url, clazz, null,map, callback);
	}
	
	
    private <T> void post(String url, final Class<? extends BaseEntity> clazz, final Map<String, String> header,
            final Map<String, String> map, final RequestListener callback) {
    	
    	if (!DeviceHelper.checkNetWork(DsnApplication.getContext())) {
			callback.onFailed(new Exception(), "无网络连接");
			return;
		}
    	
        post(url, clazz, header, map, callback, Priority.NORMAL, new DefaultRetryPolicy(10*1000, 0, 1));
    }

    private <T> void post(final String url, final Class<? extends BaseEntity> clazz, final Map<String, String> header,
            final Map<String, String> map, final RequestListener callback, final Priority priority,
 RetryPolicy retryPolicy) {
		StringRequest req = new StringRequest(Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				Logger.d1(tag, response);

				try {
					BaseEntity result = new Gson().fromJson(response, clazz);
					if (result == null) {
						callback.onFailed(new NullPointerException(), "data:" + response);
						return;
					}

					// TODO token 失效 login

					// if ("1007".equals(result.getRespNo())) {
					// Intent intent = new Intent(context, LoginActivity.class);
					// // intent.putExtra(AccountManager.CHECK_LOGIN, true);
					// context.startActivity(intent);
					// AccountManager.logout(context);
					// }

					callback.onSuccess(result);
				} catch (Exception e) {
					callback.onFailed(e, e.getClass().toString());
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Logger.e1(tag, error);
				callback.onFailed(error, "");
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				checkNull(map);
				return map;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				if (header != null) {
					checkNull(header);
					return header;
				} else {
					return Collections.emptyMap();
				}
			}

			@Override
			public Priority getPriority() {
				if (priority == Priority.LOW) {
					return Priority.LOW;
				} else if (priority == Priority.NORMAL) {
					return Priority.NORMAL;
				} else if (priority == Priority.IMMEDIATE) {
					return Priority.IMMEDIATE;
				} else if (priority == Priority.HIGH) {
					return Priority.HIGH;
				}
				return Priority.NORMAL;
			}
		};
		if (retryPolicy != null) {
			req.setRetryPolicy(retryPolicy);
		}
		Volley.newRequestQueue(context).add(req);
	}

}
