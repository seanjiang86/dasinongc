/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2014年 mob.com. All rights reserved.
 */
package com.dasinong.app.ui;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.SMSReceiver;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.IsPassSetEntity;
import com.dasinong.app.entity.LoginRegEntity;
import com.dasinong.app.net.NetConfig.ResponseCode;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestCode;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.ui.view.TopbarView;
import com.mob.tools.FakeActivity;

public class AuthCodeActivity extends BaseActivity implements OnClickListener, TextWatcher {

	private static final int RETRY_INTERVAL = 60;
	private String phone;
	private String code;
	private String formatedPhone;
	private int time = RETRY_INTERVAL;
	private EventHandler handler;
	private Handler mHandler = new Handler();

	private TopbarView mTopbarView;
	
	private EditText etIdentifyNum;
	// private TextView tvTitle;
	private TextView tvPhone;
	private TextView tvIdentifyNotify;
	private TextView tvUnreceiveIdentify;
//	private ImageView ivClear;
	private Button btnSubmit;
	// private Button btnSounds;
	private BroadcastReceiver smsReceiver;
//	private int SHOWDIALOGTYPE = 1;
	
	private int mFailedCount = 0;
	
	private boolean isAuthPhone = false;
	private boolean isAuthPempPwd = false;
	
	private TextView mCallPhoneText;
	
	private static String APPKEY = "80424b5493c0";
	private static String APPSECRET = "3c1b73e6af8f059c2e6b25f7065d77a3";
	
	public void setPhone(String phone, String code, String formatedPhone) {
		this.phone = phone;
		this.code = code;
		this.formatedPhone = formatedPhone;
	}

	private void initData() {
		// intent.putExtra("phone", phone);
		// intent.putExtra("code", code);
		// intent.putExtra("formatedPhone", formatedPhone);
		phone = getIntent().getStringExtra("phone");
		code = getIntent().getStringExtra("code");
		formatedPhone = getIntent().getStringExtra("formatedPhone");
		isAuthPhone = getIntent().getBooleanExtra("isAuthPhone", false);
		isAuthPempPwd = getIntent().getBooleanExtra("authPempPwd", false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register_login_authcode);

		initData();
		
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mTopbarView.setCenterText("填写验证码");
		mTopbarView.setLeftClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				runOnUiThread(new Runnable() {
					public void run() {
						showNotifyDialog();
					}
				});
			}
		});
		
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		btnSubmit.setEnabled(false);

		etIdentifyNum = (EditText) findViewById(R.id.edittext_authcode);
		etIdentifyNum.addTextChangedListener(this);
		tvIdentifyNotify = (TextView) findViewById(R.id.tv_identify_notify);
		String text = getString(R.string.smssdk_send_mobile_detail);
		tvIdentifyNotify.setText(Html.fromHtml(text));
		tvPhone = (TextView) findViewById(R.id.tv_phone);
		tvPhone.setText(phone);
		tvUnreceiveIdentify = (TextView) findViewById(R.id.tv_unreceive_identify);
		String unReceive = getString(R.string.smssdk_receive_msg, time);
		tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
		tvUnreceiveIdentify.setOnClickListener(this);
		tvUnreceiveIdentify.setEnabled(false);
