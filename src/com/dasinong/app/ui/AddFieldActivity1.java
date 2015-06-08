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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AddFieldActivity1 extends BaseActivity implements OnClickListener {

	private LocationUtils locationUtils;
	// 纬度
	private double latitude = 0;
	// 经度
	private double longitude = 0;
	private Button btn_in_field;
	private Button btn_no_in_field;
	private RunnableTask task = new RunnableTask();
	private static final int MAX_DELAY_COUNT = 3;
	private int count = 0;
	
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			count++;
			locationUtils.requestLocation();
		}
	};


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
		
		btn_in_field.setOnClickListener(this);
		btn_no_in_field.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		if ((latitude == 0 || longitude == 0) && count < MAX_DELAY_COUNT) {
			showToast("定位失败，请重试");
			handler.postDelayed(new RunnableTask(), 500);
			return ;
		} else if ((latitude == 0 || longitude == 0) && count >= MAX_DELAY_COUNT) {
			handler.removeCallbacks(task);
			Intent intent = new Intent(this, AddFieldActivity2.class);
			startActivity(intent);
		}
		
		
		
		 String latitudeText = String.valueOf(latitude);
		 String longitudeText =String.valueOf(longitude);

		switch (id) {
		case R.id.btn_no_in_field:
			RequestService.getInstance().sendNoInLocation(this, latitudeText, longitudeText, VillageInfoList.class, new NetRequest.RequestListener() {

				@Override
				public void onSuccess(int requestCode, BaseEntity resultData) {

					if (resultData.isOk()) {
						goToTwo();
					}else{
						showToast(resultData.getMessage());
					}
				}
				
				@Override
				public void onFailed(int requestCode, Exception error, String msg) {
					// TODO 待统一
					goToTwo();
//					showToast(text)
					
				}
			});
			break;
		case R.id.btn_in_field:
			RequestService.getInstance().sendInLocation(this, latitudeText, longitudeText, NearbyUser.class, new NetRequest.RequestListener() {

				@Override
				public void onSuccess(int requestCode, BaseEntity resultData) {
					
					if(resultData.isOk()){
						goToThree();
					} else {
						showToast(resultData.getMessage());
					}

				}

				@Override
				public void onFailed(int requestCode, Exception error, String msg) {
					// TODO 待统一
					goToThree();
				}
			});
			break;
		}
	}
	
	class RunnableTask implements Runnable{

		@Override
		public void run() {
			
				handler.sendEmptyMessage(0);
			
		}
		
	}
	
	private void goToTwo(){
		
		Intent intent = new Intent(DsnApplication.getContext(), AddFieldActivity2.class);
		startActivity(intent);
	}
	
	private void goToThree(){
		Intent intent = new Intent(DsnApplication.getContext(), AddFieldActivity3.class);
		startActivity(intent);
	}
}
