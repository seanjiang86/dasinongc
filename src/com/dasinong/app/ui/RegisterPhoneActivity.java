/*
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2014年 mob.com. All rights reserved.
 */
package com.dasinong.app.ui;

import static com.mob.tools.utils.R.getBitmapRes;
import static com.mob.tools.utils.R.getIdRes;
import static com.mob.tools.utils.R.getLayoutRes;
import static com.mob.tools.utils.R.getStringRes;
import static com.mob.tools.utils.R.getStyleRes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.UserInterruptException;

import com.dasinong.app.R;
import com.dasinong.app.ui.view.TopbarView;
import com.mob.tools.FakeActivity;

/** 短信注册页面 */
public class RegisterPhoneActivity extends BaseActivity implements OnClickListener, TextWatcher {

	// 默认使用中国区号
	private static final String DEFAULT_COUNTRY_ID = "42";

	// private EventHandler callback;

	private TopbarView mTopbarView;
	private EditText mPhoneEdit;
	private Button mNextButton;
	private TextView mAgreementText;
	
//	// 国家
//	private TextView tvCountry;
//	// 手机号码
//	private EditText etPhoneNum;
//	// 国家编号
//	private TextView tvCountryNum;
//	// clear 号码
//	private ImageView ivClear;

//	private String currentId;
	private String currentCode;
	private EventHandler handler;
	// 国家号码规则
	private HashMap<String, String> countryRules;
	private OnSendMessageHandler osmHandler;

	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY = "7ddca5c23518";
	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "674b27af7f64b70e317f147098bc782b";

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

		// 获取新好友个数
		// showDialog();
		// SMSSDK.getNewFriendsCount();
	}

	// public void setRegisterCallback(EventHandler callback) {
	// this.callback = callback;
	// }

	public void setOnSendMessageHandler(OnSendMessageHandler h) {
		osmHandler = h;
	}

	// public void show(Context context) {
	// super.show(context, null);
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);

		initSDK();
		initView();

//		currentId = DEFAULT_COUNTRY_ID;

//		View viewCountry = findViewById(R.id.rl_country);
//		btnNext = (Button) findViewById(R.id.btn_next);

//		tvCountry = (TextView) findViewById(R.id.tv_country);

		String[] country = getCurrentCountry();
		// String[] country = SMSSDK.getCountry(currentId);
		if (country != null) {
			currentCode = country[1];
//			tvCountry.setText(country[0]);
		}
//		tvCountryNum = (TextView) findViewById(R.id.tv_country_num);
//		tvCountryNum.setText("+" + currentCode);

//		etPhoneNum = (EditText) findViewById(R.id.et_write_phone);
//		etPhoneNum.setText("");
//		etPhoneNum.addTextChangedListener(this);
//		etPhoneNum.requestFocus();
//		if (etPhoneNum.getText().length() > 0) {
//			btnNext.setEnabled(true);
//			ivClear = (ImageView) findViewById(R.id.iv_clear);
//			ivClear.setVisibility(View.VISIBLE);
//		}

