package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.LoginRegEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestCode;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.AppInfoUtils;
import com.dasinong.app.utils.StringHelper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
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
	
	private View mPwdSureLayout;
	private EditText mPwdSureEdit;
	
	private TextView mProblemText;

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

		mProblemText = (TextView) this.findViewById(R.id.textview_login_problem);
		
		mPwdSureLayout = this.findViewById(R.id.layout_pwd_sure);
		mPwdSureEdit = (EditText) this.findViewById(R.id.edittext_password_sure);
		
		mNextButton = (Button) this.findViewById(R.id.button_next);
	}

	private void setView() {
		if (isLogin) {
			mTopbarView.setCenterText("手机号登录");
			mNextButton.setText("登录");
			mProblemText.setVisibility(View.VISIBLE);
			mPwdSureLayout.setVisibility(View.GONE);
		} else {
			mTopbarView.setCenterText("手机号注册");
			mNextButton.setText("注册");
			mProblemText.setVisibility(View.GONE);
			mPwdSureLayout.setVisibility(View.VISIBLE);
		}
		mTopbarView.setLeftView(true, true);

		mPhoneEdit.setText(phone);

		mPwdEdit.requestFocus();

		mNextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isLogin) {
					login();
				} else {
					register();
				}
			}
		});
		
		mAgreementText.setText(getClickableSpan());
		mAgreementText.setMovementMethod(LinkMovementMethod.getInstance());
		
		mProblemText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				requestSmsCode();
			}
		}); 
	}
	
	protected void requestSmsCode() {
		final String phone = mPhoneEdit.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			showToast("请输入手机号");
			return;
		}
		if (!StringHelper.isPhoneNumber(phone)) {
			showToast("请输入合格的手机号");
			return;
		}
		startLoadingDialog();
		RequestService.getInstance().requestSecurityCode(this, phone, BaseEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if(resultData.isOk()){
//						phone = getIntent().getStringExtra("phone");
//						code = getIntent().getStringExtra("code");
//						formatedPhone = getIntent().getStringExtra("formatedPhone");
//						isAuthPhone = getIntent().getBooleanExtra("isAuthPhone", false);
					
					Intent intent = new Intent(RegisterPasswordActivity.this,AuthCodeActivity.class);
					intent.putExtra("phone", phone);
					intent.putExtra("authPempPwd", true);
					startActivity(intent);
					finish();
				}else{
					showToast(resultData.getMessage());
				}
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
				showToast(R.string.please_check_netword);
			}
		});
	}

	private SpannableString getClickableSpan() {

		SpannableString spanableInfo = new SpannableString(mAgreementText.getText()
				.toString());
		int start = 14;
		int end = spanableInfo.length();
		spanableInfo.setSpan(new ClickableSpan() {

			@Override
			public void onClick(View arg0) {
				// Toast.makeText(RegisterActivity.this, "注册协议",
				// Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(RegisterPasswordActivity.this, RegisterServiceActivity.class);
				intent.putExtra("url", "agreement.html");
				intent.putExtra("title", "服务协议条款");  
				startActivity(intent);
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				// TODO Auto-generated method stub
				super.updateDrawState(ds);  
				ds.setUnderlineText(false);
			}

		}, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanableInfo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_33AB33)), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanableInfo.setSpan(new BackgroundColorSpan(Color.WHITE), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		mAgreementText.setHighlightColor(Color.TRANSPARENT);

		return spanableInfo;
	}

	private void login() {
		String phone = mPhoneEdit.getText().toString().trim();
		String password = mPwdEdit.getText().toString().trim();
		
		if (TextUtils.isEmpty(phone)) {
			showToast("请输入手机号");
			return;
		}

		if (!StringHelper.isPhoneNumber(phone)) {
			showToast("请输入合格的手机号");
			return;
		}

		if (TextUtils.isEmpty(password)) {
			showToast("请输入密码");
			return;
		}
		
		startLoadingDialog();
		RequestService.getInstance().loginByPwd(this, phone, password, LoginRegEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();

				if (resultData.isOk()) {
					LoginRegEntity entity = (LoginRegEntity) resultData;

					AccountManager.saveAccount(RegisterPasswordActivity.this, entity.getData());

					Intent intent = new Intent(RegisterPasswordActivity.this,MainTabActivity.class);
					startActivity(intent);
					
					finish();

				} else {
					showToast(resultData.getMessage());
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
				showToast(R.string.please_check_netword);
			}
		});

	}

	protected void register() {
		String phone = mPhoneEdit.getText().toString().trim();
		String password = mPwdEdit.getText().toString().trim();
		String surePwd = mPwdSureEdit.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			showToast("请输入手机号");
			return;
		}

		if (!StringHelper.isPhoneNumber(phone)) {
			showToast("请输入合格的手机号");
			return;
		}

		if (TextUtils.isEmpty(password)) {
			showToast("请输入密码");
			return;
		}
		if (TextUtils.isEmpty(surePwd)) {
			showToast("请确认您的新密码");
			return;
		}

		if (!password.equals(surePwd)) {
			showToast("两次密码不一致");
			return;
		}
		if (!StringHelper.isPassword(password)) {
			showToast("新密码不合格");
			return;
		}

		//
		// Intent intent = new Intent(this,RegisterCodeActivity.class);
		// intent.putExtra(RegisterCodeActivity.PHONE_NUMBER, phone);
		// startActivity(intent);
		String channel = AppInfoUtils.getChannelCode(this);
		int appInstitutionId = AppInfoUtils.getInstitutionId(this);
		
		startLoadingDialog();

		RequestService.getInstance().register(this, phone, password, phone, "", channel, appInstitutionId+"",LoginRegEntity.class, new RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();

				LoginRegEntity entity = (LoginRegEntity) resultData;

				AccountManager.saveAccount(RegisterPasswordActivity.this, entity.getData());

				Intent intent = new Intent(RegisterPasswordActivity.this, MainTabActivity.class);
				startActivity(intent);

				finish();
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();

			}
		});

	}
}
