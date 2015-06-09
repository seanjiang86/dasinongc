package com.dasinong.app.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by liuningning on 15/6/6.
 * home field entity
 */
public class FieldEntity extends BaseEntity {




    //field list
    //name:fieldID
    public Map<String,Long> fieldList;

    public CurrentFieldEntity currentField;



    public static class CurrentFieldEntity {
        /**
         * startDate : null
         * currentStageID : 12
         * petdisws : [{"petDisStatus":false,"petDisSpecName":"稻瘟病1","petDisSpecId":11,"fieldId":10,"petDisId":10}]
         * yield : 0
         * taskws : [{"taskSpecName":"播种3任务1","taskId":10,"taskStatus":false,"fieldId":10,"taskSpecId":14}]
         * userId : 10
         * locationId : 10
         * active : false
         * endDate : null
         * varietyId : 10
         * natdisws : [{"natDisId":10,"natDisSpecId":10,"natDisStatus":false,"fieldId":10,"natDisSpecName":"台风"}]
         * fieldId : 10
         * fieldName : 测试田
         */
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
        //自然
        public List<NatdiswsEntity> natdisws;
        //not show
        public int fieldId;
        //当前田的名称
        public String fieldName;



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


        }

        public class TaskwsEntity {
            /**
             * taskSpecName : 播种3任务1
             * taskId : 10
             * taskStatus : false
             * fieldId : 10
             * taskSpecId : 14
             */
            public String taskSpecName;
            public int taskId;
            public boolean taskStatus;
            public int fieldId;
            public int taskSpecId;


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


        }
    }


    public static  class Param{
        public String fieldId;
    }
}