//		ivClear = (ImageView) findViewById(R.id.iv_clear);
//		ivClear.setOnClickListener(this);
		// resId = getIdRes(activity, "btn_sounds");
		// btnSounds = (Button) findViewById(resId);
		// btnSounds.setOnClickListener(this);

		handler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					afterSubmit(result, data);
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					afterGet(result, data);
				} else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
					afterGetVoice(result, data);
				}
			}
		};
		SMSSDK.registerEventHandler(handler);
		countDown();

		smsReceiver = new SMSReceiver(new SMSSDK.VerifyCodeReadListener() {
			@Override
			public void onReadVerifyCode(final String verifyCode) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						etIdentifyNum.setText(verifyCode);
						onClick(btnSubmit);
					}
				});
			}
		});
		registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
		
		mCallPhoneText = (TextView) this.findViewById(R.id.btn_phone_kefu);
		
		if(isAuthPempPwd){
			tvUnreceiveIdentify.setVisibility(View.GONE);
			mCallPhoneText.setVisibility(View.VISIBLE);
		}else{
			tvUnreceiveIdentify.setVisibility(View.VISIBLE);
			mCallPhoneText.setVisibility(View.GONE);
		}
		
		mCallPhoneText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showCallDialog();
			}
		});
		
	}
	
	private void showCallDialog() {
		final Dialog dialog = new Dialog(this, R.style.CommonDialog);
		dialog.setContentView(R.layout.smssdk_back_verify_dialog);
		TextView tv = (TextView) dialog.findViewById(R.id.tv_dialog_hint);
		tv.setText("确定致电: 4000556050 ?");
		tv.setTextSize(18);
		Button waitBtn = (Button) dialog.findViewById(R.id.btn_dialog_ok);
		waitBtn.setText("取消");
		waitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button backBtn = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
		backBtn.setText("确定");
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+"4000556050"));  
                startActivity(intent); 
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	// public void onCreate() {
	//
	// initData();
	//
	// int resId = getLayoutRes(activity, "smssdk_input_identify_num_page");
	// if (resId > 0) {
	// activity.setContentView(resId);
	// // resId = getIdRes(activity, "ll_back");
	// // activity.findViewById(resId).setOnClickListener(this);
	// resId = getIdRes(activity, "btn_submit");
	// btnSubmit = (Button) activity.findViewById(resId);
	// btnSubmit.setOnClickListener(this);
	// btnSubmit.setEnabled(false);
	//
	// resId = getIdRes(activity, "tv_title");
	// // tvTitle = (TextView) activity.findViewById(resId);
	// resId = getStringRes(activity, "smssdk_write_identify_code");
	// // if (resId > 0) {
	// // tvTitle.setText(resId);
	// // }
	// resId = getIdRes(activity, "et_put_identify");
	// etIdentifyNum = (EditText) activity.findViewById(resId);
	// etIdentifyNum.addTextChangedListener(this);
	// resId = getIdRes(activity, "tv_identify_notify");
	// tvIdentifyNotify = (TextView) activity.findViewById(resId);
	// resId = getStringRes(activity, "smssdk_send_mobile_detail");
	// if (resId > 0) {
	// String text = getContext().getString(resId);
	// tvIdentifyNotify.setText(Html.fromHtml(text));
	// }
	// resId = getIdRes(activity, "tv_phone");
	// tvPhone = (TextView) activity.findViewById(resId);
	// tvPhone.setText(formatedPhone);
	// resId = getIdRes(activity, "tv_unreceive_identify");
	// tvUnreceiveIdentify = (TextView) activity.findViewById(resId);
	// resId = getStringRes(activity, "smssdk_receive_msg");
	// if (resId > 0) {
	// String unReceive = getContext().getString(resId, time);
	// tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
	// }
	// tvUnreceiveIdentify.setOnClickListener(this);
	// tvUnreceiveIdentify.setEnabled(false);
	// resId = getIdRes(activity, "iv_clear");
	// ivClear = (ImageView) activity.findViewById(resId);
	// ivClear.setOnClickListener(this);
	// resId = getIdRes(activity, "btn_sounds");
	// // btnSounds = (Button) findViewById(resId);
	// // btnSounds.setOnClickListener(this);
	//
	// handler = new EventHandler() {
	// public void afterEvent(int event, int result, Object data) {
	// if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
	// /** 提交验证码 */
	// afterSubmit(result, data);
	// } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
	// /** 获取验证码成功后的执行动作 */
	// afterGet(result, data);
	// }else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
	// /** 获取语音版验证码成功后的执行动作 */
	// afterGetVoice(result, data);
	// }
	// }
	// };
	// SMSSDK.registerEventHandler(handler);
	// countDown();
	// }
	//
	// smsReceiver = new SMSReceiver(new SMSSDK.VerifyCodeReadListener() {
	// @Override
	// public void onReadVerifyCode(final String verifyCode) {
	// runOnUIThread(new Runnable() {
	// @Override
	// public void run() {
	// etIdentifyNum.setText(verifyCode);
	// }
	// });
	// }
	// });
	// activity.registerReceiver(smsReceiver, new IntentFilter(
	// "android.provider.Telephony.SMS_RECEIVED"));
	// }

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void finish() {
		SMSSDK.unregisterEventHandler(handler);
		unregisterReceiver(smsReceiver);
		// TODO Auto-generated method stub
		super.finish();
	}

	/** 倒数计时 */
	private void countDown() {

		mHandler.postDelayed(new Runnable() {
			public void run() {
				time--;
				if (time == 0) {
					String unReceive = getString(R.string.smssdk_unreceive_identify_code, time);
					tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
					tvUnreceiveIdentify.setEnabled(true);
					// btnSounds.setVisibility(View.VISIBLE);
					time = RETRY_INTERVAL;
				} else {
					String unReceive = getString(R.string.smssdk_receive_msg, time);
					tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
					tvUnreceiveIdentify.setEnabled(false);
					mHandler.postDelayed(this, 1000);
				}
				// if (time == 30){
				// btnSounds.setVisibility(View.VISIBLE);
				// }
				// runOnUiThread(this, 1000);
			}
		}, 1000);

		// runOnUiThread(new Runnable() {
		// public void run() {
		// time--;
		// if (time == 0) {
		// String unReceive = getString(R.string.smssdk_unreceive_identify_code,
		// time);
		// tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
		// tvUnreceiveIdentify.setEnabled(true);
		// // btnSounds.setVisibility(View.VISIBLE);
		// time = RETRY_INTERVAL;
		// } else {
		// String unReceive = getString(R.string.smssdk_receive_msg, time);
		// tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
		// }
		// // if (time == 30){
		// // btnSounds.setVisibility(View.VISIBLE);
		// // }
		// tvUnreceiveIdentify.setEnabled(false);
		// runOnUiThread(this, 1000);
		// }
		// }
		// }, 1000);
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// 如果输入框木有，就隐藏delbtn
		if (s.length() > 0) {
			btnSubmit.setEnabled(true);
//			ivClear.setVisibility(View.VISIBLE);
			// int resId = getBitmapRes(activity, "smssdk_btn_enable");
			// if (resId > 0) {
			// btnSubmit.setBackgroundResource(resId);
			// }
		} else {
			btnSubmit.setEnabled(false);
//			ivClear.setVisibility(View.GONE);
			// int resId = getBitmapRes(activity, "smssdk_btn_disenable");
			// if (resId > 0) {
			// btnSubmit.setBackgroundResource(resId);
			// }
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	public void afterTextChanged(Editable s) {
		// btnSounds.setVisibility(View.GONE);
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_submit:
			// 提交验证码
			String verificationCode = etIdentifyNum.getText().toString().trim();
			
			if(isAuthPempPwd){
				authPempPwd(verificationCode);
			}else{
				if (!TextUtils.isEmpty(code)) {
					startLoadingDialog();
					SMSSDK.submitVerificationCode(code, phone, verificationCode);
				} else {
					// int resId = getStringRes(activity,
					// "smssdk_write_identify_code");
					// if (resId > 0) {
					// Toast.makeText(getContext(), resId,
					// Toast.LENGTH_SHORT).show();
					// }
					showToast(R.string.smssdk_write_identify_code);
				}
			}
			
//			loginRegister(phone);
			
			break;
//		case R.id.iv_clear:
//			etIdentifyNum.getText().clear();
//			break;
		case R.id.tv_unreceive_identify:
//			SHOWDIALOGTYPE = 1;
			// 没有接收到短信
			showDialog1();
			break;
		// case R.id.btn_sounds:
		//
		// break;
		}

		// int id = v.getId();
		// int id_ll_back = getIdRes(activity, "ll_back");
		// int id_btn_submit = getIdRes(activity, "btn_submit");
		// int id_tv_unreceive_identify = getIdRes(activity,
		// "tv_unreceive_identify");
		// int id_iv_clear = getIdRes(activity, "iv_clear");
		// int id_btn_sounds = getIdRes(activity, "btn_sounds");

		// if (id == id_ll_back) {
		// runOnUIThread(new Runnable() {
		// public void run() {
		// showNotifyDialog();
		// }
		// });
		// } else if (id == id_btn_submit) {
		// // 提交验证码
		// String verificationCode = etIdentifyNum.getText().toString().trim();
		// if (!TextUtils.isEmpty(code)) {
		// if (pd != null && pd.isShowing()) {
		// pd.dismiss();
		// }
		// pd = CommonDialog.ProgressDialog(activity);
		// if (pd != null) {
		// pd.show();
		// }
		// SMSSDK.submitVerificationCode(code, phone, verificationCode);
		// } else {
		// int resId = getStringRes(activity, "smssdk_write_identify_code");
		// if (resId > 0) {
		// Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
		// }
		// }
		// } else if (id == id_tv_unreceive_identify) {
		// SHOWDIALOGTYPE = 1;
		// // 没有接收到短信
		// showDialog(SHOWDIALOGTYPE);
		// } else if (id == id_iv_clear) {
		// etIdentifyNum.getText().clear();
		// } else if (id == id_btn_sounds) {
		// SHOWDIALOGTYPE = 2;
		// // 发送语音验证码
		// showDialog(SHOWDIALOGTYPE);
		// }
	}

	private void authPempPwd(String verificationCode) {
		startLoadingDialog();
		RequestService.getInstance().loginWithSecCode(this, phone, verificationCode, LoginRegEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if(resultData.isOk()){
					LoginRegEntity entity = (LoginRegEntity) resultData;
					
					AccountManager.saveAccount(AuthCodeActivity.this, entity.getData());
					
					Intent intent = new Intent(AuthCodeActivity.this,MainTabActivity.class);
					startActivity(intent);
					
					Intent setIntent = new Intent(AuthCodeActivity.this, MyInfoSetActivity.class);
					setIntent.putExtra("editType", MyInfoSetActivity.EDIT_PASSWORD);
					setIntent.putExtra("isNewPwd", true);
					startActivity(setIntent);
					
					finish();
				}else{
					showToast(resultData.getMessage());
				}
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
			}
		});
	}

	/** 弹出重新发送短信对话框,或发送语音窗口 */
	private void showDialog1() {
//		if (type == 1) {
			final Dialog dialog = new Dialog(this, R.style.CommonDialog);
//			TextView tv = new TextView(this);
//			if (type == 1) {
//				tv.setText(R.string.smssdk_resend_identify_code);
//			} else {
//				tv.setText(R.string.smssdk_send_sounds_identify_code);
//			}
//			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//			tv.setTextColor(getResources().getColor(R.color.smssdk_white));
//			int dp_10 = dipToPx(this, 10);
//			tv.setPadding(dp_10, dp_10, dp_10, dp_10);

			View view = View.inflate(this, R.layout.dialog_authcode_no, null);
			Button againBt = (Button) view.findViewById(R.id.button_get_code_again);
			Button skipBt = (Button) view.findViewById(R.id.button_skip_auth);
			
			dialog.setContentView(view);
			againBt.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.dismiss();
					tvUnreceiveIdentify.setEnabled(false);

					startLoadingDialog();

					// 重新获取验证码短信
					SMSSDK.getVerificationCode(code, phone.trim(), null);
				}
			});
			
			skipBt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					Intent intent = new Intent(AuthCodeActivity.this,RegisterPasswordActivity.class);
