package com.dasinong.app.ui;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import com.dasinong.app.utils.DeviceHelper;
import com.umeng.analytics.MobclickAgent;

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

	private String firstButtonText = "省 - 市";
	private String secondButtonText = "县 - 乡";
	private String thirdButtonText = "村";
	private LinearLayout ll_all;

	private MyComparator mComparator = new MyComparator();

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

		Collections.sort(provinceList, mComparator);

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
		
		btn_sure_location.setVisibility(View.GONE);
	}

	private void initValue() {

		viewList.add(provinceView);
		viewList.add(countyView);
		viewList.add(villageView);
		List<String> textList = new ArrayList<String>();
		if (!TextUtils.isEmpty(mprovince) && !TextUtils.isEmpty(mcity)) {
			firstButtonText = mprovince + "-" + mcity;
		}
		// TODO MING 测试
//		if (!TextUtils.isEmpty(mdistrict)) {
//			secondButtonText = mdistrict + "-";
//		}
		textList.add(firstButtonText);
		textList.add(secondButtonText);
		textList.add(thirdButtonText);
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
			provincePosition = provinceList.indexOf(mprovince);
			// TODO MING 地址定位失败解决方案
			provincePosition = provincePosition == -1 ? 0 : provincePosition;
		}
		cityList = dao.getCity(provinceList.get(provincePosition));
		Collections.sort(cityList, mComparator);
		if (!TextUtils.isEmpty(mcity)) {
			cityPosition = cityList.indexOf(mcity);
			cityPosition = cityPosition == -1 ? 0 : cityPosition;
		}
		provinceView.initBigAreaData(provinceList, provincePosition);
		initCityList();

		province = provinceList.get(provincePosition);

		provinceView.setOnBigAreaItemClickListener(new TextAdapter.OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {
				province = provinceList.get(position);
				cityList = dao.getCity(province);
				Collections.sort(cityList, mComparator);
				provincePosition = 0;
				cityPosition = 0;
				initCityList();
			}
		});
	}

	private void initCityList() {
		provinceView.initSmallAreaData(cityList, cityPosition);
		countyList = dao.getCounty(cityList.get(cityPosition));
		Collections.sort(countyList, mComparator);
		// TODO MING 测试
//		if (!TextUtils.isEmpty(mdistrict)) {
//			countyPosition = countyList.indexOf(mdistrict);
//			countyPosition = countyPosition == -1 ? 0 : countyPosition;
//		}

		provinceView.setOnSmallAreaItemClickListener(new TextAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				String currentCity = cityList.get(position);
				if (!currentCity.equals(city)) {
					city = currentCity;

					county = null;
					district = null;

					etv.setTitle("县 - 乡", 1);
					etv.setTitle(thirdButtonText, 2);
					
					countyPosition = 0;
					districtPostion = 0;
				}
				countyList = dao.getCounty(city);
				Collections.sort(countyList, mComparator);
				onrefresh(provinceView, province + "-" + city);
				
				etv.setButtonVisible(1);
			}
		});
	}

	private void initCountyList() {
		countyView.initBigAreaData(countyList, countyPosition);

		districtList = dao.getDistrict(countyList.get(countyPosition));
		Collections.sort(districtList, mComparator);
		initDistrict();

		county = countyList.get(countyPosition);

		countyView.setOnBigAreaItemClickListener(new TextAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				county = countyList.get(position);
				countyPosition = position;
				districtPostion = 0;
				districtList = dao.getDistrict(county);
				Collections.sort(districtList, mComparator);
				initDistrict();
			}
		});
	}

	private void initDistrict() {
		countyView.initSmallAreaData(districtList, districtPostion);
		countyView.setOnSmallAreaItemClickListener(new TextAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				String currentDistrict = districtList.get(position);
				if (!currentDistrict.equals(district)) {
					district = currentDistrict;

					village = null;

					etv.setTitle(thirdButtonText, 2);
				}

				districtPostion = position;
				onrefresh(countyView, county + "-" + district);

				queryVillage(province, city, county, district);
				
				etv.setButtonVisible(2);
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
				
				btn_sure_location.setVisibility(View.VISIBLE);
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
		if (!DeviceHelper.checkNetWork(this)) {
			showToast("请检测您的网络连接");
			return;
		}
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
							Collections.sort(villageList,mComparator);

							handler.sendEmptyMessage(0);
						} else {
							showToast(resultData.getMessage());
						}
						dismissLoadingDialog();
					}

					@Override
					public void onFailed(int requestCode, Exception error, String msg) {
						dismissLoadingDialog();
						showToast("请求失败，请检查网络或稍候再试");
					}
				});
	}

	private void gotoThree() {
		if (thirdButtonText.equals(etv.getTitle(2))) {
			showToast("请完善您的地理信息");
			return;
		}

		if (TextUtils.isEmpty(villageId)) {
			showToast("请完善您的地理信息");
			return;
		}
		
		MobclickAgent.onEvent(this, "AddFieldSecond");
		
		SharedPreferencesHelper.setString(this, Field.VILLAGE_ID, villageId);
		SharedPreferencesHelper.setString(this, Field.PROVINCE, province);
		SharedPreferencesHelper.setString(this, Field.CITY, city);
		SharedPreferencesHelper.setString(this, Field.COUNTY, county);
		Intent intent = new Intent(this, AddFieldActivity3.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}

	public class MyComparator implements Comparator<String> {
		Collator cmp = Collator.getInstance(java.util.Locale.CHINA);

		@Override
		public int compare(String str1, String str2) {
			if (cmp.compare(str1, str2) > 0) {
				return 1;
			} else if (cmp.compare(str1, str2) < 0) {
				return -1;
			}
			return 0;
		}
	}

}
