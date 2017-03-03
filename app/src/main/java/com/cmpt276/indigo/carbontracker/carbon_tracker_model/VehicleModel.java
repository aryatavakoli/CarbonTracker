package com.cmpt276.indigo.carbontracker.carbon_tracker_model;


public class VehicleModel implements CarbonFootprintComponent{
    private String name;
    private String make;
    private String model;
    private String year;
    private String transmisson; //automatic or manual
    private double engineDisplacment; // in cubic inches
    private double cityMileage; // Gallon per mile mileage city
    private double highwayMileage; // Gallon per mile milleage Highway
    private boolean isDeleted;          // when a Car is deleted, we should hide it instead of removing it


    public VehicleModel(){
        name = new String();
        make = new String();
        model = new String();
        year = new String();
        transmisson = new String();
        engineDisplacment = 0;
        cityMileage = 0;
        highwayMileage = 0;
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

    public double getEngineDisplacment() {
        return engineDisplacment;
    }

    public void setEngineDisplacment(double engineDisplacment) {
        this.engineDisplacment = engineDisplacment;
    }

    public String getTransmisson() {
        return transmisson;
    }

    public void setTransmisson(String transmisson) {
        this.transmisson = transmisson;
    }

    public double getCityMileage() {
        return cityMileage;
    }

    public void setCityMileage(double cityMileage) {
        this.cityMileage = cityMileage;
    }

    public double getHighwayMileage() {
        return highwayMileage;
    }

    public void setHighwayMileage(double highwayMileage) {
        this.highwayMileage = highwayMileage;
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
                "cityMileage=" + cityMileage +
                ", name='" + name + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year='" + year + '\'' +
                ", transmisson='" + transmisson + '\'' +
                ", engineDisplacment='" + engineDisplacment + '\'' +
                ", highwayMileage=" + highwayMileage +
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
        return this.name.equals(o.name);
    }
}