//		ivClear = (ImageView) findViewById(R.id.iv_clear);
//
//		// llBack.setOnClickListener(this);
//		btnNext.setOnClickListener(this);
//		ivClear.setOnClickListener(this);
//		viewCountry.setOnClickListener(this);

		handler = new EventHandler() {
			@SuppressWarnings("unchecked")
			public void afterEvent(final int event, final int result, final Object data) {

				runOnUiThread(new Runnable() {
					public void run() {
						
						
						if (result == SMSSDK.RESULT_COMPLETE) {
							if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
								// 请求支持国家列表
								onCountryListGot((ArrayList<HashMap<String, Object>>) data);
							} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
								// 请求验证码后，跳转到验证码填写页面
								afterVerificationCodeRequested();
							}
						} else {
							
							dismissLoadingDialog();
							
							if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE && data != null
									&& (data instanceof UserInterruptException)) {
								// 由于此处是开发者自己决定要中断发送的，因此什么都不用做
								return;
							}

							// 根据服务器返回的网络错误，给toast提示
							try {
								((Throwable) data).printStackTrace();
								Throwable throwable = (Throwable) data;

								JSONObject object = new JSONObject(throwable.getMessage());
								String des = object.optString("detail");
								if (!TextUtils.isEmpty(des)) {
									// Toast.makeText(RegisterPage.this, des,
									// Toast.LENGTH_SHORT).show();
									showToast(des);
									return;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							// 如果木有找到资源，默认提示
							// int resId = getStringRes(activity,
							// "smssdk_network_error");
							// if (resId > 0) {
							showToast(R.string.smssdk_network_error);
							// Toast.makeText(activity, resId,
							// Toast.LENGTH_SHORT).show();
							// }
						}
					}
				});
			}
		};

	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mPhoneEdit = (EditText) this.findViewById(R.id.edittext_phone_number);
		mAgreementText = (TextView) this.findViewById(R.id.textview_agreement);

		mNextButton = (Button) this.findViewById(R.id.button_next);
		
		mTopbarView.setCenterText("填写手机号");
		mTopbarView.setLeftView(true, true);
		
		mPhoneEdit.setText("");
		mPhoneEdit.addTextChangedListener(this);
		mPhoneEdit.requestFocus();
		if (mPhoneEdit.getText().length() > 0) {
			mPhoneEdit.setEnabled(true);
//			ivClear = (ImageView) findViewById(R.id.iv_clear);
//			ivClear.setVisibility(View.VISIBLE);
		}
		
		mNextButton.setOnClickListener(this);
	}

	public void onCreate() {
		// int resId = getLayoutRes(activity, "smssdk_regist_page");
		// if (resId > 0) {
		// activity.setContentView(resId);
		// currentId = DEFAULT_COUNTRY_ID;

		// resId = getIdRes(activity, "ll_back");
		// View llBack = activity.findViewById(resId);
		// resId = getIdRes(activity, "tv_title");
		// TextView tv = (TextView) activity.findViewById(resId);
		// resId = getStringRes(activity, "smssdk_regist");
		// if (resId > 0) {
		// tv.setText(resId);
		// }
		// resId = getIdRes(activity, "rl_country");
		// View viewCountry = activity.findViewById(resId);
		// resId = getIdRes(activity, "btn_next");
		// btnNext = (Button) activity.findViewById(resId);
		//
		// resId = getIdRes(activity, "tv_country");
		// tvCountry = (TextView) activity.findViewById(resId);
		//
		// String[] country = getCurrentCountry();
		// // String[] country = SMSSDK.getCountry(currentId);
		// if (country != null) {
		// currentCode = country[1];
		// tvCountry.setText(country[0]);
		// }
		// resId = getIdRes(activity, "tv_country_num");
		// tvCountryNum = (TextView) activity.findViewById(resId);
		// tvCountryNum.setText("+" + currentCode);
		//
		// resId = getIdRes(activity, "et_write_phone");
		// etPhoneNum = (EditText) activity.findViewById(resId);
		// etPhoneNum.setText("");
		// etPhoneNum.addTextChangedListener(this);
		// etPhoneNum.requestFocus();
		// if (etPhoneNum.getText().length() > 0) {
		// btnNext.setEnabled(true);
		// resId = getIdRes(activity, "iv_clear");
		// ivClear = (ImageView) activity.findViewById(resId);
		// ivClear.setVisibility(View.VISIBLE);
		// resId = getBitmapRes(activity, "smssdk_btn_enable");
		// if (resId > 0) {
		// btnNext.setBackgroundResource(resId);
		// }
		// }
		//
		// resId = getIdRes(activity, "iv_clear");
		// ivClear = (ImageView) activity.findViewById(resId);
		//
		// // llBack.setOnClickListener(this);
		// btnNext.setOnClickListener(this);
		// ivClear.setOnClickListener(this);
		// viewCountry.setOnClickListener(this);

		// handler = new EventHandler() {
		// @SuppressWarnings("unchecked")
		// public void afterEvent(final int event, final int result,
		// final Object data) {
		// runOnUIThread(new Runnable() {
		// public void run() {
		// if (pd != null && pd.isShowing()) {
		// pd.dismiss();
		// }
		// if (result == SMSSDK.RESULT_COMPLETE) {
		// if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
		// // 请求支持国家列表
		// onCountryListGot((ArrayList<HashMap<String, Object>>) data);
		// } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
		// // 请求验证码后，跳转到验证码填写页面
		// afterVerificationCodeRequested();
		// }
		// } else {
		// if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE
		// && data != null
		// && (data instanceof UserInterruptException)) {
		// // 由于此处是开发者自己决定要中断发送的，因此什么都不用做
		// return;
		// }
		//
		// // 根据服务器返回的网络错误，给toast提示
		// try {
		// ((Throwable) data).printStackTrace();
		// Throwable throwable = (Throwable) data;
		//
		// JSONObject object = new JSONObject(
		// throwable.getMessage());
		// String des = object.optString("detail");
		// if (!TextUtils.isEmpty(des)) {
		// Toast.makeText(activity, des, Toast.LENGTH_SHORT).show();
		// return;
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// // 如果木有找到资源，默认提示
		// int resId = getStringRes(activity,
		// "smssdk_network_error");
		// if (resId > 0) {
		// Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
		// }
		// }
		// }
		// });
		// }
		// };
		// }

	}

	private String[] getCurrentCountry() {
		String mcc = getMCC();
		String[] country = null;
		if (!TextUtils.isEmpty(mcc)) {
			country = SMSSDK.getCountryByMCC(mcc);
		}

		if (country == null) {
			Log.w("SMSSDK", "no country found by MCC: " + mcc);
			country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
		}
		return country;
	}

	private String getMCC() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// 返回当前手机注册的网络运营商所在国家的MCC+MNC. 如果没注册到网络就为空.
		String networkOperator = tm.getNetworkOperator();
		if (!TextUtils.isEmpty(networkOperator)) {
			return networkOperator;
		}

		// 返回SIM卡运营商所在国家的MCC+MNC. 5位或6位. 如果没有SIM卡返回空
		return tm.getSimOperator();
	}

	public void onResume() {
		super.onResume();
		SMSSDK.registerEventHandler(handler);
	}

	public void onPause() {
		super.onPause();
		SMSSDK.unregisterEventHandler(handler);
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() > 0) {
			mNextButton.setEnabled(true);
//			ivClear.setVisibility(View.VISIBLE);
			// int resId = getBitmapRes(activity, "smssdk_btn_enable");
			// if (resId > 0) {
			// btnNext.setBackgroundResource(resId);
			// }
		} else {
			mNextButton.setEnabled(false);
//			ivClear.setVisibility(View.GONE);
			// int resId = getBitmapRes(activity, "smssdk_btn_disenable");
			// if (resId > 0) {
			// btnNext.setBackgroundResource(resId);
			// }
		}
	}

	public void afterTextChanged(Editable s) {

	}

	public void onClick(View v) {

		switch (v.getId()) {
		// case R.id.ll_back:
		// finish();
		// break;
		// case R.id.rl_country:
		// 国家列表
		// CountryPage countryPage = new CountryPage();
		// countryPage.setCountryId(currentId);
		// countryPage.setCountryRuls(countryRules);
		// countryPage.showForResult(RegisterPage.this, null, this);
		// break;
		case R.id.button_next:
			
			startLoadingDialog();
			if (countryRules == null || countryRules.size() <= 0) {
				SMSSDK.getSupportedCountries();
			} else {
				String phone = mPhoneEdit.getText().toString().trim().replaceAll("\\s*", "");
//				String code = tvCountryNum.getText().toString().trim();
				checkPhoneNum(phone);
			}
			break;
//		case R.id.iv_clear:
//			// 清除电话号码输入框
//			mPhoneEdit.getText().clear();
//			break;
		}

		// int id = v.getId();
		// int id_ll_back = getIdRes(activity, "ll_back");
		// int id_rl_country = getIdRes(activity, "rl_country");
		// int id_btn_next = getIdRes(activity, "btn_next");
		// int id_iv_clear = getIdRes(activity, "iv_clear");

		// if (id == id_ll_back) {
		// finish();
		// } else if (id == id_rl_country) {
		// // 国家列表
		// CountryPage countryPage = new CountryPage();
		// countryPage.setCountryId(currentId);
		// countryPage.setCountryRuls(countryRules);
		// countryPage.showForResult(activity, null, this);
		// } else if (id == id_btn_next) {
		// // 请求发送短信验证码
		// if (countryRules == null || countryRules.size() <= 0) {
		// if (pd != null && pd.isShowing()) {
		// pd.dismiss();
		// }
		// pd = CommonDialog.ProgressDialog(activity);
		// if (pd != null) {
		// pd.show();
		// }
		//
		// SMSSDK.getSupportedCountries();
		// } else {
		// String phone =
		// etPhoneNum.getText().toString().trim().replaceAll("\\s*", "");
		// String code = tvCountryNum.getText().toString().trim();
		// checkPhoneNum(phone, code);
		// }
		// } else if (id == id_iv_clear) {
		// // 清除电话号码输入框
		// etPhoneNum.getText().clear();
		// }
	}

