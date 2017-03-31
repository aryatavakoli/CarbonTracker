package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

/*
    Implements TransportationModel that describes a vehicle object
 */

public class TransportationModel implements CarbonFootprintComponent{

    public enum TransportationMode {
        CAR,
        WALK_BIKE,
        BUS,
        SKYTRAIN
    }

    //Differentiate transportation mode that user selects
    public static int TransportationModeToInt(TransportationMode mode){
        if(mode == TransportationMode.CAR){
            return 0;
        }
        else if(mode == TransportationMode.WALK_BIKE){
            return 1;
        }
        else if(mode == TransportationMode.BUS){
            return 2;
        }
        else {
            return 3;
        }
    }

    public static TransportationMode IntToTransportaionMode(int value){
        if(value == 0){
            return TransportationMode.CAR;
        }
        else if(value == 1){
            return TransportationMode.WALK_BIKE;
        }
        else if(value == 2){
            return TransportationMode.BUS;
        }
        else{
            return TransportationMode.SKYTRAIN;
        }
    }

    public static final double GASOLINE_FOOTPRINT_KG_PER_LITRE = 2.35; // 8.89 kg per gallon in kg/L
    public static final double DIESEL_FOOTPRINT_KG_PER_LITRE = 2.69; // 10.16 kg per Gallon in kg/L
    private long id;
    private String name;
    private String make;
    private String model;
    private String year;
    private String transmisson; //automatic or manual
    private String engineDisplacment; // in cubic inches
    private double cityMileage; // Gallon per mile mileage city
    private double highwayMileage; // Gallon per mile milleage Highway
    private String primaryFuelType;
    private TransportationMode transportaionMode;
    private boolean isDeleted;          // when a Car is deleted, we should hide it instead of removing it

    public TransportationModel(){
        id = -1;
        name = new String();
        make = new String();
        model = new String();
        year = new String();
        transmisson = new String();
        engineDisplacment = new String();
        cityMileage = 0;
        highwayMileage = 0;
        primaryFuelType = new String();
        transportaionMode = TransportationMode.CAR;
        isDeleted = false;
    }

    public TransportationModel(long id, String name, String make, String model, String year, String transmission, String engineDisplacment,
                               double cityMileage, double highwayMileage, String primaryFuelType, TransportationMode transportaionMode, boolean isDeleted){
        this.id = id;
        this.name = name;
        this.make = make;
        this.model = model;
        this.year = year;
        this.transmisson = transmission;
        this.engineDisplacment = engineDisplacment;
        this.cityMileage = cityMileage;
        this.highwayMileage = highwayMileage;
        this.primaryFuelType = primaryFuelType;
        this.transportaionMode = transportaionMode;
        this.isDeleted = isDeleted;
    }

    public long getId() {
        return id;
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

    public TransportationMode getTransportaionMode() {
        return transportaionMode;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setTransportaionMode(TransportationMode transportaionMode) {
        this.transportaionMode = transportaionMode;
    }

    public String getEngineDisplacment() {
        return engineDisplacment;
    }

    public void setEngineDisplacment(String engineDisplacment) {
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
    //Convert miles/gallon to km/L
    public void setCityMileage(double cityMileage) {
        this.cityMileage = cityMileage * 0.43;
    }

    public double getHighwayMileage() {
        return highwayMileage;
    }

    //Convert miles/gallon to km/L
    public void setHighwayMileage(double highwayMileage) {
        this.highwayMileage = highwayMileage * 0.43;
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

    @Override
    public String toString() {
        return "TransportationModel{" +
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
        if (!(other instanceof TransportationModel)){
            return false;
        }
        TransportationModel o = (TransportationModel) other;
        // a deleted object should never be compared to other components
        if(o.isDeleted || this.isDeleted){
            return false;
        }
        return this.name.equals(o.name);
    }

    public void copyFuelData(TransportationModel other){
        this.transmisson = other.transmisson;
        this.engineDisplacment = other.engineDisplacment; // in cubic inches
        this.cityMileage = other.cityMileage; // Gallon per mile mileage city
        this.highwayMileage = other.highwayMileage; // Gallon per mile milleage Highway
        this.primaryFuelType = other.primaryFuelType;
    }
}
