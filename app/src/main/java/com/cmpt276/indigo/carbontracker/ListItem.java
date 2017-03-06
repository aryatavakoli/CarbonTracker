package com.cmpt276.indigo.carbontracker;

/**
 * Created by Administrator on 2017/3/1.
 */

public class ListItem {

    private String carName;
    private String routeName;
    private String date;
    private double col;


    public ListItem() {
        super();
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

    public double getCol(){return col;}

    public void setCol(double col){this.col = col;}

    public ListItem(String carName, String routeName, String date, double col) {

        this.carName = carName;
        this.routeName = routeName;
        this.date = date;
        this.col = col;
    }


}

