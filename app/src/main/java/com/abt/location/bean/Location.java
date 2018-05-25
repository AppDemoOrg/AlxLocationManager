package com.abt.location.bean;

/**
 * Created by Administrator on 2016/9/22.
 */
public class Location {

    public double latitude;
    public double longitude;
    public long updateTime;//最后更新时间，用于做精确度择优
    public float accuracy;
    private static Location location;

    Location() {
    }

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Location getInstance() {
        if (location == null) location = new Location();
        return location;
    }
}
