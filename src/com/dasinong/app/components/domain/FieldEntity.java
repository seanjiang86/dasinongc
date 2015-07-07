package com.dasinong.app.components.domain;

import com.dasinong.app.ui.soil.domain.DataEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by liuningning on 15/6/14.
 */
public class FieldEntity extends BaseResponse {


    public DataEntity latestReport;
    //field list
    //name:fieldID
    public Map<String,Long> fieldList;

    public CurrentFieldEntity currentField;


    public  HomeDate date;
    public  String  soilHum;

    public static  class HomeDate{
        public String lunar;//大雪
        public int day;//周41（要是）
        public String date;//10
    }




    /**
     * cropName + stageName + SubStagName; 水稻分叶期8叶
     */
    public static class CurrentFieldEntity {

        public String startDate;
        //当前的statgeID
        public int currentStageID;
        //病害
        public List<PetdiswsEntity> petdisws;
        //not show
        public int yield;
        //任务清单
        public List<TaskwsEntity> taskws;
        //not show userID
        public int userId;
        //not show location_id
        public int locationId;
        //not show
        public boolean active;
        //结束时间
        public String endDate;
        //品种(id select substage)
        public int varietyId;
        //自然（如果不是空，则是Banner的信息。如果是空，则加载Banner)
        public List<NatdiswsEntity> natdisws;

        //not show
        public int fieldId;
        //当前田的名称
        public String fieldName;


        public int  daytoharvest;    //int只给 离收获还有多少天（）(小于0不显示)

        public boolean workable;//不宜下地
        public boolean sprayable;//不宜打药




        public class PetdiswsEntity {
            /**
             * petDisStatus : false
             * petDisSpecName : 稻瘟病1
             * petDisSpecId : 11
             * fieldId : 10
             * petDisId : 10
             */
            public boolean petDisStatus;
            public String petDisSpecName;
            public int petDisSpecId;
            public int fieldId;
            public int petDisId;
            public  String description;
            public String type;
            public boolean alerttype;


        }

        public class TaskwsEntity {
            /**
             * taskSpecName : 播种3任务1
             * taskId : 10
             * taskStatus : false
             * fieldId : 10
             * taskSpecId : 14
             */


            public boolean taskStatus;//状态
            public int fieldId;
            public int taskSpecId;
            public int subStageId;
            public int taskId;//
            public  String taskSpecName;//desc
            public String stageName;
            public  String subStageName;

            /**
             *  "taskStatus": true,
             "fieldId": 10,
             "subStageId": 44,
             "taskSpecId": 29,
             "taskId": 3,
             "taskSpecName": "施第一次分蘖肥",
             "subStageName": "一次分蘖期",
             "stageName": "分蘖期"
             */


        }


        public class NatdiswsEntity {
            /**
             * natDisId : 10
             * natDisSpecId : 10
             * natDisStatus : false
             * fieldId : 10
             * natDisSpecName : 台风
             */
            public int natDisId;
            public int natDisSpecId;
            public boolean natDisStatus;
            public int fieldId;
            public String natDisSpecName;
            public  String description;
            public boolean alerttype;


        }
    }


    public static  class Param{
        public String fieldId;
        public String  lat;
        public String lon;
    }
}