//	@SuppressWarnings("unchecked")
//	public void onResult(HashMap<String, Object> data) {
//		if (data != null) {
//			int page = (Integer) data.get("page");
//			if (page == 1) {
//				// 国家列表返回
////				currentId = (String) data.get("id");
////				countryRules = (HashMap<String, String>) data.get("rules");
////				String[] country = SMSSDK.getCountry(currentId);
////				if (country != null) {
////					currentCode = country[1];
////					tvCountryNum.setText("+" + currentCode);
////					tvCountry.setText(country[0]);
////				}
//			} else if (page == 2) {
//				// 验证码校验返回
//				// Object res = data.get("res");
//				HashMap<String, Object> phoneMap = (HashMap<String, Object>) data.get("phone");
//				// if (res != null && phoneMap != null) {
//				// int resId = getStringRes(activity,
//				// "smssdk_your_ccount_is_verified");
//				// if (resId > 0) {
//				// Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
//				// }
//				showToast(R.string.smssdk_your_ccount_is_verified);
//
//				// if (callback != null) {
//				// callback.afterEvent(
//				// SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE,
//				// SMSSDK.RESULT_COMPLETE, phoneMap);
//				// }
//
//				Intent intent = new Intent();
//				// String country = (String) phoneMap.get("country");
//				// String phone = (String) phoneMap.get("phone");
//				intent.putExtra("country", (String) phoneMap.get("country"));
//				intent.putExtra("phone", (String) phoneMap.get("phone"));
//				setResult(RESULT_OK, intent);
//
//				finish();
//				// }
//			}
//		}
//	}

	private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
		// 解析国家列表
		for (HashMap<String, Object> country : countries) {
			String code = (String) country.get("zone");
			String rule = (String) country.get("rule");
			if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
				continue;
			}

			if (countryRules == null) {
				countryRules = new HashMap<String, String>();
			}
			countryRules.put(code, rule);
		}
		// 检查手机号码
		String phone = mPhoneEdit.getText().toString().trim().replaceAll("\\s*", "");
