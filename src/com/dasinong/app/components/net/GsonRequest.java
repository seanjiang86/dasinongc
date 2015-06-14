package com.dasinong.app.components.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.dasinong.app.DsnApplication;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by liuningning on 15/6/13.
 */
public class GsonRequest<T> extends Request<T> {
    private Class<T> mClassName ;
    private Response.Listener<T> mListener;
    private Gson mGson;

    private Map<String,String > param;
    public  GsonRequest(String url,Class<T> clazz,Response.Listener<T> successListener,Response.ErrorListener errorListener){
        super(Method.GET, url, errorListener);

        this.mGson = new Gson();
        this.mClassName = clazz;
        this.mListener = successListener;

    }

    public  GsonRequest(String url,Map<String,String>param,Class<T> clazz,Response.Listener<T> successListener,Response.ErrorListener errorListener){
        super(Method.POST, url, errorListener);
        this.param = param;
        this.mGson = new Gson();
        this.mClassName = clazz;
        this.mListener = successListener;

    }
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(jsonString, mClassName),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }

    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);

    }



    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return param ;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        String cookie = SharedPreferencesHelper.getString(DsnApplication.getContext(), SharedPreferencesHelper.Field.USER_AUTH_TOKEN, "");
        HashMap<String, String> map = new HashMap<>();
        map.put("Cookie", cookie);
        return map;
    }


}
