package com.dasinong.app.ui.soil.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.dasinong.app.components.domain.BaseResponse;

import java.util.List;

/**
 * Created by liuningning on 15/6/13.
 */
public class SoilAllEntity extends BaseResponse {


    public List<DataEntity> data;

    public static  class Param {

        public String fieldId = "10";

    }



}
