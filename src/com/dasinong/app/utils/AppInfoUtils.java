package com.dasinong.app.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class AppInfoUtils {
	public static String getChannelCode(Context context) {
		String code = getMetaData(context, "UMENG_CHANNEL");
		if (code != null) {
			return code;
		}
		return "";
	}

	private static String getMetaData(Context context, String key) {
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Object value = ai.metaData.get(key);
			if (value != null) {
				return value.toString();
			}
		} catch (Exception e) {
			//
		}
		return null;
	}
}
