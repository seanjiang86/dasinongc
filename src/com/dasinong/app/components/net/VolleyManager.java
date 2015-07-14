package com.dasinong.app.components.net;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

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
import com.dasinong.app.components.domain.BaseResponse;
import com.dasinong.app.components.domain.WeatherEntity;
import com.dasinong.app.utils.DeviceHelper;
import com.dasinong.app.utils.FieldUtils;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


import java.io.File;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
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

    public <T extends BaseResponse> Request addGetRequestWithNoCache(final int requestCode, String url, Object param, final Class<T> clazz, final INetRequest netRequest) {

        return addGetRequest(false, requestCode, url, param, clazz, netRequest);

    }

    public <T extends BaseResponse> Request addGetRequestWithCache(final int requestCode, String url, Object param, final Class<T> clazz, final INetRequest netRequest) {

        return addGetRequest(true, requestCode, url, param, clazz, netRequest);

    }


    public <T extends BaseResponse> Request addPostRequest(int requestCode, String url, Object param, Class<? extends BaseResponse> clazz, final INetRequest netReqeust) {
        final WeakReference<INetRequest> weakReference = new WeakReference<INetRequest>(netReqeust);
        final Response.Listener<T> successListener = createSuccessListener(requestCode, weakReference);


        final Response.ErrorListener errorListener = createErrorListener(requestCode, weakReference);
        HashMap<String, String> map = FieldUtils.convertToHashMap(param);
        //TODO:userid
        map.put("userId", "15");
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


    private <T extends BaseResponse> Request addGetRequest(boolean needCache, final int requestCode, String url, Object param, final Class<T> clazz, final INetRequest netReqeust) {
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

        if(request!=null&& !TextUtils.isEmpty(request.getUrl())){
            return mRequestQueue.getCache().get(request.getUrl());
        }

        return  null;

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



