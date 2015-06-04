package com.dasinong.app.ui;

import com.dasinong.app.R;
import com.dasinong.app.ui.view.TopbarView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterCodeActivity extends BaseActivity implements OnClickListener {

	public static final String PHONE_NUMBER = "phone_number";
	
	private TopbarView mTopbarView;
	
	private TextView mPhoneText;
	private EditText mAuthcodeEdit;
	private Button mNextButton;
	private TextView mTimerText;
	
	private String mPhoneNumber;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_code);
		
		
		initData();
		initView();
		setUpView();
		startTimer();
	}

	private void startTimer() {
		mHandler.removeMessages(0);
		mHandler.sendMessageDelayed(mHandler.obtainMessage(0), 1 * 1000);
	}

	private void initData() {
		mPhoneNumber = getIntent().getStringExtra(PHONE_NUMBER);
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mPhoneText = (TextView) this.findViewById(R.id.textview_phone_number);
		mAuthcodeEdit = (EditText) this.findViewById(R.id.edittext_authcode);
		mTimerText = (TextView) this.findViewById(R.id.textview_timer);
		mNextButton = (Button) this.findViewById(R.id.button_next);
	}

	private void setUpView() {
		mTopbarView.setCenterText("填写验证码");
		mTopbarView.setLeftView(true, true);
		
		mPhoneText.setText(mPhoneNumber);
		
		mNextButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_next:
			showToast("哈哈");
			break;
		}
	}
	
	
	private static final int DEFAULT_SMS_VERIFYCODE_COUNTDOWN_TIME = 10;
	private int mUpdateCount = DEFAULT_SMS_VERIFYCODE_COUNTDOWN_TIME;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				--mUpdateCount;
				if (mUpdateCount > 0) {
					mTimerText.setText(String.format("接收短信大约需要%d秒", mUpdateCount));
					sendMessageDelayed(obtainMessage(0), 1 * 1000);
//					mGetCodeButton.setEnabled(false);
				} else {
					mTimerText.setText("收不到验证码？");
					mUpdateCount = DEFAULT_SMS_VERIFYCODE_COUNTDOWN_TIME;
					mTimerText.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							showToast("收不到验证码");
						}
					});
					
//					mGetCodeButton.setEnabled(true);
				}
			}
		}
	};
	
}
