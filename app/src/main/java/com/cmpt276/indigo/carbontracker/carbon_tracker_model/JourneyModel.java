package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
/*
    Implements JourneyModel that describes a journey object
 */

public class JourneyModel implements CarbonFootprintComponent{
    public static final double CO2_PER_KM_BUS = 0.0089;
    public static final double CO2_PER_KM_SKYTRAIN = 0.0087;
    public static final double CO2_PER_KM_PEDESTRIAN = 0.0;
    public static final String DATE_FORMAT = "yyyy-MMM-dd";

    private long id;
    private VehicleModel vehicleModel;
    private RouteModel routeModel;
    private double co2Emission;
    private Calendar creationDate;
    private boolean isDeleted;

    public JourneyModel(){
        setId(-1);
        setVehicleModel(null);
        setRouteModel(null);
        setCo2Emission(0.0);
        setCreationDate(Calendar.getInstance());
        setIsDeleted(false);
    }

    public JourneyModel(long id,
                        VehicleModel vehicle,
                        RouteModel route,
                        float co2Emission,
                        Calendar creationDate,
                        boolean isDeleted){
        setId(id);
        setVehicleModel(vehicle);
        setRouteModel(route);
        setCo2Emission(co2Emission);
        setCreationDate(creationDate);
        setIsDeleted(isDeleted);
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
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
        calculateEmissions();
        return co2Emission;
    }

    //error handling
    public void setCo2Emission(double co2Emission){
        if(co2Emission < 0){
            throw new IllegalArgumentException("CO2 emission cannot be negative.");
        }
        this.co2Emission = co2Emission;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //parameters/arguments must be in kilometers
    //Calcualtes Carbonfootprint
    public void calculateEmissions() {

        double totalFootprint = 0;
        if(vehicleModel != null && routeModel != null){
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
        }
        co2Emission = totalFootprint;
    }

    public String getCreationDateString(){
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.format(getCreationDate().getTime());
    }

    @Override
    public boolean equals(Object other){
        if (other instanceof JourneyModel) {
            JourneyModel otherJouney = (JourneyModel)other;
            return this.id == otherJouney.id && this.id > -1 && otherJouney.id > -1;
        }
        return false;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
