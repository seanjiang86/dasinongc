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

import com.dasinong.app.R;
import com.dasinong.app.database.city.dao.impl.CityDaoImpl;
import com.dasinong.app.database.disaster.domain.CPProduct;
import com.dasinong.app.database.disaster.domain.PetDisspec;
import com.dasinong.app.database.disaster.domain.PetSolu;
import com.dasinong.app.database.disaster.service.DisasterManager;
import com.dasinong.app.entity.SoilDetail;
import com.dasinong.app.ui.AddFieldActivity1;
import com.dasinong.app.ui.AddFieldActivity2;
import com.dasinong.app.ui.AddFieldActivity4;
import com.dasinong.app.ui.soil.SoilInformationActivity;

import java.util.List;


public class HomeFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Button tv = new Button(getActivity());
        tv.setText("首页");
        tv.setTextSize(50);


        tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
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

        return tv;//inflater.inflate(R.layout.fragment_home,container,false);
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
}
