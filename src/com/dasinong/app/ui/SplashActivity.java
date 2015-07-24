package com.dasinong.app.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;

public class SplashActivity extends BaseActivity {
	private TextView tv_version;
	private String versionName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		tv_version = (TextView) findViewById(R.id.tv_version);

		PackageManager pm = getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		if (!TextUtils.isEmpty(versionName)) {
			tv_version.setText("当前版本：" + versionName);
		} else {
			tv_version.setVisibility(View.GONE);
		}

		autoLogin();

		// 发送一个延时的消息
		new Handler() {
			public void handleMessage(android.os.Message msg) {
				// 接受到消息后的处理
				boolean isFirst = SharedPreferencesHelper.getBoolean(SplashActivity.this, Field.IS_FIRST, true);

				if (isFirst) {
					// 第一次进入应用，进入导航界面
					Log.i(tag, "是第一次进入");
					SharedPreferencesHelper.setBoolean(SplashActivity.this, Field.IS_FIRST, false);
					Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
					intent.putExtra("isFirst", isFirst);
					startActivity(intent);
					finish();

				} else {
					// 不是第一次进入应用,直接跳转到主界面
					Log.i(tag, "是第二次进入");
					Intent intent = new Intent(SplashActivity.this, MainTabActivity.class);
					startActivity(intent);
					finish();
				}

			};
		}.sendEmptyMessageDelayed(0, 2000);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	private void autoLogin() {
		String phone = SharedPreferencesHelper.getString(this, Field.USER_PHONE, "");

		if (!TextUtils.isEmpty(phone)) {
			RequestService.getInstance().authcodeLoginReg(this, phone, BaseEntity.class, new RequestListener() {

				@Override
				public void onSuccess(int requestCode, BaseEntity resultData) {
				}

				@Override
				public void onFailed(int requestCode, Exception error, String msg) {
				}
			});
		}
	}
}
