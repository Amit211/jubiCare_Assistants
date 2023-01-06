package com.indev.jubicare_assistants.videocallingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class NotificationCallVo implements Parcelable {

    private HashMap data;

    public NotificationCallVo() {
    }

    private NotificationCallVo(Parcel in) {
        data = (HashMap) in.readValue(HashMap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(data);
    }

    @SuppressWarnings("unused")
    public static final Creator<NotificationCallVo> CREATOR = new Creator<NotificationCallVo>() {
        @Override
        public NotificationCallVo createFromParcel(Parcel in) {
            return new NotificationCallVo(in);
        }

        @Override
        public NotificationCallVo[] newArray(int size) {
            return new NotificationCallVo[size];
        }
    };

    public HashMap getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

}
