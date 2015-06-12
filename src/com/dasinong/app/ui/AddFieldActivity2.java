package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.DownloadManager.Query;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.database.city.dao.impl.CityDaoImpl;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.LoginRegEntity;
import com.dasinong.app.entity.VillageInfo;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.CityAdapter;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.utils.Logger;

public class AddFieldActivity2 extends BaseActivity implements OnClickListener {

	private CityDaoImpl cityDaoImpl;
	private String province;
	private String city;
	private String county;
	private String district;
	private String village;
	private String villageId;
	private List<String> provinceList;
	private List<String> cityList;
	private List<String> countyList;
	private List<String> districtList;
	private List<String> villageList;
	private Map<String, String> villageMap;
	private TextView tv_privace_city;
	private TextView tv_county_district;
	private TextView tv_village;
	private FrameLayout fl_select_location;
	private boolean provinceIsGone = true;
	private boolean countyIsGone = true;
	private boolean villageIsGone = true;
	private ListView lv_big_area;
	private ListView lv_small_area;
	private Button btn_sure_location;

	private CityAdapter frontAdapter;
	private CityAdapter behindAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_field_2);

		login();

		cityDaoImpl = new CityDaoImpl(this);

		tv_privace_city = (TextView) findViewById(R.id.tv_privace_city);
		tv_county_district = (TextView) findViewById(R.id.tv_county_district);
		tv_village = (TextView) findViewById(R.id.tv_village);
		fl_select_location = (FrameLayout) findViewById(R.id.fl_select_location);
		lv_big_area = (ListView) findViewById(R.id.lv_big_area);
		lv_small_area = (ListView) findViewById(R.id.lv_small_area);
		btn_sure_location = (Button) findViewById(R.id.btn_sure_location);

		String mprovince = getIntent().getStringExtra("mprovince");
		String mcity = getIntent().getStringExtra("mcity");
		String mdistrict = getIntent().getStringExtra("mdistrict");
		String mstreet = getIntent().getStringExtra("mstreet");
		if (TextUtils.isEmpty(mprovince) || TextUtils.isEmpty(mcity) || TextUtils.isEmpty(mdistrict) || TextUtils.isEmpty(mstreet)) {

		} else {
			tv_privace_city.setText(mprovince + "-" + mcity);
			tv_county_district.setText(mdistrict + "-" + mstreet);

		}

		tv_privace_city.setOnClickListener(this);
		tv_county_district.setOnClickListener(this);
		tv_village.setOnClickListener(this);
		btn_sure_location.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.tv_privace_city:

			if (countyList != null) {
				countyList.clear();
				frontAdapter.setData(countyList);
				frontAdapter.notifyDataSetChanged();
			}

			if (districtList != null) {
				districtList.clear();
				behindAdapter.setData(districtList);
				behindAdapter.notifyDataSetChanged();
			}

			if (provinceIsGone) {
				fl_select_location.setVisibility(View.VISIBLE);
			} else {
				fl_select_location.setVisibility(View.GONE);
			}
			provinceIsGone = !provinceIsGone;

			initProvince();
			break;
		case R.id.tv_county_district:

			if (provinceList != null) {
				provinceList.clear();
				frontAdapter.setData(provinceList);
				frontAdapter.notifyDataSetChanged();
			}

			if (cityList != null) {
				cityList.clear();
				behindAdapter.setData(cityList);
				behindAdapter.notifyDataSetChanged();
			}

			if (TextUtils.isEmpty(city)) {
				showToast("请先选择省市");
				return;
			}

			if (countyIsGone) {
				fl_select_location.setVisibility(View.VISIBLE);
			} else {
				fl_select_location.setVisibility(View.GONE);
			}
			countyIsGone = !countyIsGone;

			initCounty();

			break;
		case R.id.tv_village:
			
			if (TextUtils.isEmpty(district)) {
				showToast("请先选择省市县乡");
				return;
			}
			
			if (villageList != null && villageList.size() != 0) {
				if (villageIsGone) {
					fl_select_location.setVisibility(View.VISIBLE);
				} else {
					fl_select_location.setVisibility(View.GONE);
				}
				villageIsGone = !villageIsGone;
				initVillage();

			} else {
				return;
			}
			break;
		case R.id.btn_sure_location:
			// TODO Ming:测试流程判断先取消
			// if(TextUtils.isEmpty(villageId)){
			// return;
			// }

			gotoThree();
			break;
		}
	}

	private void initProvince() {

		provinceList = cityDaoImpl.getProvince();

		if (frontAdapter == null) {
			frontAdapter = new CityAdapter(this, provinceList, false);
		} else {
			frontAdapter.setData(provinceList);
			frontAdapter.notifyDataSetInvalidated();
		}

		lv_big_area.setAdapter(frontAdapter);

		lv_big_area.setOnItemClickListener(new ProvinceOnItemClickListener());
		lv_small_area.setOnItemClickListener(new ProvinceOnItemClickListener());

	}

	private void initCounty() {
		countyList = cityDaoImpl.getCounty(city);
		frontAdapter.setData(countyList);
		lv_big_area.setAdapter(frontAdapter);
		frontAdapter.notifyDataSetChanged();

		lv_big_area.setOnItemClickListener(new CountyOnItemClickListener());
		lv_small_area.setOnItemClickListener(new CountyOnItemClickListener());

	}

	private void initVillage() {

		frontAdapter.setData(villageList);
		lv_big_area.setAdapter(frontAdapter);
		frontAdapter.notifyDataSetChanged();

		lv_big_area.setOnItemClickListener(new VillageOnItemClickListener());
	}

	class ProvinceOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int lvId = parent.getId();
			switch (lvId) {
			case R.id.lv_big_area:
				province = provinceList.get(position);
				cityList = cityDaoImpl.getCity(province);

				if (behindAdapter == null) {
					behindAdapter = new CityAdapter(AddFieldActivity2.this, cityList, false);
				} else {
					behindAdapter.setData(cityList);
					behindAdapter.notifyDataSetChanged();
				}

				lv_small_area.setAdapter(behindAdapter);

				break;
			case R.id.lv_small_area:
				city = cityList.get(position);
				fl_select_location.setVisibility(View.GONE);

				provinceIsGone = !provinceIsGone;

				tv_privace_city.setText(province + "-" + city);
				break;
			}
		}
	}

	class CountyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int lvId = parent.getId();
			switch (lvId) {
			case R.id.lv_big_area:

				county = countyList.get(position);
				districtList = cityDaoImpl.getDistrict(county);

				behindAdapter.setData(districtList);
				lv_small_area.setAdapter(behindAdapter);
				behindAdapter.notifyDataSetChanged();

				break;
			case R.id.lv_small_area:

				district = districtList.get(position);
				fl_select_location.setVisibility(View.GONE);
				tv_county_district.setText(county + "-" + district);

				countyIsGone = !countyIsGone;

				queryVillage(province, city, county, district);

				break;
			}
		}

	}

	private void queryVillage(String province, String city, String county, String district) {
		RequestService.getInstance().getLocation(DsnApplication.getContext(), province, city, county, district, VillageInfo.class,
				new RequestListener() {

					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						if (resultData.isOk()) {
							villageMap = ((VillageInfo) resultData).data;
							villageList = new ArrayList<String>(villageMap.keySet());
						}
					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {

					}
				});
	}

	class VillageOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			village = villageList.get(position);
			villageId = villageMap.get(village);
		}

	}

	private void gotoThree() {
		SharedPreferencesHelper.setString(this, Field.VILLAGE_ID, villageId);
		SharedPreferencesHelper.setString(this, Field.PROVINCE, province);
		SharedPreferencesHelper.setString(this, Field.CITY, city);
		SharedPreferencesHelper.setString(this, Field.COUNTY, county);
		Intent intent = new Intent(this, AddFieldActivity3.class);
		startActivity(intent);
	}

	private void login() {

		RequestService.getInstance().authcodeLoginReg(this, "13112345678", LoginRegEntity.class, new NetRequest.RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {

				if (resultData.isOk()) {
					LoginRegEntity entity = (LoginRegEntity) resultData;

					AccountManager.saveAccount(AddFieldActivity2.this, entity.getData());

					Logger.d("MING", resultData.getMessage() + "onSuccess  成功");

				} else {
					Logger.d("MING", resultData.getMessage() + "onSuccess");
				}
			}

			@Override
			public void onFailed(int requestCode, Exception error, String msg) {

				Logger.d("MING", "msg" + msg + "onFailed");
			}
		});
	}

}
