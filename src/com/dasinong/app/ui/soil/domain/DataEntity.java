package com.dasinong.app.ui.soil.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liuningning on 15/6/27.
 */
public  class DataEntity implements Parcelable {

    public int ca;
    public double sK;
    public String type;
    public long testDate;
    public double phValue;
    public int userId;
    public double mg;
    public int zn;
    public int soilTestReportId;
    public double qn;
    public int mn;
    public int mo;
    public int b;
    public String fertility;
    public int fieldId;
    public int cu;
    public int fe;
    public int si;
    public String organic;
    public double humidity;
    public double s;
    public String color;
    public double an;
    public int p;
    public int qK;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ca);
        dest.writeDouble(this.sK);
        dest.writeString(this.type);
        dest.writeLong(this.testDate);
        dest.writeDouble(this.phValue);
        dest.writeInt(this.userId);
        dest.writeDouble(this.mg);
        dest.writeInt(this.zn);
        dest.writeInt(this.soilTestReportId);
        dest.writeDouble(this.qn);
        dest.writeInt(this.mn);
        dest.writeInt(this.mo);
        dest.writeInt(this.b);
        dest.writeString(this.fertility);
        dest.writeInt(this.fieldId);
        dest.writeInt(this.cu);
        dest.writeInt(this.fe);
        dest.writeInt(this.si);
        dest.writeString(this.organic);
        dest.writeDouble(this.humidity);
        dest.writeDouble(this.s);
        dest.writeString(this.color);
        dest.writeDouble(this.an);
        dest.writeInt(this.p);
        dest.writeInt(this.qK);
    }

    public DataEntity() {
    }

    protected DataEntity(Parcel in) {
        this.ca = in.readInt();
        this.sK = in.readDouble();
        this.type = in.readString();
        this.testDate = in.readLong();
        this.phValue = in.readDouble();
        this.userId = in.readInt();
        this.mg = in.readDouble();
        this.zn = in.readInt();
        this.soilTestReportId = in.readInt();
        this.qn = in.readDouble();
        this.mn = in.readInt();
        this.mo = in.readInt();
        this.b = in.readInt();
        this.fertility = in.readString();
        this.fieldId = in.readInt();
        this.cu = in.readInt();
        this.fe = in.readInt();
        this.si = in.readInt();
        this.organic = in.readString();
        this.humidity = in.readDouble();
        this.s = in.readDouble();
        this.color = in.readString();
        this.an = in.readDouble();
        this.p = in.readInt();
        this.qK = in.readInt();
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