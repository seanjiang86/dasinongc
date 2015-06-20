package com.dasinong.app.ui.soil.domain;

import com.dasinong.app.components.domain.BaseResponse;

/**
 * Created by liuningning on 15/6/13.
 */
public class SoilPostEntity extends BaseResponse {

    public static  class Param {

        public String fieldId;
        public String type;
        public String color;
        public String fertility;
        public String humidity;
        public String testDate;
        public String phValue;
        public String organic;
        public  String an;
        public String qn;
        public  String p;
        public  String qK;
        public String sK;
        public  String fe;
        public String mn;
        public  String cu;
        public  String zn;
        public  String b;
        public  String mo;
        public  String ca;
        public  String s;
        public  String si;
        public String mg;


        /**
         * fieldId=10&
         * type=type&
         * color=red&
         * fertility=fertility
         * &humidity=30.5&
         * testDate=2015/04/01&
         * phValue=6.7&
         * organic=organic&
         * an=12.1
         * &qn=10.2&
         * p=100&
         * qK=12&
         * sK=1.2&
         * fe=3.0&
         * mn=12
         * &
         * cu=21.0
         * &zn=1
         * &b=90
         * &mo=12.0
         * &ca=1.0
         * &s=0.1&
         * si=45&mg=2.3
         */

    }

    public  static class  Result{

    }


}
