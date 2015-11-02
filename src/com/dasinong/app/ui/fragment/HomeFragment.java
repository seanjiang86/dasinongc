package com.dasinong.app.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.dasinong.app.BuildConfig;
import com.dasinong.app.R;
import com.dasinong.app.components.domain.BannerEntity;
import com.dasinong.app.components.domain.BannerEntity.ItemEntity;
import com.dasinong.app.components.domain.DisasterEntity;
import com.dasinong.app.components.domain.FieldEntity;
import com.dasinong.app.components.domain.WeatherEntity;
import com.dasinong.app.components.home.view.BannerView;
import com.dasinong.app.components.home.view.CropsStateView;
import com.dasinong.app.components.home.view.DisasterView;
import com.dasinong.app.components.home.view.HomeWeatherView;
import com.dasinong.app.components.home.view.SoilView;
import com.dasinong.app.components.net.INetRequest;
import com.dasinong.app.components.net.NetError;
import com.dasinong.app.components.net.VolleyManager;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.LocationResult;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.net.NetRequest.RequestListener;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.BaseActivity;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.app.ui.soil.SoilEditorActivity;
import com.dasinong.app.ui.soil.domain.DataEntity;
import com.dasinong.app.utils.LocationUtils;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

public class HomeFragment extends Fragment implements INetRequest, BGARefreshLayout.BGARefreshLayoutDelegate {

	private static final int REQUEST_CODE_HOME_FIELD = 130;
	private static final int REQUEST_CODE_HOME_WEATHER = 131;
	private static final int REQUEST_CODE_HOME_BANNER = 132;

	private static final int REQUEST_CODE_DISASTER = 133;

	private boolean isHomeSuccess = false;
	private boolean isWeatherSuccess = false;
	private boolean isBannerSuccess = false;
	private static final String URL_FIELD = NetConfig.BASE_URL + "home";

	private static final String URL_WEATHER = NetConfig.BASE_URL + "loadWeather";
	private static final String URL_BANNER = NetConfig.BASE_URL + "getLaoNong";
	private static final String URL_DISASTER = NetConfig.BASE_URL + "getPetDisBySubStage";

	private ViewGroup mRoot;

	private BGARefreshLayout mRefreshLayout;

	private HomeWeatherView mHomeWeatherView;
	private SoilView mSoilView;
	private BannerView mBannerView;
	private DisasterView mDisasterView;
	private CropsStateView mCropStateView;

	private static final String TAG = "HomeFragment";

	public static final long DEFAULT_FIELD_ID = -1;

	private long mFiledId;
	private int mMotionId;
	FieldEntity.Param param = new FieldEntity.Param();
	WeatherEntity.Param weatherParam = new WeatherEntity.Param();

	BannerEntity.Param bannerParam = new BannerEntity.Param();

	DisasterEntity.Param diasterParam = new DisasterEntity.Param();
	private long mStartTime = -1L;
	/**
	 * unite is minute
	 */
	public static final long TIME_DISTANCE = 20 * DateUtils.MINUTE_IN_MILLIS;

	private BaseActivity mBaseActivity;

	private boolean isShowDialog = true;

	private Request mHomeRequest;
	private Request mBannerRequest;
	private Request mWeatherRequest;

	private LocationListener mLocationListener;

	private String mUserID;

