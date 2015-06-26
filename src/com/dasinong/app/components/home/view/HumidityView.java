package com.dasinong.app.components.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.app.BuildConfig;
import com.dasinong.app.R;
import com.dasinong.app.components.domain.WeatherEntity;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * Created by  lxn on 15/6/5.
 */
public class HumidityView extends LinearLayout {


    private int mItemWidth;

    public HumidityView(Context context) {
        this(context, null);
    }

    public HumidityView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public HumidityView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {


//        addView();

        WindowManager windownManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = windownManager.getDefaultDisplay().getWidth();
        mItemWidth = width / 6;


    }

    public void setOneDayWeatherData(Object o) {
        if (null != o){
            List<WeatherEntity.Hours> hoursList=(List<WeatherEntity.Hours>) o;
            if(hoursList.isEmpty()){
                if(BuildConfig.DEBUG){
                    Log.d("TAG", "hours is empty");
                }
                return;
            }

            /**
             *
            public  String icon;

            public String accumIceTotal;  //--
            public long time;//11点
            public String pOP;//－－
            public String accumSnowTotal;//－－
            public String relativeHumidity;//－－
            public String accumRainTotal;//－－
            public String windSpeed_10m;//－－
            public String windDirection_10m;//－－
            public String temperature;//温度一个
             */
            int size = hoursList.size();
            for (int i = 0; i <size ; i++) {
                WeatherEntity.Hours hour = hoursList.get(i);
                View timeItem = LayoutInflater.from(getContext()).inflate(R.layout.view_home_weather_time_item, null);
                TextView tvTime = (TextView) timeItem.findViewById(R.id.tvTime);
                TextView tvTimeHTemp = (TextView) timeItem.findViewById(R.id.tvTimeHTemp);
                ImageView icon = (ImageView) timeItem.findViewById(R.id.ivTime);
                icon.setImageResource(getIcon(hour.icon));
                //TextView tvTimeLTemp = (TextView) timeItem.findViewById(R.id.tvTimeLTemp);

                ImageView ivTime = (ImageView) timeItem.findViewById(R.id.ivTime);
                ivTime.setImageResource(getIcon(hour.icon));
                //icon
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(hour.time);
                if(BuildConfig.DEBUG){
                    Log.d("date",calendar.toString());
                }
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                tvTime.setText(String.valueOf(hourOfDay));//time
                tvTimeHTemp.setText(hour.temperature + i + " ℃");
              //  tvTimeLTemp.setText(10 + i + " ℃");
                LayoutParams layoutParams = new LayoutParams(mItemWidth, LayoutParams.MATCH_PARENT);
                addView(timeItem, layoutParams);
//            addView(timeItem);
            }
        }
    }


    private int  getIcon(String icon){


        return R.drawable.ic_weather_dafeng;
    }



}
