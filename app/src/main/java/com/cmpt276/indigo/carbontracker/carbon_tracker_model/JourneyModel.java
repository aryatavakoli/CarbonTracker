package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
/*
    Implements JourneyModel that describes a journey object
 */

public class JourneyModel implements CarbonFootprintComponent{
    private VehicleModel vehicleModel;
    private RouteModel routeModel;
    private float co2Emission;
    private Date creationDate;
    private boolean isDeleted;

    private long id;
    public JourneyModel(){
        vehicleModel = new VehicleModel();
        routeModel = new RouteModel();
        co2Emission = 0.0f;
        creationDate = new Date();
        id = System.currentTimeMillis();
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

        double totalFootprint = 0;
        float convertedFootprint;

        String fuelType = vehicleModel.getPrimaryFuelType();
        double highwayMileageKmPerLitre = vehicleModel.getHighwayMileage();
        double cityMileageKmPerLitre = vehicleModel.getCityMileage();

        double highwayDistanceInKm = routeModel.getHighwayDistance();
        double cityDistanceInKm = routeModel.getCityDistance();

        //Gasoline
        if (fuelType.contains("Gasoline") || Objects.equals(fuelType, "Regular") || Objects.equals(fuelType, "Premium"))
        {
            totalFootprint = (VehicleModel.GASOLINE_FOOTPRINT_KG_PER_LITRE) * ((cityDistanceInKm/cityMileageKmPerLitre) + (highwayDistanceInKm/highwayMileageKmPerLitre));
        }
        //Diesel
        else if (Objects.equals(fuelType, "Diesel"))
        {
            totalFootprint = (VehicleModel.DIESEL_FOOTPRINT_KG_PER_LITRE) * ((cityDistanceInKm/cityMileageKmPerLitre) + (highwayDistanceInKm/highwayMileageKmPerLitre));
        }
        else if (Objects.equals(fuelType, "Electricity") || Objects.equals(fuelType,"Electric") )
        {
            totalFootprint = 0;
        }
        if (vehicleModel.getName().equals("Walk/Bicycle")) {
            totalFootprint =  routeModel.getTotalDistance() * 0.0f ;
        } else if (vehicleModel.getName().equals("Bus")) {
            totalFootprint =  routeModel.getTotalDistance() * 89f ;
        } else if (vehicleModel.getName().equals("Skytrain")) {
            totalFootprint =  routeModel.getTotalDistance() * 8.7f ;
        }
        //Converts double to float for use with graph
        //Rounds it off
        convertedFootprint =  Math.round((float)totalFootprint * 100.0f) / 100.0f;

        co2Emission = convertedFootprint;

    }

    @Override
    public boolean equals(Object other){
        if (other instanceof JourneyModel) {
            return id == ((JourneyModel)other).id;
        }
        return false;
    }

    public long getId() {
        return id;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
