package com.dasinong.app.ui;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.dasinong.app.R;
import com.dasinong.app.ui.fragment.EncyclopediaFragment;
import com.dasinong.app.ui.fragment.HomeFragment;
import com.dasinong.app.ui.fragment.MeFragment;
import com.dasinong.app.utils.Logger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * @ClassName MainTabActivity
 * @author ysl
 * @Description
 */
public class MainTabActivity extends BaseActivity {
	private FragmentTabHost mTabHost;

	private LayoutInflater layoutInflater;

	private Class fragmentArray[] = { HomeFragment.class, EncyclopediaFragment.class, MeFragment.class };

	// private int mImageViewArray[] =
	// {R.drawable.main_tab1_selector,R.drawable.main_tab2_selector,R.drawable.main_tab3_selector,

	private String mTextviewArray[] = { "大司农", "农事百科", "更多" };

	private int index;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tab_layout);
		initData();
		initView();
		initLocation();
		// startLoadingDialog();

		// if(getIntent() != null){
		// index = getIntent().getIntExtra("index", 0);
		// if(index != 0){
		// mTabHost.setCurrentTab(index);
		// }
		// }
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			index = intent.getIntExtra("index", 0);
			if (index != 0) {
				mTabHost.setCurrentTab(index);
			}
		}
	}

	protected void initData() {
		index = getIntent().getIntExtra("index", 0);
	}

	protected void initView() {
		layoutInflater = LayoutInflater.from(this);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		int count = fragmentArray.length;

		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
		}

		if (index != 0) {
			mTabHost.setCurrentTab(index);
		}

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}

	private View getTabItemView(int index) {
		View view = layoutInflater.inflate(R.layout.view_main_tab_item, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		// imageView.setImageResource(mImageViewArray[index]);

		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextviewArray[index]);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mLocationClient.stop();
		super.onStop();
	}

	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;

	private static final LocationMode tempMode = LocationMode.Hight_Accuracy;
	private static final String tempcoor = "gcj02";
	private static final int span = 5000;

	private void initLocation() {
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);
		option.setCoorType(tempcoor);
		option.setScanSpan(span);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		
		mLocationClient.start();
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			Logger.d1("BaiduLocationApiDem", sb.toString());
		}
	}

}
