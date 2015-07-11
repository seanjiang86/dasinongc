package com.dasinong.app.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.dasinong.app.BuildConfig;
import com.dasinong.app.R;
import com.dasinong.app.components.domain.BannerEntity;
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

import com.dasinong.app.entity.LocationResult;

import com.dasinong.app.net.NetConfig;

import com.dasinong.app.ui.BaseActivity;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;
import com.dasinong.app.utils.LocationUtils;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;


public class HomeFragment extends Fragment implements INetRequest, BGARefreshLayout.BGARefreshLayoutDelegate {
    private boolean autoLogin = false;

    private static final int REQUEST_CODE_HOME_FIELD = 130;
    private static final int REQUEST_CODE_HOME_WEATHER = 131;
    private static final int REQUEST_CODE_HOME_BANNER = 132;

    private boolean isHomeSuccess = false;
    private boolean isWeatherSuccess = false;
    private boolean isBannerSuccess = false;
    private static final String URL_FIELD = NetConfig.BASE_URL + "home";

    private static final String URL_WEATHER = NetConfig.BASE_URL + "loadWeather";
    private static final String URL_BANNER = NetConfig.BASE_URL + "getLaoNong";

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

    FieldEntity.Param param = new FieldEntity.Param();
    WeatherEntity.Param weatherParam = new WeatherEntity.Param();

    BannerEntity.Param bannerParam = new BannerEntity.Param();
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


    private SharedPreferencesChangeListener mSharedPreferencesChangeListener;

    private LocationListener mLocationListener;

    private  SharedPreferences mSharedPreferences;


   // private  boolean isLogin;

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
        mFiledId = SharedPreferencesHelper.getLong(this.getActivity(), SharedPreferencesHelper.Field.FIELDID, DEFAULT_FIELD_ID);
        mStartTime = -1L;
        resetSuccessFlag();
        isShowDialog = true;

        mSharedPreferences = mBaseActivity.getSharedPreferences("dasinong_data", Context.MODE_PRIVATE);
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
    }


    private void initEvent() {

    }


    private void initRefreshLayout() {
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGAStickinessRefreshViewHolder(getActivity(), false);
        // 设置下拉刷新控件的背景颜色资源id
        refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.color_view_home_top_bg);
        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
        //mRefreshLayout.setCustomHeaderView(mBanner, false);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);

    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

        DEBUG("onBGARefreshLayoutBeginRefreshing");
        loadDataFromWithCache(true);
        isShowDialog = false;
        //  }
    }

    @Override
    public void onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        DEBUG("onBGARefreshLayoutBeginLoadingMore");
        mRefreshLayout.endRefreshing();

    }


    @Override
    public void onTaskSuccess(int requestCode, Object response) {
        switch (requestCode) {
            case REQUEST_CODE_HOME_FIELD:
                FieldEntity entity = (FieldEntity) response;

                if (entity != null) {
                    if (entity.currentField != null) {
                        mDisasterView.updateView(entity.currentField.petdisspecws, entity.currentField.petdisws);
                    }


                    mCropStateView.updateView(entity);
                    mCropStateView.setOnAddFieldClickListener(new CropsStateView.MyOnAddFieldClickListener() {
                        @Override
                        public void onWorKContentItemClick(String itemValue, int pos, boolean isSelect) {

                        }

                        @Override
                        public void onAddCroupClick() {

                        }

                        @Override
                        public void onPopWindowItemClick(Long filedId) {
                            //filde popuWidno
                            isShowDialog = true;
                            mFiledId = filedId;
                            loadDataFromWithCache(true);
                        }
                    });

                    mSoilView.updateView(entity.latestReport, entity.soilHum);
                }

                isHomeSuccess = true;
                break;
            case REQUEST_CODE_HOME_WEATHER:

                WeatherEntity weatherEntity = (WeatherEntity) response;

                mHomeWeatherView.setWeatherData(weatherEntity);

                isWeatherSuccess = true;
                break;

            case REQUEST_CODE_HOME_BANNER:

                BannerEntity banner = (BannerEntity) response;
                if (banner != null) {
                    mBannerView.updateView(banner);
                }
                isBannerSuccess = true;
                break;
            default:
                break;

        }
        if (isHomeSuccess && isWeatherSuccess && isBannerSuccess) {
            DEBUG("isSuccess All");
            DEBUG("BGA All");
            mRefreshLayout.endRefreshing();
            resetSuccessFlag();
            isShowDialog = false;
            if (mBaseActivity != null) {
                mRoot.setVisibility(View.VISIBLE);
                mBaseActivity.dismissLoadingDialog();
            }
        }


    }


    @Override
    public void onTaskFailedSuccess(int requestCode, NetError error) {
        DEBUG("BGA:" + error.netWorkCode.name());
        DEBUG("failed:requestCode" + requestCode);
        DEBUG("is..."+(NetError.NET_ERROR==error.netWorkCode));

        switch (requestCode) {
            case REQUEST_CODE_HOME_FIELD:
                DEBUG("failed:mHomeRequest" + mHomeRequest);
                FieldEntity fieldEntity = VolleyManager.getInstance().getCacheDomain(mHomeRequest, FieldEntity.class);

                onTaskSuccess(requestCode, fieldEntity);
                break;
            case REQUEST_CODE_HOME_WEATHER:
                DEBUG("failed:mWeatherRequest" + mWeatherRequest);
                WeatherEntity weather = VolleyManager.getInstance().getCacheDomain(mWeatherRequest, WeatherEntity.class);
                onTaskSuccess(requestCode, weather);

                break;

            case REQUEST_CODE_HOME_BANNER:
                DEBUG("failed:mBannerRequest" + mBannerRequest);
                BannerEntity bannerEntity = VolleyManager.getInstance().getCacheDomain(mBannerRequest, BannerEntity.class);
                onTaskSuccess(requestCode, bannerEntity);
                break;
            default:
                break;

        }


    }

