package com.example.hanall.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class CityBean implements Parcelable {
    private String tag;//所属的分类（城市的汉语拼音首字母）
    private String city; //城市名字

    public CityBean(String tag, String city) {
        this.tag = tag;
        this.city = city;
    }

    protected CityBean(Parcel in) {
        tag = in.readString();
        city = in.readString();
    }

    public static final Creator<CityBean> CREATOR = new Creator<CityBean>() {
        @Override
        public CityBean createFromParcel(Parcel in) {
            return new CityBean(in);
        }

        @Override
        public CityBean[] newArray(int size) {
            return new CityBean[size];
        }
    };

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCity() {
        if (TextUtils.isEmpty(city)) {
            return "#";
        }
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tag);
        dest.writeString(city);
    }
}
