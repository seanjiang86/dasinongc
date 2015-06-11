package com.dasinong.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.LocationResult;
import com.dasinong.app.entity.LoginRegEntity;
import com.dasinong.app.entity.NearbyUser;
import com.dasinong.app.entity.VillageId;
import com.dasinong.app.entity.VillageInfo;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.view.TopbarView;
import com.dasinong.app.utils.LocationUtils;
import com.dasinong.app.utils.LocationUtils.LocationListener;
import com.dasinong.app.utils.Logger;

public class AddFieldActivity1 extends BaseActivity implements OnClickListener {

	// private LocationUtils locationUtils;
	// 纬度
	private double latitude;
	// 经度
	private double longitude;
	// 省
	private String mprovince;
	// 市
	private String mcity;
	// 区
	private String mdistrict;
	// 街道
	private String mstreet;
	private VillageId villageId;
	private Button btn_in_field;
	private Button btn_no_in_field;
	private TopbarView topbar;
	private RunnableTask task = new RunnableTask();
	private static final int MAX_DELAY_COUNT = 3;
	private int count = 0;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			count++;
			LocationUtils.getInstance().requestLocation();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		login();
		setContentView(R.layout.activity_add_field_1);

		btn_no_in_field = (Button) findViewById(R.id.btn_no_in_field);
		btn_in_field = (Button) findViewById(R.id.btn_in_field);
		topbar = (TopbarView) findViewById(R.id.topbar);

		initLocation();
		
		initTopBar();

		btn_in_field.setOnClickListener(this);
		btn_no_in_field.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		if ((latitude == 0 || longitude == 0) && count < MAX_DELAY_COUNT) {
			showToast("定位失败，请重新点击按钮");
			handler.postDelayed(new RunnableTask(), 500);
			return;
		} else if ((latitude == 0 || longitude == 0) && count >= MAX_DELAY_COUNT) {
			handler.removeCallbacks(task);
			Intent intent = new Intent(this, AddFieldActivity2.class);
			startActivity(intent);
			return;
		}

		String latitudeText = String.valueOf(latitude);
		String longitudeText = String.valueOf(longitude);
		SharedPreferencesHelper.setString(this, Field.LATITUDE, latitudeText);
		SharedPreferencesHelper.setString(this, Field.LONGITUDE, longitudeText);

		// TODO MING:如果此时用户选择不在田里，则将地理位置信息传入下一级，填充下拉选框，并拉取下一级地域单位
		switch (id) {
		case R.id.btn_no_in_field:
			goToTwo();
			break;
		case R.id.btn_in_field:
			RequestService.getInstance().searchLocation(this, latitudeText, longitudeText, mprovince, mcity, mdistrict, mstreet, VillageId.class,
					new NetRequest.RequestListener() {

						@Override
						public void onSuccess(int requestCode, BaseEntity resultData) {

							if (resultData.isOk()) {
								villageId = (VillageId) resultData;
								goToThree();
							} else {
								showToast(resultData.getMessage() + "   addFieldActivity");
							}
						}

						@Override
						public void onFailed(int requestCode, Exception error, String msg) {
							// TODO Ming:待统一
							goToThree();
						}
					});
			break;
		}
	}

	private void initLocation() {
		
		Logger.d("MING", "进入定位方法");

		LocationUtils.getInstance().registerLocationListener(new LocationListener() {

			@Override
			public void locationNotify(LocationResult result) {
				Logger.d("MING", "定位开始执行");
				latitude = result.getLatitude();
				longitude = result.getLongitude();

				mprovince = result.getProvince();
				mcity = result.getCity();
				mdistrict = result.getDistrict();
				mstreet = result.getStreet();

				Logger.d("addFieldLocation", "latitude  =====  " + latitude);
				Logger.d("addFieldLocation", "longitude  =====  " + longitude);

				Logger.d("addFieldLocation", "province  =====  " + mprovince);
				Logger.d("addFieldLocation", "city  =====  " + mcity);
				Logger.d("addFieldLocation", "district  =====  " + mdistrict);
				Logger.d("addFieldLocation", "street  =====  " + mstreet);
				
				
				Logger.d("MING", "定位结束");
			}
		});
	}

	private void initTopBar() {
		topbar.setCenterText("田地信息");
	}

	class RunnableTask implements Runnable {

		@Override
		public void run() {

			handler.sendEmptyMessage(0);

		}

	}

	private void goToTwo() {

		Intent intent = new Intent(DsnApplication.getContext(), AddFieldActivity2.class);
		intent.putExtra("mprovince", mprovince);
		intent.putExtra("mcity", mcity);
		intent.putExtra("mdistrict", mdistrict);
		intent.putExtra("mstreet", mstreet);
		startActivity(intent);
	}

	private void goToThree() {
		SharedPreferencesHelper.setString(this, Field.VILLAGE_ID, villageId.villageId);
		SharedPreferencesHelper.setString(this, Field.PROVINCE, mprovince);
		SharedPreferencesHelper.setString(this, Field.CITY, mcity);
		SharedPreferencesHelper.setString(this, Field.COUNTY,mdistrict);
		Intent intent = new Intent(DsnApplication.getContext(), AddFieldActivity3.class);
		startActivity(intent);
	}

	private void login() {

		RequestService.getInstance().authcodeLoginReg(this, "13112345678", LoginRegEntity.class, new NetRequest.RequestListener() {

			@Override
			public void onSuccess(int requestCode, BaseEntity resultData) {

				if (resultData.isOk()) {
					LoginRegEntity entity = (LoginRegEntity) resultData;

					AccountManager.saveAccount(AddFieldActivity1.this, entity.getData());

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

	@Override
	protected void onStop() {
		LocationUtils.getInstance().unRegisterLocationListener();
		super.onStop();
	}
}
