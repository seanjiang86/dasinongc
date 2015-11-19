package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.city.dao.impl.CityDaoImpl;
import com.dasinong.app.database.variety.dao.impl.VarietyDaoImp;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.SmsSubDetailEntity;
import com.dasinong.app.entity.SmsSubscribeItem;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.StringHelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class SmsSubscribeModifiActivity extends BaseActivity implements OnClickListener {

	private TopbarView mTopbarView;
	private Spinner spinner;
	private Spinner mProvinceSp;
	private Spinner mCitySp;
	private Spinner mAreaSp;
	private Spinner mTownsSp;
	private Spinner mVillageSp;
	
	private EditText mNameEdit;
	private EditText mPhoneEdit;
	private EditText mAreaEdit;
	private CheckBox mIsAgriWeatherRb;
	private CheckBox mIsNatAlterRb;
	private CheckBox mIsRiceHelperRb;
	
	private Button mSaveButton;
	private Button mCancelButton;
	
	private List<String> province;
	private CityDaoImpl dao;
	protected VarietyDaoImp varietyDaoImp;
	
	private String subId;
	private SmsSubscribeItem smsSubscribeItem;
	private ArrayList<String> cropData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_sms_sub);

		initData();
		initView();
		setUpView();
		initProvince();
		requestSmsDetail();
		initCropList();
	}
	
	private void initCropList() {
		String[] crop = getResources().getStringArray(R.array.cropList);
		List<String> cropList = Arrays.asList(crop);
		cropData = new ArrayList<String>();
		cropData.addAll(cropList);
		cropData.add(0, "请选择作物");
		setCrop();
	}

	private void requestSmsDetail() {
		startLoadingDialog();
		RequestService.getInstance().loadSubScribeDetail(this, subId, SmsSubDetailEntity.class, new RequestListener() {
			
			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {
				dismissLoadingDialog();
				if(resultData.isOk()){
					SmsSubDetailEntity result = (SmsSubDetailEntity) resultData;
					updateUI(result.getData());
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

	protected void updateUI(SmsSubscribeItem smsSubscribeItem) {
		this.smsSubscribeItem = smsSubscribeItem;
		mNameEdit.setText(smsSubscribeItem.getTargetName());
		mNameEdit.setSelection(mNameEdit.length());
		mPhoneEdit.setText(smsSubscribeItem.getCellphone());
		mAreaEdit.setText(smsSubscribeItem.getArea());
		mIsAgriWeatherRb.setChecked(smsSubscribeItem.isAgriWeather());
		mIsNatAlterRb.setChecked(smsSubscribeItem.isNatAler());
		mIsRiceHelperRb.setChecked(smsSubscribeItem.isRiceHelper());
		setProvince();
		setCrop();
	}

	private void initData() {
		Intent intent = getIntent();
		subId = intent.getStringExtra("subId");
	}

	private void initProvince() {
		new Thread() {
			public void run() {
				varietyDaoImp = new VarietyDaoImp(SmsSubscribeModifiActivity.this);
				dao = new CityDaoImpl(SmsSubscribeModifiActivity.this);
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

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);

		mProvinceSp = (Spinner) this.findViewById(R.id.spinner_province);
		mCitySp = (Spinner) this.findViewById(R.id.spinner_city);
		mAreaSp = (Spinner) this.findViewById(R.id.spinner_area);
		mTownsSp = (Spinner) this.findViewById(R.id.spinner_towns);
		mVillageSp = (Spinner) this.findViewById(R.id.spinner_village);
		mNameEdit = (EditText) this.findViewById(R.id.edittext_name);
		mPhoneEdit = (EditText) this.findViewById(R.id.edittext_phone);
		mAreaEdit = (EditText) this.findViewById(R.id.edittext_area);
		mIsAgriWeatherRb = (CheckBox) this.findViewById(R.id.rb_isAgriWeather);
		mIsNatAlterRb = (CheckBox) this.findViewById(R.id.rb_isNatAlter);
		mIsRiceHelperRb = (CheckBox) this.findViewById(R.id.rb_isRiceHelper);
		spinner = (Spinner) this.findViewById(R.id.spinner);
		
		mCancelButton = (Button) this.findViewById(R.id.button_cancel);
		mSaveButton = (Button) this.findViewById(R.id.button_save);
	}

	private void setUpView() {
		mTopbarView.setCenterText("免费短信订阅");
		mTopbarView.setLeftView(true, true);

		mSaveButton.setText("修改");
		
		mCancelButton.setOnClickListener(this);
		mSaveButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_cancel:
			finish();
			break;
		case R.id.button_save:
			subscribe();
			break;
		}
	}

	private void subscribe() {
		String targetName = mNameEdit.getText().toString().trim();

		if (TextUtils.isEmpty(targetName)) {
			showToast("请输入对方姓名");
			return;
		}
		String cellphone = mPhoneEdit.getText().toString().trim();

		if (TextUtils.isEmpty(cellphone) || !StringHelper.isPhoneNumber(cellphone)) {
			showToast("请输入合格的订阅手机号");
			return;
		}

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
		
		String province = (String) mProvinceSp.getSelectedItem();
		String city = (String) mCitySp.getSelectedItem();
		String country = (String) mAreaSp.getSelectedItem();
		String district = "";
		if (mTownsSp.getSelectedItemPosition() > 0) {
			district = (String) mTownsSp.getSelectedItem();
		}
		
		String area = mAreaEdit.getText().toString().trim();

		if (TextUtils.isEmpty(area)) {
			showToast("请输入农田大小");
			return;
		}
		String cropId = (String) spinner.getSelectedItem();

		if (spinner.getSelectedItemPosition() == 0) {
			showToast("请选择作物");
			return;
		}
		boolean isAgriWeather = mIsAgriWeatherRb.isChecked();
		boolean isNatAlter = mIsNatAlterRb.isChecked();
		boolean isRiceHelper = mIsRiceHelperRb.isChecked();

		if (!isAgriWeather && !isNatAlter && !isRiceHelper) {
			showToast("请至少选择一项短信服务");
			return;
		}

		startLoadingDialog();
		RequestService.getInstance().smsSubscribe(this, subId, targetName, cellphone, province, city, country, district, area,
				cropId, isAgriWeather, isNatAlter, isRiceHelper, BaseEntity.class, new RequestListener() {

					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						dismissLoadingDialog();
						if (resultData.isOk()) {
							showToast("更新成功");
							setResult(RESULT_OK);
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

	private void setProvince() {
		if(province == null){
			return;
		}
		mProvinceSp.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, province));
		if(smsSubscribeItem!=null){
			for(int i =0;i<province.size();i++){
				if(province.get(i).equals(smsSubscribeItem.getProvince())){
					mProvinceSp.setSelection(i);
					break;
				}
			}
		}
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

	protected void setCity(final String province2) {
		List<String> city = dao.getCity(province2);
		city.add(0, "请选择市");
		mCitySp.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, city));
		if(smsSubscribeItem!=null){
			for(int i =0;i<city.size();i++){
				if(city.get(i).equals(smsSubscribeItem.getCity())){
					mCitySp.setSelection(i);
					break;
				}
			}
		}
		mCitySp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String city = (String) mCitySp.getSelectedItem();
				setArea(province2, city);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		// mAreaSp.setAdapter(null);
		// mTownsSp.setAdapter(null);
	}

	protected void setArea(final String province2, final String city) {
		List<String> county = dao.getCounty(province2, city);
		county.add(0, "请选择区");
		mAreaSp.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, county));
		if(smsSubscribeItem!=null){
			for(int i =0;i<county.size();i++){
				if(county.get(i).equals(smsSubscribeItem.getCountry())){
					mAreaSp.setSelection(i);
					break;
				}
			}
		}
		mAreaSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String area = (String) mAreaSp.getSelectedItem();
				setTowns(province2, city, area);
				
//				setCrop(area);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		// mTownsSp.setAdapter(null);
	}

	protected void setCrop() {
		spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, cropData));
		if(smsSubscribeItem!=null){
			for(int i =0;i<cropData.size();i++){
				if(cropData.get(i).equals(smsSubscribeItem.getCropName())){
					spinner.setSelection(i);
					break;
				}
			}
		}
	}

	protected void setTowns(String province2, String city, String area) {
		List<String> county = dao.getDistrict(province2, city, area);
		county.add(0, "请选择镇");
		mTownsSp.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, county));
		if(smsSubscribeItem!=null){
			for(int i =0;i<county.size();i++){
				if(county.get(i).equals(smsSubscribeItem.getDistrict())){
					mTownsSp.setSelection(i);
					break;
				}
			}
		}
	}

}
