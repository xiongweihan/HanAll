package com.example.hanall.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GalleryBean implements Parcelable {

    private int imgid;

    public GalleryBean(int imgId){
        this.imgid = imgId;
    }
    public GalleryBean(Parcel in) {
        imgid = in.readInt();
    }

    public static final Creator<GalleryBean> CREATOR = new Creator<GalleryBean>() {
        @Override
        public GalleryBean createFromParcel(Parcel in) {
            return new GalleryBean(in);
        }

        @Override
        public GalleryBean[] newArray(int size) {
            return new GalleryBean[size];
        }
    };

    public int getIngId() {
        return imgid;
    }

    public void setIngId(int ingId) {
        this.imgid = ingId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imgid);
    }
}
