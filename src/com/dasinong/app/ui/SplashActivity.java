package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.umeng.update.UmengUpdateAgent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		// 友盟更新
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		
		//发送一个延时的消息
		new Handler(){
			public void handleMessage(android.os.Message msg) {
				//接受到消息后的处理
				boolean isFirst = SharedPreferencesHelper.getBoolean(SplashActivity.this, Field.IS_FIRST, true);
				
				if(isFirst){
					//第一次进入应用，进入导航界面
					Log.i(tag, "是第一次进入");
					SharedPreferencesHelper.setBoolean(SplashActivity.this, Field.IS_FIRST, false);
					Intent intent = new Intent(SplashActivity.this,GuideActivity.class);
					intent.putExtra("isFirst", isFirst);
					startActivity(intent);
					finish();

				}else{
					//不是第一次进入应用,直接跳转到主界面
					Log.i(tag, "是第二次进入");
					Intent intent = new Intent(SplashActivity.this,MainTabActivity.class);
					startActivity(intent);
					finish();
				}
				
			};
		}.sendEmptyMessageDelayed(0, 2000);
		
	}
}
