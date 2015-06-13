package com.dasinong.app.components.net;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Build;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
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
import com.dasinong.app.DsnApplication;
import com.dasinong.app.components.domain.BaseResponse;
import com.dasinong.app.utils.DeviceHelper;
import com.dasinong.app.utils.FieldUtils;
import com.dasinong.app.utils.FileUtils;
import com.dasinong.app.utils.MD5Utils;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by liuningning on 15/6/13.
 */
public class VolleyManager {

    private volatile static VolleyManager instance;
    private RequestQueue mRequestQueue;

    private Context mContext;
    private volatile Boolean isInit;


    private String mCacheDir;

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

    public <T extends BaseResponse> void addGetRequestWithNoCache(final int requestCode, String url, Object param, final Class<T> clazz, final INetRequest<T> netRequest) {

        addGetRequest(false, requestCode, url, param, clazz, netRequest);

    }

    public <T extends BaseResponse> void addGetRequestWithCache(final int requestCode, String url, Object param, final Class<T> clazz, final INetRequest<T> netRequest) {

        addGetRequest(true, requestCode, url, param, clazz, netRequest);

    }


    public <T extends BaseResponse> void addPostRequest(int requestCode, String url, Object param, Class<? extends BaseResponse> clazz, final INetRequest<T> netReqeust) {
        final WeakReference<INetRequest> weakReference = new WeakReference<INetRequest>(netReqeust);
        final Response.Listener<T> successListener = createSuccessListener(requestCode, url, weakReference);


        final Response.ErrorListener errorListener = createErrorListener(requestCode, weakReference);
        HashMap<String, String> map = FieldUtils.convertToHashMap(param);

        final GsonRequest<T> request = new GsonRequest(url, map, clazz, successListener, errorListener);
        //disabel volley cache
        request.setShouldCache(false);

        mRequestQueue.add(request);

    }

    private RequestQueue createRequestQueue() {
        File cacheDir = new File(mContext.getCacheDir(), "network");

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
        mCacheDir = cacheDir.getAbsolutePath();
        queue.start();
        return queue;

    }


    private <T extends BaseResponse> void addGetRequest(boolean needCache, final int requestCode, String url, Object param, final Class<T> clazz, final INetRequest<T> netReqeust) {
        final String finalUrl = createUrl(url, param);

        final WeakReference<INetRequest> weakReference = new WeakReference<INetRequest>(netReqeust);
        final Response.Listener<T> successListener = createSuccessListener(requestCode, finalUrl, weakReference);


        final Response.ErrorListener errorListener = createErrorListener(requestCode, weakReference);


        final GsonRequest<T> request = new GsonRequest(finalUrl, clazz, successListener, errorListener);
        //disabel volley cache
        request.setShouldCache(false);

        if (needCache) {
            executeReadCache(requestCode, clazz, finalUrl, weakReference);

        }


        if (!DeviceHelper.checkNetWork(DsnApplication.getContext())) {
            netReqeust.onTaskFailedSuccess(requestCode, new NetError(NetError.NETWORK_UNAVAILABLEE));
            return;
        } else {
            mRequestQueue.add(request);
        }

    }


    private <T extends BaseResponse> void executeReadCache(final int requestCode, final Class<T> clazz, final String finalUrl, final WeakReference<INetRequest> weakReference) {
        AsyncTask<String, Void, T> task = new AsyncTask<String, Void, T>() {
            @Override
            protected T doInBackground(String... params) {
                return readEntitySync(finalUrl, clazz);
            }

            @Override
            protected void onPostExecute(T result) {
                INetRequest<T> tem = weakReference.get();
                if (tem != null && result != null) {
                    tem.onCache(requestCode, result);
                }
            }
        };

        if (task != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                task.execute();
            }
        }
    }

    private String buildUrl(String url, Object param) {

        StringBuilder urlWithParm = new StringBuilder(url);
        if (param != null) {
            urlWithParm.append(encodeParameters(FieldUtils.convertToHashMap(param)));
        }
        return url.toString();
    }


    private <T> T readEntitySync(final String url, final Class type) {
        String entityKey = createEntityKey(url);
        String cacheFilePath = new File(mCacheDir, entityKey).getAbsolutePath();

        return (T) FileUtils.readEntity(type, cacheFilePath);
    }


    /**
     * 从URL中生成Entity的key
     *
     * @param url
     * @return
     */
    private String createEntityKey(String url) {
        return MD5Utils.md5(url);
    }


    /**
     * @param url
     * @param entity
     */
    private void saveEntity(final String url, final Object entity) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                String entityKey = createEntityKey(url);
                String cacheFilePath = new File(mCacheDir, entityKey).getAbsolutePath();
                if (entity != null) {
                    FileUtils.writeEntity(entity, cacheFilePath);

                }

                return null;
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }

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

    private <T extends BaseResponse> Response.Listener<T> createSuccessListener(final int requestCode, final String finalUrl, final WeakReference<INetRequest> weakReference) {
        return new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                saveEntity(finalUrl, response);
                INetRequest<T> tem = weakReference.get();
                if (tem != null) {
                    tem.onTaskSuccess(requestCode, response);
                }
            }

        };
    }

}



