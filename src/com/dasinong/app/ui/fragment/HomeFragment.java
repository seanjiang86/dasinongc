package com.dasinong.app.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;

import com.dasinong.app.R;
import com.dasinong.app.components.domain.FieldEntity;
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
import com.dasinong.app.ui.soil.SoilEditorActivity;

import com.dasinong.app.utils.Logger;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

/**
 * 报错注释 06.12 Ming
 *
 * @author Ming
 */

/**
 * 报错注释 06.12 Ming  此处注释掉一个借口
 *
 * @author Ming
 */

public class HomeFragment extends Fragment implements View.OnClickListener, INetRequest, BGARefreshLayout.BGARefreshLayoutDelegate {

    private static final int REQUEST_CODE_HOME_FIELD = 130;
    private static final int REQUEST_CODE_HOME_WEATHER = 131;
    private static final String URL_FIELD = NetConfig.BASE_URL + "home";
    private static final String URL_WEATHER = NetConfig.BASE_URL;

    private ViewGroup mRoot;

    private BGARefreshLayout mRefreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("HomeFragment", "oncreateView");


        if (mRoot != null) {
            ViewGroup parent = (ViewGroup) mRoot.getParent();

            parent.removeView(mRoot);
            return mRoot;
        }
        mRoot = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        mRefreshLayout = (BGARefreshLayout) mRoot.findViewById(R.id.rl_modulename_refresh);

        SoilView soilView = (SoilView) mRoot.findViewById(R.id.home_soilview);

        initRefreshLayout();
        soilView.setOnClickListener(this);
        return mRoot;
    }

    private void loadDataFromWithCache() {

        loadFieldData("10");
        //loadWeatherData();

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


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        // 在这里加载最新数据
        new AsyncTask<Void, Void, Void>() {


            /**
             * Override this method to perform a computation on a background thread. The
             * specified parameters are the parameters passed to {@link #execute}
             * by the caller of this task.
             * <p/>
             * This method can call {@link #publishProgress} to publish updates
             * on the UI thread.
             *
             * @param params The parameters of the task.
             * @return A result, defined by the subclass of this task.
             * @see #onPreExecute()
             * @see #onPostExecute
             * @see #publishProgress
             */
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


    public void loadFieldData(String fiedlId) {
        FieldEntity.Param param = new FieldEntity.Param();
        param.fieldId = fiedlId;
        VolleyManager.getInstance().addGetRequestWithNoCache(
                REQUEST_CODE_HOME_FIELD,
                URL_FIELD,
                param,
                FieldEntity.class,
                this
        );

    }

    public void loadWeatherData() {
        VolleyManager.getInstance().addGetRequestWithCache(
                REQUEST_CODE_HOME_WEATHER,
                URL_WEATHER,
                null,
                null,
                this
        );

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.home_soilview:
                Intent intent = new Intent(this.getActivity(), SoilEditorActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onTaskSuccess(int requestCode, Object response) {

        switch (requestCode) {
            case REQUEST_CODE_HOME_FIELD:
                break;
            case REQUEST_CODE_HOME_WEATHER:
                break;
            default:
                break;

        }

    }

    @Override
    public void onTaskFailedSuccess(int requestCode, NetError error) {

        Log.d("HOMeTassk", String.valueOf(error.netWorkCode.ordinal()));
    }

    @Override
    public void onCache(int requestCode, Object response) {
        switch (requestCode) {
            case REQUEST_CODE_HOME_FIELD:
                break;
            case REQUEST_CODE_HOME_WEATHER:
                break;
            default:
                break;

        }

    }


    private void login() {

        RequestService.getInstance().authcodeLoginReg(this.getActivity(), "13112345678", LoginRegEntity.class, new NetRequest.RequestListener() {

            @Override
            public void onSuccess(int requestCode, BaseEntity resultData) {

                if (resultData.isOk()) {
                    LoginRegEntity entity = (LoginRegEntity) resultData;

                    Logger.d("Home1", resultData.getMessage());
                    AccountManager.saveAccount(HomeFragment.this.getActivity(), entity.getData());
                    loadDataFromWithCache();

                } else {
                    Logger.d("Home", resultData.getMessage());
                }
            }

            @Override
            public void onFailed(int requestCode, Exception error, String msg) {

                Logger.d("Home", "msg" + msg);
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
        Log.d("HomeFragment", "onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d("HomeFragment", "onDestroyView");
    }
}