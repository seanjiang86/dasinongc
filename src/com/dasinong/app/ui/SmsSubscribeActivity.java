package com.dasinong.app.ui;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.city.dao.impl.CityDaoImpl;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.view.TopbarView;

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

public class SmsSubscribeActivity extends BaseActivity implements OnClickListener {

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_sms_sub);

		initData();
		initView();
		setUpView();
		initProvince();
	}

	private void initData() {
		
	}

	private void initProvince() {
		new Thread() {
			public void run() {
				dao = new CityDaoImpl(SmsSubscribeActivity.this);
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
		
		mCancelButton = (Button) this.findViewById(R.id.button_cancel);
		mSaveButton = (Button) this.findViewById(R.id.button_save);
	}

	private void setUpView() {
		mTopbarView.setCenterText("免费短信订阅");
		mTopbarView.setLeftView(true, true);

		spinner = (Spinner) this.findViewById(R.id.spinner);
		spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, new String[] { "sdf", "fsdgsfdg", "fgsdgs", "sdf",
				"fsdgsfdg", "fgsdgs" }));
		
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
		String cellphone = mPhoneEdit.getText().toString().trim();
		String province = (String) mProvinceSp.getSelectedItem();
		String city = (String) mCitySp.getSelectedItem();
		String country = (String) mAreaSp.getSelectedItem();
		String district = (String) mTownsSp.getSelectedItem();
		String area = mAreaEdit.getText().toString().trim();
		String cropId = (String) spinner.getSelectedItem();
		boolean isAgriWeather = mIsAgriWeatherRb.isChecked();
		boolean isNatAlter = mIsNatAlterRb.isChecked();
		boolean isRiceHelper = mIsRiceHelperRb.isChecked();
		
		startLoadingDialog();
		RequestService.getInstance().smsSubscribe(this, targetName, cellphone, province, 
				city, country, district, area, cropId, 
				isAgriWeather, isNatAlter, isRiceHelper, BaseEntity.class, new RequestListener() {
					
					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						dismissLoadingDialog();
					}
					
					@Override
					public void onFailed(int requestCode, Exception error, String msg) {
						dismissLoadingDialog();
					}
				});
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

}
