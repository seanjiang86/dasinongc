package com.dasinong.app.ui;

import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.view.TopbarView;

import android.R;
import android.os.Bundle;
import android.widget.EditText;

public class LoginActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startLoadingDialog();
		
		initView();
		setView();  
	}

	private void setView() {
//		mTopbarView = this.findViewById(R.id.topbar)
	}

	private void initView() {
		
	}

	
	
}