//					startActivity(intent);
					dialog.dismiss();
					checkUser(phone);
//					finish();
				}
			});

			dialog.setCanceledOnTouchOutside(true);
			dialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					tvUnreceiveIdentify.setEnabled(true);
				}
			});
			dialog.show();
//		} else if (type == 2) {
//			final Dialog dialog = new Dialog(this, R.style.CommonDialog);
//			dialog.setContentView(R.layout.smssdk_send_msg_dialog);
//			TextView tv_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
//			tv_title.setText(R.string.smssdk_make_sure_send_sounds);
//			TextView tv = (TextView) dialog.findViewById(R.id.tv_dialog_hint);
//			String text = getString(R.string.smssdk_send_sounds_identify_code);
//			tv.setText(text);
//			((Button) dialog.findViewById(R.id.btn_dialog_ok)).setOnClickListener(new OnClickListener() {
//				public void onClick(View v) {
//					// TODO 发送语言
//					dialog.dismiss();
//					SMSSDK.getVoiceVerifyCode(phone, code);
//				}
//			});
//			((Button) dialog.findViewById(R.id.btn_dialog_cancel)).setOnClickListener(new OnClickListener() {
//				public void onClick(View v) {
//					dialog.dismiss();
//				}
//			});
//			dialog.setCanceledOnTouchOutside(true);
//			dialog.show();
//		}

	}
	
	private void checkUser(final String phone) {
		((BaseActivity)AuthCodeActivity.this).startLoadingDialog();
		RequestService.getInstance().checkUser(AuthCodeActivity.this, phone, BaseEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				((BaseActivity)AuthCodeActivity.this).dismissLoadingDialog();
				if(resultData.isOk()){
					checkPwd();
				}else if(resultData.isCode(ResponseCode.CODE_100)){
					Intent intent = new Intent(AuthCodeActivity.this,RegisterPasswordActivity.class);
					intent.putExtra("phone", phone);
					intent.putExtra("isLogin", false);
					AuthCodeActivity.this.startActivity(intent);
					finish();
				}else{
					((BaseActivity)AuthCodeActivity.this).showToast(resultData.getMessage());
				}
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				((BaseActivity)AuthCodeActivity.this).dismissLoadingDialog();
				
			}
		});
	}
	
	private void checkPwd() {
		startLoadingDialog();
		RequestService.getInstance().isPassSet(this, IsPassSetEntity.class, new RequestListener() {
            
            @Override
            public void onSuccess(int requestCode, BaseEntity resultData) {
                dismissLoadingDialog();
                if(resultData.isOk()){
                    IsPassSetEntity entity = (IsPassSetEntity) resultData;
                    
                    if(!entity.isData()){
                        requestCode();
                    }
                    
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
	
	private void requestCode(){
	    startLoadingDialog();
	    RequestService.getInstance().requestSecurityCode(this, phone, BaseEntity.class, new RequestListener() {
            
            @Override
            public void onSuccess(int requestCode, BaseEntity resultData) {
                dismissLoadingDialog();
                if(resultData.isOk()){
                    showToast("验证码重新发送成功");
                    isAuthPempPwd = true;
                    tvUnreceiveIdentify.setVisibility(View.GONE);
                    mCallPhoneText.setVisibility(View.VISIBLE);
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

	/**
	 * 提交验证码成功后的执行事件
	 * 
	 * @param result
	 * @param data
	 */
	private void afterSubmit(final int result, final Object data) {
		runOnUiThread(new Runnable() {
			public void run() {

				dismissLoadingDialog();
				
				if (result == SMSSDK.RESULT_COMPLETE) {
//					HashMap<String, Object> res = new HashMap<String, Object>();
//					res.put("res", true);
//					res.put("page", 2);
//					res.put("phone", data);
//					setResult(res);
					
					
//					intent.putExtra("phone", data);
					
//					setResult(RESULT_OK, intent);
					
//					finish();
					
					if(isAuthPhone){
						setResult(RESULT_OK);
						finish();
					}else{
						Intent intent = new Intent();
						intent.putExtra("res", true);
						intent.putExtra("page", 2);
						
						@SuppressWarnings("unchecked")
						HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
						
						intent.putExtra("country", (String) phoneMap.get("country"));
						intent.putExtra("phone", (String) phoneMap.get("phone"));
						loginRegister((String) phoneMap.get("phone"));
					}
					
				} else {
					((Throwable) data).printStackTrace();
					// 验证码不正确
					// int resId = getStringRes(activity,
					// "smssdk_virificaition_code_wrong");
					// if (resId > 0) {
					// Toast.makeText(activity, resId,
					// Toast.LENGTH_SHORT).show();
					// }
					showToast(R.string.smssdk_virificaition_code_wrong);
					
					mFailedCount  ++ ;
					
					if(mFailedCount>=3){
						checkUser(phone); 
					}
					
				}
			}
		});
	}

	private void loginRegister(String cellphone) {
		startLoadingDialog();
		RequestService.getInstance().authcodeLoginReg(this, cellphone, LoginRegEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if(resultData.isOk()){
					LoginRegEntity entity = (LoginRegEntity) resultData;
					
					AccountManager.saveAccount(AuthCodeActivity.this, entity.getData());
					
					Intent intent = new Intent(AuthCodeActivity.this,MainTabActivity.class);
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
	
	/**
	 * 获取验证码成功后,的执行动作
	 * 
	 * @param result
	 * @param data
	 */
	private void afterGet(final int result, final Object data) {
		runOnUiThread(new Runnable() {
			public void run() {

				dismissLoadingDialog();

				if (result == SMSSDK.RESULT_COMPLETE) {
					// int resId = getStringRes(activity,
					// "smssdk_virificaition_code_sent");
					// if (resId > 0) {
					// Toast.makeText(activity, resId,
					// Toast.LENGTH_SHORT).show();
					// }
					showToast(R.string.smssdk_virificaition_code_sent);
					String unReceive = getString(R.string.smssdk_receive_msg, time);
					tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
					// btnSounds.setVisibility(View.GONE);
					time = RETRY_INTERVAL;
					countDown();
				} else {
					((Throwable) data).printStackTrace();
					Throwable throwable = (Throwable) data;
					// 根据服务器返回的网络错误，给toast提示
					try {
						JSONObject object = new JSONObject(throwable.getMessage());
						String des = object.optString("detail");
						if (!TextUtils.isEmpty(des)) {
							// Toast.makeText(activity, des,
							// Toast.LENGTH_SHORT).show();
							showToast(des);
							return;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					// / 如果木有找到资源，默认提示
					// int resId = getStringRes(activity,
					// "smssdk_network_error");
					// if (resId > 0) {
					// Toast.makeText(activity, resId,
					// Toast.LENGTH_SHORT).show();
					// }
					showToast(R.string.smssdk_network_error);
				}
			}
		});
	}

	/**
	 * 获取语音验证码成功后,的执行动作
	 * 
	 * @param result
	 * @param data
	 */
	private void afterGetVoice(final int result, final Object data) {
		runOnUiThread(new Runnable() {
			public void run() {

				dismissLoadingDialog();

				if (result == SMSSDK.RESULT_COMPLETE) {
					showToast(R.string.smssdk_send_sounds_success);
					// btnSounds.setVisibility(View.GONE);
				} else {
					((Throwable) data).printStackTrace();
					Throwable throwable = (Throwable) data;
					// 根据服务器返回的网络错误，给toast提示
					try {
						JSONObject object = new JSONObject(throwable.getMessage());
						String des = object.optString("detail");
						if (!TextUtils.isEmpty(des)) {
							// Toast.makeText(activity, des,
							// Toast.LENGTH_SHORT).show();
							showToast(des);
							return;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					// 如果木有找到资源，默认提示
					// int resId = getStringRes(activity,
					// "smssdk_network_error");
					// if (resId > 0) {
					// Toast.makeText(activity, resId,
					// Toast.LENGTH_SHORT).show();
					// }
					showToast(R.string.smssdk_network_error);
				}

			}
		});
	}

	/** 按返回键时，弹出的提示对话框 */
	private void showNotifyDialog() {
		final Dialog dialog = new Dialog(this, R.style.CommonDialog);
		dialog.setContentView(R.layout.smssdk_back_verify_dialog);
		TextView tv = (TextView) dialog.findViewById(R.id.tv_dialog_hint);
		tv.setText(R.string.smssdk_close_identify_page_dialog);
		Button waitBtn = (Button) dialog.findViewById(R.id.btn_dialog_ok);
		waitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button backBtn = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
//		backBtn.setText(R.string.smssdk_back);
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				finish();
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

//	@Override
//	public boolean onKeyEvent(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//			runOnUIThread(new Runnable() {
//				public void run() {
//					showNotifyDialog();
//				}
//			});
//			return true;
//		} else {
//			return false;
//		}
//	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			runOnUiThread(new Runnable() {
				public void run() {
					showNotifyDialog();
				}
			});
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

}
