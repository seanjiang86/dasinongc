package com.dasinong.app.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dasinong.app.R;
import com.dasinong.app.components.home.view.SoilView;
import com.dasinong.app.database.city.dao.impl.CityDaoImpl;
import com.dasinong.app.database.disaster.domain.CPProduct;
import com.dasinong.app.database.disaster.domain.PetDisspec;
import com.dasinong.app.database.disaster.domain.PetSolu;
import com.dasinong.app.database.disaster.service.DisasterManager;
import com.dasinong.app.entity.BaseEntity;
import com.dasinong.app.entity.FieldEntity;
import com.dasinong.app.entity.LoginRegEntity;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.AddFieldActivity1;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.ui.soil.SoilEditorActivity;
import com.dasinong.app.ui.soil.SoilListActivity;
import com.dasinong.app.utils.Logger;

import java.util.List;

/**
 * 报错注释 06.12 Ming
 *
 * @author Ming
 */

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

/**
 * 报错注释 06.12 Ming  此处注释掉一个借口
 *
 * @author Ming
 */

public class HomeFragment extends Fragment implements View.OnClickListener, NetRequest.RequestListener, SoilView.OnSoilCickListenr, BGARefreshLayout.BGARefreshLayoutDelegate {

    private static final int REQUST_CODE_HOME_TASk = 130;


    private BGARefreshLayout mRefreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Volley.newRequestQueue(this.getActivity()).add(null).getCacheEntry();
        //
        //first loading

        //step1
        //online
        //read cache
        //loadFromServer()

        LinearLayout ll = new LinearLayout(this.getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);

        Button login = new Button(getActivity());

        login.setTextSize(50);
        login.setText("login");


        Button tv = new Button(getActivity());

        ll.addView(login);
        ll.addView(tv);
        tv.setText("首页");
        tv.setTextSize(50);
        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadDataFrom("");
            }
        });

        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {


//                login();
                // TODO Auto-generated method stub
                //Intent intent = new Intent(getActivity(), AddFieldActivity4.class);
                //startActivity(intent);
                // queryCity();
                // queryDisaster();


                Intent intent = new Intent(getActivity(), AddFieldActivity1.class);

                startActivity(intent);


                // queryCity();
                Log.d("TAG", "HomeFragment");


            }


        });

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.rl_modulename_refresh);

        SoilView soilView = (SoilView) view.findViewById(R.id.home_soilview);
        soilView.setOnSoilListener(this);
        initRefreshLayout();
        soilView.setOnClickListener(this);
        return view;
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


    private void login() {

        RequestService.getInstance().authcodeLoginReg(this.getActivity(), "13112345678", LoginRegEntity.class, new NetRequest.RequestListener() {

            @Override
            public void onSuccess(int requestCode, BaseEntity resultData) {

                if (resultData.isOk()) {
                    LoginRegEntity entity = (LoginRegEntity) resultData;

                    AccountManager.saveAccount(HomeFragment.this.getActivity(), entity.getData());


                } else {
                    Logger.d("TAG", resultData.getMessage());
                }
            }

            @Override
            public void onFailed(int requestCode, Exception error, String msg) {

                Logger.d("TAG", "msg" + msg);
            }
        });
    }


    /**
     * 城市的查询
     */
    private void queryCity() {
        CityDaoImpl dao = new CityDaoImpl(HomeFragment.this.getActivity());
        List<String> province = dao.getProvince();

        List<String> city = dao.getCity(province.get(3));
        List<String> county = dao.getCounty(city.get(0));
        List<String> district = dao.getDistrict(county.get(0));
    }

    /**
     * 病虫草的查询
     */
    private void queryDisaster() {
        DisasterManager manager = DisasterManager.getInstance(this.getActivity());
        //列表
        List<PetDisspec> disease = manager.getDisease("病害");
        //防治方案
        List<PetSolu> cureSolution = manager.getCureSolution(disease.get(0).petDisSpecId);

        // 冶疗方案
        List<PetSolu> preventSolution = manager.getPreventSolution(disease.get(0).petDisSpecId);

        //药
        List<CPProduct> allDrug = manager.getAllDrug(10);

        Log.d("TAG", "---");
    }

    public void loadDataFrom(String fiedlId) {

        FieldEntity.Param param = new FieldEntity.Param();
        param.fieldId = fiedlId;
        RequestService.getInstance().sendRequestWithOutToken(this.getActivity(),
                FieldEntity.class,
                REQUST_CODE_HOME_TASk,
                NetConfig.SubUrl.GET_HOME_TASK,
                param,
                this
        );

    }

    @Override
    public void onSuccess(int requestCode, BaseEntity resultData) {

        Log.d("TAG", "home success");
    }

    @Override
    public void onFailed(int requestCode, Exception error, String msg) {

        Log.d("TAG", "home failed");

    }

    @Override
    public void onSoilCheck() {
        Intent intent = new Intent(this.getActivity(), SoilListActivity.class);
        startActivity(intent);
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

}



