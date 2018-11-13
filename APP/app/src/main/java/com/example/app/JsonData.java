package com.example.app;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 笙笙 on 2017/7/24.
 */
public class JsonData {
    @SerializedName("id")
    private int id;

    @SerializedName("time")
    private String time;

    @SerializedName("address")
    private String address;

    @SerializedName("posture1")
    private int posture1;

    @SerializedName("posture2")
    private int posture2;

    @SerializedName("posture3")
    private int posture3;

    @SerializedName("posture4")
    private int posture4;

    @SerializedName("distance")
    private String distance;

    @SerializedName("focus_ratio")
    private Double focus_ratio;

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public int getPosture1() {
        return posture1;
    }

    public int getPosture2() {
        return posture2;
    }

    public int getPosture3() {
        return posture3;
    }

    public int getPosture4() {
        return posture4;
    }

    public String getDistance() {
        return distance;
    }

    public Double getFocus_ratio() {
        return focus_ratio;
    }

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPosture1(int posture1) {
        this.posture1 = posture1;
    }

    public void setPosture2(int posture2) {
        this.posture2 = posture2;
    }

    public void setPosture3(int posture3) {
        this.posture3 = posture3;
    }

    public void setPosture4(int posture4) {
        this.posture4 = posture4;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setFocus_ratio(Double focus_ratio) {
        this.focus_ratio = focus_ratio;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
