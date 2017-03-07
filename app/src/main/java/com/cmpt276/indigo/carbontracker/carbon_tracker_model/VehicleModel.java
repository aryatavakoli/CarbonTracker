package com.cmpt276.indigo.carbontracker.carbon_tracker_model;


import java.util.Objects;

public class VehicleModel implements CarbonFootprintComponent{
    private static final double gasolineFootprint = 2.35; // 8.89 kg per gallon in kg/L
    private static final double dieselFootprint = 2.69; // 10.16 kg per Gallon in kg/L
    private String name;
    private String make;
    private String model;
    private String year;
    private String transmisson; //automatic or manual
    private double engineDisplacment; // in cubic inches
    private double cityMileage; // Gallon per mile mileage city
    private double highwayMileage; // Gallon per mile milleage Highway
    private String primaryFuelType;
    private boolean isDeleted;          // when a Car is deleted, we should hide it instead of removing it
    private double carbonFootprint;

    public VehicleModel(){
        name = new String();
        make = new String();
        model = new String();
        year = new String();
        transmisson = new String();
        engineDisplacment = 0;
        cityMileage = 0;
        highwayMileage = 0;
        primaryFuelType = new String();
        isDeleted = false;
        carbonFootprint = 0;
    }

    public VehicleModel(String name, String make, String model, String year){
        this.name = name;
        this.make = make;
        this.model = model;
        this.year = year;
        transmisson = new String();
        engineDisplacment = 0;
        cityMileage = 0;
        highwayMileage = 0;
        isDeleted = false;
    }

    public VehicleModel(String name, String make, String model, String year, String transmisson){
        this.name = name;
        this.make = make;
        this.model = model;
        this.year = year;
        this.transmisson = transmisson;
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

    //Convert miles to km
    //convert to L/km
    public void setCityMileage(double cityMileage) {
        this.cityMileage = cityMileage * 2.35;
    }

    public double getHighwayMileage() {
        return highwayMileage;
    }

    //Convert miles to km
    // 1 km = 1.61 miles
    public void setHighwayMileage(double highwayMileage) {
        this.highwayMileage = highwayMileage * 2.35;
    }

    public String getPrimaryFuelType() {
        return primaryFuelType;
    }

    public void setPrimaryFuelType(String primaryFuelType) {
        this.primaryFuelType = primaryFuelType;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted){
        this.isDeleted = isDeleted;
    }

    //parameters/arguments must be in kilometers
    public double getCarbonFootprint(double highwayDistance, double cityDistance) {
        String fuelType = getPrimaryFuelType();
        double highway_mileage = getHighwayMileage();
        double city_mileage = getCityMileage();

        //Gasoline
        if (fuelType.contains("Gasoline") || Objects.equals(fuelType, "Regular") || Objects.equals(fuelType, "Premium"))
        {
            carbonFootprint = (gasolineFootprint) * ((city_mileage * cityDistance) + (highway_mileage* highway_mileage));
        }
        //Diesel
        else if (Objects.equals(fuelType, "Diesel"))
        {
            carbonFootprint = (dieselFootprint) * ((city_mileage * cityDistance) + (highway_mileage* highway_mileage));
        }
        else
        {
            carbonFootprint = 0;
        }
        return carbonFootprint;
    }

    @Override
    public String toString() {
        return "VehicleModel{" +
                "name='" + name + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year='" + year + '\'' +
                ", transmisson='" + transmisson + '\'' +
                ", engineDisplacment=" + engineDisplacment +
                ", cityMileage=" + cityMileage +
                ", highwayMileage=" + highwayMileage +
                ", primaryFuelType='" + primaryFuelType + '\'' +
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

    public void copyFuelData(VehicleModel other){
        this.transmisson = other.transmisson;
        this.engineDisplacment = other.engineDisplacment; // in cubic inches
        this.cityMileage = other.cityMileage; // Gallon per mile mileage city
        this.highwayMileage = other.highwayMileage; // Gallon per mile milleage Highway
        this.primaryFuelType = other.primaryFuelType;
        this.carbonFootprint = other.carbonFootprint;
    }
}
