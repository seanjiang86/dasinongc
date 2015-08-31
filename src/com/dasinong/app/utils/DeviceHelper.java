package com.dasinong.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class DeviceHelper {

	public static boolean checkNetWork(Context context) {
		ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		cwjManager.getActiveNetworkInfo();
		NetworkInfo networkInfo = cwjManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isConnected();
		} else {
			return false;
		}
	}

	public static boolean checkGPS(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps && network) {
			return true;
		}
		return false;
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

	public static String getPhoneNumber(Context context) {

		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		String phoneId = tm.getLine1Number();

		return phoneId;
	}

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}

	public final static void hideIME(View v) {
		if (v == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	public final static void showIME(View v) {
		v.requestFocus();
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(v, 0);
	}

	/**
	 * 获取设备唯一标识
	 */
	public static String getDeviceId(Context context) {
		String id = getUniqueID(context);
		if (id == null)
			id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		return id;
	}

	private static String getUniqueID(Context context) {

		String telephonyDeviceId = "NoTelephonyId";
		String androidDeviceId = "NoAndroidId";

		// get telephony id
		try {
			final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			telephonyDeviceId = tm.getDeviceId();
			if (telephonyDeviceId == null) {
				telephonyDeviceId = "NoTelephonyId";
			}
		} catch (Exception e) {
		}

		// get internal android device id
		try {
			androidDeviceId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
			if (androidDeviceId == null) {
				androidDeviceId = "NoAndroidId";
			}
		} catch (Exception e) {

		}

		// build up the uuid
		try {
			String id = getStringIntegerHexBlocks(androidDeviceId.hashCode()) + "-" + getStringIntegerHexBlocks(telephonyDeviceId.hashCode());

			return id;
		} catch (Exception e) {
			return "0000-0000-1111-1111";
		}
	}

	public static String getStringIntegerHexBlocks(int value) {
		String result = "";
		String string = Integer.toHexString(value);

		int remain = 8 - string.length();
		char[] chars = new char[remain];
		Arrays.fill(chars, '0');
		string = new String(chars) + string;

		int count = 0;
		for (int i = string.length() - 1; i >= 0; i--) {
			count++;
			result = string.substring(i, i + 1) + result;
			if (count == 4) {
				result = "-" + result;
				count = 0;
			}
		}

		if (result.startsWith("-")) {
			result = result.substring(1, result.length());
		}

		return result;
	}

}