//		String code = tvCountryNum.getText().toString().trim();
		checkPhoneNum(phone);
	}

	/** 分割电话号码 */
	private String splitPhoneNum(String phone) {
		StringBuilder builder = new StringBuilder(phone);
		builder.reverse();
		for (int i = 4, len = builder.length(); i < len; i += 5) {
			builder.insert(i, ' ');
		}
		builder.reverse();
		return builder.toString();
	}

	/** 检查电话号码 */
	private void checkPhoneNum(final String phone) {
		if (currentCode.startsWith("+")) {
			currentCode = currentCode.substring(1);
		}

		if (TextUtils.isEmpty(phone)) {
			// int resId = getStringRes(activity, "smssdk_write_mobile_phone");
			// if (resId > 0) {
			// Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
			// }
			showToast(R.string.smssdk_write_mobile_phone);
			return;
		}

		String rule = countryRules.get(currentCode);
		Pattern p = Pattern.compile(rule);
		Matcher m = p.matcher(phone);
//		int resId = 0;
		if (!m.matches()) {
			// resId = getStringRes(activity,
			// "smssdk_write_right_mobile_phone");
			// if (resId > 0) {
			// Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
			// }

			showToast(R.string.smssdk_write_right_mobile_phone);
			return;
		}
		// showDialog(phone, code);
		dismissLoadingDialog();
		startLoadingDialog();
		new Thread(){
			public void run() {
				SMSSDK.getVerificationCode(currentCode, phone.trim(), osmHandler);
			};
		}.start();
	}

