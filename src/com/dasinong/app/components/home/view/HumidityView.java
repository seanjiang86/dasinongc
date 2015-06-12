package com.dasinong.app.components.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.app.R;

/**
 * Created by sczhang on 15/6/5.
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
            for (int i = 0; i < 24; i++) {
                View timeItem = LayoutInflater.from(getContext()).inflate(R.layout.view_home_weather_time_item, null);
                TextView tvTime = (TextView) timeItem.findViewById(R.id.tvTime);
                TextView tvTimeHTemp = (TextView) timeItem.findViewById(R.id.tvTimeHTemp);
                TextView tvTimeLTemp = (TextView) timeItem.findViewById(R.id.tvTimeLTemp);

                ImageView ivTime = (ImageView) timeItem.findViewById(R.id.ivTime);
                tvTime.setText(i + "点");
                tvTimeHTemp.setText(20 + i + " ℃");
                tvTimeLTemp.setText(10 + i + " ℃");
                LayoutParams layoutParams = new LayoutParams(mItemWidth, LayoutParams.MATCH_PARENT);
                addView(timeItem, layoutParams);
//            addView(timeItem);
            }
        }
    }


    static class ViewHolder {
        TextView tvTime;
        TextView tvTimeHTemp;
        TextView tvTimeLTemp;
        ImageView ivTime;
    }
}
