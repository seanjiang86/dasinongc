package com.dasinong.app.ui;

import java.util.List;

import org.hybridsquad.android.library.CropHelper;

import com.dasinong.app.R;
import com.dasinong.app.database.city.dao.impl.CityDaoImpl;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.IsPassSetEntity;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.StringHelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class MyInfoSetActivity extends BaseActivity {

	public static final int EDIT_PHONE = 100;
	public static final int EDIT_PASSWORD = EDIT_PHONE + 1;
	public static final int EDIT_NAME = EDIT_PASSWORD + 1;
	public static final int EDIT_ADDRESS = EDIT_NAME + 1;
	public static final int EDIT_HOME_PHONE = EDIT_ADDRESS + 1;
	public static final int EDIT_AUTH_PHONE = EDIT_HOME_PHONE + 1;

	private TopbarView mTopbarView;
	private EditText mEditText;
	private EditText mPwdEdit;
	private EditText mSurePwdEdit;

	private View mSelectAreaLayout;
	private Spinner mProvinceSp;
	private Spinner mCitySp;
	private Spinner mAreaSp;
	private Spinner mTownsSp;
	private Spinner mVillageSp;

	private int editType = EDIT_PHONE;
	private boolean isNewPwd = false;
	protected String info;

	private List<String> province;
	private CityDaoImpl dao;
	protected IsPassSetEntity entity;
	
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
		isNewPwd = getIntent().getBooleanExtra("isNewPwd", false);
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		mEditText = (EditText) this.findViewById(R.id.edittext_info);
		mPwdEdit = (EditText) this.findViewById(R.id.edittext_password_new);
		mSurePwdEdit = (EditText) this.findViewById(R.id.edittext_password_sure);

		mSelectAreaLayout = this.findViewById(R.id.layout_select_area);
		mProvinceSp = (Spinner) this.findViewById(R.id.spinner_province);
		mCitySp = (Spinner) this.findViewById(R.id.spinner_city);
		mAreaSp = (Spinner) this.findViewById(R.id.spinner_area);
		mTownsSp = (Spinner) this.findViewById(R.id.spinner_towns);
		mVillageSp = (Spinner) this.findViewById(R.id.spinner_village);
	}

	private void setUpView() {
		switch (editType) {
		case EDIT_PHONE:
			mEditText.setVisibility(View.VISIBLE);

			mTopbarView.setCenterText("手机号码");
			mEditText.setHint("11位电话号码");
			mEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
			break;
		case EDIT_PASSWORD:
			
			isPassSet();
			
			break;
		case EDIT_NAME:
			mEditText.setVisibility(View.VISIBLE);

			mEditText.setVisibility(View.VISIBLE);
			mTopbarView.setCenterText("真实姓名");
			mEditText.setHint("真实姓名");
			break;
		case EDIT_ADDRESS:
			mSelectAreaLayout.setVisibility(View.VISIBLE);
			mEditText.setVisibility(View.VISIBLE);
			mTopbarView.setCenterText("我的地址");
			mEditText.setHint("您的详细地址");
			initProvince();
			break;
		case EDIT_HOME_PHONE:
			mEditText.setVisibility(View.VISIBLE);

			mTopbarView.setCenterText("家庭电话");
			mEditText.setHint("家庭电话");
			break;
		}
		mTopbarView.setLeftView(true, true);
		mTopbarView.setRightText("确定");
		mTopbarView.setRightClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				info = mEditText.getText().toString().trim();
//				if (TextUtils.isEmpty(info)) {
//					showToast("请输入编辑信息");
//					return;
//				}
//
//				if (editType == EDIT_PHONE) {
//					if (StringHelper.isPhoneNumber(info)) {
//						Intent intent = new Intent(MyInfoSetActivity.this, RegisterPhoneActivity.class);
//						intent.putExtra("isAuthPhone", true);
//						intent.putExtra("phone", info);
//						startActivityForResult(intent, MyInfoSetActivity.EDIT_AUTH_PHONE);
//					} else {
//						showToast("请输入正确的手机号");
//					}
//					return;
//				}
//
//				Intent intent = new Intent();
//				intent.putExtra("info", info);
//				setResult(RESULT_OK, intent);
//				finish();
				uploadInfo();
			}
		});
	}

	private void isPassSet() {
		String phone = SharedPreferencesHelper.getString(this, Field.USER_PHONE, "");
		startLoadingDialog();
		RequestService.getInstance().isPassSet(this, phone , IsPassSetEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if(resultData.isOk()){
					entity = (IsPassSetEntity) resultData;
					
					if(entity.isData() && !isNewPwd){
						mEditText.setVisibility(View.VISIBLE);
					}else{
						mEditText.setVisibility(View.GONE);
					}
					mPwdEdit.setVisibility(View.VISIBLE);
					mSurePwdEdit.setVisibility(View.VISIBLE);

					mTopbarView.setCenterText("修改密码");
					mEditText.setHint("原密码");
					mPwdEdit.setHint("新密码");
					mSurePwdEdit.setHint("确认新密码");
					mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					mPwdEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					mSurePwdEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					
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

	protected void uploadInfo() {
		String userName = "";
		String password = "";
		String address = "";
		String telephone = "";

		switch (editType) {
		case EDIT_PHONE:
			info = mEditText.getText().toString().trim();
			if (TextUtils.isEmpty(info)) {
				showToast("请输入手机号");
				return;
			}
			if (StringHelper.isPhoneNumber(info)) {
				Intent intent = new Intent(MyInfoSetActivity.this, RegisterPhoneActivity.class);
				intent.putExtra("isAuthPhone", true);
				intent.putExtra("phone", info);
				startActivityForResult(intent, MyInfoSetActivity.EDIT_AUTH_PHONE);
			} else {
				showToast("请输入正确的手机号");
			}
			
			return;
		case EDIT_PASSWORD:
			String oPassword = "";
			if(!isNewPwd && entity != null && entity.isData()){
				oPassword = mEditText.getText().toString().trim();
				if (TextUtils.isEmpty(oPassword)) {
					showToast("请输入原密码");
					return;
				}
			}
			String nPassword = mPwdEdit.getText().toString().trim();
			if(TextUtils.isEmpty(nPassword)){
				showToast("请输入新密码");
				return;
			}
			String surePwd = mSurePwdEdit.getText().toString().trim();
			if(TextUtils.isEmpty(surePwd)){
				showToast("请确认新密码");
				return;
			}
			if(!nPassword.equals(surePwd)){
				showToast("两次密码不一致");
				return;
			}
			if(!StringHelper.isPassword(nPassword)){
				showToast("新密码不合格");
				return;
			}
//			password = info;
			resetPwd(oPassword,nPassword);
			return;
		case EDIT_NAME:
			info = mEditText.getText().toString().trim();
			if (TextUtils.isEmpty(info)) {
				showToast("请输入真实姓名");
				return;
			}
			userName = info;
			break;
		case EDIT_ADDRESS:
			if (mProvinceSp.getSelectedItemPosition() == 0) {
				showToast("请选择省");
				return;
			}
			if (mCitySp.getSelectedItemPosition() == 0) {
				showToast("请选择市");
				return;
			}
			if (mAreaSp.getSelectedItemPosition() == 0) {
				showToast("请选择区");
				return;
			}
			if (mTownsSp.getSelectedItemPosition() == 0) {
				showToast("请选择镇");
				return;
			}
			String editText = mEditText.getText().toString().trim();
			if (TextUtils.isEmpty(editText)) {
				showToast("请输入您的详细地址");
				return;
			}
			
			String province = (String) mProvinceSp.getSelectedItem();
			String city = (String) mCitySp.getSelectedItem();
			String area = (String) mAreaSp.getSelectedItem();
			String towns = (String) mTownsSp.getSelectedItem();

			info = province + " " + city + " " + area + " " + towns+" "+editText;

			address = info;
			
			break;
		case EDIT_HOME_PHONE:
			info = mEditText.getText().toString().trim();
			if (TextUtils.isEmpty(info)) {
				showToast("请输入家庭电话");
				return;
			}
			telephone = info;
			break;
		}

		requestUpload(userName, "", password, address, telephone);
	}

	private void resetPwd(String oPassword, String nPassword) {
		startLoadingDialog();
		RequestService.getInstance().resetPwd(this, oPassword, nPassword, BaseEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if(resultData.isOk()){
					showToast("密码更改成功");
					Intent intent = new Intent();
					intent.putExtra("info", info);
					setResult(RESULT_OK, intent);
					finish();
				}else{
					showToast(resultData.getMessage());
				}
			}
			
			@Override
			public void onFailed(int requestCode, Exception error, String msg) {
				dismissLoadingDialog();
				showToast(msg);
			}
		});
	}

	private void requestUpload(String userName, String cellphone, String password, String address, String telephone) {
		startLoadingDialog();
		RequestService.getInstance().uploadInfo(this, userName, cellphone, password, address, telephone, BaseEntity.class,
				new RequestListener() {

					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						dismissLoadingDialog();
						if (resultData.isOk()) {
							showToast("更新成功");
							Intent intent = new Intent();
							intent.putExtra("info", info);
							setResult(RESULT_OK, intent);
							finish();
						} else {
							showToast(resultData.getMessage());
						}
					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {
						dismissLoadingDialog();
					}
				});
	}

	private void initProvince() {
		new Thread() {
			public void run() {
				dao = new CityDaoImpl(MyInfoSetActivity.this);
				province = dao.getProvince();
				province.add(0, "请选择省");
				runOnUiThread(new Runnable() {
					public void run() {
						setProvince();
					}
				});
			};
		}.start();
	}

	private void setProvince() {
		mProvinceSp.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, province));
		mProvinceSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String province = (String) mProvinceSp.getSelectedItem();
				setCity(province);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	protected void setCity(String province2) {
		List<String> city = dao.getCity(province2);
		city.add(0, "请选择市");
		mCitySp.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, city));
		mCitySp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String city = (String) mCitySp.getSelectedItem();
				setArea(city);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		// mAreaSp.setAdapter(null);
		// mTownsSp.setAdapter(null);
	}

	protected void setArea(String city) {
		List<String> county = dao.getCounty(city);
		county.add(0, "请选择区");
		mAreaSp.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, county));
		mAreaSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String area = (String) mAreaSp.getSelectedItem();
				setTowns(area);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		// mTownsSp.setAdapter(null);
	}

	protected void setTowns(String area) {
		List<String> county = dao.getDistrict(area);
		county.add(0, "请选择镇");
		mTownsSp.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, county));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case MyInfoSetActivity.EDIT_AUTH_PHONE:
//				Intent intent = new Intent();
//				intent.putExtra("info", info);
//				setResult(RESULT_OK, intent);
//				finish();
				
				requestUpload("",info, "", "", "");
				
				break;
			}
		}

	}

}
