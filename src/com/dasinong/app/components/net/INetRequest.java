package com.dasinong.app.components.net;

import com.android.volley.VolleyError;

/**
 * Created by liuningning on 15/6/13.
 */
public interface INetRequest {

    public void onTaskSuccess(int requestCode,Object response);
//    public void onTaskExceptionSuccess(int requestCode,T response);
    public void onTaskFailedSuccess(int requestCode,NetError error);
    public void onCache(int requestCode,Object response);
}
