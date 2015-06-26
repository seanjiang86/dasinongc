package com.dasinong.app.components.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.app.R;
import com.dasinong.app.components.domain.WeatherEntity;

import java.util.Calendar;
import java.util.List;

/**
 * Created by lxn on 15/6/5.
 */
public class HomeWeatherView extends LinearLayout implements View.OnClickListener {


    private View mRoot;
    private boolean mIsWeekWeatherShow = false;
    private HumidityView mHumView;
    private HorizontalScrollView mHorHumView;


    private static final String TAG = "[HomeWeatherView]";

    private ShrinkAnimation mShrinkAnimation;
    private ExpandAnimation mExpandAnimation;

    private String[] weeks;

    /**
     * 当前天所相关的的View
     */
    private TextView mCurrentWeatherUpdateTime;

    private ImageView mCurrentWeatherIcon;
    private TextView mCurrentWeatherStatus;


    /**
     * 七天相关的view
     */
    private LinearLayout mSevenDaysContainer;

    private TextView mOpenSevenDays;


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
        mOpenSevenDays.setOnClickListener(this);
    }

    private void init() {
        weeks = getResources().getStringArray(R.array.weeks);


        mRoot = LayoutInflater.from(getContext()).inflate(R.layout.view_home_weather, null);


        initCurrentWeatherView();

        initSevenDayView();


        mHorHumView = (HorizontalScrollView) mRoot.findViewById(R.id.horHumView);
        mHumView = (HumidityView) mRoot.findViewById(R.id.humView);
        mIsWeekWeatherShow = false;
        mOpenSevenDays.setText(getContext().getString(R.string.weather_open_one_week));
        addView(mRoot);


    }


    private void initCurrentWeatherView() {

        mCurrentWeatherUpdateTime = (TextView) findViewById(R.id.weather_update_time);
        mCurrentWeatherIcon = (ImageView) findViewById(R.id.ivHomeWicon);
        mCurrentWeatherStatus = (TextView) findViewById(R.id.current_weather_status);

    }

    private void updateCurrentWeatherView(WeatherEntity.CurrentWeather currentWeather) {

        mCurrentWeatherUpdateTime.setText("更新于d分钟前");
        mCurrentWeatherIcon.setImageResource(R.drawable.ic_weather_dafeng);
        mCurrentWeatherStatus.setText("晴转多云");

    }


    private void initSevenDayView() {
        mSevenDaysContainer = (LinearLayout) mRoot.findViewById(R.id.lyWeekWeather);
        mOpenSevenDays = (TextView) mRoot.findViewById(R.id.tvCloseWeekTemp);
    }


    private void updateSevenDayView(List<WeatherEntity.SevenDay> days) {
        if (days == null || days.isEmpty()) {

            return;
        }
        mSevenDaysContainer.removeAllViews();

        int size = days.size();
        View weekWeather;
        for (int i = 0; i < size; i++) {
            WeatherEntity.SevenDay item = days.get(i);

            if (item == null) {
                continue;
            }
            //得到时间进行比较
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(item.forecast_time);
            Calendar today = Calendar.getInstance();

            if (today.after(calendar)) {
                return;
            }
            int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            weekWeather = getView(item, weeks[week]);

            mSevenDaysContainer.addView(weekWeather);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvCloseWeekTemp:
                initAnimation();
                if (mIsWeekWeatherShow) {
                    mIsWeekWeatherShow = false;
                    // gone
                    startShrinkAnimation();
                    mOpenSevenDays.setText(getContext().getString(R.string.weather_open_one_week));
                } else {
                    mIsWeekWeatherShow = true;
                    // Visibility
                    startExpandAnimation();
                    mOpenSevenDays.setText(getContext().getString(R.string.weather_close_one_week));
                }
                break;
        }
    }

    public void setWeatherData(WeatherEntity entity) {
        if (entity == null) {
            return;
        }

        updateSevenDayView(entity.n7d);

        setOneDayWeather(entity.n12h);

    }

    private void setOneDayWeather(List<WeatherEntity.Hours> hours) {
        if (null != hours && !hours.isEmpty()) {
            {
                mHorHumView.removeAllViews();
                mHumView.setOneDayWeatherData(hours);
                //TODO scroll postion
                autoScrollPostion();
            }

        }
    }


    private void initAnimation() {
        if (mShrinkAnimation == null || mExpandAnimation == null) {
            int height = mSevenDaysContainer.getHeight();
            mShrinkAnimation = new ShrinkAnimation(mSevenDaysContainer, height);
            mExpandAnimation = new ExpandAnimation(mSevenDaysContainer, height);
            long duration = (long) (height / getResources().getDisplayMetrics().density);
            mShrinkAnimation.setDuration(duration);
            mExpandAnimation.setDuration(duration);
        }

    }

    private void startShrinkAnimation() {
        mOpenSevenDays.clearAnimation();
        mOpenSevenDays.startAnimation(mShrinkAnimation);
    }

    private void startExpandAnimation() {
        mOpenSevenDays.clearAnimation();
        mOpenSevenDays.startAnimation(mExpandAnimation);
    }


    private String getSevenWeather(Object weather) {


        return "晴转多云";

    }


    private String getSevenWindLevel(Object ddLevel) {


        return "3-4级";

    }


    private int getIconRes(Object weather) {
        //getResources().getIdentifier(name, "drawable", getContext().getPackageName())
        return R.drawable.ic_weather_dafeng;
    }


    private void autoScrollPostion() {
    }


    private View getView(WeatherEntity.SevenDay item, String week) {
        View weekWeather;
        weekWeather = LayoutInflater.from(getContext()).inflate(R.layout.view_home_weather_week_item, null);

        TextView tvItemDay = (TextView) weekWeather.findViewById(R.id.tvItemDay);
        ImageView ivItemWind = (ImageView) weekWeather.findViewById(R.id.ivItemWind);
        TextView tvItemTempLow = (TextView) weekWeather.findViewById(R.id.tvItemTempLow);
        TextView tvItemTempHight = (TextView) weekWeather.findViewById(R.id.tvItemTempHight);
        TextView tvItemWeather = (TextView) weekWeather.findViewById(R.id.tvItemWeather);
        TextView tvItemWindSpeed = (TextView) weekWeather.findViewById(R.id.tvItemWindSpeed);

        /**
         *  public  String ff_level;//风力编码
         public String dd_level;//风向编码（3－4级
         public String min_temp;//最低温
         public String temp;//平均温度
         public String weather;//天气现象编码(晴转多云)
         public long forecast_time;//预报时间（周一,timestamp）
         public String rain;//降不量
         public String max_temp;//最高温度

         */


        tvItemDay.setText(week);//周

        tvItemTempLow.setText(item.min_temp + "");
        tvItemTempHight.setText(item.max_temp + "~");//


        tvItemWeather.setText(getSevenWeather(item.weather));//晴转多云


        tvItemWindSpeed.setText(getSevenWindLevel(item.dd_level));//3-4级

        ivItemWind.setImageResource(getIconRes(item.weather));
        return weekWeather;
    }


}
