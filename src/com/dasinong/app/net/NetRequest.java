package com.dasinong.app.net;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Request.Priority;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetConfig.Params;
import com.dasinong.app.ui.RegisterPhoneActivity;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.utils.DeviceHelper;
import com.dasinong.app.utils.Logger;
import com.dasinong.app.utils.Logger.LogTag;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.http.client.entity.FileUploadEntity;

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

	public <T> void get(int requestCode, Map<String, String> map, String subUrl, final RequestListener callback,
			final Class<? extends BaseEntity> clazz) {

		if (!DeviceHelper.checkNetWork(DsnApplication.getContext())) {
			callback.onFailed(requestCode, new Exception(), "无网络连接");
			return;
		}

		String url = NetConfig.getRequestUrl(subUrl);
		if (map != null && !map.isEmpty()) {
			checkNull(map);
			url = url + encodeParameters(map);
		}

		Logger.d(LogTag.HTTP, url);

		get(requestCode, url, clazz, callback, Priority.NORMAL, new DefaultRetryPolicy(10 * 1000, 0, 1));
	}

	private <T> void get(final int requestCode, final String url, final Class<? extends BaseEntity> clazz, final RequestListener callback,
			final Priority priority, RetryPolicy retryPolicy) {
		StringGetRequest req = new StringGetRequest(url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				try {
					Logger.d(LogTag.HTTP, response);
					BaseEntity result = new Gson().fromJson(response, clazz);
					if (result == null) {
						callback.onFailed(requestCode, new NullPointerException(), "data:" + response);
						return;
					}

					if (result.isAuthTokenInvalid()) {
					    Toast.makeText(DsnApplication.getContext(), "登录过期,请您重新登录!", 0).show();
						 Intent intent = new Intent(context,RegisterPhoneActivity.class);
						 // intent.putExtra(AccountManager.CHECK_LOGIN,true);
						 context.startActivity(intent);
						AccountManager.logout(context);
					}

					callback.onSuccess(requestCode, result);
				} catch (Exception e) {
					Toast.makeText(context, context.getResources().getString(R.string.please_check_netword), Toast.LENGTH_SHORT).show();
					callback.onFailed(requestCode, e, e.getClass().toString());
				}

			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Logger.e(LogTag.HTTP, Log.getStackTraceString(error));
				error.printStackTrace();
				Intent intent = null ;
				if(error != null && error.networkResponse != null){
					switch (error.networkResponse.statusCode) {
					case 401:
						AccountManager.logout(context);
						Toast.makeText(context, context.getResources().getString(R.string.longin_info_outtime), Toast.LENGTH_LONG).show();
						intent = new Intent(context ,RegisterPhoneActivity.class);
						context.startActivity(intent);
						break;
					case 403:
						AccountManager.logout(context);
						Toast.makeText(context, context.getResources().getString(R.string.longin_info_outtime), Toast.LENGTH_LONG).show();
						intent = new Intent(context ,RegisterPhoneActivity.class);
						context.startActivity(intent);
						break;
					default:
						Toast.makeText(context, context.getResources().getString(R.string.please_check_netword), Toast.LENGTH_SHORT).show();
						break;
					}
				} else {
					Toast.makeText(context, context.getResources().getString(R.string.please_check_netword), Toast.LENGTH_SHORT).show();
				}
				callback.onFailed(requestCode, error, error.getMessage());
			}
		}) {

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

	public <T> void requestPost(int requestCode, Map<String, String> map, String subUrl, final RequestListener callback,
			final Class<? extends BaseEntity> clazz) {

		if (!DeviceHelper.checkNetWork(DsnApplication.getContext())) {
			callback.onFailed(requestCode, new Exception(), "无网络连接");
			return;
		}

		String url = NetConfig.getRequestUrl(subUrl);

		Logger.d(LogTag.HTTP, url);

		post(requestCode, url, clazz, map, callback);
	}

	private <T> void post(int requestCode, String url, final Class<? extends BaseEntity> clazz, final Map<String, String> map,
			final RequestListener callback) {
		post(requestCode, url, clazz, map, callback, Priority.NORMAL, new DefaultRetryPolicy(10 * 1000, 0, 1));
	}

	private <T> void post(final int requestCode, final String url, final Class<? extends BaseEntity> clazz, final Map<String, String> map,
			final RequestListener callback, final Priority priority, RetryPolicy retryPolicy) {
		StringPostRequest req = new StringPostRequest(url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				try {
					Logger.d(LogTag.HTTP, response);

					BaseEntity result = new Gson().fromJson(response, clazz);
					if (result == null) {
						callback.onFailed(requestCode, new NullPointerException(), "data:" + response);
						return;
					}

					if (result.isAuthTokenInvalid()) {
					    Toast.makeText(DsnApplication.getContext(), "登录过期,请您重新登录!", 0).show();
						 Intent intent = new Intent(context,RegisterPhoneActivity.class);
						 // intent.putExtra(AccountManager.CHECK_LOGIN,true);
						 context.startActivity(intent);
						AccountManager.logout(context);
					}

					callback.onSuccess(requestCode, result);
				} catch (Exception e) {
					callback.onFailed(requestCode, e, e.getClass().toString());
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Logger.e(LogTag.HTTP, Log.getStackTraceString(error));
				callback.onFailed(requestCode, error, error.getMessage());
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				checkNull(map);
				return map;
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

	public <T> void upload(final int requestCode, String url, String filePath, final Class<? extends BaseEntity> clazz, final RequestListener callback) {
		RequestParams params = new RequestParams();

		Logger.e(LogTag.HTTP, "uploadPath:" + filePath + "\r\n" + "url:" + url);
		
		String token = AccountManager.getAuthToken(context);

		// params.setHeader(new Header);
//		String cookie = SharedPreferencesHelper.getString(DsnApplication.getContext(), Field.USER_AUTH_TOKEN, "");
//		Logger.e(LogTag.HTTP, "--" + cookie);
//		if (!TextUtils.isEmpty(cookie)) {
//			params.addHeader("Cookie", cookie);
//		}
		// params.addQueryStringParameter("name", "value");

		// 只包含字符串参数时默认使用BodyParamsEntity，
		// 类似于UrlEncodedFormEntity（"application/x-www-form-urlencoded"）。
		// params.addBodyParameter("name", "value");

		// 加入文件参数后默认使用MultipartEntity（"multipart/form-data"），
		// 如需"multipart/related"，xUtils中提供的MultipartEntity支持设置subType为"related"。
		// 使用params.setBodyEntity(httpEntity)可设置更多类型的HttpEntity（如：
		// MultipartEntity,BodyParamsEntity,FileUploadEntity,InputStreamUploadEntity,StringEntity）。
		// 例如发送json参数：params.setBodyEntity(new StringEntity(jsonStr,charset));
		params.addBodyParameter("file", new File(filePath));
		params.addBodyParameter(Params.token , token);
		// FileUploadEntity entity = new FileUploadEntity(file, contentType)
		// params.setBodyEntity(bodyEntity)
		HttpUtils http = new HttpUtils();
		// http.configCookieStore(cookieStore)
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				Logger.d(LogTag.HTTP, "upload  onStart");
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				// Toast.makeText(DsnApplication.getContext(), "onLoading",
				// 0).show();
				Logger.d(LogTag.HTTP, "total:" + total + "--current:" + current + "--isUploading:" + isUploading);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Logger.d(LogTag.HTTP, "total:" + responseInfo.result);
				String response = responseInfo.result;
				try {
					Logger.d(LogTag.HTTP, response);

					BaseEntity result = new Gson().fromJson(response, clazz);
					if (result == null) {
						callback.onFailed(requestCode, new NullPointerException(), "data:" + response);
						return;
					}

					if (result.isAuthTokenInvalid()) {
						// Intent intent = new
						// Intent(context,LoginActivity.class);
						// // intent.putExtra(AccountManager.CHECK_LOGIN,true);
						// context.startActivity(intent);
						AccountManager.logout(context);
					}

					callback.onSuccess(requestCode, result);
				} catch (Exception e) {
					callback.onFailed(requestCode, e, e.getClass().toString());
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Logger.d(LogTag.HTTP, "error:" + Log.getStackTraceString(error) + "--msg:" + msg);
				callback.onFailed(requestCode, error, msg);
			}
		});
	}
	
	public <T> void uploadImages(final int requestCode, String url, List<String> paths,String cropName, String disasterType,String disasterName,String affectedArea,String eruptionTime, String disasterDist,String fieldOperations,String fieldId , final Class<? extends BaseEntity> clazz,
			final RequestListener callback) {
		RequestParams params = new RequestParams();

		Logger.e(LogTag.HTTP, "uploadPath:" + paths + "\r\n" + "url:" + url);

//		String cookie = SharedPreferencesHelper.getString(DsnApplication.getContext(), Field.USER_AUTH_TOKEN, "");
//		Logger.e(LogTag.HTTP, "--" + cookie);
//		if (!TextUtils.isEmpty(cookie)) {
//			params.addHeader("Cookie", cookie);
//		}
		
		String token = AccountManager.getAuthToken(context);

		for (int i = 0; i < paths.size(); i++) {
			params.addBodyParameter("file" + i, new File(paths.get(i)));
		}
		params.addBodyParameter("cropName",cropName);
		params.addBodyParameter("disasterType",disasterType);
		params.addBodyParameter("disasterName",disasterName);
		params.addBodyParameter("affectedArea",affectedArea);
		params.addBodyParameter("eruptionTime",eruptionTime);
		params.addBodyParameter("disasterDist",disasterDist);
		params.addBodyParameter("fieldOperations",fieldOperations);
		params.addBodyParameter("fieldId",fieldId);
		params.addBodyParameter(Params.token , token);
		
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				Logger.d(LogTag.HTTP, "upload  onStart");
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				Logger.d(LogTag.HTTP, "total:" + total + "--current:" + current + "--isUploading:" + isUploading);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Logger.d(LogTag.HTTP, "total:" + responseInfo.result);
				String response = responseInfo.result;
				try {
					Logger.d(LogTag.HTTP, response);

					BaseEntity result = new Gson().fromJson(response, clazz);
					if (result == null) {
						callback.onFailed(requestCode, new NullPointerException(), "data:" + response);
						return;
					}

					if (result.isAuthTokenInvalid()) {
						AccountManager.logout(context);
					}

					callback.onSuccess(requestCode, result);
				} catch (Exception e) {
					callback.onFailed(requestCode, e, e.getClass().toString());
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Logger.d(LogTag.HTTP, "error:" + Log.getStackTraceString(error) + "--msg:" + msg);
				callback.onFailed(requestCode, error, msg);
			}
		});
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
		void onSuccess(int requestCode, BaseEntity resultData);

		void onFailed(int requestCode, Exception error, String msg);
	}

	public <T> void getWXAccessToken(final int requestCode, String url,String appid, String secret,String code, final Class<? extends BaseEntity> clazz, final RequestListener callback) {
		HttpUtils http = new HttpUtils();
		
		http.send(HttpMethod.GET, url+ "appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code",  new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				Logger.d(LogTag.HTTP, "error:" + Log.getStackTraceString(error) + "--msg:" + msg);
				callback.onFailed(requestCode, error, msg);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Logger.d(LogTag.HTTP, "total:" + responseInfo.result);
				String response = responseInfo.result;
				try {
					Logger.d(LogTag.HTTP, response);

					BaseEntity result = new Gson().fromJson(response, clazz);
					if (result == null) {
						callback.onFailed(requestCode, new NullPointerException(), "data:" + response);
						return;
					}
					callback.onSuccess(requestCode, result);
				} catch (Exception e) {
					callback.onFailed(requestCode, e, e.getClass().toString());
				}
				
			}
		});
	}
	
	public <T> void getWXUserInfo(final int requestCode, String url,String access_token, String openid, final Class<? extends BaseEntity> clazz, final RequestListener callback) {
		HttpUtils http = new HttpUtils();
		
		http.send(HttpMethod.GET, url+"access_token=" + access_token + "&openid=" + openid, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				Logger.d(LogTag.HTTP, "error:" + Log.getStackTraceString(error) + "--msg:" + msg);
				callback.onFailed(requestCode, error, msg);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Logger.d(LogTag.HTTP, "total:" + responseInfo.result);
				String response = responseInfo.result;
				try {
					Logger.d(LogTag.HTTP, response);

					BaseEntity result = new Gson().fromJson(response, clazz);
					if (result == null) {
						callback.onFailed(requestCode, new NullPointerException(), "data:" + response);
						return;
					}

					if (result.isAuthTokenInvalid()) {
						AccountManager.logout(context);
					}

					callback.onSuccess(requestCode, result);
				} catch (Exception e) {
					callback.onFailed(requestCode, e, e.getClass().toString());
				}
				
			}
		});
	}

}
