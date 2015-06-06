package com.dasinong.app.entity;

import java.util.List;

/**
 * Created by liuningning on 15/6/6.
 * home field entity
 */
public class FieldEntity extends BaseEntity {

    public List<Integer> fieldIds;

    public CurrentFieldEntity currentField;


    public static  class CurrentFieldEntity {
        public String startDate;
        public int yield;
        public int locationId;
        public String endDate;
        public int fieldId;
        public int currentStageID;
        public List<Integer> taskIds;
        public List<Integer> petDisIds;
        public int userId;
        public boolean active;
        public int varietyId;
        public String fieldName;
        public List<Integer> natDisIds;


    }


    public  static  class Param{
        public String username;
    }
}
