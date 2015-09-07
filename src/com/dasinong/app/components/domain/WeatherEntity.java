package com.dasinong.app.components.domain;

import java.util.List;

/**
 * Created by liuningning on 15/6/24.
 */
public class WeatherEntity extends BaseResponse {


    public List<SevenDay> n7d;
    public List<Hours> n12h;

    public SectionWeather POP;

    public CurrentWeather current;
    public int workable;
    public int sprayable;
    
    public long sunrise;
    public long sunset;

    public static class SevenDay {

        public String ff_level;//风向编码（3－4级
        public String dd_level;//风力编码
        public String min_temp;//最低温
        public String temp;//平均温度
        public String weather;//天气现象编码(晴转多云)
        public long forecast_time;//预报时间（周一,timestamp）
        public String rain;//降不量
        public String max_temp;//最高温度

    }

    public static class Hours {
        /**
         * icon
         */
        public String icon;
        /***/
        public String accumIceTotal;  //--
        public long time;//11点
        public String pOP;//－－
        public String accumSnowTotal;//－－
        public String relativeHumidity;//－－
        public String accumRainTotal;//－－
        public String windSpeed_10m;//－－
        public String windDirection_10m;//－－
        public String temperature;//温度一个
        
    }

    public static class SectionWeather {

        public int morning;
        public int noon;
        public int night;
        public int nextmidnight;
    }


    public static class CurrentWeather {


        public String code;
        public long timeStamp;
        public String l1;
        public String l2;
        public String l3;
        public String l4;
        public String l5;
        public String l6;
        public String l7;
        public String daymax;
        public String daymin;
    }


    public static class Param {
        public String monitorLocationId;

        public String lat;
        public String lon ;
    }
}
