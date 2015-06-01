package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.view.TopbarView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends BaseActivity {

	private TopbarView mTopbarView;
	private EditText mUsernameEdit;
	private EditText mPasswordEdit;
	private EditText mPhoneEdit;
	private EditText mAddressEdit;
	
	private Button mRegisterButton;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		initView();
		setView();
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mUsernameEdit = (EditText) this.findViewById(R.id.edittext_username);
		mPasswordEdit = (EditText) this.findViewById(R.id.edittext_password);
		mPhoneEdit = (EditText) this.findViewById(R.id.edittext_phone);
		mAddressEdit = (EditText) this.findViewById(R.id.edittext_address);
		
		mRegisterButton = (Button) this.findViewById(R.id.button_register);
	}

	private void setView() {
//		mTopbarView = this.findViewById(R.id.topba)
		
		mRegisterButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				register();
			}
		});
		
	}

	protected void register() {
		String username = mUsernameEdit.getText().toString().trim();
		String password = mPasswordEdit.getText().toString().trim();
		String phont = mPhoneEdit.getText().toString().trim();
		String address = mAddressEdit.getText().toString().trim();
		
		if(TextUtils.isEmpty(username)){
			showToast("请输入用户名");
			return;
		}
		if(TextUtils.isEmpty(password)){
			showToast("请输入密码");
			return;
		}
		if(TextUtils.isEmpty(phont)){
			showToast("请输入手机号");
			return;
		}
		if(TextUtils.isEmpty(address)){
			showToast("请输入地址");
			return;
		}
		
		RequestService.getInstance().register(this, username, password, phont, address, BaseEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(BaseEntity resultData) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailed(Exception error, String msg) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
}
