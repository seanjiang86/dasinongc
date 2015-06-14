package com.dasinong.app.components.home.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dasinong.app.R;
import com.dasinong.app.ui.soil.SoilListActivity;


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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.soil_check:

                getContext().startActivity(new Intent(getContext(), SoilListActivity.class));


                break;
            default:
                break;
        }

    }



}
