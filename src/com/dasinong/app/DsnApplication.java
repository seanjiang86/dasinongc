package com.dasinong.app;

import java.io.File;

import com.dasinong.app.components.net.VolleyManager;
import com.dasinong.app.ui.manager.CrashHandler;
import com.dasinong.app.utils.Logger;
import com.liam.imageload.LoadUtils;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class DsnApplication extends Application {
	private static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = DsnApplication.this;
		VolleyManager.getInstance().init(this);
		initExceptionCrash();
		LoadUtils.init(getContext());
	}

	private void initExceptionCrash() {
		String externalFileDir = "";
		try {
			externalFileDir = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED) ? DsnApplication.getContext()
					.getExternalFilesDir("").getPath() : "dasinong" + File.separator;
		} catch (Exception e) {
			externalFileDir = "dasinong" + File.separator;
			Logger.e("DsnApplication", Log.getStackTraceString(e));
		}

		File file = new File(externalFileDir);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}

		Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(DsnApplication.getContext(), externalFileDir));
	}

	public static Context getContext() {
		return mContext;
	}

}
