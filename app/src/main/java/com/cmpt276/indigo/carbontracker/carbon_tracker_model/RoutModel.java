package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

public class RoutModel extends CarbonFootprintComponentCollection{
    private String name;
    private int cityDistance;
    private int highwayDistance;
    private int totalDistance;

    public String getName() {
        return name;
    }

    public int getCityDistance() {
        return cityDistance;
    }

    public int getHighwayDistance() {
        return highwayDistance;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCityDistance(int cityDistance) {
        this.cityDistance = cityDistance;
    }

    public void setHighwayDistance(int highwayDistance) {
        this.highwayDistance = highwayDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }
}
