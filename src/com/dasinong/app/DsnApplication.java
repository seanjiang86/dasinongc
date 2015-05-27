package com.dasinong.app;

import java.io.File;

import com.dasinong.app.ui.manager.CrashHandler;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

public class DsnApplication extends Application {
	public static Context mContext;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = DsnApplication.this;

		initExceptionCrash();

	}

	private void initExceptionCrash() {
		String externalFileDir = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED) ? DsnApplication.getContext()
				.getExternalFilesDir("").getPath() : "dasinong" + File.separator;

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
