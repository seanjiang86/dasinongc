package com.dasinong.app.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.view.TopbarView;

public class RecommendSMSActivity extends BaseActivity implements OnClickListener {
	private EditText et_phone_number1;
	private EditText et_phone_number2;
	private EditText et_phone_number3;
	private EditText et_phone_number4;
	private EditText et_phone_number5;
	private Button btn_submit_phone;
	private TopbarView topbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend_sms);

		topbar = (TopbarView) findViewById(R.id.topbar);
		et_phone_number1 = (EditText) findViewById(R.id.et_phone_number1);
		et_phone_number2 = (EditText) findViewById(R.id.et_phone_number2);
		et_phone_number3 = (EditText) findViewById(R.id.et_phone_number3);
		et_phone_number4 = (EditText) findViewById(R.id.et_phone_number4);
		et_phone_number5 = (EditText) findViewById(R.id.et_phone_number5);
		btn_submit_phone = (Button) findViewById(R.id.btn_submit_phone);

		// et_phone_number1.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before, int
		// count) {
		// }
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// String phone = s.toString();
		//
		// System.out.println(phone);
		//
		// String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
		//
		// System.out.println("手机号码是否匹配  " + phone.matches(regExp));
		//
		// System.out.println("所有是否匹配 " +
		// (TextUtils.isEmpty(phone)&&phone.matches(regExp)));
		//
		// if(!TextUtils.isEmpty(phone)&&phone.matches(regExp)){
		// System.out.println("我已经运行");
		// et_phone_number2.setVisibility(View.VISIBLE);
		// }
		// }
		// });

		initTopbar();

		btn_submit_phone.setOnClickListener(this);
	}

	private void initTopbar() {
		topbar.setCenterText("短信推荐");
		topbar.setLeftView(true, true);
	}
		
	// TODO MING 代码优化，phone加入list，进行循环
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_submit_phone) {
			String phoneNum1 = et_phone_number1.getText().toString().trim();
			String phoneNum2 = et_phone_number2.getText().toString().trim();
			String phoneNum3 = et_phone_number3.getText().toString().trim();
			String phoneNum4 = et_phone_number4.getText().toString().trim();
			String phoneNum5 = et_phone_number5.getText().toString().trim();

			if (TextUtils.isEmpty(phoneNum5 + phoneNum4 + phoneNum3 + phoneNum2 + phoneNum1)) {
				showToast("请至少填入一个手机号");
				return;
			}

			StringBuilder sb = new StringBuilder();
			String regExp = "^1[3|4|5|8][0-9]\\d{8}$";
			if (!TextUtils.isEmpty(phoneNum1)) {
				if (phoneNum1.matches(regExp)) {
					sb.append(phoneNum1);
				} else {
					showToast("手机号1填写有误");
					return;
				}
			}
			if (!TextUtils.isEmpty(phoneNum2)) {
				if (phoneNum2.matches(regExp)) {
					if (sb.length() != 0) {
						if(sb.indexOf(phoneNum2) == -1){
							sb.append("," + phoneNum2);
						}
					} else {
						sb.append(phoneNum2);
					}
				} else {
					showToast("手机号2填写有误");
					return;
				}
			}
			if (!TextUtils.isEmpty(phoneNum3)) {
				if (phoneNum3.matches(regExp)) {
					if (sb.length() != 0) {
						if(sb.indexOf(phoneNum3) == -1){
							sb.append("," + phoneNum3);
						}
					} else {
						sb.append(phoneNum3);
					}
				} else {
					showToast("手机号3填写有误");
					return;
				}
			}
			if (!TextUtils.isEmpty(phoneNum4)) {
				if (phoneNum4.matches(regExp)) {
					if (sb.length() != 0) {
						if(sb.indexOf(phoneNum4) == -1){
							sb.append("," + phoneNum4);
						}
					} else {
						sb.append(phoneNum4);
					}
				} else {
					showToast("手机号4填写有误");
					return;
				}
			}
			if (!TextUtils.isEmpty(phoneNum5)) {
				if (phoneNum5.matches(regExp)) {
					if (sb.length() != 0) {
						if(sb.indexOf(phoneNum5) == -1){
							sb.append("," + phoneNum5);
						}
					} else {
						sb.append(phoneNum5);
					}
				} else {
					showToast("手机号5填写有误");
					return;
				}
			}
			String phoneNums = sb.toString();
			sendPhoneNums(phoneNums);
		}
	}

	private void sendPhoneNums(String nums) {
		startLoadingDialog();
		RequestService.getInstance().refApp(this, nums, BaseEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				if(resultData.isOk()){
					showToast("邀请短信已发出");
					dismissLoadingDialog();
				}else{
					showToast(resultData.getMessage());
					dismissLoadingDialog();
				}
			}
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				showToast("网络连接异常，请检测网络连接");
				dismissLoadingDialog();
			}
		});
	}

}