//	/** 是否请求发送验证码，对话框 */
//	public void showDialog(final String phone, final String code) {
		// int resId = getStyleRes(activity, "CommonDialog");
		// if (resId > 0) {
		// final String phoneNum = "+" + code + " " + splitPhoneNum(phone);
		// final Dialog dialog = new Dialog(getContext(), resId);
		// resId = getLayoutRes(activity, "smssdk_send_msg_dialog");
		// if (resId > 0) {
		// dialog.setContentView(resId);
		// resId = getIdRes(activity, "tv_phone");
		// ((TextView) dialog.findViewById(resId)).setText(phoneNum);
		// resId = getIdRes(activity, "tv_dialog_hint");
		// TextView tv = (TextView) dialog.findViewById(resId);
		// resId = getStringRes(activity, "smssdk_make_sure_mobile_detail");
		// if (resId > 0) {
		// String text = getContext().getString(resId);
		// tv.setText(Html.fromHtml(text));
		// }
		// resId = getIdRes(activity, "btn_dialog_ok");
		// if (resId > 0) {
		// ((Button) dialog.findViewById(resId))
		// .setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// // 跳转到验证码页面
		// dialog.dismiss();
		//
		// if (pd != null && pd.isShowing()) {
		// pd.dismiss();
		// }
		// pd = CommonDialog.ProgressDialog(activity);
		// if (pd != null) {
		// pd.show();
		// }
		// Log.e("verification phone ==>>", phone);
		// SMSSDK.getVerificationCode(code,
		// phone.trim(), osmHandler);
		// }
		// });
		// }
		// resId = getIdRes(activity, "btn_dialog_cancel");
		// if (resId > 0) {
		// ((Button) dialog.findViewById(resId)).setOnClickListener(new
		// OnClickListener() {
		// public void onClick(View v) {
		// dialog.dismiss();
		// }
		// });
		// }
		// dialog.setCanceledOnTouchOutside(true);
		// dialog.show();
		// }
		// }
//		int resId = R.style.CommonDialog;
//		if (resId > 0) {
//			final String phoneNum = "+" + code + " " + splitPhoneNum(phone);
//			final Dialog dialog = new Dialog(this, resId);
//			if (resId > 0) {
//				dialog.setContentView(R.layout.smssdk_send_msg_dialog);
//				((TextView) dialog.findViewById(R.id.tv_phone)).setText(phoneNum);
//				TextView tv = (TextView) dialog.findViewById(R.id.tv_dialog_hint);
//				if (resId > 0) {
//					String text = getString(R.string.smssdk_make_sure_mobile_detail);
//					tv.setText(Html.fromHtml(text));
//				}
//				if (resId > 0) {
//					((Button) dialog.findViewById(R.id.btn_dialog_ok)).setOnClickListener(new OnClickListener() {
//						public void onClick(View v) {
//							// 跳转到验证码页面
//							dialog.dismiss();
//
//							startLoadingDialog();
//
//							Log.e("verification phone ==>>", phone);
//							SMSSDK.getVerificationCode(code, phone.trim(), osmHandler);
//						}
//					});
//				}
//				if (resId > 0) {
//					((Button) dialog.findViewById(R.id.btn_dialog_cancel)).setOnClickListener(new OnClickListener() {
//						public void onClick(View v) {
//							dialog.dismiss();
//						}
//					});
//				}
//				dialog.setCanceledOnTouchOutside(true);
//				dialog.show();
//			}
//		}
//	}

	/** 请求验证码后，跳转到验证码填写页面 */
	private void afterVerificationCodeRequested() {
		
		dismissLoadingDialog();
		
		String phone = mPhoneEdit.getText().toString().trim().replaceAll("\\s*", "");
		String code = currentCode;
		if (code.startsWith("+")) {
			code = code.substring(1);
		}
		String formatedPhone = "+" + code + " " + splitPhoneNum(phone);
		// 验证码页面
		// IdentifyNumPage page = new IdentifyNumPage();
		// page.setPhone(phone, code, formatedPhone);
		// page.showForResult(this, null, this);

		Intent intent = new Intent(this, AuthCodeActivity.class);
		intent.putExtra("phone", phone);
		intent.putExtra("code", code);
		intent.putExtra("formatedPhone", formatedPhone);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent intent) {
		if (RESULT_OK == arg1) {
			setResult(RESULT_OK, intent);
			finish();
		}
		super.onActivityResult(arg0, arg1, intent);
	}

}
