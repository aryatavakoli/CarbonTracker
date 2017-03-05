package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import java.util.Date;

public class JourneyModel implements CarbonFootprintComponent{
    private VehicleModel vehicleModel;
    private RouteModel routeModel;
    private double co2Emission;
    private Date creationDate;


    public JourneyModel(){
        vehicleModel = new VehicleModel();
        routeModel = new RouteModel();
        co2Emission = 0;
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

    public double getCo2Emission(){
        return co2Emission;
    }

    public void setCo2Emission(double co2Emission){
        if(co2Emission < 0){
            throw new IllegalArgumentException("CO2 emission cannot be negative.");
        }
        this.co2Emission = co2Emission;
    }

    @Override
    public boolean equals(Object other){
        //Needs to be implemented
        return false;
    }
}
