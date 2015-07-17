package com.dasinong.app.ui;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestCode;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.StringHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends BaseActivity {

	// 填写从短信SDK应用后台注册得到的APPKEY
		private static String APPKEY = "7ddca5c23518";
		// 填写从短信SDK应用后台注册得到的APPSECRET
		private static String APPSECRET = "674b27af7f64b70e317f147098bc782b";
		private boolean ready;
	
	private TopbarView mTopbarView;
	private EditText mPhoneEdit;
	private Button mNextButton;
	private TextView mAgreementText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		initView();
		setView();
		
		
		initSDK();
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mPhoneEdit = (EditText) this.findViewById(R.id.edittext_phone_number);
		mAgreementText = (TextView) this.findViewById(R.id.textview_agreement);
		
		mNextButton = (Button) this.findViewById(R.id.button_next);
//		mUsernameEdit = (EditText) this.findViewById(R.id.edittext_username);
//		mPasswordEdit = (EditText) this.findViewById(R.id.edittext_password);
//		mUsernameEdit = (EditText) this.findViewById(R.id.edittext_username);
//		mPasswordEdit = (EditText) this.findViewById(R.id.edittext_password);
//		mPhoneEdit = (EditText) this.findViewById(R.id.edittext_phone);
//		mAddressEdit = (EditText) this.findViewById(R.id.edittext_address);
		
//		mRegisterButton = (Button) this.findViewById(R.id.button_register);
	}

	private void setView() {
		mTopbarView.setCenterText("填写手机号");
		mTopbarView.setLeftView(true, true);
		
		mNextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				register();
			}
		});
		
	}

	protected void register() {
		String phone = mPhoneEdit.getText().toString().trim();
		
		if(TextUtils.isEmpty(phone)){
			showToast("请输入手机号");
			return;
		}
		
		if(!StringHelper.isPhoneNumber(phone)){
			showToast("请输入合格的手机号");
			return;
		}
		
//		Intent intent = new Intent(this,RegisterCodeActivity.class);
//		intent.putExtra(RegisterCodeActivity.PHONE_NUMBER, phone);
//		startActivity(intent);
		
//		RequestService.getInstance().register(this,RequestCode.REGISTER, username, password, phont, address, BaseEntity.class, new RequestListener() {
//			
//			@Override
//			public void onSuccess(int requestCode,BaseEntity resultData) {
//				
//			}
//			
//			@Override
//			public void onFailed(int requestCode,Exception error, String msg) {
//				
//			}
//		});
		
		sendSms();
	}
	
	private void initSDK() {
		// 初始化短信SDK
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		final Handler handler = new Handler();
		EventHandler eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
		ready = true;

		// 获取新好友个数
//		showDialog();
//		SMSSDK.getNewFriendsCount();

	}
	
	
	protected void onDestroy() {
		if (ready) {
			// 销毁回调监听接口
			SMSSDK.unregisterAllEventHandler();
		}
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
//		if (ready) {
//			// 获取新好友个数
//			showDialog();
//			SMSSDK.getNewFriendsCount();
//		}
	}
	
	private void sendSms() {
		// 打开注册页面
//		RegisterPage registerPage = new RegisterPage();
//		registerPage.setRegisterCallback(new EventHandler() {
//			public void afterEvent(int event, int result, Object data) {
//				// 解析注册结果
//				if (result == SMSSDK.RESULT_COMPLETE) {
//					@SuppressWarnings("unchecked")
//					HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
//					String country = (String) phoneMap.get("country");
//					String phone = (String) phoneMap.get("phone");
//					// 提交用户信息
//					// registerUser(country, phone);
//					showToast("提交注册信息");
//				}
//			}
//		});
//		registerPage.show(this);
		
		Intent intent = new Intent(this,RegisterPhoneActivity.class);
		startActivityForResult(intent, 0);
		
	}
	

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		
		// 解析注册结果
		if (arg1 == RESULT_OK) {
			@SuppressWarnings("unchecked")
//			HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
			String country = arg2.getStringExtra("country");
			String phone = arg2.getStringExtra("phone");
			// 提交用户信息
			// registerUser(country, phone);
			showToast("提交注册信息"+country + "--"+phone);
		}
		
	}
	
}
