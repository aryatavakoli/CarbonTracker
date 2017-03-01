package com.cmpt276.indigo.carbontracker.carbon_tracker_model;


public class VehicleModel implements CarbonFootprintComponent{
    private String name;
    private String make;
    private String model;
    private String year;
    private double carbonFootprintGpm; // Amount of carbonfoot in grams per mile
    private boolean isDeleted;          // when a Car is deleted, we should hide it instead of removing it


    public VehicleModel(){
        name = new String();
        make = new String();
        model = new String();
        year = new String();
        carbonFootprintGpm = 0;
        isDeleted = false;
    }

    public String getName() {
        return name;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
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
    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted){
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "VehicleModel{" +
                "name='" + name + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year='" + year + '\'' +
                ", carbonFootprintGpm=" + carbonFootprintGpm +
                ", isDeleted=" + isDeleted +
                '}';
    }

    @Override
    public boolean equals(Object other){
        if (other == null){
            return false;
        }
        if (this == other){
            return true;
        }
        if (!(other instanceof VehicleModel)){
            return false;
        }
        VehicleModel o = (VehicleModel) other;
        // a deleted object should never be compared to other components
        if(o.isDeleted || this.isDeleted){
            return false;
        }
        return o.name == this.name;
    }
}
