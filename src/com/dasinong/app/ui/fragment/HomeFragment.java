package com.dasinong.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.toolbox.Volley;
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
import com.dasinong.app.entity.SoilDetail;
import com.dasinong.app.net.NetConfig;
import com.dasinong.app.net.NetRequest;
import com.dasinong.app.net.RequestService;
import com.dasinong.app.ui.AddFieldActivity1;
import com.dasinong.app.ui.AddFieldActivity2;
import com.dasinong.app.ui.AddFieldActivity4;
import com.dasinong.app.ui.MainTabActivity;
import com.dasinong.app.ui.manager.AccountManager;
import com.dasinong.app.ui.soil.SoilEditorActivity;
import com.dasinong.app.ui.soil.SoilInformationActivity;
import com.dasinong.app.ui.soil.SoilListActivity;
import com.dasinong.app.utils.Logger;

import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener, NetRequest.RequestListener,SoilView.OnSoilCickListenr {

    private static final int REQUST_CODE_HOME_TASk = 130;

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


                login();
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

        View view =inflater.inflate(R.layout.fragment_home, container, false);
        SoilView  soilView = (SoilView) view.findViewById(R.id.home_soilview);
        soilView.setOnSoilListener(this);
        soilView.setOnClickListener(this);
        return view;
    }

    private void login() {

        RequestService.getInstance().authcodeLoginReg(this.getActivity(), "13112345678", LoginRegEntity.class, new NetRequest.RequestListener() {

            @Override
            public void onSuccess(int requestCode, BaseEntity resultData) {

                if(resultData.isOk()){
                    LoginRegEntity entity = (LoginRegEntity) resultData;

                    AccountManager.saveAccount(HomeFragment.this.getActivity(), entity.getData());


                }else{
                    Logger.d("TAG", resultData.getMessage());
                }
            }

            @Override
            public void onFailed(int requestCode, Exception error, String msg) {

                Logger.d("TAG","msg"+msg);
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

    public void loadDataFrom(String fiedlId){

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

        Log.d("TAG","home success");
    }

    @Override
    public void onFailed(int requestCode, Exception error, String msg) {

        Log.d("TAG","home failed");

    }

    @Override
    public void onSoilCheck() {
        Intent intent = new Intent(this.getActivity(),SoilListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.home_soilview:
                Intent intent = new Intent(this.getActivity(),SoilEditorActivity.class);
                startActivity(intent);
                break;
        }
    }
}
