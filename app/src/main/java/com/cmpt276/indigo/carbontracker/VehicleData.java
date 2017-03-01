package com.cmpt276.indigo.carbontracker;

/**
 * Created by arya on 27/02/17.
 */

public class VehicleData {
    private String make;
    private String model;
    private String year;
    private double carbonFootprintGpm;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getCarbonFootprintGpm() {
        return carbonFootprintGpm;
    }

    public void setCarbonFootprintGpm(double carbonFootprintGpm) {
        this.carbonFootprintGpm = carbonFootprintGpm;
    }

    @Override
    public String toString() {
        return "VehicleData{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", carbonFootprintGpm=" + carbonFootprintGpm +
                '}';
    }
}
