package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;

import android.os.Bundle;

public class SearchDiseaseResultActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disease_list);
		
		initData();
		initView();
		setUpView();
		requestData();
		
	}

	private void initData() {
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		
	}

	private void setUpView() {
		// TODO Auto-generated method stub
		
	}

	private void requestData() {
		RequestService.getInstance().browsePetDisByType(this, "虫害", BaseEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				
			}
		});
	}
	
}
