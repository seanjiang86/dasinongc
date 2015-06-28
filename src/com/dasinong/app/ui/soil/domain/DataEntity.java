package com.dasinong.app.ui.soil.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liuningning on 15/6/27.
 */
public  class DataEntity implements Parcelable {

    public String ca;
    public String sK;
    public String type;
    public long testDate;
    public String phValue;
    public int userId;
    public String mg;
    public String zn;
    public String soilTestReportId;
    public String qn;
    public String mn;
    public String mo;
    public String b;
    public String fertility;
    public int fieldId;
    public String cu;
    public String fe;
    public String si;
    public String organic;
    public String humidity;
    public String s;
    public String color;
    public String an;
    public String p;
    public String qK;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ca);
        dest.writeString(this.sK);
        dest.writeString(this.type);
        dest.writeLong(this.testDate);
        dest.writeString(this.phValue);
        dest.writeInt(this.userId);
        dest.writeString(this.mg);
        dest.writeString(this.zn);
        dest.writeString(this.soilTestReportId);
        dest.writeString(this.qn);
        dest.writeString(this.mn);
        dest.writeString(this.mo);
        dest.writeString(this.b);
        dest.writeString(this.fertility);
        dest.writeInt(this.fieldId);
        dest.writeString(this.cu);
        dest.writeString(this.fe);
        dest.writeString(this.si);
        dest.writeString(this.organic);
        dest.writeString(this.humidity);
        dest.writeString(this.s);
        dest.writeString(this.color);
        dest.writeString(this.an);
        dest.writeString(this.p);
        dest.writeString(this.qK);
    }

    public DataEntity() {
    }

    protected DataEntity(Parcel in) {
        this.ca = in.readString();
        this.sK = in.readString();
        this.type = in.readString();
        this.testDate = in.readLong();
        this.phValue = in.readString();
        this.userId = in.readInt();
        this.mg = in.readString();
        this.zn = in.readString();
        this.soilTestReportId = in.readString();
        this.qn = in.readString();
        this.mn = in.readString();
        this.mo = in.readString();
        this.b = in.readString();
        this.fertility = in.readString();
        this.fieldId = in.readInt();
        this.cu = in.readString();
        this.fe = in.readString();
        this.si = in.readString();
        this.organic = in.readString();
        this.humidity = in.readString();
        this.s = in.readString();
        this.color = in.readString();
        this.an = in.readString();
        this.p = in.readString();
        this.qK = in.readString();
    }

    public static final Parcelable.Creator<DataEntity> CREATOR = new Parcelable.Creator<DataEntity>() {
        public DataEntity createFromParcel(Parcel source) {
            return new DataEntity(source);
        }

        public DataEntity[] newArray(int size) {
            return new DataEntity[size];
        }
    };
}