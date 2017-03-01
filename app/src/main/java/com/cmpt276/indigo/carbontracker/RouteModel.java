package com.cmpt276.indigo.carbontracker;

/**
 * Created by parmis on 2017-02-27.
 */

public class RouteModel {
    private String name;
    private int highwayDistance;
    private int cityDistance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHighwayDistance() {
        return highwayDistance;
    }

    public void setHighwayDistance(int highwayDistance) {
        this.highwayDistance = highwayDistance;
    }

    public int getCityDistance() {
        return cityDistance;
    }

    public void setCityDistance(int cityDistance) {
        this.cityDistance = cityDistance;
    }
}
