package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.view.TopbarView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SmsSettingActivity extends BaseActivity {

	private TopbarView mTopbarView;
	
	private ListView mSmsList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sms_sub_list);
		
		initView();
		setUpView();
		getSubScribeLists();
	}

	private void getSubScribeLists() {
		startLoadingDialog();
		RequestService.getInstance().getSubScribeLists(this, BaseEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mSmsList = (ListView) this.findViewById(R.id.list_sms_list);
	}

	private void setUpView() {
		mTopbarView.setCenterText("免费短信订阅");
		mTopbarView.setLeftView(true, true);
		mTopbarView.setRightText("编辑");
		mTopbarView.setRightClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(SmsSettingActivity.this,SmsSubscribeActivity.class);
//				startActivity(intent);
				
				
				
			}
		});
		
		mSmsList.setAdapter(new ArrayAdapter<>(this, R.layout.textview, new String[]{"sfdsf","dsfsdfs"}));
		
		View view = View.inflate(this, R.layout.layout_sms_sub_list_footer, null);
		Button button = (Button) view.findViewById(R.id.button_sms_sub);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SmsSettingActivity.this,SmsSubscribeActivity.class);
				startActivity(intent);
			}
		});
		mSmsList.addFooterView(view);
	}
	
}
