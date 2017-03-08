package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import java.util.Date;
import java.util.Objects;


public class JourneyModel implements CarbonFootprintComponent{
    private VehicleModel vehicleModel;
    private RouteModel routeModel;
    private float co2Emission;
    private Date creationDate;

    public JourneyModel(){
        vehicleModel = new VehicleModel();
        routeModel = new RouteModel();
        co2Emission = 0.0f;
        creationDate = new Date();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public VehicleModel getVehicleModel() {
        return vehicleModel;
    }

    public RouteModel getRouteModel() {
        return routeModel;
    }

    public void setRouteModel(RouteModel routeModel) {
        this.routeModel = routeModel;
    }

    public void setVehicleModel(VehicleModel vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public float getCo2Emission(){
        return co2Emission;
    }

    public void setCo2Emission(float co2Emission){
        if(co2Emission < 0){
            throw new IllegalArgumentException("CO2 emission cannot be negative.");
        }
        this.co2Emission = co2Emission;
    }

    //parameters/arguments must be in kilometers
    //Calcualtes Carbonfootprint
    public void calculateEmissions() {

        double total_footPrint = 0;
        float converted_Footprint;

        String fuelType = vehicleModel.getPrimaryFuelType();
        double highway_mileage = vehicleModel.getHighwayMileage();
        double city_mileage = vehicleModel.getCityMileage();

        double highwayDistance = routeModel.getHighwayDistance();
        double cityDistance = routeModel.getCityDistance();

        //Gasoline
        if (fuelType.contains("Gasoline") || Objects.equals(fuelType, "Regular") || Objects.equals(fuelType, "Premium"))
        {
            total_footPrint = (VehicleModel.GASOLINE_FOOTPRINT) * ((cityDistance/city_mileage) + (highwayDistance/highway_mileage));
        }
        //Diesel
        else if (Objects.equals(fuelType, "Diesel"))
        {
            total_footPrint = (VehicleModel.DIESEL_FOOTPRINT) * ((cityDistance/city_mileage) + (highwayDistance/highway_mileage));
        }
        else if (Objects.equals(fuelType, "Electricity") || Objects.equals(fuelType,"Electric") )
        {
            total_footPrint = 0;
        }

        //Converts double to float for use with graph
        //Rounds it off
        converted_Footprint =  Math.round((float)total_footPrint * 100.0f) / 100.0f;

        co2Emission = converted_Footprint;

    }

    @Override
    public boolean equals(Object other){
        //Needs to be implemented
        return false;
    }
}
