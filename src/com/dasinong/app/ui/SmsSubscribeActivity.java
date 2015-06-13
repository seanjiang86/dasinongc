package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.ui.view.TopbarView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SmsSubscribeActivity extends BaseActivity {

	private TopbarView mTopbarView;
	private Spinner spinner;
	
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
		
		spinner = (Spinner) this.findViewById(R.id.spinner);
		spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, new  String[]{"sdf","fsdgsfdg","fgsdgs","sdf","fsdgsfdg","fgsdgs"}));
		
	}
	
}
