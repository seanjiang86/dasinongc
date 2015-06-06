package com.dasinong.app.ui;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.R;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.LocationResult;
import com.dasinong.app.entity.NearbyUser;
import com.dasinong.app.entity.VillageInfoList;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestCode;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.utils.LocationUtils;
import com.dasinong.app.utils.LocationUtils.LocationListener;

import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AddFieldActivity1 extends BaseActivity implements OnClickListener {

	private LocationUtils locationUtils;
	// 纬度
	private double latitude = 0;
	// 经度
	private double longitude = 0;
	private Button btn_in_field;
	private Button btn_no_in_field;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_field_1);

		btn_no_in_field = (Button) findViewById(R.id.btn_no_in_field);
		btn_in_field = (Button) findViewById(R.id.btn_in_field);

		locationUtils = LocationUtils.getInstance();

		locationUtils.registerLocationListener(new LocationListener() {

			@Override
			public void locationNotify(LocationResult result) {
				latitude = result.getLatitude();
				longitude = result.getLongitude();
			}
		});

	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		if (latitude == 0 || longitude == 0) {
			// TODO 获取不到信息， handler 发送延时消息，重新获取定位信息，

			if (latitude == 0 || longitude == 0) {
				Intent intent = new Intent(DsnApplication.getContext(), AddFieldActivity2.class);
				startActivity(intent);
				return;
			}
		}
		
		 String latitudeText = String.valueOf(latitude);
		 String longitudeText =String.valueOf(longitude);

		switch (id) {
		case R.id.btn_no_in_field:

			RequestService.getInstance().sendNoInLocation(this, latitudeText, longitudeText, VillageInfoList.class, new NetRequest.RequestListener() {

				@Override
				public void onSuccess(int requestCode, BaseEntity resultData) {

					if (resultData.isOk()) {
						Intent intent = new Intent(DsnApplication.getContext(), AddFieldActivity2.class);
						startActivity(intent);
					}else{
						showToast(resultData.getMessage());
					}
				}

				@Override
				public void onFailed(int requestCode, Exception error, String msg) {
					// TODO 待统一

//					showToast(text)
					
				}
			});
			break;
		case R.id.btn_in_field:
			RequestService.getInstance().sendInLocation(this, latitudeText, longitudeText, NearbyUser.class, new NetRequest.RequestListener() {

				@Override
				public void onSuccess(int requestCode, BaseEntity resultData) {
					
					if(resultData.isOk()){
						Intent intent = new Intent(DsnApplication.getContext(), AddFieldActivity3.class);
						startActivity(intent);
					} else {
						showToast(resultData.getMessage());
					}

				}

				@Override
				public void onFailed(int requestCode, Exception error, String msg) {
					// TODO 待统一
					
				}
			});
			break;
		}
	}
}
