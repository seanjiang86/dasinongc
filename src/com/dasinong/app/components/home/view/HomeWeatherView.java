package com.dasinong.app.components.home.view;

import android.content.Context;
import android.os.Debug;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dasinong.app.BuildConfig;
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

    /**
     * four section
     */
    private TextView mWeatherMorning;
    private TextView mWeatherAfternoon;
    private TextView mWeatherNight;
    private TextView mWeatherMidnight;



    private String[]  winds;

    private String[] windDirect;


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
        winds = getResources().getStringArray(R.array.wind);
        windDirect = getResources().getStringArray(R.array.winddirect);

        mRoot = LayoutInflater.from(getContext()).inflate(R.layout.view_home_weather, null);

        addView(mRoot);

        initCurrentWeatherView();
        initFourSectionView();

        initHoursView();
        initSevenDayView();


    }

    /**
     * 当前天所相关的的View start
     */
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
    /**
     * 当前天所相关的的View end
     */


    /**
     * 当时间区间 start
     */
    private void initFourSectionView() {

        mWeatherMorning = (TextView) findViewById(R.id.weather_morning);
        mWeatherAfternoon = (TextView) findViewById(R.id.weather_afternoon);
        mWeatherNight = (TextView) findViewById(R.id.weather_night);
        mWeatherMidnight = (TextView) findViewById(R.id.weather_midnight);


    }

    private void updateFourSectionView(WeatherEntity.POP pop) {
        if(pop==null){
            return;
        }
        mWeatherMorning.setText(pop.morning + "%");
        mWeatherAfternoon.setText(pop.afternoon + "%");
        mWeatherNight.setText(pop.night + "%");
        mWeatherMidnight.setText(pop.midnight + "%");

    }

    /**
     * 当时间区间 end
     */


    /**
     * 七天相关的view
     */
    private void initSevenDayView() {
        mSevenDaysContainer = (LinearLayout) mRoot.findViewById(R.id.lyWeekWeather);
        mOpenSevenDays = (TextView) mRoot.findViewById(R.id.tvCloseWeekTemp);

        mIsWeekWeatherShow = false;
        mOpenSevenDays.setText(getContext().getString(R.string.weather_open_one_week));
    }


    private void updateSevenDayView(List<WeatherEntity.SevenDay> days) {
        if (days == null || days.isEmpty()) {

            return;
        }
        mSevenDaysContainer.removeAllViews();

        int size = days.size();
        View weekWeather;
        Calendar today = Calendar.getInstance();

        for (int i = 0; i < size; i++) {
            WeatherEntity.SevenDay item = days.get(i);

            if (item == null) {
                continue;
            }
            //得到时间进行比较
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(item.forecast_time);



            if (today.after(calendar)) {
                continue;



            }
            int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            weekWeather = getView(item, weeks[week]);

            mSevenDaysContainer.addView(weekWeather);
        }
    }


    /**
     * 七天相关的end
     */


    /**
     * 24 hours的view
     */
    private void initHoursView() {

        mHorHumView = (HorizontalScrollView) mRoot.findViewById(R.id.horHumView);
        mHumView = (HumidityView) mRoot.findViewById(R.id.humView);
    }


    private void updateHoursView(List<WeatherEntity.Hours> hours) {

        if (null != hours && !hours.isEmpty()) {
            {

                mHumView.setOneDayWeatherData(hours);
                //TODO scroll postion
                autoScrollPosition();
            }

        }

    }

    private void autoScrollPosition() {
    }


    /**
     * 24 hours的end
     */


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
        //updateCurrentWeatherView(entity.current);
        updateSevenDayView(entity.n7d);
        updateHoursView(entity.n12h);
        updateFourSectionView(entity.pop);


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

        //TODO:
        return "晴转多云";

    }


    private String getSevenWindLevel(String ddLevel) {

        int index = 0;
        try{
            index = Integer.parseInt(ddLevel)%windDirect.length;
        }catch (Exception e){
            DEBUG("ddLevle parse error"+ddLevel);
        }

        return windDirect[index];

    }


    private int getIconRes(Object weather) {
        //getResources().getIdentifier(name, "drawable", getContext().getPackageName())
        return R.drawable.ic_weather_dafeng;
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


    private  void DEBUG(String msg){
        if(BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }

}
