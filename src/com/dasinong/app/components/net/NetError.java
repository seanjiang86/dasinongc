package com.dasinong.app.components.net;

/**
 * Created by liuningning on 15/6/13.
 */
public class NetError extends Exception {


    public static final NetErrorStatus NETWORK_UNAVAILABLEE = NetErrorStatus.NETWORK_UNAVAILABLEE;
    public static final NetErrorStatus NET_TIMEOUT = NetErrorStatus.NET_TIMEOUT;
    public static final NetErrorStatus NET_AUTHFAILURE = NetErrorStatus.NET_AUTHFAILURE;
    public static final NetErrorStatus NET_ERROR = NetErrorStatus.NET_ERROR;
    public static final NetErrorStatus SERVER_ERROR = NetErrorStatus.SERVER_ERROR;
    public static final NetErrorStatus PARSE_ERROR = NetErrorStatus.PARSE_ERROR;
    public static final NetErrorStatus UNKNOW_ERROR = NetErrorStatus.UNKNOW_ERROR;
    public NetErrorStatus netWorkCode;


    public NetError(NetErrorStatus netWorkCode) {
        this.netWorkCode = netWorkCode;

    }





    public enum NetErrorStatus

    {
        NETWORK_UNAVAILABLEE, NET_TIMEOUT, NET_AUTHFAILURE, NET_ERROR,
        SERVER_ERROR, PARSE_ERROR, UNKNOW_ERROR
    }
}
