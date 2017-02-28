package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

public class JourneyModel extends CarbonFootprintComponentCollection{
    private VehicleModel vehicleModel;
    private RoutModel routModel;

    public VehicleModel getVehicleModel() {
        return vehicleModel;
    }

    public RoutModel getRoutModel() {
        return routModel;
    }

    public void setRoutModel(RoutModel routModel) {
        this.routModel = routModel;
    }

    public void setVehicleModel(VehicleModel vehicleModel) {
        this.vehicleModel = vehicleModel;
    }
}
