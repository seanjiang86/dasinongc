package com.dasinong.app.components.home.view;

import android.content.Context;
import android.text.TextUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by  lxn on 15/6/5.
 */
public class HumidityView extends LinearLayout {


    private static final String TAG = "HumidityView";
    private int mItemWidth;


    //weather icon
    private static final HashMap<String, String> weatherIconMap = new HashMap<String, String>();
    //weather status
    private static final HashMap<String, String> weatherStatusMap = new HashMap<String, String>();

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

    public void setOneDayWeatherData(List<WeatherEntity.Hours> hoursList) {

        if (hoursList == null || hoursList.isEmpty()) {
            DEBUG("24 hour is empty");
            return;
        }

        int size = hoursList.size();
        removeAllViews();
        for (int i = 0; i < size; i++) {
            WeatherEntity.Hours hour = hoursList.get(i);
            View timeItem = LayoutInflater.from(getContext()).inflate(R.layout.view_home_weather_time_item, null);
            TextView tvTime = (TextView) timeItem.findViewById(R.id.tvTime);
            TextView tvTimeHTemp = (TextView) timeItem.findViewById(R.id.tvTimeHTemp);
            ImageView icon = (ImageView) timeItem.findViewById(R.id.ivTime);

            //time
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(hour.time);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            tvTime.setText(String.valueOf(hourOfDay) + ":00");


            //icon
            icon.setImageResource(getIcon(hour.icon));



            //tem


            tvTimeHTemp.setText(hour.temperature + " ℃");


            LayoutParams layoutParams = new LayoutParams(mItemWidth, LayoutParams.MATCH_PARENT);
            addView(timeItem, layoutParams);

        }
    }


    private int getIcon(String icon) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);


