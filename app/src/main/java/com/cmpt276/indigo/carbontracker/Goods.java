package com.cmpt276.indigo.carbontracker;

/**
 * Created by Administrator on 2017/3/1.
 */

public class Goods {
    private String id;
    private String carName;
    private String routeName;
    private String date;
    private float carbon;
    private int totalDistance;

    public Goods() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getCarbon() {
        return carbon;
    }

    public void setCarbon(float carbon) {
        this.carbon = carbon;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Goods(String id, String goodsName, String codeBar, float num,
                 float curPrice, float money) {
        super();
        this.id = id;
        this.carName = goodsName;
        this.routeName = codeBar;
        this.date = date;
        this.carbon = carbon;
        this.totalDistance = totalDistance;
    }


}

