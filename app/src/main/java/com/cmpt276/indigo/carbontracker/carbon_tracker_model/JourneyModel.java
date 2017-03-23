package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
/*
    Implements JourneyModel that describes a journey object
 */

public class JourneyModel implements CarbonFootprintComponent{
    public static final float CO2_PER_KM_BUS = 0.0089f;
    public static final float CO2_PER_KM_SKYTRAIN = 0.0087f;
    public static final float CO2_PER_KM_PEDESTRIAN = 0.0f;
    private VehicleModel vehicleModel;
    private RouteModel routeModel;
    private float co2Emission;
    private Date creationDate;
    private boolean isDeleted;
    private long id;

    //components needed in a journey
    public JourneyModel(){
        vehicleModel = new VehicleModel();
        routeModel = new RouteModel();
        co2Emission = 0.0f;
        creationDate = new Date();
        id = System.currentTimeMillis(); //each journey has its unique id
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
        double litres = (cityDistanceInKm / cityMileageKmPerLitre)
                + (highwayDistanceInKm / highwayMileageKmPerLitre);
        if (fuelType.contains("Gasoline") || Objects.equals(fuelType, "Regular") || Objects.equals(fuelType, "Premium"))
        {
            totalFootprint = (VehicleModel.GASOLINE_FOOTPRINT_KG_PER_LITRE) * litres;
        }
        //Diesel
        else if (Objects.equals(fuelType, "Diesel"))
        {
            totalFootprint = (VehicleModel.DIESEL_FOOTPRINT_KG_PER_LITRE) * litres;
        }
        else if (Objects.equals(fuelType, "Electricity") || Objects.equals(fuelType,"Electric") )
        {
            totalFootprint = 0;
        }
        if (vehicleModel.getTransportaionMode() == VehicleModel.TransportationMode.WALK_BIKE) {
            totalFootprint =  routeModel.getTotalDistance() * CO2_PER_KM_PEDESTRIAN ; //user chooses walk or bike
        } else if (vehicleModel.getTransportaionMode() == VehicleModel.TransportationMode.BUS) {
            totalFootprint =  routeModel.getTotalDistance() * CO2_PER_KM_BUS; //user chooses bus
        } else if (vehicleModel.getTransportaionMode() == VehicleModel.TransportationMode.SKYTRAIN) {
            totalFootprint =  routeModel.getTotalDistance() * CO2_PER_KM_SKYTRAIN ; //user chooses skytrain
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

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
