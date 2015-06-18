package com.dasinong.app.ui;

import org.hybridsquad.android.library.CropHelper;

import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.StringHelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MyInfoSetActivity extends BaseActivity {

	public static final int EDIT_PHONE = 100;
	public static final int EDIT_PASSWORD = EDIT_PHONE + 1;
	public static final int EDIT_NAME = EDIT_PASSWORD + 1;
	public static final int EDIT_ADDRESS = EDIT_NAME + 1;
	public static final int EDIT_HOME_PHONE = EDIT_ADDRESS + 1;
	public static final int EDIT_AUTH_PHONE = EDIT_HOME_PHONE + 1;

	private TopbarView mTopbarView;
	private EditText mEditText;

	private int editType = EDIT_PHONE;
	protected String info;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_my_info_set);
		
		initData();
		initView();
		setUpView();

	}

	private void initData() {
		editType = getIntent().getIntExtra("editType", EDIT_PHONE);
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mEditText = (EditText) this.findViewById(R.id.edittext_info);
	}

	private void setUpView() {
		switch (editType) {
		case EDIT_PHONE:
			mTopbarView.setCenterText("手机号码");
			mEditText.setHint("11位电话号码");
			mEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
			break;
		case EDIT_PASSWORD:
			mTopbarView.setCenterText("修改密码");
			mEditText.setHint("密码");
			mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			break;
		case EDIT_NAME:
			mTopbarView.setCenterText("真实姓名");
			mEditText.setHint("真实姓名");
			break;
		case EDIT_ADDRESS:
			mTopbarView.setCenterText("我的地址");
			mEditText.setHint("我的地址");
			break;
		case EDIT_HOME_PHONE:
			mTopbarView.setCenterText("家庭电话");
			mEditText.setHint("家庭电话");
			break;
		}
		mTopbarView.setLeftView(true, true);
		mTopbarView.setRightText("确定");
		mTopbarView.setRightClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				info = mEditText.getText().toString().trim();
				if(TextUtils.isEmpty(info)){
					showToast("请输入编辑信息");
					return;
				}
				
				if(editType == EDIT_PHONE){
					if(StringHelper.isPhoneNumber(info)){
						Intent intent = new Intent(MyInfoSetActivity.this,RegisterPhoneActivity.class);
						intent.putExtra("isAuthPhone", true);
						intent.putExtra("phone", info);
						startActivityForResult(intent, MyInfoSetActivity.EDIT_AUTH_PHONE);
					}else{
						showToast("请输入正确的手机号");
					}
					return;
				}
				
				Intent intent = new Intent();
				intent.putExtra("info", info);
				setResult(RESULT_OK,intent);
				finish();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case MyInfoSetActivity.EDIT_AUTH_PHONE:
				Intent intent = new Intent();
				intent.putExtra("info", info);
				setResult(RESULT_OK,intent);
				finish();
				break;
			}
		}

	}

}
