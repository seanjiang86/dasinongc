package com.dasinong.app.ui;

import java.util.List;

import com.dasinong.app.R;
import com.dasinong.app.database.city.dao.impl.CityDaoImpl;
import com.dasinong.app.ui.view.TopbarView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SmsSubscribeActivity extends BaseActivity {

	private TopbarView mTopbarView;
	private Spinner spinner;
	private Spinner mProvinceSp;
	private Spinner mCitySp;
	private Spinner mAreaSp;
	private Spinner mTownsSp;
	private Spinner mVillageSp;
	private List<String> province;
	private CityDaoImpl dao;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sms_sub);
		
		initData();
		initView();
		setUpView();
		
	}

	private void initData() {
		dao = new CityDaoImpl(this);
		province = dao.getProvince();
		province.add(0, "请选择省");
	}

	private void initView() {
		mTopbarView = (TopbarView) this.findViewById(R.id.topbar);
		
		mProvinceSp = (Spinner) this.findViewById(R.id.spinner_province);
		mCitySp = (Spinner) this.findViewById(R.id.spinner_city);
		mAreaSp = (Spinner) this.findViewById(R.id.spinner_area);
		mTownsSp = (Spinner) this.findViewById(R.id.spinner_towns);
		mVillageSp = (Spinner) this.findViewById(R.id.spinner_village);
	}

	private void setUpView() {
		mTopbarView.setCenterText("免费短信订阅");
		mTopbarView.setLeftView(true, true);
		
		spinner = (Spinner) this.findViewById(R.id.spinner);
		spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, new  String[]{"sdf","fsdgsfdg","fgsdgs","sdf","fsdgsfdg","fgsdgs"}));
		
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
		
//		mAreaSp.setAdapter(null);
//		mTownsSp.setAdapter(null);
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
//		mTownsSp.setAdapter(null);
	}

	protected void setTowns(String area) {
		List<String> county = dao.getDistrict(area);
		county.add(0, "请选择镇");
		mTownsSp.setAdapter(new ArrayAdapter<String>(this, R.layout.textview, county));
	}
	
}
