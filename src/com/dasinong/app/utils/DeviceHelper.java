package com.dasinong.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class DeviceHelper {

	public static boolean checkNetWork(Context context) {
		ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		cwjManager.getActiveNetworkInfo();
		NetworkInfo networkInfo = cwjManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return true;
		} else {
			return false;
		}
	}

	public static int getVersionName(Context context) {
		try {
			// 获取packagemanager的实例
			PackageManager packageManager = context.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			String version = packInfo.versionName;
			int versionCode = packInfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static String getIp(Context context) {
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		// 检查Wifi状态
		if (!wm.isWifiEnabled())
			wm.setWifiEnabled(true);
		WifiInfo wi = wm.getConnectionInfo();
		// 获取32位整型IP地址
		int ipAdd = wi.getIpAddress();
		// 把整型地址转换成“*.*.*.*”地址
		String ip = intToIp(ipAdd);
		return ip;
	}

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}


}
