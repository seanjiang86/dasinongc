package com.dasinong.app.components.home.view;

import android.content.Context;
import android.text.TextUtils;
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

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    private String[] winds;

    private String[] windDirect;

    private static final HashMap<String, String> weatherMaps = new HashMap<>();

    private static final HashMap<String, String> iconMaps = new HashMap<>();
    //weather icon
    private static final HashMap<String, String> weatherIconMap = new HashMap<>();
    //weather status
    private static final HashMap<String, String> weatherStatusMap = new HashMap<>();


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


        weatherIconMap.put("clear", "sunnyday.png");
        weatherIconMap.put("clearnight", "sunnynight.png");
        weatherIconMap.put("cloudy", "cloudy.png");
        weatherIconMap.put("cloudyheavyfreezingrain", "frozenrainheavy.png");
        weatherIconMap.put("cloudyheavyfreezingrainlightning", "icebigthunder.png");
        weatherIconMap.put("cloudyheavyfreezingrainlightningnight", "icebigthunder.png");
        weatherIconMap.put("cloudyheavyfreezingrainnight", "frozenrainheavy.png");
        weatherIconMap.put("cloudyheavymix", "rainbigsnow.png");
        weatherIconMap.put("cloudyheavymixlightning", "rainbigsnow.png");
        weatherIconMap.put("cloudyheavymixlightningnight", "rainbigsnowthunder.png");
        weatherIconMap.put("cloudyheavymixnight", "rainbigsnow.png");
        weatherIconMap.put("cloudyheavyrain", "rainheavy.png");
        weatherIconMap.put("cloudyheavyrainlightning", "rainheavierthunder.png");
        weatherIconMap.put("cloudyheavyrainlightningnight", "rainheavierthunder.png");
        weatherIconMap.put("cloudyheavyrainnight", "rainheavy.png");
        weatherIconMap.put("cloudyheavysleet", "icebig.png");
        weatherIconMap.put("cloudyheavysleetlightning", "icebig.png");
        weatherIconMap.put("cloudyheavysleetlightningnight", "icebigthunder.png");
        weatherIconMap.put("cloudyheavysleetnight", "icebig.png");
        weatherIconMap.put("cloudyheavysnow", "snowbig.png");
        weatherIconMap.put("cloudyheavysnowlightning", "snowbigheavythunder.png");
        weatherIconMap.put("cloudyheavysnowlightningnight", "snowbigheavythunder.png");
        weatherIconMap.put("cloudyheavysnownight", "snowbig.png");
        weatherIconMap.put("cloudylightfreezingrain", "sunnywithfrozenrainday.png");
        weatherIconMap.put("cloudylightfreezingrainnight",
                "sunnywithfrozenrainnight.png");
        weatherIconMap.put("cloudylightmix", "sunnyrainsnowday.png");
        weatherIconMap.put("cloudylightmixnight", "sunnyrainsnownight.png");
        weatherIconMap.put("cloudylightrain", "rainsmall.png");
        weatherIconMap.put("cloudylightrainlightning", "rainsmall.png");
        weatherIconMap.put("cloudylightrainlightningnight", "rainsmallthunder.png");
        weatherIconMap.put("cloudylightrainnight", "rainsmall.png");
        weatherIconMap.put("cloudylightsleet", "sunnywithiceday.png");
        weatherIconMap.put("cloudylightsleetnight", "sunnywithicenight.png");
        weatherIconMap.put("cloudylightsnow", "snowsmall.png");
        weatherIconMap.put("cloudylightsnownight", "snowsmall.png");
        weatherIconMap.put("cloudymediumfreezingrain", "frozenrain.png");
        weatherIconMap.put("cloudymediumfreezingrainlightning", "icemidthunder.png");
        weatherIconMap.put("cloudymediumfreezingrainlightningnight",
                "icemidthunder.png");
        weatherIconMap.put("cloudymediumfreezingrainnight", "frozenrain.png");
        weatherIconMap.put("cloudymediummix", "rainmidsnow.png");
        weatherIconMap.put("cloudymediummixlightning", "rainmidsnowthunder.png");
        weatherIconMap.put("cloudymediummixlightningnight", "rainmidsnowthunder.png");
        weatherIconMap.put("cloudymediummixnight", "rainmidsnow.png");
        weatherIconMap.put("cloudymediumrain", "rainmid.png");
        weatherIconMap.put("cloudymediumrainlightning", "rainmidthunder.png");
        weatherIconMap.put("cloudymediumrainlightningnight", "rainmidthunder.png");
        weatherIconMap.put("cloudymediumrainnight", "rainmid.png");
        weatherIconMap.put("cloudymediumsleet", "sunnywithiceday.png");
        weatherIconMap.put("cloudymediumsleetlightning", "icemidthunder.png");
        weatherIconMap.put("cloudymediumsleetlightningnight", "icemidthunder.png");
        weatherIconMap.put("cloudymediumsleetnight", "sunnywithicenight.png");
        weatherIconMap.put("cloudymediumsnow", "snowmid.png");
        weatherIconMap.put("cloudymediumsnowlightning", "snowmidthunder.png");
        weatherIconMap.put("cloudymediumsnowlightningnight", "snowmidthunder.png");
        weatherIconMap.put("cloudymediumsnownight", "snowmid.png");
        weatherIconMap.put("cloudynight", "cloudy.png");
        weatherIconMap.put("cloudyverylightfreezingrain",
                "sunnywithfrozenrainday.png");
        weatherIconMap.put("cloudyverylightfreezingrainnight",
                "sunnywithfrozenrainnight.png");
        weatherIconMap.put("cloudyverylightmix", "sunnyrainsnowday.png");
        weatherIconMap.put("cloudyverylightmixnight", "sunnyrainsnownight.png");
        weatherIconMap.put("cloudyverylightrain", "sunnyrainday.png");
        weatherIconMap.put("cloudyverylightrainnight", "sunnyrainnight.png");
        weatherIconMap.put("cloudyverylightsleet", "sunnywithiceday.png");
        weatherIconMap.put("cloudyverylightsleetnight", "sunnywithicenight.png");
        weatherIconMap.put("cloudyverylightsnow", "sunnysnowday.png");
        weatherIconMap.put("cloudyverylightsnownight", "sunnysnownight.png");
        weatherIconMap.put("mostlyclear", "cloudyday.png");
        weatherIconMap.put("mostlyclearheavyfreezingrain", "frozenrainheavy.png");
        weatherIconMap.put("mostlyclearheavyfreezingrainlightning",
                "icebigthunder.png");
        weatherIconMap.put("mostlyclearheavyfreezingrainlightningnight",
                "icebigthunder.png");
        weatherIconMap.put("mostlyclearheavymix", "rainbigsnow.png");
        weatherIconMap.put("mostlyclearheavymixlightning", "rainbigsnowthunder.png");
        weatherIconMap.put("mostlyclearheavymixlightningnight",
                "rainbigsnowthunder.png");
        weatherIconMap.put("mostlyclearheavymixnight", "rainheavy.png");
        weatherIconMap.put("mostlyclearheavyrain", "rainheavierthunder.png");
        weatherIconMap.put("mostlyclearheavyrainlightning", "rainheavierthunder.png");
        weatherIconMap.put("mostlyclearheavyrainlightningnight",
                "rainheavierthunder.png");
        weatherIconMap.put("mostlyclearheavyrainnight", "rainheavy.png");
        weatherIconMap.put("mostlyclearheavysleet", "icebig.png");
        weatherIconMap.put("mostlyclearheavysleetlightning", "icebigthunder.png");
        weatherIconMap.put("mostlyclearheavysleetlightningnight", "icebigthunder.png");
        weatherIconMap.put("mostlyclearheavysleetnight", "icebig.png");
        weatherIconMap.put("mostlyclearheavysnow", "snowbigheavy.png");
        weatherIconMap.put("mostlyclearheavysnowlightning", "snowbigheavythunder.png");
        weatherIconMap.put("mostlyclearheavysnowlightningnight",
                "snowbigheavythunder.png");
        weatherIconMap.put("mostlyclearheavysnownight", "snowbigheavy.png");
        weatherIconMap.put("mostlyclearlightfreezingrain",
                "sunnywithfrozenrainday.png");
        weatherIconMap.put("mostlyclearlightfreezingrainnight",
                "sunnywithfrozenrainnight.png");
        weatherIconMap.put("mostlyclearlightmix", "sunnyrainsnowday.png");
        weatherIconMap.put("mostlyclearlightmixnight", "sunnyrainsnownight.png");
        weatherIconMap.put("mostlyclearlightrain", "rainsmall.png");
        weatherIconMap.put("mostlyclearlightrainlightning", "rainsmallthunder.png");
        weatherIconMap.put("mostlyclearlightrainlightningnight",
                "rainsmallthunder.png");
        weatherIconMap.put("mostlyclearlightrainnight", "rainsmall.png");
        weatherIconMap.put("mostlyclearlightsleet", "sunnywithiceday.png");
        weatherIconMap.put("mostlyclearlightsleetnight", "sunnywithicenight.png");
        weatherIconMap.put("mostlyclearlightsnow", "snowsmall.png");
        weatherIconMap.put("mostlyclearlightsnownight", "snowsmall.png");
        weatherIconMap.put("mostlyclearmediumfreezingrain", "frozenrain.png");
        weatherIconMap.put("mostlyclearmediumfreezingrainlightning",
                "icemidthunder.png");
        weatherIconMap.put("mostlyclearmediumfreezingrainlightningnight",
                "icemidthunder.png");
        weatherIconMap.put("mostlyclearmediumfreezingrainnight", "icemidthunder.png");
        weatherIconMap.put("mostlyclearheavyfreezingrainnight", "frozenrain.png");
        weatherIconMap.put("mostlyclearmediummix", "rainmidsnow.png");
        weatherIconMap.put("mostlyclearmediummixlightning", "rainmidsnowthunder.png");
        weatherIconMap.put("mostlyclearmediummixlightningnight",
                "rainmidsnowthunder.png");
        weatherIconMap.put("mostlyclearmediummixnight", "rainmidsnowthunder.png");
        weatherIconMap.put("mostlyclearmediumrain", "rainmid.png");
        weatherIconMap.put("mostlyclearmediumrainlightning", "rainmidthunder.png");
        weatherIconMap.put("mostlyclearmediumrainlightningnight",
                "rainmidthunder.png");
        weatherIconMap.put("mostlyclearmediumrainnight", "rainmid.png");
        weatherIconMap.put("mostlyclearmediumsleet", "sunnywithiceday.png");
        weatherIconMap.put("mostlyclearmediumsleetlightning", "icemidthunder.png");
        weatherIconMap.put("mostlyclearmediumsleetlightningnight",
                "icemidthunder.png");
        weatherIconMap.put("mostlyclearmediumsleetnight", "sunnywithicenight.png");
        weatherIconMap.put("mostlyclearmediumsnow", "snowmid.png");
        weatherIconMap.put("mostlyclearmediumsnowlightning", "snowmidthunder.png");
        weatherIconMap.put("mostlyclearmediumsnowlightningnight",
                "snowmidthunder.png");
        weatherIconMap.put("mostlyclearmediumsnownight", "snowmid.png");
        weatherIconMap.put("mostlyclearnight", "cloudynight.png");
        weatherIconMap.put("mostlyclearverylightfreezingrain",
                "sunnywithfrozenrainday.png");
        weatherIconMap.put("mostlyclearverylightfreezingrainnight",
                "sunnywithfrozenrainnight.png");
        weatherIconMap.put("mostlyclearverylightmix", "sunnyrainsnowday.png");
        weatherIconMap.put("mostlyclearverylightmixnight", "sunnyrainsnownight.png");
        weatherIconMap.put("mostlyclearverylightrain", "sunnyrainsnowday.png");
        weatherIconMap.put("mostlyclearverylightrainnight", "sunnyrainsnownight.png");
        weatherIconMap.put("mostlyclearverylightsleet", "sunnywithiceday.png");
        weatherIconMap.put("mostlyclearverylightsleetnight", "sunnywithicenight.png");
        weatherIconMap.put("mostlyclearverylightsnow", "sunnyrainsnowday.png");
        weatherIconMap.put("mostlyclearverylightsnownight", "sunnyrainsnownight.png");
        weatherIconMap.put("mostlycloudy", "cloudyday.png");
        weatherIconMap.put("mostlycloudyheavyfreezingrain", "frozenrainheavy.png");
        weatherIconMap.put("mostlycloudyheavyfreezingrainlightning",
                "icebigthunder.png");
        weatherIconMap.put("mostlycloudyheavyfreezingrainlightningnight",
                "icebigthunder.png");
        weatherIconMap.put("mostlycloudyheavyfreezingrainnight",
                "frozenrainheavy.png");
        weatherIconMap.put("mostlycloudyheavymix", "rainbigsnow.png");
        weatherIconMap.put("mostlycloudyheavymixlightning", "rainbigsnowthunder.png");
        weatherIconMap.put("mostlycloudyheavymixlightningnight",
                "rainbigsnowthunder.png");
        weatherIconMap.put("mostlycloudyheavymixnight", "rainbigsnow.png");
        weatherIconMap.put("mostlycloudyheavyrain", "rainheavy.png");
        weatherIconMap.put("mostlycloudyheavyrainlightning", "rainheavierthunder.png");
        weatherIconMap.put("mostlycloudyheavyrainlightningnight",
                "rainheavierthunder.png");
        weatherIconMap.put("mostlycloudyheavyrainnight", "rainheavy.png");
        weatherIconMap.put("mostlycloudyheavysleet", "icebig.png");
        weatherIconMap.put("mostlycloudyheavysleetlightning", "icebigthunder.png");
        weatherIconMap.put("mostlycloudyheavysleetlightningnight",
                "icebigthunder.png");
        weatherIconMap.put("mostlycloudyheavysleetnight", "icebig.png");
        weatherIconMap.put("mostlycloudyheavysnow", "snowbigheavy.png");
        weatherIconMap.put("mostlycloudyheavysnowlightning",
                "snowbigheavythunder.png");
        weatherIconMap.put("mostlycloudyheavysnowlightningnight",
                "snowbigheavythunder.png");
        weatherIconMap.put("mostlycloudyheavysnownight", "snowbigheavy.png");
        weatherIconMap.put("mostlycloudylightfreezingrain",
                "sunnywithfrozenrainday.png");
        weatherIconMap.put("mostlycloudylightfreezingrainnight",
                "sunnywithfrozenrainnight.png");
        weatherIconMap.put("mostlycloudylightmix", "sunnyrainsnowday.png");
        weatherIconMap.put("mostlycloudylightmixnight", "sunnyrainsnownight.png");
        weatherIconMap.put("mostlycloudylightrain", "rainsmall.png");
        weatherIconMap.put("mostlycloudylightrainlightning", "rainsmallthunder.png");
        weatherIconMap.put("mostlycloudylightrainlightningnight",
                "rainsmallthunder.png");
        weatherIconMap.put("mostlycloudylightrainnight", "rainsmall.png");
        weatherIconMap.put("mostlycloudylightsleet", "sunnywithiceday.png");
        weatherIconMap.put("mostlycloudylightsleetnight", "sunnywithicenight.png");
        weatherIconMap.put("mostlycloudylightsnow", "snowsmall.png");
        weatherIconMap.put("mostlycloudylightsnownight", "snowsmall.png");
        weatherIconMap.put("mostlycloudymediumfreezingrain", "frozenrain.png");
        weatherIconMap.put("mostlycloudymediumfreezingrainlightning",
                "icemidthunder.png");
        weatherIconMap.put("mostlycloudymediumfreezingrainlightningnight",
                "icemidthunder.png");
        weatherIconMap.put("mostlycloudymediumfreezingrainnight", "frozenrain.png");
        weatherIconMap.put("mostlycloudymediummix", "rainmidsnow.png");
        weatherIconMap.put("mostlycloudymediummixlightning", "rainmidsnowthunder.png");
        weatherIconMap.put("mostlycloudymediummixlightningnight",
                "rainmidsnowthunder.png");
        weatherIconMap.put("mostlycloudymediummixnight", "rainmidsnow.png");
        weatherIconMap.put("mostlycloudymediumrain", "rainmid.png");
        weatherIconMap.put("mostlycloudymediumrainlightning", "rainmidthunder.png");
        weatherIconMap.put("mostlycloudymediumrainlightningnight",
                "rainmidthunder.png");
        weatherIconMap.put("mostlycloudymediumrainnight", "rainmid.png");
        weatherIconMap.put("mostlycloudymediumsleet", "sunnywithiceday.png");
        weatherIconMap.put("mostlycloudymediumsleetlightning", "icemidthunder.png");
        weatherIconMap.put("mostlycloudymediumsleetlightningnight",
                "icemidthunder.png");
        weatherIconMap.put("mostlycloudymediumsleetnight", "sunnywithicenight.png");
        weatherIconMap.put("mostlycloudymediumsnow", "snowmid.png");
        weatherIconMap.put("mostlycloudymediumsnowlightning", "snowmidthunder.png");
        weatherIconMap.put("mostlycloudymediumsnowlightningnight",
                "snowmidthunder.png");
        weatherIconMap.put("mostlycloudymediumsnownight", "snowmid.png");
        weatherIconMap.put("mostlycloudynight", "cloudynight.png");
        weatherIconMap.put("mostlycloudyverylightfreezingrain",
                "sunnywithfrozenrainday.png");
        weatherIconMap.put("mostlycloudyverylightfreezingrainnight",
                "sunnywithfrozenrainnight.png");
        weatherIconMap.put("mostlycloudyverylightmix", "sunnyrainsnowday.png");
        weatherIconMap.put("mostlycloudyverylightmixnight", "sunnyrainsnownight.png");
        weatherIconMap.put("mostlycloudyverylightrain", "sunnyrainday.png");
        weatherIconMap.put("mostlycloudyverylightrainnight", "sunnyrainnight.png");
        weatherIconMap.put("mostlycloudyverylightsleet", "sunnywithiceday.png");
        weatherIconMap.put("mostlycloudyverylightsleetnight", "sunnywithicenight.png");
        weatherIconMap.put("mostlycloudyverylightsnow", "sunnysnowday.png");
        weatherIconMap.put("mostlycloudyverylightsnownight", "sunnysnownight.png");
        weatherIconMap.put("partlycloudy", "cloudyday.png");
        weatherIconMap.put("partlycloudyheavyfreezingrain", "frozenrainheavy.png");
        weatherIconMap.put("partlycloudyheavyfreezingrainlightning",
                "icebigthunder.png");
        weatherIconMap.put("partlycloudyheavyfreezingrainlightningnight",
                "frozenrainheavy.png");
        weatherIconMap.put("partlycloudyheavyfreezingrainnight",
                "frozenrainheavy.png");
        weatherIconMap.put("partlycloudyheavymix", "rainbigsnow.png");
        weatherIconMap.put("partlycloudyheavymixlightning", "rainbigsnowthunder.png");
        weatherIconMap.put("partlycloudyheavymixlightningnight",
                "rainbigsnowthunder.png");
        weatherIconMap.put("partlycloudyheavymixnight", "rainbigsnow.png");
        weatherIconMap.put("partlycloudyheavyrain", "rainheavy.png");
        weatherIconMap.put("partlycloudyheavyrainlightning", "rainheavierthunder.png");
        weatherIconMap.put("partlycloudyheavyrainlightningnight",
                "rainheavierthunder.png");
        weatherIconMap.put("partlycloudyheavyrainnight", "rainheavy.png");
        weatherIconMap.put("partlycloudyheavysleet", "icebig.png");
        weatherIconMap.put("partlycloudyheavysleetlightning", "icebigthunder.png");
        weatherIconMap.put("partlycloudyheavysleetlightningnight",
                "icebigthunder.png");
        weatherIconMap.put("partlycloudyheavysleetnight", "icebig.png");
        weatherIconMap.put("partlycloudyheavysnow", "snowbigheavy.png");
        weatherIconMap.put("partlycloudyheavysnowlightning",
                "snowbigheavythunder.png");
        weatherIconMap.put("partlycloudyheavysnowlightningnight",
                "snowbigheavythunder.png");
        weatherIconMap.put("partlycloudyheavysnownight", "snowbigheavy.png");
        weatherIconMap.put("partlycloudylightfreezingrain",
                "sunnywithfrozenrainday.png");
        weatherIconMap.put("partlycloudylightfreezingrainnight",
                "sunnywithfrozenrainnight.png");
        weatherIconMap.put("partlycloudylightmix", "sunnyrainsnowday.png");
        weatherIconMap.put("partlycloudylightmixnight", "sunnyrainsnownight.png");
        weatherIconMap.put("partlycloudylightrain", "rainsmall.png");
        weatherIconMap.put("partlycloudylightrainlightning", "rainsmallthunder.png");
        weatherIconMap.put("partlycloudylightrainlightningnight",
                "rainsmallthunder.png");
        weatherIconMap.put("partlycloudylightrainnight", "rainsmall.png");
        weatherIconMap.put("partlycloudylightsleet", "sunnywithiceday.png");
        weatherIconMap.put("partlycloudylightsleetnight", "sunnywithicenight.png");
        weatherIconMap.put("partlycloudylightsnow", "snowsmall.png");
        weatherIconMap.put("partlycloudylightsnownight", "snowsmall.png");
        weatherIconMap.put("partlycloudymediumfreezingrain", "frozenrain.png");
        weatherIconMap.put("partlycloudymediumfreezingrainlightning",
                "icemidthunder.png");
        weatherIconMap.put("partlycloudymediumfreezingrainlightningnight",
                "icemidthunder.png");
        weatherIconMap.put("partlycloudymediumfreezingrainnight", "icemidthunder.png");
        weatherIconMap.put("partlycloudymediummix", "rainmidsnow.png");
        weatherIconMap.put("partlycloudymediummixlightning", "rainmidsnowthunder.png");
        weatherIconMap.put("partlycloudymediummixnight", "rainmidsnowthunder.png");
        weatherIconMap.put("partlycloudymediummixlightningnight", "rainmidsnow.png");
        weatherIconMap.put("partlycloudymediumrain", "rainmid.png");
        weatherIconMap.put("partlycloudymediumrainlightning", "rainmidthunder.png");
        weatherIconMap.put("partlycloudymediumrainlightningnight",
                "rainmidthunder.png");
        weatherIconMap.put("partlycloudymediumrainnight", "rainmid.png");
        weatherIconMap.put("partlycloudymediumsleet", "sunnywithiceday.png");
        weatherIconMap.put("partlycloudymediumsleetlightning", "icemidthunder.png");
        weatherIconMap.put("partlycloudymediumsleetlightningnight",
                "icemidthunder.png");
        weatherIconMap.put("partlycloudymediumsleetnight", "sunnywithicenight.png");
        weatherIconMap.put("partlycloudymediumsnow", "snowmid.png");
        weatherIconMap.put("partlycloudymediumsnowlightning", "snowmidthunder.png");
        weatherIconMap.put("partlycloudymediumsnowlightningnight",
                "snowmidthunder.png");
        weatherIconMap.put("partlycloudymediumsnownight", "snowmid.png");
        weatherIconMap.put("partlycloudynight", "cloudynight.png");
        weatherIconMap.put("partlycloudyverylightfreezingrain",
                "sunnywithfrozenrainday.png");
        weatherIconMap.put("partlycloudyverylightfreezingrainnight",
                "sunnywithfrozenrainnight.png");
        weatherIconMap.put("partlycloudyverylightmix", "sunnyrainsnowday.png");
        weatherIconMap.put("partlycloudyverylightmixnight", "sunnyrainsnownight.png");
        weatherIconMap.put("partlycloudyverylightrain", "sunnyrainday.png");
        weatherIconMap.put("partlycloudyverylightrainnight", "sunnyrainnight.png");
        weatherIconMap.put("partlycloudyverylightsleet", "sunnywithiceday.png");
        weatherIconMap.put("partlycloudyverylightsleetnight", "sunnywithicenight.png");
        weatherIconMap.put("partlycloudyverylightsnow", "sunnysnowday.png");
        weatherIconMap.put("partlycloudyverylightsnownight", "sunnysnownight.png");

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

    private void updateCurrentWeatherView(WeatherEntity.CurrentWeather currentWeather) {

        for (Map<String, WeatherEntity.Level> levelMap : currentWeather.observe.values()) {
            Collection<WeatherEntity.Level> values = levelMap.values();
            for (WeatherEntity.Level item : values) {
                mCurrentTemp.setText(item.l1 + "°");
                mCurrentWindowLevel.setText(item.l3 + "级");
                mCurrentWindowDirect.setText(getCurrentWindDirect(item.l4));
                mCurrentWeatherStatus.setText(getWeather(item.l5));
                mCurrentWeatherIcon.setImageResource(getCurrentIconRes(item.l5));
                mCurrentRain.setText(item.l6);
                mCurrentWeatherUpdateTime.setText("更新于" + getCurrentUpdateTime(item.l7) + "前");

            }

        }


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
        return "10分钟前";
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
        if (pop == null) {
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


                //TODO scroll postion
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
        // updateCurrentWeatherView(entity.current);
        updateSevenDayView(entity.n7d);
        updateHoursView(entity.n12h);
        updateFourSectionView(entity.pop);


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
