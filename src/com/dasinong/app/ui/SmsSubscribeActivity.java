package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.ui.view.TopbarView;

import android.os.Bundle;

public class SmsSubscribeActivity extends BaseActivity {

	private TopbarView mTopbarView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sms_sub);
		
		initView();
		setUpView();
		
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
	}

	private void setUpView() {
		mTopbarView.setCenterText("免费短信订阅");
		mTopbarView.setLeftView(true, true);
	}
	
}