	private String mAddress = "";
	private String msg;
	private LinearLayout ll_view_group;
	private ImageView[] imageViews;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof BaseActivity) {
			mBaseActivity = (BaseActivity) activity;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mRoot != null) {
			ViewGroup parent = (ViewGroup) mRoot.getParent();

			if (parent != null) {

				parent.removeView(mRoot);

			}

			return mRoot;
		}
		mRoot = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

		mStartTime = -1L;
		resetSuccessFlag();
		isShowDialog = true;
		mUserID = SharedPreferencesHelper.getString(getActivity().getApplicationContext(), SharedPreferencesHelper.Field.USER_ID, "");
		mFiledId = SharedPreferencesHelper.getLong(this.getActivity(), SharedPreferencesHelper.Field.FIELDID, DEFAULT_FIELD_ID);
		initView();
		initRefreshLayout();
		initEvent();
		return mRoot;
	}

	private void initView() {
		mCropStateView = (CropsStateView) mRoot.findViewById(R.id.stateView);

		mHomeWeatherView = (HomeWeatherView) mRoot.findViewById(R.id.weather);

		mRefreshLayout = (BGARefreshLayout) mRoot.findViewById(R.id.rl_modulename_refresh);

		mSoilView = (SoilView) mRoot.findViewById(R.id.home_soilview);

		mDisasterView = (DisasterView) mRoot.findViewById(R.id.home_disaster);

		mBannerView = (BannerView) mRoot.findViewById(R.id.home_banner);

		ll_view_group = (LinearLayout) mRoot.findViewById(R.id.ll_view_group);
	}

	private void initEvent() {

	}

	private void initRefreshLayout() {
		mRefreshLayout.setDelegate(this);
		BGARefreshViewHolder refreshViewHolder = new BGAStickinessRefreshViewHolder(getActivity(), false);
		refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.color_view_home_top_bg);
		mRefreshLayout.setRefreshViewHolder(refreshViewHolder);

	}

	@Override
	public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
		loadDataFromWithCache(true);
		isShowDialog = false;

	}

	@Override
	public void onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {

		mRefreshLayout.endRefreshing();

	}

	@Override
	public void onTaskSuccess(int requestCode, Object response) {
		switch (requestCode) {
		case REQUEST_CODE_HOME_FIELD:

			FieldEntity entity = (FieldEntity) response;

			if (entity != null) {
				if (entity.currentField != null && !entity.currentField.stagelist.isEmpty()) {
					Collections.sort(entity.currentField.stagelist);
				}

				if (entity.currentField != null && !entity.currentField.taskws.isEmpty()) {
					Collections.sort(entity.currentField.taskws);
				}

				if (entity.currentField != null) {

					mDisasterView.updateView(entity.currentField.petdisspecws, entity.currentField.petdisws);
					SharedPreferencesHelper.setString(this.getActivity(), Field.CROP_NAME, entity.currentField.cropName);
					SharedPreferencesHelper.setString(this.getActivity(), Field.CURRENT_VILLAGE_ID, entity.currentField.varietyId + "");
				} else {
					mDisasterView.updateView(null, null);
				}

				mCropStateView.updateView(entity, mAddress);

				mCropStateView.setOnAddFieldClickListener(new CropsStateView.MyOnAddFieldClickListener() {
					@Override
					public void onWorKContentItemClick(String itemValue, int pos, boolean isSelect) {

					}

					@Override
					public void onAddCroupClick() {

					}

					@Override
					public void onPopWindowItemClick(Long filedId) {
						// filde popuWidno
						isShowDialog = true;
						mFiledId = filedId;
						loadDataFromWithCache(true);

					}

					@Override
					public void onDialogClick(int substage) {
						diasterParam.subStageId = String.valueOf(substage);
						diasterParam.varietyId = SharedPreferencesHelper.getString(HomeFragment.this.getActivity(), Field.VARIETY_ID, "");
						loadDisaster();
					}

				});

				mSoilView.updateView(entity.latestReport, entity.soilHum);

				mSoilView.setonEditorListener(new SoilView.OnEditorListener() {
					@Override
					public void onEditListener(DataEntity entity) {
						Intent intent = SoilEditorActivity.createIntent(HomeFragment.this.getActivity(), entity);
						HomeFragment.this.startActivityForResult(intent, 101);

					}
				});
			}

			isHomeSuccess = true;
			break;
		case REQUEST_CODE_HOME_WEATHER:

			WeatherEntity weatherEntity = (WeatherEntity) response;

			mHomeWeatherView.setWeatherData(weatherEntity);

			mCropStateView.updateWorkStage(weatherEntity);

			isWeatherSuccess = true;
			break;

		case REQUEST_CODE_HOME_BANNER:

			BannerEntity banner = (BannerEntity) response;
			if (banner != null) {

				if (banner.newdata != null && banner.newdata.size() > 1) {
					Collections.sort(banner.newdata);
					initPoint(banner.newdata);
				} else if (banner.newdata == null || banner.newdata.isEmpty()) {
					mBannerView.setVisibility(View.GONE);
					ll_view_group.setVisibility(View.GONE);
				}

				mBannerView.initView(banner);

				mBannerView.setOnPageChangeListener(new MyOnPageChangeListener());

			}
			isBannerSuccess = true;
			break;
		case REQUEST_CODE_DISASTER:
			DisasterEntity disasterEntity = (DisasterEntity) response;
			mDisasterView.updateView(disasterEntity.data, null);
			break;
		default:
			break;

		}
		if (isHomeSuccess && isWeatherSuccess && isBannerSuccess) {

			mRefreshLayout.endRefreshing();
			resetSuccessFlag();
			isShowDialog = false;
			if (mBaseActivity != null) {
				mRoot.setVisibility(View.VISIBLE);
				mBaseActivity.dismissLoadingDialog();
			}
		}

	}

	private void initPoint(List<ItemEntity> newdata) {
		imageViews = new ImageView[newdata.size()];

		if (ll_view_group.getChildCount() > 0) {
			ll_view_group.removeAllViews();
		}

		for (int i = 0; i < imageViews.length; i++) {
			ImageView imageView = new ImageView(getActivity());

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
			params.setMargins(10, 0, 10, 0);

			imageView.setLayoutParams(params);
			imageView.setPadding(30, 0, 30, 0);

			imageViews[i] = imageView;

			if (i == 0) {
				imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.page_indicator);
			}

			ll_view_group.addView(imageView);
		}
	}

	@Override
	public void onTaskFailedSuccess(int requestCode, NetError error) {

		switch (requestCode) {
		case REQUEST_CODE_HOME_FIELD:

			FieldEntity fieldEntity = VolleyManager.getInstance().getCacheDomain(mHomeRequest, FieldEntity.class);
			onTaskSuccess(requestCode, fieldEntity);
			break;
		case REQUEST_CODE_HOME_WEATHER:

			WeatherEntity weather = VolleyManager.getInstance().getCacheDomain(mWeatherRequest, WeatherEntity.class);
			onTaskSuccess(requestCode, weather);

			break;

		case REQUEST_CODE_HOME_BANNER:

			BannerEntity bannerEntity = VolleyManager.getInstance().getCacheDomain(mBannerRequest, BannerEntity.class);
			onTaskSuccess(requestCode, bannerEntity);
			break;
		default:
			break;

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("TAG", "onResume");

		// mFiledId = SharedPreferencesHelper.getLong(this.getActivity(),
		// SharedPreferencesHelper.Field.FIELDID, DEFAULT_FIELD_ID);
		String currentUserID = SharedPreferencesHelper.getString(getActivity().getApplicationContext(), SharedPreferencesHelper.Field.USER_ID, "");

		if (!mUserID.equals(currentUserID)) {
			mUserID = currentUserID;
			loadDataFromWithCache(true);
			return;
		}

		if (mStartTime < 0) {
			loadDataFromWithCache(true);
		} else {
			long currentFieldId = SharedPreferencesHelper.getLong(this.getActivity(), SharedPreferencesHelper.Field.FIELDID, DEFAULT_FIELD_ID);
			if (mFiledId == currentFieldId) {
				loadDataFromWithCache(false);
			} else {
				mFiledId = SharedPreferencesHelper.getLong(this.getActivity(), SharedPreferencesHelper.Field.FIELDID, DEFAULT_FIELD_ID);
				loadDataFromWithCache(true);
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		LocationUtils.getInstance().unRegisterLocationListener();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

	}

	private void initLocation() {
		if (mLocationListener == null) {
			mLocationListener = new LocationListener();
		} else {
			LocationUtils.getInstance().unRegisterLocationListener();
			mLocationListener = new LocationListener();
		}
		LocationUtils.getInstance().registerLocationListener(mLocationListener);
	}

	private void loadDataFromWithCache(boolean isForce) {

		resetSuccessFlag();
		if (!isForce) {
			long distance = SystemClock.currentThreadTimeMillis() - mStartTime;
			if (distance < TIME_DISTANCE) {
				return;
			}
			mStartTime = SystemClock.currentThreadTimeMillis();

		} else {
			mStartTime = SystemClock.currentThreadTimeMillis();
			if (mBaseActivity != null && isShowDialog) {
				((BaseActivity) getActivity()).startLoadingDialog();
			}
		}

		if (AccountManager.isLogin(this.getActivity())) {
			readFieldFromLocal();

			if (mFiledId != DEFAULT_FIELD_ID) {
				param.fieldId = String.valueOf(mFiledId);
				String key = "field" + mFiledId + SharedPreferencesHelper.getString(this.getActivity(), "current_subStage_id", "-1");
				String value = SharedPreferencesHelper.getString(getActivity(), key, null);
				param.task = FieldEntity.TASK_TYPE_ALL;
				// TODO MING : 缓存问题
				// if (TextUtils.isEmpty(value)) {
				// param.task = FieldEntity.TASK_TYPE_ALL;
				// } else {
				// param.task = FieldEntity.TASK_TYPE_NONE;
				// }
				param.lat = "";
				param.lon = "";

				weatherParam.lat = "";
				weatherParam.lon = "";
				weatherParam.monitorLocationId = String.valueOf(mMotionId);

				bannerParam.lat = "";
				bannerParam.lon = "";
				bannerParam.monitorLocationId = String.valueOf(mMotionId);

				loadFieldData(param);
				loadWeatherData(weatherParam);
				loadBanner(bannerParam);
			} else {
				initLocation();
			}

		} else {
			initLocation();

		}

	}

	private void readFieldFromLocal() {
		mMotionId = SharedPreferencesHelper.getInt(this.getActivity(), "FIELD_" + mFiledId, -1);

		String[] userFields = AccountManager.getUserFields(this.getActivity().getApplicationContext());
		String[] motionIds = AccountManager.getLocations(this.getActivity().getApplicationContext());
		if (mFiledId == DEFAULT_FIELD_ID) {

			if (userFields != null && userFields.length > 0) {
				mFiledId = Long.parseLong(userFields[0].trim());
			}

			if (motionIds != null && motionIds.length > 0) {

				mMotionId = Integer.parseInt(motionIds[0]);
				SharedPreferencesHelper.setInt(this.getActivity(), "FIELD_" + mFiledId, mMotionId);
			}
		} else {

			// get postion
			if (mMotionId == DEFAULT_FIELD_ID) {
				int position = 0;
				if (userFields != null) {
					for (int i = 0; i < userFields.length; i++) {
						if (Long.parseLong(userFields[i]) == mFiledId) {
							position = i;
							break;
						}
					}
				}

				if (motionIds != null && position < motionIds.length) {
					mMotionId = Integer.parseInt(motionIds[position]);
					SharedPreferencesHelper.setInt(this.getActivity(), "FIELD_" + mFiledId, mMotionId);
				} else {
					mMotionId = (int) DEFAULT_FIELD_ID;
				}
			}
		}
	}

	public void loadFieldData(FieldEntity.Param param) {

		mHomeRequest = VolleyManager.getInstance().addGetRequestWithCache(REQUEST_CODE_HOME_FIELD, URL_FIELD, param, FieldEntity.class, this);

	}

	public void loadWeatherData(WeatherEntity.Param param) {
		mWeatherRequest = VolleyManager.getInstance()
				.addGetRequestWithCache(REQUEST_CODE_HOME_WEATHER, URL_WEATHER, param, WeatherEntity.class, this);
	}

	private void loadBanner(BannerEntity.Param param) {
		mBannerRequest = VolleyManager.getInstance().addGetRequestWithCache(REQUEST_CODE_HOME_BANNER, URL_BANNER, param, BannerEntity.class, this);
	}

	private void loadDisaster() {
		// addPostRequest
		mBannerRequest = VolleyManager.getInstance().addGetRequestWithCache(REQUEST_CODE_DISASTER, URL_DISASTER, diasterParam, DisasterEntity.class, this);
	}

	private void DEBUG(String msg) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, msg);
		}
	}

	private void resetSuccessFlag() {
		isHomeSuccess = false;
		isBannerSuccess = false;
		isWeatherSuccess = false;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mBaseActivity = null;
	}

	private class LocationListener implements LocationUtils.LocationListener {

		@Override
		public void locationNotify(LocationResult result) {

			param.fieldId = String.valueOf(DEFAULT_FIELD_ID);
			String lat = String.valueOf(result.getLatitude());
			String lon = String.valueOf(result.getLongitude());

			mAddress = result.getCity();

			param.lat = lat;
			param.lon = lon;
			weatherParam.lat = lat;
			weatherParam.lon = lon;
			weatherParam.monitorLocationId = String.valueOf(DEFAULT_FIELD_ID);
			bannerParam.lat = lat;
			bannerParam.lon = lon;
			bannerParam.monitorLocationId = String.valueOf(mMotionId);

			loadFieldData(param);
			loadWeatherData(weatherParam);
			loadBanner(bannerParam);

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK && requestCode == 101) {
			loadFieldData(param);
		}
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < imageViews.length; i++) {
				if (i == arg0) {
					imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
				} else {
					imageViews[i].setBackgroundResource(R.drawable.page_indicator);
				}
			}
		}
	}
}