//        if (hourOfDay < 6 || hourOfDay > 18) {
//            icon = icon + "night";
//        }
        icon = weatherIconMap.get(icon);
        if (!TextUtils.isEmpty(icon)) {

            int resId = getResources().getIdentifier(icon, "drawable", getContext().getPackageName());

            DEBUG("icon is " + icon + " resId:" + resId);
            if (resId != 0) {
                return resId;
            }


        } else {
            DEBUG("icon is empty");
        }
        return R.drawable.ic_weather_dafeng;
    }


    static {


        //
        weatherStatusMap.put("clear", "晴");
        weatherStatusMap.put("clearnight", "晴");
        weatherStatusMap.put("cloudy", "阴");
        weatherStatusMap.put("cloudyheavyfreezingrain", "阴转重度冻雨");
        weatherStatusMap.put("cloudyheavyfreezingrainlightning", "重度冻雨伴雷电");
        weatherStatusMap.put("cloudyheavyfreezingrainlightningnight", "重度冻雨伴雷电");
        weatherStatusMap.put("cloudyheavyfreezingrainnight", "阴转重度冻雨");
        weatherStatusMap.put("cloudyheavymix", "阴转重度雨夹雪");
        weatherStatusMap.put("cloudyheavymixlightning", "阴转重度雨夹雪");
        weatherStatusMap.put("cloudyheavymixlightningnight", "大雨夹雪伴雷电");
        weatherStatusMap.put("cloudyheavymixnight", "阴转重度雨夹雪");
        weatherStatusMap.put("cloudyheavyrain", "阴转暴雨");
        weatherStatusMap.put("cloudyheavyrainlightning", "暴雨并伴有雷电");
        weatherStatusMap.put("cloudyheavyrainlightningnight", "暴雨并伴有雷电");
        weatherStatusMap.put("cloudyheavyrainnight", "阴转暴雨");
        weatherStatusMap.put("cloudyheavysleet", "重度的冰粒");
        weatherStatusMap.put("cloudyheavysleetlightning", "重度的冰粒");
        weatherStatusMap.put("cloudyheavysleetlightningnight", "重度冰粒伴雷电");
        weatherStatusMap.put("cloudyheavysleetnight", "重度的冰粒");
        weatherStatusMap.put("cloudyheavysnow", "阴转大雪");
        weatherStatusMap.put("cloudyheavysnowlightning", "大雪并伴有雷电");
        weatherStatusMap.put("cloudyheavysnowlightningnight", "大雪并伴有雷电");
        weatherStatusMap.put("cloudyheavysnownight", "大雪");
        weatherStatusMap.put("cloudylightfreezingrain", "轻度的冻雨");
        weatherStatusMap.put("cloudylightfreezingrainnight", "轻度的冻雨");
        weatherStatusMap.put("cloudylightmix", "轻度的雨夹雪");
        weatherStatusMap.put("cloudylightmixnight", "轻度的雨夹雪");
        weatherStatusMap.put("cloudylightrain", "小雨");
        weatherStatusMap.put("cloudylightrainlightning", "小雨");
        weatherStatusMap.put("cloudylightrainlightningnight", "小雨并伴有雷电");
        weatherStatusMap.put("cloudylightrainnight", "小雨");
        weatherStatusMap.put("cloudylightsleet", "轻度的冰粒");
        weatherStatusMap.put("cloudylightsleetnight", "轻度的冰粒");
        weatherStatusMap.put("cloudylightsnow", "小雪");
        weatherStatusMap.put("cloudylightsnownight", "小雪");
        weatherStatusMap.put("cloudymediumfreezingrain", "中度的冻雨");
        weatherStatusMap.put("cloudymediumfreezingrainlightning", "冻雨伴雷电");
        weatherStatusMap.put("cloudymediumfreezingrainlightningnight", "冻雨伴雷电");
        weatherStatusMap.put("cloudymediumfreezingrainnight", "中度的冻雨");
        weatherStatusMap.put("cloudymediummix", "中度的雨夹雪");
        weatherStatusMap.put("cloudymediummixlightning", "晴转中度雨夹雪");
        weatherStatusMap.put("cloudymediummixlightningnight", "雨夹雪伴雷电");
        weatherStatusMap.put("cloudymediummixnight", "中度的雨夹雪");
        weatherStatusMap.put("cloudymediumrain", "中雨");
        weatherStatusMap.put("cloudymediumrainlightning", "中雨并伴有雷电");
        weatherStatusMap.put("cloudymediumrainlightningnight", "中雨并伴有雷电");
        weatherStatusMap.put("cloudymediumrainnight", "中雨");
        weatherStatusMap.put("cloudymediumsleet", "中度的冰粒");
        weatherStatusMap.put("cloudymediumsleetlightning", "冰粒伴雷电");
        weatherStatusMap.put("cloudymediumsleetlightningnight", "冰粒伴雷电");
        weatherStatusMap.put("cloudymediumsleetnight", "中度的冰粒");
        weatherStatusMap.put("cloudymediumsnow", "中雪");
        weatherStatusMap.put("cloudymediumsnowlightning", "中雪并伴有雷电");
        weatherStatusMap.put("cloudymediumsnowlightningnight", "中雪并伴有雷电");
        weatherStatusMap.put("cloudymediumsnownight", "中雪");
        weatherStatusMap.put("cloudynight", "阴");
        weatherStatusMap.put("cloudyverylightfreezingrain", "零星冻雨");
        weatherStatusMap.put("cloudyverylightfreezingrainnight", "零星冻雨");
        weatherStatusMap.put("cloudyverylightmix", "零星雨夹雪");
        weatherStatusMap.put("cloudyverylightmixnight", "零星雨夹雪");
        weatherStatusMap.put("cloudyverylightrain", "毛毛雨");
        weatherStatusMap.put("cloudyverylightrainnight", "毛毛雨");
        weatherStatusMap.put("cloudyverylightsleet", "零星冰粒");
        weatherStatusMap.put("cloudyverylightsleetnight", "轻微的冰粒");
        weatherStatusMap.put("cloudyverylightsnow", "零星小雪");
        weatherStatusMap.put("cloudyverylightsnownight", "零星小雪");
        weatherStatusMap.put("mostlyclear", "晴天有云");
        weatherStatusMap.put("mostlyclearheavyfreezingrain", "晴转重度冻雨");
        weatherStatusMap.put("mostlyclearheavyfreezingrainlightning", "重度冻雨伴雷电");
        weatherStatusMap.put("mostlyclearheavyfreezingrainlightningnight", "重度冻雨伴雷电");
        weatherStatusMap.put("mostlyclearheavymix", "重度的雨夹雪");
        weatherStatusMap.put("mostlyclearheavymixlightning", "大雨夹雪伴雷电");
        weatherStatusMap.put("mostlyclearheavymixlightningnight", "大雨夹雪伴雷电");
        weatherStatusMap.put("mostlyclearheavymixnight", "晴转暴雨");
        weatherStatusMap.put("mostlyclearheavyrain", "暴雨并伴有雷电");
        weatherStatusMap.put("mostlyclearheavyrainlightning", "暴雨并伴有雷电");
        weatherStatusMap.put("mostlyclearheavyrainlightningnight", "暴雨并伴有雷电");
        weatherStatusMap.put("mostlyclearheavyrainnight", "晴转暴雨");
        weatherStatusMap.put("mostlyclearheavysleet", "晴转大冰粒");
        weatherStatusMap.put("mostlyclearheavysleetlightning", "重度冰粒伴雷电");
        weatherStatusMap.put("mostlyclearheavysleetlightningnight", "重度冰粒伴雷电");
        weatherStatusMap.put("mostlyclearheavysleetnight", "晴转大冰粒");
        weatherStatusMap.put("mostlyclearheavysnow", "晴转暴雪");
        weatherStatusMap.put("mostlyclearheavysnowlightning", "暴雪并伴有雷电");
        weatherStatusMap.put("mostlyclearheavysnowlightningnight", "暴雪并伴有雷电");
        weatherStatusMap.put("mostlyclearheavysnownight", "晴转暴雪");
        weatherStatusMap.put("mostlyclearlightfreezingrain", "晴伴有轻微冻雨");
        weatherStatusMap.put("mostlyclearlightfreezingrainnight", "轻微冻雨伴雷电");
        weatherStatusMap.put("mostlyclearlightmix", "轻微雨夹雪");
        weatherStatusMap.put("mostlyclearlightmixnight", "轻微雨夹雪");
        weatherStatusMap.put("mostlyclearlightrain", "晴转小雨");
        weatherStatusMap.put("mostlyclearlightrainlightning", "晴转小雨伴雷电");
        weatherStatusMap.put("mostlyclearlightrainlightningnight", "晴转小雨伴雷电");
        weatherStatusMap.put("mostlyclearlightrainnight", "晴转小雨");
        weatherStatusMap.put("mostlyclearlightsleet", "晴伴有轻微冰粒");
        weatherStatusMap.put("mostlyclearlightsleetnight", "晴伴有轻微冰粒");
        weatherStatusMap.put("mostlyclearlightsnow", "晴转小雪");
        weatherStatusMap.put("mostlyclearlightsnownight", "晴转小雪");
        weatherStatusMap.put("mostlyclearmediumfreezingrain", "晴转冻雨");
        weatherStatusMap.put("mostlyclearmediumfreezingrainlightning", "晴转冻雨伴雷电");
        weatherStatusMap.put("mostlyclearmediumfreezingrainlightningnight", "晴转冻雨伴雷电");
        weatherStatusMap.put("mostlyclearmediumfreezingrainnight", "冻雨伴雷电");
        weatherStatusMap.put("mostlyclearheavyfreezingrainnight", "晴转冻雨");
        weatherStatusMap.put("mostlyclearmediummix", "晴转中度雨夹雪");
        weatherStatusMap.put("mostlyclearmediummixlightning", "雨夹雪伴雷电");
        weatherStatusMap.put("mostlyclearmediummixlightningnight", "雨夹雪伴雷电");
        weatherStatusMap.put("mostlyclearmediummixnight", "雨夹雪伴雷电");
        weatherStatusMap.put("mostlyclearmediumrain", "晴转中雨");
        weatherStatusMap.put("mostlyclearmediumrainlightning", "晴转中雨伴雷电");
        weatherStatusMap.put("mostlyclearmediumrainlightningnight", "晴转中雨伴雷电");
        weatherStatusMap.put("mostlyclearmediumrainnight", "晴转中雨");
        weatherStatusMap.put("mostlyclearmediumsleet", "晴伴中等冰粒");
        weatherStatusMap.put("mostlyclearmediumsleetlightning", "晴转冰粒伴雷电");
        weatherStatusMap.put("mostlyclearmediumsleetlightningnight", "晴转冰粒伴雷电");
        weatherStatusMap.put("mostlyclearmediumsleetnight", "晴伴中等冰粒");
        weatherStatusMap.put("mostlyclearmediumsnow", "晴转中雪");
        weatherStatusMap.put("mostlyclearmediumsnowlightning", "晴转中雪伴雷电");
        weatherStatusMap.put("mostlyclearmediumsnowlightningnight", "晴转中雪伴雷电");
        weatherStatusMap.put("mostlyclearmediumsnownight", "晴转中雪");
        weatherStatusMap.put("mostlyclearnight", "晴天有云");
        weatherStatusMap.put("mostlyclearverylightfreezingrain", "晴天偶有小冻雨");
        weatherStatusMap.put("mostlyclearverylightfreezingrainnight", "晴天偶有小冻雨");
        weatherStatusMap.put("mostlyclearverylightmix", "晴天偶有雨夹雪");
        weatherStatusMap.put("mostlyclearverylightmixnight", "晴天偶有雨夹雪");
        weatherStatusMap.put("mostlyclearverylightrain", "晴天偶有微雨");
        weatherStatusMap.put("mostlyclearverylightrainnight", "晴天偶有微雨");
        weatherStatusMap.put("mostlyclearverylightsleet", "晴天偶有小冰粒");
        weatherStatusMap.put("mostlyclearverylightsleetnight", "晴天偶有小冰粒");
        weatherStatusMap.put("mostlyclearverylightsnow", "晴天偶有微雪");
        weatherStatusMap.put("mostlyclearverylightsnownight", "晴天偶有微雪");
        weatherStatusMap.put("mostlycloudy", "大部多云");
        weatherStatusMap.put("mostlycloudyheavyfreezingrain", "重度的冻雨");
        weatherStatusMap.put("mostlycloudyheavyfreezingrainlightning", "重度冻雨伴雷电");
        weatherStatusMap.put("mostlycloudyheavyfreezingrainlightningnight", "重度冻雨伴雷电");
        weatherStatusMap.put("mostlycloudyheavyfreezingrainnight", "重度的冻雨");
        weatherStatusMap.put("mostlycloudyheavymix", "重度的雨夹雪");
        weatherStatusMap.put("mostlycloudyheavymixlightning", "大雨夹雪伴雷电");
        weatherStatusMap.put("mostlycloudyheavymixlightningnight", "大雨夹雪伴雷电");
        weatherStatusMap.put("mostlycloudyheavymixnight", "重度的雨夹雪");
        weatherStatusMap.put("mostlycloudyheavyrain", "阴转暴雪");
        weatherStatusMap.put("mostlycloudyheavyrainlightning", "暴雨并伴有雷电");
        weatherStatusMap.put("mostlycloudyheavyrainlightningnight", "暴雨并伴有雷电");
        weatherStatusMap.put("mostlycloudyheavyrainnight", "阴转暴雪");
        weatherStatusMap.put("mostlycloudyheavysleet", "重度的冰粒");
        weatherStatusMap.put("mostlycloudyheavysleetlightning", "重度冰粒伴雷电");
        weatherStatusMap.put("mostlycloudyheavysleetlightningnight", "重度冰粒伴雷电");
        weatherStatusMap.put("mostlycloudyheavysleetnight", "重度的冰粒");
        weatherStatusMap.put("mostlycloudyheavysnow", "阴转暴雪");
        weatherStatusMap.put("mostlycloudyheavysnowlightning", "暴雪并伴有雷电");
        weatherStatusMap.put("mostlycloudyheavysnowlightningnight", "暴雪并伴有雷电");
        weatherStatusMap.put("mostlycloudyheavysnownight", "阴转暴雪");
        weatherStatusMap.put("mostlycloudylightfreezingrain", "轻微冻雨");
        weatherStatusMap.put("mostlycloudylightfreezingrainnight", "轻微冻雨");
        weatherStatusMap.put("mostlycloudylightmix", "轻微雨夹雪");
        weatherStatusMap.put("mostlycloudylightmixnight", "轻微雨夹雪");
        weatherStatusMap.put("mostlycloudylightrain", "小雨");
        weatherStatusMap.put("mostlycloudylightrainlightning", "小雨并伴有雷电");
        weatherStatusMap.put("mostlycloudylightrainlightningnight", "小雨并伴有雷电");
        weatherStatusMap.put("mostlycloudylightrainnight", "小雨");
        weatherStatusMap.put("mostlycloudylightsleet", "轻微的冰粒");
        weatherStatusMap.put("mostlycloudylightsleetnight", "轻微的冰粒");
        weatherStatusMap.put("mostlycloudylightsnow", "小雪");
        weatherStatusMap.put("mostlycloudylightsnownight", "小雪");
        weatherStatusMap.put("mostlycloudymediumfreezingrain", "中等的冻雨");
        weatherStatusMap.put("mostlycloudymediumfreezingrainlightning", "冻雨伴雷电");
        weatherStatusMap.put("mostlycloudymediumfreezingrainlightningnight", "冻雨伴雷电");
        weatherStatusMap.put("mostlycloudymediumfreezingrainnight", "中等的冻雨");
        weatherStatusMap.put("mostlycloudymediummix", "中等的雨夹雪");
        weatherStatusMap.put("mostlycloudymediummixlightning", "中雨夹雪伴雷电");
        weatherStatusMap.put("mostlycloudymediummixlightningnight", "中雨夹雪伴雷电");
        weatherStatusMap.put("mostlycloudymediummixnight", "中等的雨夹雪");
        weatherStatusMap.put("mostlycloudymediumrain", "中雨");
        weatherStatusMap.put("mostlycloudymediumrainlightning", "中雨并伴有雷电");
        weatherStatusMap.put("mostlycloudymediumrainlightningnight", "中雨并伴有雷电");
        weatherStatusMap.put("mostlycloudymediumrainnight", "中雨");
        weatherStatusMap.put("mostlycloudymediumsleet", "中等的冰粒");
        weatherStatusMap.put("mostlycloudymediumsleetlightning", "冰粒伴雷电");
        weatherStatusMap.put("mostlycloudymediumsleetlightningnight", "冰粒伴雷电");
        weatherStatusMap.put("mostlycloudymediumsleetnight", "中等的冰粒");
        weatherStatusMap.put("mostlycloudymediumsnow", "中雪");
        weatherStatusMap.put("mostlycloudymediumsnowlightning", "中雪并伴有雷电");
        weatherStatusMap.put("mostlycloudymediumsnowlightningnight", "中雪并伴有雷电");
        weatherStatusMap.put("mostlycloudymediumsnownight", "中雪");
        weatherStatusMap.put("mostlycloudynight", "大部多云");
        weatherStatusMap.put("mostlycloudyverylightfreezingrain", "阴偶有小冻雨");
        weatherStatusMap.put("mostlycloudyverylightfreezingrainnight", "阴偶有小冻雨");
        weatherStatusMap.put("mostlycloudyverylightmix", "阴偶有雨夹雪");
        weatherStatusMap.put("mostlycloudyverylightmixnight", "阴偶有雨夹雪");
        weatherStatusMap.put("mostlycloudyverylightrain", "阴偶有小雨");
        weatherStatusMap.put("mostlycloudyverylightrainnight", "阴偶有小雨");
        weatherStatusMap.put("mostlycloudyverylightsleet", "阴偶有小冰粒");
        weatherStatusMap.put("mostlycloudyverylightsleetnight", "多云偶有小冰粒");
        weatherStatusMap.put("mostlycloudyverylightsnow", "阴偶有小雪");
        weatherStatusMap.put("mostlycloudyverylightsnownight", "阴偶有小雪");
        weatherStatusMap.put("partlycloudy", "多云");
        weatherStatusMap.put("partlycloudyheavyfreezingrain", "重度的冻雨");
        weatherStatusMap.put("partlycloudyheavyfreezingrainlightning", "重度冻雨伴雷电");
        weatherStatusMap.put("partlycloudyheavyfreezingrainlightningnight", "重度的冻雨");
        weatherStatusMap.put("partlycloudyheavyfreezingrainnight", "重度的冻雨");
        weatherStatusMap.put("partlycloudyheavymix", "重度的雨夹雪");
        weatherStatusMap.put("partlycloudyheavymixlightning", "大雨夹雪伴雷电");
        weatherStatusMap.put("partlycloudyheavymixlightningnight", "大雨夹雪伴雷电");
        weatherStatusMap.put("partlycloudyheavymixnight", "重度的雨夹雪");
        weatherStatusMap.put("partlycloudyheavyrain", "多云转暴雨");
        weatherStatusMap.put("partlycloudyheavyrainlightning", "暴雨并伴有雷电");
        weatherStatusMap.put("partlycloudyheavyrainlightningnight", "暴雨并伴有雷电");
        weatherStatusMap.put("partlycloudyheavyrainnight", "多云转暴雨");
        weatherStatusMap.put("partlycloudyheavysleet", "重度的冰粒");
        weatherStatusMap.put("partlycloudyheavysleetlightning", "重度冰粒伴雷电");
        weatherStatusMap.put("partlycloudyheavysleetlightningnight", "重度冰粒伴雷电");
        weatherStatusMap.put("partlycloudyheavysleetnight", "重度的冰粒");
        weatherStatusMap.put("partlycloudyheavysnow", "多云转暴雪");
        weatherStatusMap.put("partlycloudyheavysnowlightning", "暴雪并伴有雷电");
        weatherStatusMap.put("partlycloudyheavysnowlightningnight", "暴雪并伴有雷电");
        weatherStatusMap.put("partlycloudyheavysnownight", "多云转暴雪");
        weatherStatusMap.put("partlycloudylightfreezingrain", "轻微冻雨");
        weatherStatusMap.put("partlycloudylightfreezingrainnight", "轻微冻雨");
        weatherStatusMap.put("partlycloudylightmix", "轻微雨夹雪");
        weatherStatusMap.put("partlycloudylightmixnight", "轻微雨夹雪");
        weatherStatusMap.put("partlycloudylightrain", "小雨");
        weatherStatusMap.put("partlycloudylightrainlightning", "小雨并伴有雷电");
        weatherStatusMap.put("partlycloudylightrainlightningnight", "小雨并伴有雷电");
        weatherStatusMap.put("partlycloudylightrainnight", "小雨");
        weatherStatusMap.put("partlycloudylightsleet", "轻微的冰粒");
        weatherStatusMap.put("partlycloudylightsleetnight", "轻微的冰粒");
        weatherStatusMap.put("partlycloudylightsnow", "小雪");
        weatherStatusMap.put("partlycloudylightsnownight", "小雪");
        weatherStatusMap.put("partlycloudymediumfreezingrain", "中等的冻雨");
        weatherStatusMap.put("partlycloudymediumfreezingrainlightning", "冻雨伴雷电");
        weatherStatusMap.put("partlycloudymediumfreezingrainlightningnight", "冻雨伴雷电");
        weatherStatusMap.put("partlycloudymediumfreezingrainnight", "冻雨伴雷电");
        weatherStatusMap.put("partlycloudymediummix", "中等的雨夹雪");
        weatherStatusMap.put("partlycloudymediummixlightning", "中雨夹雪伴雷电");
        weatherStatusMap.put("partlycloudymediummixnight", "中雨夹雪伴雷电");
        weatherStatusMap.put("partlycloudymediummixlightningnight", "中等的雨夹雪");
        weatherStatusMap.put("partlycloudymediumrain", "中雨");
        weatherStatusMap.put("partlycloudymediumrainlightning", "中雨并伴有雷电");
        weatherStatusMap.put("partlycloudymediumrainlightningnight", "中雨并伴有雷电");
        weatherStatusMap.put("partlycloudymediumrainnight", "中雨");
        weatherStatusMap.put("partlycloudymediumsleet", "中等的冰粒");
        weatherStatusMap.put("partlycloudymediumsleetlightning", "冰粒伴雷电");
        weatherStatusMap.put("partlycloudymediumsleetlightningnight", "冰粒伴雷电");
        weatherStatusMap.put("partlycloudymediumsleetnight", "中等的冰粒");
        weatherStatusMap.put("partlycloudymediumsnow", "中雪");
        weatherStatusMap.put("partlycloudymediumsnowlightning", "中雪并伴有雷电");
        weatherStatusMap.put("partlycloudymediumsnowlightningnight", "中雪并伴有雷电");
        weatherStatusMap.put("partlycloudymediumsnownight", "中雪");
        weatherStatusMap.put("partlycloudynight", "多云");
        weatherStatusMap.put("partlycloudyverylightfreezingrain", "多云偶有小冻雨");
        weatherStatusMap.put("partlycloudyverylightfreezingrainnight", "多云偶有小冻雨");
        weatherStatusMap.put("partlycloudyverylightmix", "多云偶有雨夹雪");
        weatherStatusMap.put("partlycloudyverylightmixnight", "多云偶有雨夹雪");
        weatherStatusMap.put("partlycloudyverylightrain", "多云偶有小雨");
        weatherStatusMap.put("partlycloudyverylightrainnight", "多云偶有小雨");
        weatherStatusMap.put("partlycloudyverylightsleet", "多云偶有小冰粒");
        weatherStatusMap.put("partlycloudyverylightsleetnight", "多云偶有小冰粒");
        weatherStatusMap.put("partlycloudyverylightsnow", "多云偶有小雪");
        weatherStatusMap.put("partlycloudyverylightsnownight", "多云偶有小雪");


        weatherIconMap.put("clear", "sunnyday");
        weatherIconMap.put("clearnight", "sunnynight");
        weatherIconMap.put("cloudy", "cloudy");
        weatherIconMap.put("cloudyheavyfreezingrain", "frozenrainheavy");
        weatherIconMap.put("cloudyheavyfreezingrainlightning", "icebigthunder");
        weatherIconMap.put("cloudyheavyfreezingrainlightningnight", "icebigthunder");
        weatherIconMap.put("cloudyheavyfreezingrainnight", "frozenrainheavy");
        weatherIconMap.put("cloudyheavymix", "rainbigsnow");
        weatherIconMap.put("cloudyheavymixlightning", "rainbigsnow");
        weatherIconMap.put("cloudyheavymixlightningnight", "rainbigsnowthunder");
        weatherIconMap.put("cloudyheavymixnight", "rainbigsnow");
        weatherIconMap.put("cloudyheavyrain", "rainheavy");
        weatherIconMap.put("cloudyheavyrainlightning", "rainheavierthunder");
        weatherIconMap.put("cloudyheavyrainlightningnight", "rainheavierthunder");
        weatherIconMap.put("cloudyheavyrainnight", "rainheavy");
        weatherIconMap.put("cloudyheavysleet", "icebig");
        weatherIconMap.put("cloudyheavysleetlightning", "icebig");
        weatherIconMap.put("cloudyheavysleetlightningnight", "icebigthunder");
        weatherIconMap.put("cloudyheavysleetnight", "icebig");
        weatherIconMap.put("cloudyheavysnow", "snowbig");
        weatherIconMap.put("cloudyheavysnowlightning", "snowbigheavythunder");
        weatherIconMap.put("cloudyheavysnowlightningnight", "snowbigheavythunder");
        weatherIconMap.put("cloudyheavysnownight", "snowbig");
        weatherIconMap.put("cloudylightfreezingrain", "sunnywithfrozenrainday");
        weatherIconMap.put("cloudylightfreezingrainnight", "sunnywithfrozenrainnight");
        weatherIconMap.put("cloudylightmix", "sunnyrainsnowday");
        weatherIconMap.put("cloudylightmixnight", "sunnyrainsnownight");
        weatherIconMap.put("cloudylightrain", "rainsmall");
        weatherIconMap.put("cloudylightrainlightning", "rainsmall");
        weatherIconMap.put("cloudylightrainlightningnight", "rainsmallthunder");
        weatherIconMap.put("cloudylightrainnight", "rainsmall");
        weatherIconMap.put("cloudylightsleet", "sunnywithiceday");
        weatherIconMap.put("cloudylightsleetnight", "sunnywithicenight");
        weatherIconMap.put("cloudylightsnow", "snowsmall");
        weatherIconMap.put("cloudylightsnownight", "snowsmall");
        weatherIconMap.put("cloudymediumfreezingrain", "frozenrain");
        weatherIconMap.put("cloudymediumfreezingrainlightning", "icemidthunder");
        weatherIconMap.put("cloudymediumfreezingrainlightningnight",
                "icemidthunder");
        weatherIconMap.put("cloudymediumfreezingrainnight", "frozenrain");
        weatherIconMap.put("cloudymediummix", "rainmidsnow");
        weatherIconMap.put("cloudymediummixlightning", "rainmidsnowthunder");
        weatherIconMap.put("cloudymediummixlightningnight", "rainmidsnowthunder");
        weatherIconMap.put("cloudymediummixnight", "rainmidsnow");
        weatherIconMap.put("cloudymediumrain", "rainmid");
        weatherIconMap.put("cloudymediumrainlightning", "rainmidthunder");
        weatherIconMap.put("cloudymediumrainlightningnight", "rainmidthunder");
        weatherIconMap.put("cloudymediumrainnight", "rainmid");
        weatherIconMap.put("cloudymediumsleet", "sunnywithiceday");
        weatherIconMap.put("cloudymediumsleetlightning", "icemidthunder");
        weatherIconMap.put("cloudymediumsleetlightningnight", "icemidthunder");
        weatherIconMap.put("cloudymediumsleetnight", "sunnywithicenight");
        weatherIconMap.put("cloudymediumsnow", "snowmid");
        weatherIconMap.put("cloudymediumsnowlightning", "snowmidthunder");
        weatherIconMap.put("cloudymediumsnowlightningnight", "snowmidthunder");
        weatherIconMap.put("cloudymediumsnownight", "snowmid");
        weatherIconMap.put("cloudynight", "cloudy");
        weatherIconMap.put("cloudyverylightfreezingrain",
                "sunnywithfrozenrainday");
        weatherIconMap.put("cloudyverylightfreezingrainnight",
                "sunnywithfrozenrainnight");
        weatherIconMap.put("cloudyverylightmix", "sunnyrainsnowday");
        weatherIconMap.put("cloudyverylightmixnight", "sunnyrainsnownight");
        weatherIconMap.put("cloudyverylightrain", "sunnyrainday");
        weatherIconMap.put("cloudyverylightrainnight", "sunnyrainnight");
        weatherIconMap.put("cloudyverylightsleet", "sunnywithiceday");
        weatherIconMap.put("cloudyverylightsleetnight", "sunnywithicenight");
        weatherIconMap.put("cloudyverylightsnow", "sunnysnowday");
        weatherIconMap.put("cloudyverylightsnownight", "sunnysnownight");
        weatherIconMap.put("mostlyclear", "cloudyday");
        weatherIconMap.put("mostlyclearheavyfreezingrain", "frozenrainheavy");
        weatherIconMap.put("mostlyclearheavyfreezingrainlightning",
                "icebigthunder");
        weatherIconMap.put("mostlyclearheavyfreezingrainlightningnight",
                "icebigthunder");
        weatherIconMap.put("mostlyclearheavymix", "rainbigsnow");
        weatherIconMap.put("mostlyclearheavymixlightning", "rainbigsnowthunder");
        weatherIconMap.put("mostlyclearheavymixlightningnight",
                "rainbigsnowthunder");
        weatherIconMap.put("mostlyclearheavymixnight", "rainheavy");
        weatherIconMap.put("mostlyclearheavyrain", "rainheavierthunder");
        weatherIconMap.put("mostlyclearheavyrainlightning", "rainheavierthunder");
        weatherIconMap.put("mostlyclearheavyrainlightningnight",
                "rainheavierthunder");
        weatherIconMap.put("mostlyclearheavyrainnight", "rainheavy");
        weatherIconMap.put("mostlyclearheavysleet", "icebig");
        weatherIconMap.put("mostlyclearheavysleetlightning", "icebigthunder");
        weatherIconMap.put("mostlyclearheavysleetlightningnight", "icebigthunder");
        weatherIconMap.put("mostlyclearheavysleetnight", "icebig");
        weatherIconMap.put("mostlyclearheavysnow", "snowbigheavy");
        weatherIconMap.put("mostlyclearheavysnowlightning", "snowbigheavythunder");
        weatherIconMap.put("mostlyclearheavysnowlightningnight",
                "snowbigheavythunder");
        weatherIconMap.put("mostlyclearheavysnownight", "snowbigheavy");
        weatherIconMap.put("mostlyclearlightfreezingrain",
                "sunnywithfrozenrainday");
        weatherIconMap.put("mostlyclearlightfreezingrainnight",
                "sunnywithfrozenrainnight");
        weatherIconMap.put("mostlyclearlightmix", "sunnyrainsnowday");
        weatherIconMap.put("mostlyclearlightmixnight", "sunnyrainsnownight");
        weatherIconMap.put("mostlyclearlightrain", "rainsmall");
        weatherIconMap.put("mostlyclearlightrainlightning", "rainsmallthunder");
        weatherIconMap.put("mostlyclearlightrainlightningnight",
                "rainsmallthunder");
        weatherIconMap.put("mostlyclearlightrainnight", "rainsmall");
        weatherIconMap.put("mostlyclearlightsleet", "sunnywithiceday");
        weatherIconMap.put("mostlyclearlightsleetnight", "sunnywithicenight");
        weatherIconMap.put("mostlyclearlightsnow", "snowsmall");
        weatherIconMap.put("mostlyclearlightsnownight", "snowsmall");
        weatherIconMap.put("mostlyclearmediumfreezingrain", "frozenrain");
        weatherIconMap.put("mostlyclearmediumfreezingrainlightning",
                "icemidthunder");
        weatherIconMap.put("mostlyclearmediumfreezingrainlightningnight",
                "icemidthunder");
        weatherIconMap.put("mostlyclearmediumfreezingrainnight", "icemidthunder");
        weatherIconMap.put("mostlyclearheavyfreezingrainnight", "frozenrain");
        weatherIconMap.put("mostlyclearmediummix", "rainmidsnow");
        weatherIconMap.put("mostlyclearmediummixlightning", "rainmidsnowthunder");
        weatherIconMap.put("mostlyclearmediummixlightningnight",
                "rainmidsnowthunder");
        weatherIconMap.put("mostlyclearmediummixnight", "rainmidsnowthunder");
        weatherIconMap.put("mostlyclearmediumrain", "rainmid");
        weatherIconMap.put("mostlyclearmediumrainlightning", "rainmidthunder");
        weatherIconMap.put("mostlyclearmediumrainlightningnight",
                "rainmidthunder");
        weatherIconMap.put("mostlyclearmediumrainnight", "rainmid");
        weatherIconMap.put("mostlyclearmediumsleet", "sunnywithiceday");
        weatherIconMap.put("mostlyclearmediumsleetlightning", "icemidthunder");
        weatherIconMap.put("mostlyclearmediumsleetlightningnight",
                "icemidthunder");
        weatherIconMap.put("mostlyclearmediumsleetnight", "sunnywithicenight");
        weatherIconMap.put("mostlyclearmediumsnow", "snowmid");
        weatherIconMap.put("mostlyclearmediumsnowlightning", "snowmidthunder");
        weatherIconMap.put("mostlyclearmediumsnowlightningnight",
                "snowmidthunder");
        weatherIconMap.put("mostlyclearmediumsnownight", "snowmid");
        weatherIconMap.put("mostlyclearnight", "cloudynight");
        weatherIconMap.put("mostlyclearverylightfreezingrain",
                "sunnywithfrozenrainday");
        weatherIconMap.put("mostlyclearverylightfreezingrainnight",
                "sunnywithfrozenrainnight");
        weatherIconMap.put("mostlyclearverylightmix", "sunnyrainsnowday");
        weatherIconMap.put("mostlyclearverylightmixnight", "sunnyrainsnownight");
        weatherIconMap.put("mostlyclearverylightrain", "sunnyrainsnowday");
        weatherIconMap.put("mostlyclearverylightrainnight", "sunnyrainsnownight");
        weatherIconMap.put("mostlyclearverylightsleet", "sunnywithiceday");
        weatherIconMap.put("mostlyclearverylightsleetnight", "sunnywithicenight");
        weatherIconMap.put("mostlyclearverylightsnow", "sunnyrainsnowday");
        weatherIconMap.put("mostlyclearverylightsnownight", "sunnyrainsnownight");
        weatherIconMap.put("mostlycloudy", "cloudyday");
        weatherIconMap.put("mostlycloudyheavyfreezingrain", "frozenrainheavy");
        weatherIconMap.put("mostlycloudyheavyfreezingrainlightning",
                "icebigthunder");
        weatherIconMap.put("mostlycloudyheavyfreezingrainlightningnight",
                "icebigthunder");
        weatherIconMap.put("mostlycloudyheavyfreezingrainnight",
                "frozenrainheavy");
        weatherIconMap.put("mostlycloudyheavymix", "rainbigsnow");
        weatherIconMap.put("mostlycloudyheavymixlightning", "rainbigsnowthunder");
        weatherIconMap.put("mostlycloudyheavymixlightningnight",
                "rainbigsnowthunder");
        weatherIconMap.put("mostlycloudyheavymixnight", "rainbigsnow");
        weatherIconMap.put("mostlycloudyheavyrain", "rainheavy");
        weatherIconMap.put("mostlycloudyheavyrainlightning", "rainheavierthunder");
        weatherIconMap.put("mostlycloudyheavyrainlightningnight",
                "rainheavierthunder");
        weatherIconMap.put("mostlycloudyheavyrainnight", "rainheavy");
        weatherIconMap.put("mostlycloudyheavysleet", "icebig");
        weatherIconMap.put("mostlycloudyheavysleetlightning", "icebigthunder");
        weatherIconMap.put("mostlycloudyheavysleetlightningnight",
                "icebigthunder");
        weatherIconMap.put("mostlycloudyheavysleetnight", "icebig");
        weatherIconMap.put("mostlycloudyheavysnow", "snowbigheavy");
        weatherIconMap.put("mostlycloudyheavysnowlightning",
                "snowbigheavythunder");
        weatherIconMap.put("mostlycloudyheavysnowlightningnight",
                "snowbigheavythunder");
        weatherIconMap.put("mostlycloudyheavysnownight", "snowbigheavy");
        weatherIconMap.put("mostlycloudylightfreezingrain",
                "sunnywithfrozenrainday");
        weatherIconMap.put("mostlycloudylightfreezingrainnight",
                "sunnywithfrozenrainnight");
        weatherIconMap.put("mostlycloudylightmix", "sunnyrainsnowday");
        weatherIconMap.put("mostlycloudylightmixnight", "sunnyrainsnownight");
        weatherIconMap.put("mostlycloudylightrain", "rainsmall");
        weatherIconMap.put("mostlycloudylightrainlightning", "rainsmallthunder");
        weatherIconMap.put("mostlycloudylightrainlightningnight",
                "rainsmallthunder");
        weatherIconMap.put("mostlycloudylightrainnight", "rainsmall");
        weatherIconMap.put("mostlycloudylightsleet", "sunnywithiceday");
        weatherIconMap.put("mostlycloudylightsleetnight", "sunnywithicenight");
        weatherIconMap.put("mostlycloudylightsnow", "snowsmall");
        weatherIconMap.put("mostlycloudylightsnownight", "snowsmall");
        weatherIconMap.put("mostlycloudymediumfreezingrain", "frozenrain");
        weatherIconMap.put("mostlycloudymediumfreezingrainlightning",
                "icemidthunder");
        weatherIconMap.put("mostlycloudymediumfreezingrainlightningnight",
                "icemidthunder");
        weatherIconMap.put("mostlycloudymediumfreezingrainnight", "frozenrain");
        weatherIconMap.put("mostlycloudymediummix", "rainmidsnow");
        weatherIconMap.put("mostlycloudymediummixlightning", "rainmidsnowthunder");
        weatherIconMap.put("mostlycloudymediummixlightningnight",
                "rainmidsnowthunder");
        weatherIconMap.put("mostlycloudymediummixnight", "rainmidsnow");
        weatherIconMap.put("mostlycloudymediumrain", "rainmid");
        weatherIconMap.put("mostlycloudymediumrainlightning", "rainmidthunder");
        weatherIconMap.put("mostlycloudymediumrainlightningnight",
                "rainmidthunder");
        weatherIconMap.put("mostlycloudymediumrainnight", "rainmid");
        weatherIconMap.put("mostlycloudymediumsleet", "sunnywithiceday");
        weatherIconMap.put("mostlycloudymediumsleetlightning", "icemidthunder");
        weatherIconMap.put("mostlycloudymediumsleetlightningnight",
                "icemidthunder");
        weatherIconMap.put("mostlycloudymediumsleetnight", "sunnywithicenight");
        weatherIconMap.put("mostlycloudymediumsnow", "snowmid");
        weatherIconMap.put("mostlycloudymediumsnowlightning", "snowmidthunder");
        weatherIconMap.put("mostlycloudymediumsnowlightningnight",
                "snowmidthunder");
        weatherIconMap.put("mostlycloudymediumsnownight", "snowmid");
        weatherIconMap.put("mostlycloudynight", "cloudynight");
        weatherIconMap.put("mostlycloudyverylightfreezingrain",
                "sunnywithfrozenrainday");
        weatherIconMap.put("mostlycloudyverylightfreezingrainnight",
                "sunnywithfrozenrainnight");
        weatherIconMap.put("mostlycloudyverylightmix", "sunnyrainsnowday");
        weatherIconMap.put("mostlycloudyverylightmixnight", "sunnyrainsnownight");
        weatherIconMap.put("mostlycloudyverylightrain", "sunnyrainday");
        weatherIconMap.put("mostlycloudyverylightrainnight", "sunnyrainnight");
        weatherIconMap.put("mostlycloudyverylightsleet", "sunnywithiceday");
        weatherIconMap.put("mostlycloudyverylightsleetnight", "sunnywithicenight");
        weatherIconMap.put("mostlycloudyverylightsnow", "sunnysnowday");
        weatherIconMap.put("mostlycloudyverylightsnownight", "sunnysnownight");
        weatherIconMap.put("partlycloudy", "cloudyday");
        weatherIconMap.put("partlycloudyheavyfreezingrain", "frozenrainheavy");
        weatherIconMap.put("partlycloudyheavyfreezingrainlightning",
                "icebigthunder");
        weatherIconMap.put("partlycloudyheavyfreezingrainlightningnight",
                "frozenrainheavy");
        weatherIconMap.put("partlycloudyheavyfreezingrainnight",
                "frozenrainheavy");
        weatherIconMap.put("partlycloudyheavymix", "rainbigsnow");
        weatherIconMap.put("partlycloudyheavymixlightning", "rainbigsnowthunder");
        weatherIconMap.put("partlycloudyheavymixlightningnight",
                "rainbigsnowthunder");
        weatherIconMap.put("partlycloudyheavymixnight", "rainbigsnow");
        weatherIconMap.put("partlycloudyheavyrain", "rainheavy");
        weatherIconMap.put("partlycloudyheavyrainlightning", "rainheavierthunder");
        weatherIconMap.put("partlycloudyheavyrainlightningnight",
                "rainheavierthunder");
        weatherIconMap.put("partlycloudyheavyrainnight", "rainheavy");
        weatherIconMap.put("partlycloudyheavysleet", "icebig");
        weatherIconMap.put("partlycloudyheavysleetlightning", "icebigthunder");
        weatherIconMap.put("partlycloudyheavysleetlightningnight",
                "icebigthunder");
        weatherIconMap.put("partlycloudyheavysleetnight", "icebig");
        weatherIconMap.put("partlycloudyheavysnow", "snowbigheavy");
        weatherIconMap.put("partlycloudyheavysnowlightning",
                "snowbigheavythunder");
        weatherIconMap.put("partlycloudyheavysnowlightningnight",
                "snowbigheavythunder");
        weatherIconMap.put("partlycloudyheavysnownight", "snowbigheavy");
        weatherIconMap.put("partlycloudylightfreezingrain",
                "sunnywithfrozenrainday");
        weatherIconMap.put("partlycloudylightfreezingrainnight",
                "sunnywithfrozenrainnight");
        weatherIconMap.put("partlycloudylightmix", "sunnyrainsnowday");
        weatherIconMap.put("partlycloudylightmixnight", "sunnyrainsnownight");
        weatherIconMap.put("partlycloudylightrain", "rainsmall");
        weatherIconMap.put("partlycloudylightrainlightning", "rainsmallthunder");
        weatherIconMap.put("partlycloudylightrainlightningnight",
                "rainsmallthunder");
        weatherIconMap.put("partlycloudylightrainnight", "rainsmall");
        weatherIconMap.put("partlycloudylightsleet", "sunnywithiceday");
        weatherIconMap.put("partlycloudylightsleetnight", "sunnywithicenight");
        weatherIconMap.put("partlycloudylightsnow", "snowsmall");
        weatherIconMap.put("partlycloudylightsnownight", "snowsmall");
        weatherIconMap.put("partlycloudymediumfreezingrain", "frozenrain");
        weatherIconMap.put("partlycloudymediumfreezingrainlightning",
                "icemidthunder");
        weatherIconMap.put("partlycloudymediumfreezingrainlightningnight",
                "icemidthunder");
        weatherIconMap.put("partlycloudymediumfreezingrainnight", "icemidthunder");
        weatherIconMap.put("partlycloudymediummix", "rainmidsnow");
        weatherIconMap.put("partlycloudymediummixlightning", "rainmidsnowthunder");
        weatherIconMap.put("partlycloudymediummixnight", "rainmidsnowthunder");
        weatherIconMap.put("partlycloudymediummixlightningnight", "rainmidsnow");
        weatherIconMap.put("partlycloudymediumrain", "rainmid");
        weatherIconMap.put("partlycloudymediumrainlightning", "rainmidthunder");
        weatherIconMap.put("partlycloudymediumrainlightningnight",
                "rainmidthunder");
        weatherIconMap.put("partlycloudymediumrainnight", "rainmid");
        weatherIconMap.put("partlycloudymediumsleet", "sunnywithiceday");
        weatherIconMap.put("partlycloudymediumsleetlightning", "icemidthunder");
        weatherIconMap.put("partlycloudymediumsleetlightningnight",
                "icemidthunder");
        weatherIconMap.put("partlycloudymediumsleetnight", "sunnywithicenight");
        weatherIconMap.put("partlycloudymediumsnow", "snowmid");
        weatherIconMap.put("partlycloudymediumsnowlightning", "snowmidthunder");
        weatherIconMap.put("partlycloudymediumsnowlightningnight",
                "snowmidthunder");
        weatherIconMap.put("partlycloudymediumsnownight", "snowmid");
        weatherIconMap.put("partlycloudynight", "cloudynight");
        weatherIconMap.put("partlycloudyverylightfreezingrain",
                "sunnywithfrozenrainday");
        weatherIconMap.put("partlycloudyverylightfreezingrainnight",
                "sunnywithfrozenrainnight");
        weatherIconMap.put("partlycloudyverylightmix", "sunnyrainsnowday");
        weatherIconMap.put("partlycloudyverylightmixnight", "sunnyrainsnownight");
        weatherIconMap.put("partlycloudyverylightrain", "sunnyrainday");
        weatherIconMap.put("partlycloudyverylightrainnight", "sunnyrainnight");
        weatherIconMap.put("partlycloudyverylightsleet", "sunnywithiceday");
        weatherIconMap.put("partlycloudyverylightsleetnight", "sunnywithicenight");
        weatherIconMap.put("partlycloudyverylightsnow", "sunnysnowday");
        weatherIconMap.put("partlycloudyverylightsnownight", "sunnysnownight");

    }


    private void DEBUG(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }

}
