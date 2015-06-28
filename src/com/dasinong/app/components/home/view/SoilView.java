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
import android.widget.Toast;

import com.dasinong.app.R;
import com.dasinong.app.ui.soil.SoilEditorActivity;
import com.dasinong.app.ui.soil.SoilListActivity;
import com.dasinong.app.ui.soil.domain.DataEntity;


/**
 * Created by liuningning on 15/6/5.
 */
public class SoilView extends LinearLayout implements View.OnClickListener {

    private TextView mSoilCheck;


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

                getContext().startActivity(new Intent(getContext(), SoilListActivity.class));


                break;
            default:

                Intent intent = new Intent(this.getContext(), SoilEditorActivity.class);
                getContext().startActivity(intent);
                break;
        }

    }


    public void updateView(DataEntity latestReport) {
        if (latestReport == null) {
            return;
        }
        TextView color = (TextView) this.findViewById(R.id.soil_color);
        if(!TextUtils.isEmpty(latestReport.color)){
            color.setText(latestReport.color+latestReport.type);
        }

        TextView fertility = (TextView) this.findViewById(R.id.soil_fertility);

        if (!TextUtils.isEmpty(latestReport.fertility)) {
            fertility.setText(latestReport.fertility);

        }

        TextView humidity = (TextView) this.findViewById(R.id.soil_tem);
        if (!TextUtils.isEmpty(latestReport.humidity)) {
            humidity.setText(latestReport.humidity);

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
