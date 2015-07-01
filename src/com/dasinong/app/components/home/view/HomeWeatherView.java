package com.dasinong.app.components.home.view;

import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dasinong.app.BuildConfig;
import com.dasinong.app.R;
import com.dasinong.app.components.domain.WeatherEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lxn on 15/6/5.
 */
public class HomeWeatherView extends LinearLayout implements View.OnClickListener {


    private View mRoot;
    private boolean mIsWeekWeatherShow = false;
    private HumidityView mHumView;
    private HorizontalScrollView mHorHumView;


    private static final String TAG = "HomeWeatherView";

    private ShrinkAnimation mShrinkAnimation;
    private ExpandAnimation mExpandAnimation;

    private String[] weeks;

    /**
     * 当前天所相关的的View
     */
    private TextView mCurrentWeatherUpdateTime;

    private ImageView mCurrentWeatherIcon;
    private TextView mCurrentWeatherStatus;

    private TextView mCurrentTemp;
    private TextView mCurrentWindowLevel;
    private TextView mCurrentWindowDirect;
    private TextView mCurrentRain;


    /**
     * 七天相关的view
     */
    private TableLayout mSevenDaysContainer;

    private TextView mOpenSevenDays;


    private int weatherCount = 1;
    /**
     * four section
     */
    private TextView mWeatherMorning;
    private TextView mWeatherAfternoon;
    private TextView mWeatherNight;
    private TextView mWeatherMidnight;

    private ImageView mWeatherMorningIcon;
    private ImageView mWeatherAfternoonIcon;
    private ImageView mWeatherNightIcon;
    private ImageView mWeatherMidnightIcon;

    private String[] winds;

    private String[] windDirect;

    private static final HashMap<String, String> weatherMaps = new HashMap<>();

    private static final HashMap<String, String> iconMaps = new HashMap<>();



    private int height;



    private LayoutInflater mLayoutInflater;


    static {

        weatherMaps.put("00", "晴");
        weatherMaps.put("01", "多云");
        weatherMaps.put("02", "阴");
        weatherMaps.put("03", "阵雨");
        weatherMaps.put("04", "雷阵雨");
        weatherMaps.put("05", "雷阵雨伴有冰雹");
        weatherMaps.put("06", "雨夹雪");
        weatherMaps.put("07", "小雨");
        weatherMaps.put("08", "中雨");
        weatherMaps.put("09", "大雨");
        weatherMaps.put("10", "暴雨");
        weatherMaps.put("11", "大暴雨");
        weatherMaps.put("12", "特大暴雨");
        weatherMaps.put("13", "阵雪");
        weatherMaps.put("14", "小雪");
        weatherMaps.put("15", "中雪");
        weatherMaps.put("16", "大雪");
        weatherMaps.put("17", "暴雪");
        weatherMaps.put("18", "雾");
        weatherMaps.put("19", "冻雨");
        weatherMaps.put("20", "沙尘暴");
        weatherMaps.put("21", "小到中雨");
        weatherMaps.put("22", "中到大雨");
        weatherMaps.put("23", "大雨到暴雨");
        weatherMaps.put("24", "暴雨到大暴雨");
        weatherMaps.put("25", "大暴雨到特大暴雨");
        weatherMaps.put("26", "小到中雪");
        weatherMaps.put("27", "中到大雪");
        weatherMaps.put("28", "大到暴雪");
        weatherMaps.put("29", "浮尘");
        weatherMaps.put("30", "扬沙");
        weatherMaps.put("31", "强沙尘暴");
        weatherMaps.put("53", "霾");
        weatherMaps.put("99", "无");


        iconMaps.put("00", "sunnyday");
        iconMaps.put("01", "cloudyday");
        iconMaps.put("02", "cloudy");
        iconMaps.put("03", "rainscatteredday");
        iconMaps.put("04", "stormday");
        iconMaps.put("05", "stormhail");
        iconMaps.put("06", "rainsnow");
        iconMaps.put("07", "rainsmall");
        iconMaps.put("08", "rainmid");
        iconMaps.put("09", "rainbig");
        iconMaps.put("10", "rainheavy");
        iconMaps.put("11", "rainheavier");
        iconMaps.put("12", "rainsevereextreme");
        iconMaps.put("13", "snowscatteredday");
        iconMaps.put("14", "snowsmall");
        iconMaps.put("15", "snowmid");
        iconMaps.put("16", "snowbig");
        iconMaps.put("17", "snowbigheavy");
        iconMaps.put("18", "fogday");
        iconMaps.put("19", "frozenrain");
        iconMaps.put("20", "duststorm");
        iconMaps.put("21", "rainsmallmid");
        iconMaps.put("22", "rainmidbig");
        iconMaps.put("23", "rainbigheavy");
        iconMaps.put("24", "rainheavyheavier");
        iconMaps.put("25", "rainheaviersevere");
        iconMaps.put("26", "snowsmallmid");
        iconMaps.put("27", "snowmidbig");
        iconMaps.put("28", "snowbigheavy");
        iconMaps.put("29", "dust");
        iconMaps.put("30", "dustmid");
        iconMaps.put("31", "duststormheavy");
        iconMaps.put("53", "haze");
        iconMaps.put("99", "na");


    }




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

        mLayoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        mCurrentTemp = (TextView) findViewById(R.id.tvTemperature);
        mCurrentWindowLevel = (TextView) findViewById(R.id.wether_window_level);
        mCurrentWindowDirect = (TextView) findViewById(R.id.tvWind);
        mCurrentRain = (TextView) findViewById(R.id.tvRainfall);
        mCurrentWeatherUpdateTime = (TextView) findViewById(R.id.weather_update_time);
        mCurrentWeatherIcon = (ImageView) findViewById(R.id.ivHomeWicon);
        mCurrentWeatherStatus = (TextView) findViewById(R.id.current_weather_status);

    }

    private void updateCurrentWeatherView(WeatherEntity.CurrentWeather item) {


                mCurrentTemp.setText(item.l1 + "°");
                mCurrentWindowLevel.setText(item.l3 + "级");
                mCurrentWindowDirect.setText(getCurrentWindDirect(item.l4));
                mCurrentWeatherStatus.setText(getWeather(item.l5));
                mCurrentWeatherIcon.setImageResource(getCurrentIconRes(item.l5));
                mCurrentRain.setText(item.l6);
                mCurrentWeatherUpdateTime.setText( getCurrentUpdateTime(item.l7));




    }

    private String getCurrentWindDirect(String level4) {

        int index = 0;
        try {
            index = Integer.parseInt(level4) % windDirect.length;
        } catch (Exception e) {
            DEBUG("l4" + level4);
        }

        return windDirect[index];

    }

    private int getCurrentIconRes(String level5) {

        String iconName = iconMaps.get(level5);


        int resId = 0;
        if (!TextUtils.isEmpty(iconName)) {
            resId = getResources().getIdentifier(iconName, "drawable", getContext().getPackageName());
        }
        DEBUG("getCurrentIconRes" + resId + "name" + iconName);
        return resId != 0 ? resId : R.drawable.na;
    }


    private String getWeather(String level5) {
        String status = weatherMaps.get(level5);
        return TextUtils.isEmpty(status) ? "晴转多云" : status;

    }


    private String getCurrentUpdateTime(String Level7) {

        //20:45
        //当前时间与它的时间并差

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");


        try {


            Date date = sdf.parse(Level7);
            DEBUG(Level7);
            Date serverDate = new Date();
            serverDate.setHours(date.getHours()-1);
            serverDate.setMinutes(date.getMinutes());
            Date today = new Date();
            DEBUG("srver"+serverDate.toLocaleString()+"\t"+serverDate.getTime());
            DEBUG("now"+today.toString()+"\t"+today.getTime());
          long time =  Math.abs(today.getTime() - serverDate.getTime());
            DEBUG("time:"+time);

            if(time/ DateUtils.SECOND_IN_MILLIS<1){
                return "刚刚";
            }else if(time/ DateUtils.MINUTE_IN_MILLIS<60){
                return "更新于" +time/ DateUtils.SECOND_IN_MILLIS+"分钟前";
            }else if(time/ DateUtils.HOUR_IN_MILLIS<24){
                return "更新于" +time/ DateUtils.HOUR_IN_MILLIS+"小时前";
            }else {
                return "更新于" +time/ DateUtils.DAY_IN_MILLIS+"天前";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "刚刚";
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

        mWeatherMorningIcon = (ImageView) findViewById(R.id.icon_weather_morning);
        mWeatherAfternoonIcon = (ImageView) findViewById(R.id.icon_weather_afternoon);
        mWeatherNightIcon = (ImageView) findViewById(R.id.icon_weather_night);
        mWeatherMidnightIcon = (ImageView) findViewById(R.id.icon_weather_midnight);



    }

    private void updateFourSectionView(WeatherEntity.SectionWeather pop) {
        if (pop == null) {
            return;
        }
        mWeatherMorning.setText(pop.morning + "%");
        mWeatherMorningIcon.setImageResource(getFourWeatherIcon(pop.morning));
        mWeatherAfternoon.setText(pop.noon + "%");
        mWeatherAfternoonIcon.setImageResource(getFourWeatherIcon(pop.noon));
        mWeatherNight.setText(pop.night + "%");
        mWeatherNightIcon.setImageResource(getFourWeatherIcon(pop.night));
        mWeatherMidnight.setText(pop.nextmidnight + "%");
        mWeatherMidnightIcon.setImageResource(getFourWeatherIcon(pop.nextmidnight));
    }

    private int getFourWeatherIcon(int weather) {
        if(weather<20)
        {
            return R.drawable.pop0;

        }else if(weather<40){
            return R.drawable.pop1;
        }else if(weather<=60){
            return R.drawable.pop3;
        }else if(weather<=80){
            return R.drawable.pop4;
        }else if(weather< 100){
            return R.drawable.pop5;
        }

        return R.drawable.pop5;


    }

    /**
     * 当时间区间 end
     */


    /**
     * 七天相关的view
     */
    private void initSevenDayView() {
        mSevenDaysContainer = (TableLayout) mRoot.findViewById(R.id.lyWeekWeather);
        mOpenSevenDays = (TextView) mRoot.findViewById(R.id.tvCloseWeekTemp);

        mIsWeekWeatherShow = false;
        // mOpenSevenDays.setText(getContext().getString(R.string.weather_open_one_week));
    }


    private void updateSevenDayView(List<WeatherEntity.SevenDay> days) {
        if (days == null || days.isEmpty()) {

            return;
        }
        weatherCount = 0;
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
            weatherCount++;

            int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            weekWeather = getView(item, weeks[week]);


            mSevenDaysContainer.addView(weekWeather);

            TableRow row = (TableRow) mLayoutInflater.inflate(R.layout.weather_row_line, null, false);
            mSevenDaysContainer.addView(row);

        }


        mSevenDaysContainer.setVisibility(View.GONE);
        mSevenDaysContainer.measure(0, 0);

        height = mSevenDaysContainer.getMeasuredHeight();

        mOpenSevenDays.setText(getContext().getString(R.string.weather_open_one_week, weatherCount));


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
        tvItemDay.setText(week);//周

        tvItemTempLow.setText(item.min_temp + "");
        tvItemTempHight.setText(item.max_temp + "~");//


        tvItemWeather.setText(getSevenWeather(item.weather));//晴转多云


        tvItemWindSpeed.setText(getSevenWindLevel(item.dd_level));//3-4级
        ivItemWind.setImageResource(getSevenWeatherIcon(item.weather));

        return weekWeather;
    }


    private String getSevenWeather(String weather) {
        String status = weatherMaps.get(weather);
        return TextUtils.isEmpty(status) ? "晴转多云" : status;

    }
    private int getSevenWeatherIcon(String weather) {
        String iconName = iconMaps.get(weather);
        int resId = 0;
        if (!TextUtils.isEmpty(iconName)) {
            resId = getResources().getIdentifier(iconName, "drawable", getContext().getPackageName());
        }
        DEBUG("getSevenWeatherIcon" + resId + "name" + iconName);
        return resId != 0 ? resId : R.drawable.na;

    }

    private String getSevenWindLevel(String ddLevel) {

        int index = 0;
        try {
            index = Integer.parseInt(ddLevel) % windDirect.length;
        } catch (Exception e) {
            DEBUG("ddLevle parse error" + ddLevel);
        }

        return windDirect[index];

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

                autoScrollPosition();
            }

        }

    }

    private void autoScrollPosition() {


        mHorHumView.post(new Runnable() {
            @Override
            public void run() {
                mHorHumView.measure(0, 0);
                int width = mHorHumView.getMeasuredWidth();

                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);


                 double distance = width / mHumView.getChildCount() * hour;
                mHorHumView.smoothScrollTo((int) distance, 0);
            }
        });
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
                    mOpenSevenDays.setText(getContext().getString(R.string.weather_open_one_week, weatherCount));
                } else {
                    mIsWeekWeatherShow = true;
                    // Visibility
                    startExpandAnimation();
                    mOpenSevenDays.setText(getContext().getString(R.string.weather_close_one_week, weatherCount));
                }
                break;
        }
    }

    public void setWeatherData(WeatherEntity entity) {
        if (entity == null) {
            return;
        }
        updateCurrentWeatherView(entity.current);
        updateSevenDayView(entity.n7d);
        updateHoursView(entity.n12h);
        updateFourSectionView(entity.POP);


    }


    private void initAnimation() {
        if (mShrinkAnimation == null || mExpandAnimation == null) {

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


    private void DEBUG(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }

}
