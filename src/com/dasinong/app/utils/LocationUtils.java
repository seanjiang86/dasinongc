package com.dasinong.app.utils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.dasinong.app.DsnApplication;
import com.dasinong.app.entity.LocationResult;
import com.dasinong.app.net.RequestService;

/** 
* @ClassName: LocationUtils 
* @说明:
* @author yusonglin
* @date 2015-6-5 上午11:14:05 
*/ 

public class LocationUtils {

	
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	private LocationListener mLocationListener;

	private static final LocationMode tempMode = LocationMode.Hight_Accuracy;
	private static final String tempcoor = "gcj02";
	private static final int span = 0;

	private volatile static LocationUtils mInstance;

	private LocationUtils() {
		initLocation();
	}

	public static LocationUtils getInstance() {
		if (mInstance == null) {
			synchronized (RequestService.class) {
				if (mInstance == null) {
					mInstance = new LocationUtils();
				}
			}
		}
		return mInstance;
	}
	
	private void initLocation() {
		mLocationClient = new LocationClient(DsnApplication.getContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(DsnApplication.getContext());

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);
		option.setCoorType(tempcoor);
		option.setScanSpan(span);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	public void requestLocation() {
		mLocationClient.requestLocation();
	}
	
	public void registerLocationListener(LocationListener listener) {
		this.mLocationListener = listener;
		mLocationClient.start();
	}
	
	
	public void unRegisterLocationListener() {
		this.mLocationListener = null;
		mLocationClient.stop();
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
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			
			LocationResult result = new LocationResult();
			
			result.setLatitude(location.getLatitude());
			result.setLongitude(location.getLongitude());
			result.setCountry(location.getCountry());
			result.setProvince(location.getProvince());
			result.setCity(location.getCity());
			result.setDistrict(location.getDistrict());
			result.setStreet(location.getStreet());
			
			if(mLocationListener != null){
				mLocationListener.locationNotify(result);
			}
			
			Logger.d1("BaiduLocationApiDem", sb.toString());
		}
	}
	
	public interface LocationListener{
		void locationNotify(LocationResult result);
	}
	
}
