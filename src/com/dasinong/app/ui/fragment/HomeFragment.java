package com.dasinong.app.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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
import com.dasinong.app.entity.LoginRegEntity;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.utils.Logger;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;


public class HomeFragment extends Fragment implements  INetRequest, BGARefreshLayout.BGARefreshLayoutDelegate {

    private static final int REQUEST_CODE_HOME_FIELD = 130;
    private static final int REQUEST_CODE_HOME_WEATHER = 131;
    private static final int REQUEST_CODE_HOME_BANNER = 132;

    private  boolean home = false;
    private boolean weather = false;
    private boolean banner = false;
    private static final String URL_FIELD = NetConfig.BASE_URL + "home";
    /**
     * loadWeather?monitorLocationId=101010100
     */
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
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);


        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时的文本
        //refreshViewHolder.setLoadingMoreText(loadingMoreText);
        // 设置整个加载更多控件的背景颜色资源id
        //refreshViewHolder.setLoadMoreBackgroundColorRes(loadMoreBackgroundColorRes);
        // 设置整个加载更多控件的背景drawable资源id
        //refreshViewHolder.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes);
        // 设置下拉刷新控件的背景颜色资源id
        //refreshViewHolder.setRefreshViewBackgroundColorRes(refreshViewBackgroundColorRes);
        // 设置下拉刷新控件的背景drawable资源id
        //refreshViewHolder.setRefreshViewBackgroundDrawableRes(refreshViewBackgroundDrawableRes);
        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
        //mRefreshLayout.setCustomHeaderView(mBanner, false);
        // 可选配置  -------------END
    }


    private void loadDataFromWithCache() {

        loadFieldData("10");
        loadWeatherData();
        loadBanner();

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


    private void loadBanner() {


        VolleyManager.getInstance().addGetRequestWithCache(
                REQUEST_CODE_HOME_BANNER,
                URL_BANNER,
                null,
                BannerEntity.class,
                this
        );

    }



    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        // 在这里加载最新数据
        new AsyncTask<Void, Void, Void>() {


            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // 加载完毕后在UI线程结束下拉刷新
                mRefreshLayout.endRefreshing();
            }
        }.execute();

    }

    @Override
    public void onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

        // 加载完毕后在UI线程结束加载更多
        mRefreshLayout.endLoadingMore();
    }

    // 通过代码方式控制进入正在刷新状态。应用场景：某些应用在activity的onStart方法中调用，自动进入正在刷新状态获取最新数据
    public void beginRefreshing() {
        mRefreshLayout.beginRefreshing();
        onBGARefreshLayoutBeginRefreshing(mRefreshLayout);
    }

    // 通过代码方式控制进入加载更多状态
    public void beginLoadingMore() {
        mRefreshLayout.beginLoadingMore();
        onBGARefreshLayoutBeginLoadingMore(mRefreshLayout);
    }







    @Override
    public void onTaskSuccess(int requestCode, Object response) {

        switch (requestCode) {
            case REQUEST_CODE_HOME_FIELD:
                home = true;
                FieldEntity entity = (FieldEntity) response;
                DEBUG("entity:" + entity.toString());
                if (entity != null) {
                    mDisasterView.updateView(entity.currentField.natdisws, entity.currentField.petdisws);
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
                        }
                    });
                    mCropStateView.updateView(entity);

                    mSoilView.updateView(entity.latestReport);
                }
                break;
            case REQUEST_CODE_HOME_WEATHER:
                weather = true;
                WeatherEntity weatherEntity = (WeatherEntity) response;

                DEBUG(weatherEntity.toString());

                    mHomeWeatherView.setWeatherData(weatherEntity);

                break;

            case  REQUEST_CODE_HOME_BANNER:
                banner = true;
                BannerEntity banner = (BannerEntity) response;
                if(banner!=null){
                    mBannerView.updateView(banner);
                }
                break;
            default:
                break;

        }


    }

    @Override
    public void onTaskFailedSuccess(int requestCode, NetError error) {


    }

    @Override
    public void onCache(int requestCode, Object response) {
        switch (requestCode) {
            case REQUEST_CODE_HOME_FIELD:
                DEBUG("onCache callback");
                onTaskSuccess(requestCode, response);
                break;
            case REQUEST_CODE_HOME_WEATHER:
                WeatherEntity entity = (WeatherEntity) response;
                DEBUG(entity.toString());

                break;
            default:
                break;

        }

    }


    private void login() {

        RequestService.getInstance().authcodeLoginReg(this.getActivity(), "13999999191", LoginRegEntity.class, new NetRequest.RequestListener() {

            @Override
            public void onSuccess(int requestCode, BaseEntity resultData) {

                if (resultData.isOk()) {
                    LoginRegEntity entity = (LoginRegEntity) resultData;

                    Logger.d("Home1", resultData.getMessage());
                    AccountManager.saveAccount(HomeFragment.this.getActivity(), entity.getData());
                    loadDataFromWithCache();

                }
            }

            @Override
            public void onFailed(int requestCode, Exception error, String msg) {


                loadDataFromWithCache();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        login();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    private void DEBUG(String msg) {
        if (false) {
            Log.d(TAG, msg);
        }
    }
}