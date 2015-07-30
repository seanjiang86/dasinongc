package com.dasinong.app.components.home.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.ui.soil.SoilEditorActivity;
import com.dasinong.app.ui.soil.SoilListActivity;
import com.dasinong.app.ui.soil.domain.DataEntity;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by liuningning on 15/6/5.
 */
public class SoilView extends LinearLayout implements View.OnClickListener {

    private TextView mSoilCheck;

    private DataEntity entity;


    public SoilView(Context context) {
        super(context);
        initView();
    }

    public SoilView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        this.setOrientation(VERTICAL);
        this.setBackgroundColor(Color.WHITE);

        LayoutInflater.from(getContext()).inflate(R.layout.view_home_soil, this, true);
        mSoilCheck = (TextView) findViewById(R.id.soil_check);
        mSoilCheck.setOnClickListener(this);
        this.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.soil_check:
            	
            	 // 友盟自定义事件统计
                MobclickAgent.onEvent(this.getContext(), "CheckSoil");
                
                getContext().startActivity(new Intent(getContext(), SoilListActivity.class));


                break;
            default:
            	
            	// 友盟自定义事件统计
                MobclickAgent.onEvent(this.getContext(), "EditorSoilInfo");
            	
                Intent intent = SoilEditorActivity.createIntent(this.getContext(),entity);
                getContext().startActivity(intent);
                break;
        }

    }


    public void updateView(DataEntity latestReport,String soilHum) {
        if(!TextUtils.isEmpty(soilHum)){
            TextView humidity = (TextView) this.findViewById(R.id.soil_tem);
            try {
                double tem = Double.parseDouble(soilHum) * 100;
                humidity.setText(String.format("%.1d", tem) + "%");
            }finally {

            }

        }else {
            soilHum="";
        }
        if (latestReport == null) {
            return;
        }

        entity =latestReport;
        entity.humidity = soilHum;
        TextView color = (TextView) this.findViewById(R.id.soil_color);
        if(!TextUtils.isEmpty(latestReport.color)){
            color.setText(latestReport.color+latestReport.type);
        }

        TextView fertility = (TextView) this.findViewById(R.id.soil_fertility);

        if (!TextUtils.isEmpty(latestReport.fertility)) {
            fertility.setText(latestReport.fertility);

        }




        TextView qn = (TextView) this.findViewById(R.id.soil_qn);
        if (!TextUtils.isEmpty(latestReport.qn)&&!"0.0".equals(latestReport.qn)) {
            qn.setText(latestReport.qn);

        }
        TextView p = (TextView) this.findViewById(R.id.soil_P);
        if (!TextUtils.isEmpty(latestReport.qn)&&!"0.0".equals(latestReport.p)) {
            p.setText(latestReport.p);

        }
        TextView k = (TextView) this.findViewById(R.id.soil_qk);
        if (!TextUtils.isEmpty(latestReport.qK)&&!"0.0".equals(latestReport.qK)) {
            k.setText(latestReport.qK);

        }

    }
}
