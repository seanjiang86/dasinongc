package com.dasinong.app.components.home.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.SuperscriptSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.ui.soil.SoilListActivity;
import com.dasinong.app.ui.soil.domain.DataEntity;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by liuningning on 15/6/5.
 */
public class SoilView extends LinearLayout implements View.OnClickListener {

    private TextView mSoilCheck;

    private DataEntity entity;


    private OnEditorListener mOnEditorListener;


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
                if( this.mOnEditorListener!=null) {
                    MobclickAgent.onEvent(this.getContext(), "EditorSoilInfo");
                    this.mOnEditorListener.onEditListener(entity);

                }
                break;
        }

    }


    public void updateView(DataEntity latestReport,String soilHum) {
        TextView humidity = (TextView) this.findViewById(R.id.soil_tem);
        TextView soil_tem_unit = (TextView) this.findViewById(R.id.soil_tem_unit);
        TextView color = (TextView) this.findViewById(R.id.soil_color);
        TextView fertility = (TextView) this.findViewById(R.id.soil_fertility);
        TextView qn = (TextView) this.findViewById(R.id.soil_qn);
        TextView p = (TextView) this.findViewById(R.id.soil_P);
        TextView k = (TextView) this.findViewById(R.id.soil_qk);
        humidity.setText("－－");
        color.setText("－－");
        fertility.setText("－－");
        qn.setText("－－");
        p.setText("－－");
        k.setText("－－");
        if(!TextUtils.isEmpty(soilHum)){
        	humidity.setText(soilHum);

//            String dest = soilHum+"  m³/m³";
//            SpannableString tem = new SpannableString(dest);
//            int len = soilHum.length();
//            int destLen = dest.length();
//            tem.setSpan(new SuperscriptSpan(),len+1,len+2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            tem.setSpan(new SuperscriptSpan(),destLen-1,destLen, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            humidity.setText(dest);
//            try {
//
//                double tem = Double.parseDouble(soilHum) * 100;
//                humidity.setText(String.format("%.1f", tem) + "%");
//            }finally {
//
//            }

        }else {
            soilHum="";
            soil_tem_unit.setVisibility(View.GONE);
        }
        if (latestReport == null) {
            return;
        }

        entity =latestReport;
        entity.humidity = soilHum;


        if(!TextUtils.isEmpty(latestReport.color)){
            color.setText(latestReport.color+latestReport.type);
        }



        if (!TextUtils.isEmpty(latestReport.fertility)) {
            fertility.setText(latestReport.fertility);

        }





        if (!TextUtils.isEmpty(latestReport.qn)&&!"0.0".equals(latestReport.qn)) {
            qn.setText(latestReport.qn);

        }

        if (!TextUtils.isEmpty(latestReport.qn)&&!"0.0".equals(latestReport.p)) {
            p.setText(latestReport.p);

        }

        if (!TextUtils.isEmpty(latestReport.qK)&&!"0.0".equals(latestReport.qK)) {
            k.setText(latestReport.qK);

        }

    }


    public void setonEditorListener(SoilView.OnEditorListener l){

        this.mOnEditorListener = l;

    }


    public interface OnEditorListener{

        void onEditListener(DataEntity entity);

    }
}