//    @Override
//    public void onCache(int requestCode, Object response) {
//        switch (requestCode) {
//            case REQUEST_CODE_HOME_FIELD:
//            case REQUEST_CODE_HOME_WEATHER:
//            case REQUEST_CODE_HOME_BANNER:
//                onTaskSuccess(requestCode, response);
//                break;
//            default:
//                break;
//
//        }
//
//    }


    @Override
    public void onResume() {
        super.onResume();

        if(mSharedPreferencesChangeListener ==null){
            mSharedPreferencesChangeListener = new SharedPreferencesChangeListener();
        }
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mSharedPreferencesChangeListener);
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
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mSharedPreferencesChangeListener);
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
        if(mLocationListener==null){
            mLocationListener = new LocationListener();
        }else {
            LocationUtils.getInstance().unRegisterLocationListener();
            mLocationListener = new LocationListener();
        }
        LocationUtils.getInstance().registerLocationListener(mLocationListener);
    }


    private void loadDataFromWithCache(boolean isForce) {
        if (BuildConfig.DEBUG && autoLogin) {
            // login();
        }

        if (!isForce) {
            long distance = SystemClock.currentThreadTimeMillis() - mStartTime;
            if (distance < TIME_DISTANCE) {
                return;
            }
            mStartTime = SystemClock.currentThreadTimeMillis();

        } else {
            mStartTime = SystemClock.currentThreadTimeMillis();
            if (mBaseActivity != null && isShowDialog) {
                DEBUG("showDialog");
                mRoot.setVisibility(View.GONE);
                mBaseActivity.startLoadingDialog();
            }
        }

        if (AccountManager.isLogin(this.getActivity())) {
            DEBUG("BGA"+AccountManager.isLogin(this.getActivity()));
            boolean isEmpty = TextUtils.isEmpty(String.valueOf(mFiledId));
            if (!isEmpty && mFiledId != DEFAULT_FIELD_ID) {
                param.fieldId = String.valueOf(mFiledId);
                String key = "field" + mFiledId + SharedPreferencesHelper.getString(this.getActivity(), "current_subStage_id", "-1");
                String value = SharedPreferencesHelper.getString(getActivity(), key, null);
                if (TextUtils.isEmpty(value)) {
                    param.task = FieldEntity.TASK_TYPE_ALL;
                } else {
                    param.task = FieldEntity.TASK_TYPE_NONE;
                }
                param.lat = "";
                param.lon = "";
                weatherParam.lat = "";
                weatherParam.lon = "";

                int motionID = SharedPreferencesHelper.getInt(this.getActivity(), "FIELD_" + mFiledId, -1);
                weatherParam.monitorLocationId = String.valueOf(motionID);
                bannerParam.lat = "";
                bannerParam.lon = "";
                bannerParam.monitorLocationId = String.valueOf(motionID);
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

    public void loadFieldData(FieldEntity.Param param) {

        mHomeRequest = VolleyManager.getInstance().addGetRequestWithCache(
                REQUEST_CODE_HOME_FIELD,
                URL_FIELD,
                param,
                FieldEntity.class,
                this
        );


    }


    public void loadWeatherData(WeatherEntity.Param param) {
        mWeatherRequest = VolleyManager.getInstance().addGetRequestWithCache(
                REQUEST_CODE_HOME_WEATHER,
                URL_WEATHER,
                param,
                WeatherEntity.class,
                this
        );


    }


    private void loadBanner(BannerEntity.Param param) {
        mBannerRequest = VolleyManager.getInstance().addGetRequestWithCache(
                REQUEST_CODE_HOME_BANNER,
                URL_BANNER,
                param,
                BannerEntity.class,
                this
        );


    }

    private void DEBUG(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }


//    private void login() {
//
//        RequestService.getInstance().authcodeLoginReg(this.getActivity(), "13999999191", LoginRegEntity.class, new NetRequest.RequestListener() {
//
//            @Override
//            public void onSuccess(int requestCode, BaseEntity resultData) {
//
//                if (resultData.isOk()) {
//                    LoginRegEntity entity = (LoginRegEntity) resultData;
//
//                    AccountManager.saveAccount(HomeFragment.this.getActivity(), entity.getData());
//                    AtuoLoadDataFromWithCache();
//                }
//            }
//
//            @Override
//            public void onFailed(int requestCode, Exception error, String msg) {
//
//                AtuoLoadDataFromWithCache();
//
//            }
//        });
//    }


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

    //-------test--------


//    private void AtuoLoadDataFromWithCache() {
//        loadFieldData("24");
//        loadWeatherData();
//        loadBanner(null);
//
//    }
//
//    public void loadFieldData(String fiedlId) {
//        FieldEntity.Param param = new FieldEntity.Param();
//        param.fieldId = fiedlId;
//
//        VolleyManager.getInstance().addGetRequestWithCache(
//                REQUEST_CODE_HOME_FIELD,
//                URL_FIELD,
//                param,
//                FieldEntity.class,
//                this
//        );
//
//    }
//
//
//    public void loadWeatherData() {
//        WeatherEntity.Param param = new WeatherEntity.Param();
//        param.monitorLocationId = "101010100";
//        VolleyManager.getInstance().addGetRequestWithCache(
//                REQUEST_CODE_HOME_WEATHER,
//                URL_WEATHER,
//                param,
//                WeatherEntity.class,
//                this
//        );
//
//    }


//-------test--------



    private class SharedPreferencesChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener{


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            DEBUG("key"+key);
            if(key.equals(SharedPreferencesHelper.Field.USER_ID)){
                loadDataFromWithCache(true);
            }
        }
    }


    private  class LocationListener implements  LocationUtils.LocationListener {



        @Override
        public void locationNotify(LocationResult result) {
            DEBUG("BGA定位开始执行");
            param.fieldId = String.valueOf(DEFAULT_FIELD_ID);
            String lat = String.valueOf(result.getLatitude());
            String lon = String.valueOf(result.getLongitude());
            param.lat = lat;
            param.lon = lon;
            weatherParam.lat = lat;
            weatherParam.lon = lon;
            weatherParam.monitorLocationId = String.valueOf(DEFAULT_FIELD_ID);
            bannerParam.lat = lat;
            bannerParam.lon = lon;

            loadFieldData(param);
            loadWeatherData(weatherParam);
            loadBanner(bannerParam);
            DEBUG("BGA定位结束");
        }
    }
}