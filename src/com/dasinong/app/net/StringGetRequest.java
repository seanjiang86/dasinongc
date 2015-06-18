package com.dasinong.app.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.dasinong.app.DsnApplication;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.utils.Logger;

public class StringGetRequest extends Request<String> {
	// private Map<String, String> mMap;
	private Response.Listener<String> mListener;
	public String cookieFromResponse;
	private String mHeader;
	private Map<String, String> sendHeader = new HashMap<String, String>(1);

	public StringGetRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
		super(Request.Method.GET, url, errorListener);
		mListener = listener;
		// mMap = map;
	}

	// 当http请求是post时，则需要该使用该函数设置往里面添加的键值对
	// @Override
	// protected Map<String, String> getParams() throws AuthFailureError {
	// return mMap;
	// }
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {

		// try {
		// return Response.success(new String(response.data, "UTF-8"),
		// HttpHeaderParser.parseCacheHeaders(response));
		// } catch (UnsupportedEncodingException e) {
		// return Response.error(new ParseError(e));
		// } catch (Exception je) {
		// return Response.error(new ParseError(je));
		// }

		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//			 String jsonString =
//			 new String(response.data, "UTF-8");
			mHeader = response.headers.toString();
			Log.w("LOG", "get headers in parseNetworkResponse " + response.headers.toString());
			Pattern pattern = Pattern.compile("Set-Cookie.*?;");
			Matcher m = pattern.matcher(mHeader);
			if (m.find()) {
				cookieFromResponse = m.group();
				Logger.d("LOG", "cookie from server " + cookieFromResponse);
			}
			JSONObject jsonObject = new JSONObject(jsonString);
			if (!TextUtils.isEmpty(cookieFromResponse)) {
				cookieFromResponse = cookieFromResponse.substring(11, cookieFromResponse.length() - 1);
				jsonObject.put("Cookie", cookieFromResponse);
				Logger.e1("Cookie", cookieFromResponse);
				SharedPreferencesHelper.setString(DsnApplication.getContext(), Field.USER_AUTH_TOKEN, cookieFromResponse);
			}
			return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		String cookie = SharedPreferencesHelper.getString(DsnApplication.getContext(), Field.USER_AUTH_TOKEN, "");
		if (!TextUtils.isEmpty(cookie)) {
			sendHeader.put("Cookie", cookie);
		}
		return sendHeader;
	}
}
