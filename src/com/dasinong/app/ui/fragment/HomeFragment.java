package com.dasinong.app.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
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
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.LocationResult;
import com.dasinong.app.entity.LoginRegEntity;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
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

    BannerEntity.Param  bannerParam = new BannerEntity.Param();
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
        DEBUG("onBegin...........");
        loadDataFromWithCache(true);
        isShowDialog = false;
    }

    @Override
    public void onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以


        // 加载完毕后在UI线程结束加载更多

    }


    @Override
    public void onTaskSuccess(int requestCode, Object response) {
        switch (requestCode) {
            case REQUEST_CODE_HOME_FIELD:
                FieldEntity entity = (FieldEntity) response;

                if (entity != null) {
                    if (entity.currentField != null) {
                        mDisasterView.updateView(entity.currentField.natdisws, entity.currentField.petdisws);
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
                            loadDataFromWithCache(true);
                        }
                    });
                    mCropStateView.updateView(entity);

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
        DEBUG("isHomeSuccess:"+isHomeSuccess);
        DEBUG("isWeather:"+isWeatherSuccess);
        DEBUG("isBanner:"+isBannerSuccess);
        if (isHomeSuccess && isWeatherSuccess && isBannerSuccess) {
            DEBUG("isSuccess All");
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
        if (mStartTime < 0) {
            loadDataFromWithCache(true);
        } else {
            long currentFieldId = SharedPreferencesHelper.getLong(this.getActivity(), SharedPreferencesHelper.Field.FIELDID, DEFAULT_FIELD_ID);
            if (mFiledId == currentFieldId) {
                loadDataFromWithCache(false);
            } else {
                loadDataFromWithCache(true);
            }
        }

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
        LocationUtils.getInstance().registerLocationListener(new LocationUtils.LocationListener() {

            @Override
            public void locationNotify(LocationResult result) {
                DEBUG("定位开始执行");
                param.fieldId = String.valueOf(DEFAULT_FIELD_ID);
                param.lat = String.valueOf(result.getLatitude());
                param.lon = String.valueOf(result.getLongitude());
                weatherParam.lat = String.valueOf(result.getLatitude());
                weatherParam.lon = String.valueOf(result.getLongitude());
                weatherParam.monitorLocationId = String.valueOf(DEFAULT_FIELD_ID);

                bannerParam.lat = String.valueOf(result.getLatitude());
                bannerParam.lon = String.valueOf(result.getLongitude());
                loadFieldData(param);
                loadWeatherData(weatherParam);
                loadBanner(bannerParam);
                DEBUG("定位结束");
            }
        });
    }


    private void loadDataFromWithCache(boolean isForce) {
        if (BuildConfig.DEBUG && autoLogin) {
            DEBUG("---auto login---");
            login();
            return;
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
            param.fieldId = String.valueOf(mFiledId);
            param.lat = "";
            param.lon = "";
            weatherParam.lat = "";
            weatherParam.lon = "";
            weatherParam.monitorLocationId = SharedPreferencesHelper.getString(this.getActivity(), "FIELD_" + mFiledId, "");
            bannerParam.lat = "";
            bannerParam.lon = "";
            bannerParam.monitorLocationId = SharedPreferencesHelper.getString(this.getActivity(), "FIELD_" + mFiledId, "");
            loadFieldData(param);
            loadWeatherData(weatherParam);
            loadBanner(bannerParam);
        } else {
            DEBUG("------- not login");
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


    private void login() {

        RequestService.getInstance().authcodeLoginReg(this.getActivity(), "13999999191", LoginRegEntity.class, new NetRequest.RequestListener() {

            @Override
            public void onSuccess(int requestCode, BaseEntity resultData) {

                if (resultData.isOk()) {
                    LoginRegEntity entity = (LoginRegEntity) resultData;

                    AccountManager.saveAccount(HomeFragment.this.getActivity(), entity.getData());
                    AtuoLoadDataFromWithCache();
                }
            }

            @Override
            public void onFailed(int requestCode, Exception error, String msg) {

                AtuoLoadDataFromWithCache();

            }
        });
    }


    private void resetSuccessFlag() {
        isHomeSuccess = false;
        isBannerSuccess = false;
        isWeatherSuccess = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBaseActivity = null;
    }

    //-------test--------


    private void AtuoLoadDataFromWithCache() {
        loadFieldData("10");
        loadWeatherData();
        loadBanner(null);

    }

    public void loadFieldData(String fiedlId) {
        FieldEntity.Param param = new FieldEntity.Param();
        param.fieldId = fiedlId;

        VolleyManager.getInstance().addGetRequestWithCache(
                REQUEST_CODE_HOME_FIELD,
                URL_FIELD,
                param,
                FieldEntity.class,
                this
        );

    }


    public void loadWeatherData() {
        WeatherEntity.Param param = new WeatherEntity.Param();
        param.monitorLocationId = "101010100";
        VolleyManager.getInstance().addGetRequestWithCache(
                REQUEST_CODE_HOME_WEATHER,
                URL_WEATHER,
                param,
                WeatherEntity.class,
                this
        );

    }


//-------test--------


}