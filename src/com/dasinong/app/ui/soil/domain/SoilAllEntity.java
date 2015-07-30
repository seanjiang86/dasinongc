package com.dasinong.app.ui.soil.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.dasinong.app.DsnApplication;
import com.dasinong.app.components.domain.BaseResponse;
import com.dasinong.app.ui.manager.SharedPreferencesHelper;

import java.util.List;

/**
 * Created by liuningning on 15/6/13.
 */
public class SoilAllEntity extends BaseResponse {


    public List<DataEntity> data;

    public static  class Param {

    	public String fieldId="0";
    	public Param(Long fieldId){
    		this.fieldId=""+fieldId;
    	}
    	public Param(String fieldId){
    		this.fieldId = fieldId;
    	}
    }
}
