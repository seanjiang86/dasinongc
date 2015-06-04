package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestCode;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.StringHelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterPasswordActivity extends BaseActivity {

	private TopbarView mTopbarView;
	private EditText mPhoneEdit;
	private EditText mPwdEdit;
	private Button mNextButton;
	private TextView mAgreementText;
	
	private String phone;
	private boolean isLogin;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_password);
		
		initData();
		initView();
		setView();
	}

	private void initData() {
		phone = getIntent().getStringExtra("phone");
		isLogin = getIntent().getBooleanExtra("isLogin", false);
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mPhoneEdit = (EditText) this.findViewById(R.id.edittext_phone_number);
		mPwdEdit = (EditText) this.findViewById(R.id.edittext_password);
		mAgreementText = (TextView) this.findViewById(R.id.textview_agreement);
		
		mNextButton = (Button) this.findViewById(R.id.button_next);
	}

	private void setView() {
		if(isLogin){
			mTopbarView.setCenterText("手机号登录");
			mNextButton.setText("登录");
		}else{
			mTopbarView.setCenterText("手机号注册");
			mNextButton.setText("注册");
		}
		mTopbarView.setLeftView(true, true);
		
		mPhoneEdit.setText(phone);
		
		mPwdEdit.requestFocus();
		
		mNextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isLogin){
					login();
				}else{
					register();
				}
			}
		});
		
	}
	
	private void login() {
		String phone = mPhoneEdit.getText().toString().trim();
		String password = mPwdEdit.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			showToast("请输入手机号");
			return;
		}
		
		if(!StringHelper.isPhoneNumber(phone)){
			showToast("请输入合格的手机号");
			return;
		}
		
		if(TextUtils.isEmpty(password)){
			showToast("请输入密码");
			return;
		}
		
		startLoadingDialog();
		
		RequestService.getInstance().loginByPwd(this, phone, password, BaseEntity.class, new RequestListener() {
			
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

	protected void register() {
		String phone = mPhoneEdit.getText().toString().trim();
		String password = mPwdEdit.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			showToast("请输入手机号");
			return;
		}
		
		if(!StringHelper.isPhoneNumber(phone)){
			showToast("请输入合格的手机号");
			return;
		}
		
		if(TextUtils.isEmpty(password)){
			showToast("请输入密码");
			return;
		}
//		
//		Intent intent = new Intent(this,RegisterCodeActivity.class);
//		intent.putExtra(RegisterCodeActivity.PHONE_NUMBER, phone);
//		startActivity(intent);
		
		startLoadingDialog();
		
		RequestService.getInstance().register(this, phone, password, phone, "", BaseEntity.class, new RequestListener() {
			
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
}
