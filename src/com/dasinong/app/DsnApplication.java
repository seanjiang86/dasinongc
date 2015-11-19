package com.dasinong.app;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.dasinong.app.components.net.VolleyManager;
import com.dasinong.app.ui.WebBrowserActivity;
import com.dasinong.app.ui.manager.CrashHandler;
import com.dasinong.app.utils.Logger;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

public class DsnApplication extends Application {
	private static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = DsnApplication.this;
		VolleyManager.getInstance().init(this);
		initExceptionCrash();
//		LoadUtils.init(getContext());
		
		umengMsgCustomAction();
	}

	private void umengMsgCustomAction() {
		/**
		 * 该Handler是在BroadcastReceiver中被调用，故
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
		 * */
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
		    @Override
		    public void dealWithCustomAction(Context context, UMessage msg) {
//		        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
		    	
		    	Intent intent = new Intent(DsnApplication.this,WebBrowserActivity.class);
		    	intent.putExtra(WebBrowserActivity.URL, msg.custom);
		    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    	startActivity(intent);
		    }
		};
		PushAgent.getInstance(this).setNotificationClickHandler(notificationClickHandler);
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
