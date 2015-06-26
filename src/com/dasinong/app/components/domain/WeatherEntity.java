package com.dasinong.app.components.domain;

import java.util.List;
import java.util.Map;

/**
 * Created by liuningning on 15/6/24.
 */
public class WeatherEntity extends  BaseResponse{


    /**
     * message : 获取成功
     * 7d : [{"ff_level":0,"dd_level":0,"min_temp":21,"temp":25.5,
     * "weather":3,"forecast_time":1435104000000,"rain":1,"max_temp":30}
     * ,{"ff_level":0,"dd_level":0,"min_temp":22,"temp":26,"weather":4,
     * "forecast_time":1435168800000,"rain":1.5,"max_temp":30},{"ff_level":0,"dd_level":0,"min_temp":21,"temp":26,"weather":4,"forecast_time":1435255200000,"rain":4.2,"max_temp":31},{"ff_level":0,"dd_level":0,"min_temp":21,"temp":27.5,"weather":2,"forecast_time":1435341600000,"rain":0,"max_temp":34},{"ff_level":0,"dd_level":0,"min_temp":22,"temp":27,"weather":2,"forecast_time":1435428000000,"rain":0,"max_temp":32},{"ff_level":0,"dd_level":0,"min_temp":21,"temp":24.5,"weather":4,"forecast_time":1435514400000,"rain":51.1,"max_temp":28},{"ff_level":0,"dd_level":0,"min_temp":19,"temp":20,"weather":4,"forecast_time":1435600800000,"rain":1,"max_temp":23},null]
     * respcode : 200
     * 12h : [{"icon":"cloudy","accumIceTotal":0,"time":1435104000000,"pOP":40,
     * "accumSnowTotal":0,"relativeHumidity":59,"accumRainTotal":0,"windSpeed_10m":4.8,
     * "windDirection_10m":203,"temperature":25.9},
     *
     *
     * {"icon":"cloudy","accumIceTotal":0,
     * "time":1435107600000,"pOP":30,"accumSnowTotal":0,"relativeHumidity":59,
     *
     * "accumRainTotal":0,"
     * windSpeed_10m":4.5,"windDirection_10m":201,"temperature":25.4},{"icon":"cloudy","accumIceTotal":0,"time":1435111200000,"pOP":50,"accumSnowTotal":0,"relativeHumidity":59,"accumRainTotal":0,"windSpeed_10m":4.9,"windDirection_10m":203,"temperature":24.7},{"icon":"cloudy","accumIceTotal":0,"time":1435114800000,"pOP":50,"accumSnowTotal":0,"relativeHumidity":59,"accumRainTotal":0,"windSpeed_10m":5.1,"windDirection_10m":211,"temperature":24.2},{"icon":"cloudy","accumIceTotal":0,"time":1435075200000,"pOP":50,"accumSnowTotal":0,"relativeHumidity":56,"accumRainTotal":0,"windSpeed_10m":4.4,"windDirection_10m":211,"temperature":24.8},{"icon":"cloudy","accumIceTotal":0,"time":1435122000000,"pOP":50,"accumSnowTotal":0,"relativeHumidity":50,"accumRainTotal":0,"windSpeed_10m":3.7,"windDirection_10m":207,"temperature":26.1},{"icon":"cloudy","accumIceTotal":0,"time":1435125600000,"pOP":50,"accumSnowTotal":0,"relativeHumidity":44,"accumRainTotal":0,"windSpeed_10m":3.4,"windDirection_10m":195,"temperature":27.6},{"icon":"mostlycloudy","accumIceTotal":0,"time":1435129200000,"pOP":50,"accumSnowTotal":0,"relativeHumidity":39,"accumRainTotal":0,"windSpeed_10m":3.5,"windDirection_10m":180,"temperature":29.4},{"icon":"cloudy","accumIceTotal":0,"time":1435132800000,"pOP":50,"accumSnowTotal":0,"relativeHumidity":37,"accumRainTotal":0,"windSpeed_10m":3.9,"windDirection_10m":173,"temperature":30.6},{"icon":"cloudy","accumIceTotal":0,"time":1435136400000,"pOP":40,"accumSnowTotal":0,"relativeHumidity":37,"accumRainTotal":0,"windSpeed_10m":4.3,"windDirection_10m":175,"temperature":30},{"icon":"cloudy","accumIceTotal":0,"time":1435140000000,"pOP":30,"accumSnowTotal":0,"relativeHumidity":39,"accumRainTotal":0,"windSpeed_10m":3.8,"windDirection_10m":180,"temperature":29},{"icon":"cloudy","accumIceTotal":0,"time":1435143600000,"pOP":10,"accumSnowTotal":0,"relativeHumidity":41,"accumRainTotal":0,"windSpeed_10m":4,"windDirection_10m":184,"temperature":28.1},{"icon":"cloudynight","accumIceTotal":0,"time":1435147200000,"pOP":10,"accumSnowTotal":0,"relativeHumidity":43,"accumRainTotal":0,"windSpeed_10m":4.5,"windDirection_10m":193,"temperature":27.3},{"icon":"cloudynight","accumIceTotal":0,"time":1435150800000,"pOP":20,"accumSnowTotal":0,"relativeHumidity":44,"accumRainTotal":0,"windSpeed_10m":4.5,"windDirection_10m":207,"temperature":26.8},{"icon":"cloudynight","accumIceTotal":0,"time":1435154400000,"pOP":30,"accumSnowTotal":0,"relativeHumidity":46,"accumRainTotal":0,"windSpeed_10m":3.4,"windDirection_10m":218,"temperature":26.4},{"icon":"cloudyverylightrainnight","accumIceTotal":0,"time":1435158000000,"pOP":50,"accumSnowTotal":0,"relativeHumidity":49,"accumRainTotal":0.1,"windSpeed_10m":2.7,"windDirection_10m":216,"temperature":25.8},{"icon":"cloudyverylightrainnight","accumIceTotal":0,"time":1435161600000,"pOP":50,"accumSnowTotal":0,"relativeHumidity":53,"accumRainTotal":0.3,"windSpeed_10m":2.2,"windDirection_10m":207,"temperature":25.4},{"icon":"cloudyverylightrainnight","accumIceTotal":0,"time":1435165200000,"pOP":60,"accumSnowTotal":0,"relativeHumidity":57,"accumRainTotal":0.6,"windSpeed_10m":2,"windDirection_10m":191,"temperature":24.8},{"icon":"cloudyverylightrainnight","accumIceTotal":0,"time":1435168800000,"pOP":50,"accumSnowTotal":0,"relativeHumidity":60,"accumRainTotal":0.7,"windSpeed_10m":2.8,"windDirection_10m":176,"temperature":24.2},{"icon":"cloudyverylightrainnight","accumIceTotal":0,"time":1435172400000,"pOP":40,"accumSnowTotal":0,"relativeHumidity":63,"accumRainTotal":0.9,"windSpeed_10m":3.2,"windDirection_10m":184,"temperature":23.8},{"icon":"cloudyverylightrainnight","accumIceTotal":0,"time":1435176000000,"pOP":20,"accumSnowTotal":0,"relativeHumidity":65,"accumRainTotal":1,"windSpeed_10m":2.3,"windDirection_10m":187,"temperature":23.4},{"icon":"cloudy","accumIceTotal":0,"time":1435179600000,"pOP":30,"accumSnowTotal":0,"relativeHumidity":68,"accumRainTotal":1,"windSpeed_10m":2.5,"windDirection_10m":189,"temperature":22.6},{"icon":"cloudyverylightrain","accumIceTotal":0,"time":1435183200000,"pOP":20,"accumSnowTotal":0,"relativeHumidity":68,"accumRainTotal":1.1,"windSpeed_10m":2.6,"windDirection_10m":196,"temperature":22.6},{"icon":"cloudyverylightrain","accumIceTotal":0,"time":1435186800000,"pOP":20,"accumSnowTotal":0,"relativeHumidity":63,"accumRainTotal":1.2,"windSpeed_10m":1.9,"windDirection_10m":199,"temperature":23.7},{"icon":"mostlycloudyverylightrain","accumIceTotal":0,"time":1435190400000,"pOP":10,"accumSnowTotal":0,"relativeHumidity":57,"accumRainTotal":1.2,"windSpeed_10m":0.9,"windDirection_10m":193,"temperature":25.2}]
     * current :
     * {"observe":
     * {"101010100":{"l":{"l1":"25","l2":"63","l3":"1","l4":"2","l5":"02","l6":"0","l6":"0.0","l7":"19:35"}}}}
     */

    public List<SevenDay> n7d;
    public List<Hours> n12h;


   public  CurrentWeather current;// Map<String,Map<String,Map<String,String>>> current;

    public static class SevenDay{

            public  String ff_level;//风力编码
            public String dd_level;//风向编码（3－4级
            public String min_temp;//最低温
            public String temp;//平均温度
            public String weather;//天气现象编码(晴转多云)
            public long forecast_time;//预报时间（周一,timestamp）
            public String rain;//降不量
            public String max_temp;//最高温度

    }

    public static class Hours{
        /**icon*/
        public  String icon;
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

    public static  class POP{

        public int morning;
        public  int afternoon;
        public int night;
        public int midnight;
    }


    public static class CurrentWeather{

        public Map<String,Map<String,Level>> observe;
    }


    public static  class Level {

        public String l1;//30
        public String l2;//
        public String l3;//4级
        public String l4;//东北风
        public String l5;//晴转多云
        public String l6;//80
        public String l7;//15:45---18分钟之前录入
    }



    public  static  class  Param{
      public String   monitorLocationId;
    }
}
