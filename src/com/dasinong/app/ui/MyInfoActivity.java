package com.dasinong.app.ui;

import java.io.File;

import com.dasinong.app.R;
import com.dasinong.app.ui.view.TopbarView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyInfoActivity extends BaseActivity implements OnClickListener {

	private TopbarView mTopbarView;

	private RelativeLayout mHeadviewLayout;
	private ImageView mHeadviewImage;

	private RelativeLayout mPhoneLayout;
	private TextView mPhoneText;
	private Button mAuthPhoneButton;

	private RelativeLayout mResetPwdLayout;

	private RelativeLayout mNameLayout;
	private TextView mNameText;

	private RelativeLayout mAddressLayout;
	private TextView mAddressText;

	private RelativeLayout mHomephoneLayout;
	private TextView mHomephoneText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);

		initView();
		setUpView();
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mHeadviewLayout = (RelativeLayout) this.findViewById(R.id.layout_headview);
		mHeadviewImage = (ImageView) this.findViewById(R.id.imageview_headview);
		mPhoneLayout = (RelativeLayout) this.findViewById(R.id.layout_phoneNumber);
		mPhoneText = (TextView) this.findViewById(R.id.textview_phone_number);
		mAuthPhoneButton = (Button) this.findViewById(R.id.button_auth_phone);
		mResetPwdLayout = (RelativeLayout) this.findViewById(R.id.layout_reset_password);
		mNameLayout = (RelativeLayout) this.findViewById(R.id.layout_name);
		mNameText = (TextView) this.findViewById(R.id.textview_name);
		mAddressLayout = (RelativeLayout) this.findViewById(R.id.layout_address);
		mAddressText = (TextView) this.findViewById(R.id.textview_address);
		mHomephoneLayout = (RelativeLayout) this.findViewById(R.id.layout_home_phone);
		mHomephoneText = (TextView) this.findViewById(R.id.textview_home_phone);
	}

	private void setUpView() {
		mTopbarView.setCenterText("个人信息");
		mTopbarView.setLeftView(true, true);

		mHeadviewLayout.setOnClickListener(this);
		mPhoneLayout.setOnClickListener(this);
		mAuthPhoneButton.setOnClickListener(this);
		mResetPwdLayout.setOnClickListener(this);
		mNameLayout.setOnClickListener(this);
		mAddressLayout.setOnClickListener(this);
		mHomephoneLayout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_headview:

			break;
		case R.id.layout_phoneNumber:

			break;
		case R.id.button_auth_phone:

			break;
		case R.id.layout_reset_password:

			break;
		case R.id.layout_name:

			break;
		case R.id.layout_address:

			break;
		case R.id.layout_home_phone:

			break;
		}
	}

	private void upload(String path) {
	  
	  
	}
}
