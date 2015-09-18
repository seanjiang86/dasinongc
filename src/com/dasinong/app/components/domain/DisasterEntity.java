package com.dasinong.app.components.domain;

import java.util.List;

import com.dasinong.app.ui.manager.SharedPreferencesHelper;

/**
 * Created by liuningning on 15/7/14.
 */
public class DisasterEntity extends  BaseResponse {

    public List<FieldEntity.CurrentFieldEntity.Petdisspecws> data;

    public static  class Param{
        public String subStageId;
        public String varietyId;
        
    }
}
