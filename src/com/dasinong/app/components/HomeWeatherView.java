package com.dasinong.app.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.app.R;

import butterknife.InjectView;


/**
 * Created by sczhang on 15/6/5.
 */
public class HomeWeatherView extends LinearLayout implements View.OnClickListener {
    @InjectView(R.id.ivHomeWicon)
    ImageView ivHomeWicon;
    @InjectView(R.id.lyWeather)
    LinearLayout lyWeather;
    @InjectView(R.id.lyTemp)
    LinearLayout lyTemp;
    @InjectView(R.id.tvTemperature)
    TextView tvTemperature;
    @InjectView(R.id.ivWind)
    ImageView ivWind;
    @InjectView(R.id.tvWind)
    TextView tvWind;
    @InjectView(R.id.ivRainfall)
    ImageView ivRainfall;
    @InjectView(R.id.tvRainfall)
    TextView tvRainfall;
    @InjectView(R.id.lyWind)
    LinearLayout lyWind;
    @InjectView(R.id.ivLine)
    View ivLine;
    @InjectView(R.id.lyHumidity)
    LinearLayout lyHumidity;
    @InjectView(R.id.ivLine2)
    View ivLine2;
    @InjectView(R.id.lyTimeTemp)
    LinearLayout lyTimeTemp;
    @InjectView(R.id.hcTimeTemp)
    HorizontalScrollView hcTimeTemp;
    @InjectView(R.id.ivLine3)
    View ivLine3;
    @InjectView(R.id.lyWeekWeather)
    LinearLayout lyWeekWeather;
    @InjectView(R.id.tvCloseWeekTemp)
    TextView tvCloseWeekTemp;
    private View mRoot;
    private boolean mIsWeekWeatherShow = false;

    public HomeWeatherView(Context context) {
        this(context, null);
    }

    public HomeWeatherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeWeatherView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        setViewListener();
    }

    private void setViewListener() {
        tvCloseWeekTemp.setOnClickListener(this);
    }

    private void init() {
        mRoot = LayoutInflater.from(getContext()).inflate(R.layout.view_home_weather, null);
        lyWeekWeather = (LinearLayout) mRoot.findViewById(R.id.lyWeekWeather);
        tvCloseWeekTemp = (TextView) mRoot.findViewById(R.id.tvCloseWeekTemp);
        for (int i = 0; i < 7; i++) {
            View weekWeather = LayoutInflater.from(getContext()).inflate(R.layout.view_home_weather_week_item, null);
            TextView tvItemDay = (TextView) weekWeather.findViewById(R.id.tvItemDay);
            ImageView ivItemWind = (ImageView) weekWeather.findViewById(R.id.ivItemWind);
            TextView tvItemTempLow = (TextView) weekWeather.findViewById(R.id.tvItemTempLow);
            TextView tvItemTempHight = (TextView) weekWeather.findViewById(R.id.tvItemTempHight);
            TextView tvItemWeather = (TextView) weekWeather.findViewById(R.id.tvItemWeather);
            TextView tvItemWindSpeed = (TextView) weekWeather.findViewById(R.id.tvItemWindSpeed);
            tvItemDay.setText("周" + i);
            tvItemTempLow.setText(6 + i + "");
            tvItemTempHight.setText(18 + i + "~");
            tvItemWeather.setText("晴天" + i);
            tvItemWindSpeed.setText("3-4级");
            lyWeekWeather.addView(weekWeather);
        }
        mIsWeekWeatherShow = false ;
        lyWeekWeather.setVisibility(View.GONE);
        addView(mRoot);



    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvCloseWeekTemp:
                if (mIsWeekWeatherShow){
                    mIsWeekWeatherShow = false ;
                    lyWeekWeather.setVisibility(View.GONE);
                }else{
                    mIsWeekWeatherShow = true ;
                    lyWeekWeather.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
