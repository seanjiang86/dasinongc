package com.dasinong.app.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.database.city.dao.impl.CityDaoImpl;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.VillageInfo;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.adapter.TextAdapter;
import com.dasinong.app.ui.adapter.TextAdapter.OnItemClickListener;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.ExpandTabView;
import com.dasinong.app.ui.view.ExpandTabView.OnButtonClickListener;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.ui.view.ViewMiddle;
import com.dasinong.app.ui.view.ViewRight;

public class AddFieldActivity2 extends MyBaseActivity {

	private CityDaoImpl dao;
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
	private List<View> viewList = new ArrayList<View>();
	private Map<String, String> villageMap;
	private Button btn_sure_location;

	private String mstreet;
	private String mdistrict;
	private String mcity;
	private String mprovince;
	private TopbarView topbar;
	private ViewMiddle provinceView;
	private ViewMiddle countyView;
	private ExpandTabView etv;
	private ViewRight villageView;

	private int provincePosition;
	private int cityPosition;
	private int countyPosition;
	private int districtPostion;

	private String firstButtonText = "请选择";
	private String secondButtonText = "请选择";
	private LinearLayout ll_all;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			initVillage();
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_field_2);

		dao = new CityDaoImpl(this);
		provinceList = dao.getProvince();

		mprovince = getIntent().getStringExtra("mprovince");
		mcity = getIntent().getStringExtra("mcity");
		mdistrict = getIntent().getStringExtra("mdistrict");

		province = mprovince;
		city = mcity;
		county = mdistrict;

		initView();
		initValue();

		initTopBar();

		initProvinceList();

		ll_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				etv.onPressBack();
			}
		});

		btn_sure_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gotoThree();
			}
		});
	}

	private void initView() {
		ll_all = (LinearLayout) findViewById(R.id.ll_all);
		btn_sure_location = (Button) findViewById(R.id.btn_sure_location);
		topbar = (TopbarView) findViewById(R.id.topbar);
		etv = (ExpandTabView) findViewById(R.id.etv);
		provinceView = new ViewMiddle(this);
		provinceView.setBackgroundResource(R.drawable.choosearea_bg_left);
		countyView = new ViewMiddle(this);
		countyView.setBackgroundResource(R.drawable.choosearea_bg_mid);
		villageView = new ViewRight(this);
	}

	private void initValue() {

		viewList.add(provinceView);
		viewList.add(countyView);
		viewList.add(villageView);
		List<String> textList = new ArrayList<String>();
		if (!TextUtils.isEmpty(mprovince) && !TextUtils.isEmpty(mcity)) {
			firstButtonText = mprovince + "-" + mcity;
		}
		if (!TextUtils.isEmpty(mdistrict)) {
			secondButtonText = mdistrict + "-";
		}
		textList.add(firstButtonText);
		textList.add(secondButtonText);
		textList.add("请选择");
		etv.setValue(textList, viewList);

		etv.setOnButtonClickListener(new OnButtonClickListener() {
			@Override
			public void onClick(int selectPosition) {
				switch (selectPosition) {
				case 1:
					if (TextUtils.isEmpty(city)) {
						onrefresh(countyView, "");
						showToast("请先选择上级单位");
						return;
					}
					initCountyList();
					break;
				case 2:
					if (TextUtils.isEmpty(district)) {
						onrefresh(villageView, "");
						showToast("请先选择上级单位");
						return;
					}
					break;
				}
			}
		});
	}

	private void initProvinceList() {
		if (!TextUtils.isEmpty(mprovince)) {
			// TODO MING:测试数据需修改
			provincePosition = provinceList.indexOf("北京");
		}
		cityList = dao.getCity(provinceList.get(provincePosition));
		if (!TextUtils.isEmpty(mcity)) {
			// TODO MING:测试数据需修改
			cityPosition = cityList.indexOf("北京");
		}
		provinceView.initBigAreaData(provinceList, provincePosition);
		initCityList();

		province = provinceList.get(provincePosition);

		provinceView.setOnBigAreaItemClickListener(new TextAdapter.OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {
				province = provinceList.get(position);
				cityList = dao.getCity(province);
				provincePosition = 0;
				cityPosition = 0;
				initCityList();
			}
		});
	}

	private void initCityList() {
		provinceView.initSmallAreaData(cityList, cityPosition);
		countyList = dao.getCounty(cityList.get(cityPosition));

		if (!TextUtils.isEmpty(mdistrict)) {
			countyPosition = countyList.indexOf(mdistrict);
		}

		provinceView.setOnSmallAreaItemClickListener(new TextAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				String currentCity = cityList.get(position);
				if (!currentCity.equals(city)) {
					city = currentCity;

					county = null;
					district = null;

					etv.setTitle("请选择", 1);
				}
				countyList = dao.getCounty(city);
				onrefresh(provinceView, province + "-" + city);
				countyPosition = 0;
			}
		});
	}

	private void initCountyList() {
		countyView.initBigAreaData(countyList, countyPosition);

		districtList = dao.getDistrict(countyList.get(countyPosition));
		initDistrict();

		county = countyList.get(countyPosition);

		countyView.setOnBigAreaItemClickListener(new TextAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				county = countyList.get(position);
				countyPosition = position;
				districtPostion = 0;
				districtList = dao.getDistrict(county);
				initDistrict();
			}
		});
	}

	private void initDistrict() {
		countyView.initSmallAreaData(districtList, districtPostion);
		countyView.setOnSmallAreaItemClickListener(new TextAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				district = districtList.get(position);
				districtPostion = position;
				onrefresh(countyView, county + "-" + district);

				queryVillage(province, city, county, district);
			}
		});
	}

	private void initVillage() {
		villageView.initData(villageList);
		villageView.setOnItemclickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {
				village = villageList.get(position);
				villageId = villageMap.get(village);

				onrefresh(villageView, village);
			}
		});
	}

	private void onrefresh(View view, String showText) {
		etv.onPressBack();
		int position = getPosition(view);
		if (position >= 0 && !etv.getTitle(position).equals(showText) && !TextUtils.isEmpty(showText)) {
			etv.setTitle(showText, position);
		}
	}

	private int getPosition(View tView) {
		for (int i = 0; i < viewList.size(); i++) {
			if (viewList.get(i) == tView) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void onBackPressed() {
		if (!etv.onPressBack()) {
			finish();
		}
	}

	private void initTopBar() {
		topbar.setCenterText("农田信息");
		topbar.setLeftView(true, true);
	}

	private void queryVillage(String province, String city, String county, String district) {
		startLoadingDialog();
		RequestService.getInstance().getLocation(DsnApplication.getContext(), province, city, county, district, VillageInfo.class,
				new RequestListener() {

					@Override
					public void onSuccess(int requestCode, BaseEntity resultData) {
						if (resultData.isOk()) {
							villageMap = ((VillageInfo) resultData).data;
							if (villageList != null) {
								villageList.clear();
							}
							villageList = new ArrayList<String>(villageMap.keySet());

							handler.sendEmptyMessage(0);
						} else {
							showToast(resultData.getMessage());
						}
						dismissLoadingDialog();
					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {

					}
				});
	}

	private void gotoThree() {
		if (TextUtils.isEmpty(villageId)) {
			showToast("请完善您的地理信息");
			return;
		}
		SharedPreferencesHelper.setString(this, Field.VILLAGE_ID, villageId);
		SharedPreferencesHelper.setString(this, Field.PROVINCE, province);
		SharedPreferencesHelper.setString(this, Field.CITY, city);
		SharedPreferencesHelper.setString(this, Field.COUNTY, county);
		Intent intent = new Intent(this, AddFieldActivity3.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}
}
