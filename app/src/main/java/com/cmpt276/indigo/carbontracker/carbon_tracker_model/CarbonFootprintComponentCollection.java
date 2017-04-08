package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import android.content.Context;
import android.database.Cursor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
    Implements CarbonFootprintComponentCollection that is Singleton class providing an interface between Model and UI
 */

public class CarbonFootprintComponentCollection {

    private ArrayList<JourneyModel> journies;
    private ArrayList<String> vehicleMakes;
    private ArrayList<TransportationModel> readVehicles;
    private static CarbonFootprintComponentCollection instance = new CarbonFootprintComponentCollection();

    public static CarbonFootprintComponentCollection getInstance(){
        return instance;
    }

    private CarbonFootprintComponentCollection(){
        journies = new ArrayList<>();
        vehicleMakes = new ArrayList<>();
    }

    public ArrayList<TransportationModel> getVehicles(Context context) {
        TransportationDBAdapter transportationDBAdapter = new TransportationDBAdapter(context);
        return transportationDBAdapter.getAllVehicles();
    }

    public ArrayList<RouteModel> getRoutes(Context context) {
        RouteDBAdapter routeDBAdapter = new RouteDBAdapter(context);
        return routeDBAdapter.getAllRoutes();
    }

    public ArrayList<JourneyModel> getJournies(Context context) {
        JourneyDBAdapter journeyDBAdapter = new JourneyDBAdapter(context);
        return journeyDBAdapter.getAllJournies();
    }

    public ArrayList<UtilityModel> getUtilities(Context context) {
        UtilityDBAdapter utilityDBAdapter = new UtilityDBAdapter(context);
        return utilityDBAdapter.getAllUtilities();
    }

    //Adding component to one of arrayList based on its underlying type
    //Throw an exception if component cannot be casted to a valid type
    public void add(Context context, CarbonFootprintComponent component){
        validateComponentDuplication(context, component);
        if (component instanceof TransportationModel){
            TransportationDBAdapter transportationDBAdapter = new TransportationDBAdapter(context);
            transportationDBAdapter.open();
            transportationDBAdapter.insertRow((TransportationModel)component);
            transportationDBAdapter.close();
        }
        else if (component instanceof RouteModel){
            RouteDBAdapter routeDBAdapter = new RouteDBAdapter(context);
            routeDBAdapter.open();
            routeDBAdapter.insertRow((RouteModel)component);
            routeDBAdapter.close();
        }
        else if (component instanceof JourneyModel){
            JourneyDBAdapter journeyDBAdapter = new JourneyDBAdapter(context);
            journeyDBAdapter.open();
            journeyDBAdapter.insertRow((JourneyModel)component);
            journeyDBAdapter.close();
        }
        else if (component instanceof UtilityModel){
            UtilityDBAdapter utilityDBAdapter = new UtilityDBAdapter(context);
            utilityDBAdapter.open();
            utilityDBAdapter.insertRow((UtilityModel) component);
            utilityDBAdapter.close();
        }
        else{
            throw new IllegalArgumentException("Input component is not valid.");
        }
    }

    //Edit an existing component to replace it with the passed argument
    private <E extends CarbonFootprintComponent> void edit(ArrayList<E> list, CarbonFootprintComponent component, Context context){
        if (component instanceof TransportationModel){
//            TransportationModel vehicle = (TransportationModel) component;
//            TransportationDBAdapter vehicleDBAdapter = new TransportationDBAdapter(context);
//            vehicleDBAdapter.open();
//            vehicleDBAdapter.updateRow(vehicle);
//            vehicleDBAdapter.close();
        }
//        else
//        {
//            throw new IllegalArgumentException("Input component could not be found in the list.");
//        }
    }

    //edit component from one of arrayList based on its underlying type
    //Throw an exception if component cannot be casted to a valid type or found
    public void edit(Context context, CarbonFootprintComponent component){
        if (component instanceof TransportationModel){
            TransportationModel vehicle = (TransportationModel) component;
            TransportationDBAdapter transportationDBAdapter = new TransportationDBAdapter(context);
            transportationDBAdapter.open();
            transportationDBAdapter.updateRow(vehicle);
            transportationDBAdapter.close();
        }
        else if (component instanceof RouteModel){
            RouteModel route = (RouteModel) component;
            RouteDBAdapter routeDBAdapter = new RouteDBAdapter((context));
            routeDBAdapter.open();
            routeDBAdapter.updateRow(route);
            routeDBAdapter.close();
        }
        else if (component instanceof JourneyModel){
            JourneyModel journeyModel = (JourneyModel) component;
            JourneyDBAdapter journeyDBAdapter = new JourneyDBAdapter((context));
            journeyDBAdapter.open();
            journeyDBAdapter.updateRow(journeyModel);
            journeyDBAdapter.close();
        }
        else if (component instanceof UtilityModel){
            UtilityModel utilityModel = (UtilityModel) component;
            UtilityDBAdapter utilityDBAdapter = new UtilityDBAdapter((context));
            utilityDBAdapter.open();
            utilityDBAdapter.updateRow(utilityModel);
            utilityDBAdapter.close();
        }
        else{
            throw new IllegalArgumentException("Input component is not valid.");
        }
    }

    //removing(hide) component from one of arrayList based on its underlying type
    //Throw an exception if component cannot be casted to a valid type
    public boolean remove(Context context, CarbonFootprintComponent component){
        boolean result = false;
        if (component instanceof TransportationModel){
            TransportationModel vehicle = (TransportationModel) component;
            if(vehicle.getId() > -1){
                vehicle.setIsDeleted(true);
                TransportationDBAdapter transportationDBAdapter = new TransportationDBAdapter(context);
                transportationDBAdapter.open();
                result = transportationDBAdapter.updateRow(vehicle);
                transportationDBAdapter.close();
            }
            else
            {
                throw new IllegalArgumentException("Input component could not be found in the list.");
            }
        }
        else if (component instanceof RouteModel){
            RouteModel route = (RouteModel) component;
            if(route.getId() > -1){
                route.setIsDeleted(true);
                RouteDBAdapter routeDBAdapter = new RouteDBAdapter(context);
                routeDBAdapter.open();
                result = routeDBAdapter.updateRow(route);
                routeDBAdapter.close();
            }
            else
            {
                throw new IllegalArgumentException("Input component could not be found in the list.");
            }
        } else if (component instanceof JourneyModel) {
            JourneyModel journeyModel = (JourneyModel) component;
            if(journeyModel.getId() > -1){
                journeyModel.setIsDeleted(true);
                JourneyDBAdapter journeyDBAdapter = new JourneyDBAdapter(context);
                journeyDBAdapter.open();
                result = journeyDBAdapter.updateRow(journeyModel);
                journeyDBAdapter.close();
            }
            else
            {
                throw new IllegalArgumentException("Input component could not be found in the list.");
            }
        }
        else if (component instanceof UtilityModel) {
            UtilityModel utility = (UtilityModel) component;
            if(utility.getId() > -1){
                utility.setIsDeleted(true);
                UtilityDBAdapter utilityDBAdapter = new UtilityDBAdapter(context);
                utilityDBAdapter.open();
                result = utilityDBAdapter.updateRow(utility);
                utilityDBAdapter.close();
            }
            else
            {
                throw new IllegalArgumentException("Input component could not be found in the list.");
            }
        }
        else{
            throw new IllegalArgumentException("Input component is not valid.");
        }
        return result;
    }

    //Check the arrayList to see if it contains component
    private <E extends CarbonFootprintComponent> void  validateComponentDuplication(ArrayList<E> list, CarbonFootprintComponent component){
        if (list.contains(component)){
            throw new DuplicateComponentException();
        }
    }

    //Validating component based on its underlying
    //Throw an exception if the component is not valid
    private void validateComponentDuplication(Context context, CarbonFootprintComponent component){
        // check from database
        if (component instanceof TransportationModel){
            TransportationModel transportationModel = (TransportationModel)component;
            TransportationDBAdapter transportationDBAdapter = new TransportationDBAdapter(context);
            transportationDBAdapter.open();
            Cursor c = transportationDBAdapter.getName(transportationModel.getName());
            if(c.getCount() > 0)
            {
                transportationDBAdapter.close();
                throw new DuplicateComponentException();
            }
            transportationDBAdapter.close();
            return;
        }
        else if (component instanceof RouteModel){
            RouteModel routeModel = (RouteModel) component;
            RouteDBAdapter routeDBAdapter = new RouteDBAdapter(context);
            routeDBAdapter.open();
            Cursor c = routeDBAdapter.getName(routeModel.getName());
            if(c.getCount() > 0)
            {
                routeDBAdapter.close();
                throw new DuplicateComponentException();
            }
            routeDBAdapter.close();
            return;
        }
        else if (component instanceof JourneyModel){
            //TODO: Check the database. Journey can be duplicate
            validateComponentDuplication(journies, component);
        }
        else if (component instanceof UtilityModel){
            UtilityModel utilityModel = (UtilityModel) component;
            UtilityDBAdapter utilityDBAdapter = new UtilityDBAdapter(context);
            utilityDBAdapter.open();
            Cursor c = utilityDBAdapter.getName(utilityModel.getName());
            if (c.getCount() > 0){
                utilityDBAdapter.close();
                throw new DuplicateComponentException();
            }
            utilityDBAdapter.close();
            return;
        }
        else{
            throw new IllegalArgumentException("Input component is not valid.");
        }
    }

    // Followings are sample data for testing, need to modified to properly get data from fueldatainputstream class
    public ArrayList<String> getVehicleMakes(){
        return vehicleMakes;
    }

    public ArrayList<String> getVehicleModel(String make){
        return extractModels(make);
    }

    public ArrayList<String> getVehicleYear(String make, String model){
        return extractYears(make, model);
    }

    public ArrayList<String> getVehicleTransmission(String make, String model, String year){
        return extractTransmission(make, model, year);
    }
    public ArrayList<String> getVehicleEngineDisplacement(String make, String model, String year, String transmission){
        return extractEngineDisplacement(make, model, year, transmission);
    }

    public void loadDataFile(InputStream is){
        if (readVehicles == null) {
            readVehicles = FuelDataInputStream.getInstance().readDataFile(is);
            extractMakes();
        }
    }

    private void extractMakes() {
        Set<String> makesSet = new HashSet<>();
        for(TransportationModel v : readVehicles){
            makesSet.add(v.getMake());
        }
        vehicleMakes = new ArrayList<>(makesSet);
        Collections.sort(vehicleMakes);
    }

    private ArrayList<String> extractModels(String make) {
        Set<String> modelsSet = new HashSet<>();
        for(TransportationModel v : readVehicles){
            if(v.getMake().equals(make)){
                modelsSet.add(v.getModel());
            }
        }
        ArrayList<String> vehicleModels = new ArrayList<>(modelsSet);
        Collections.sort(vehicleModels);
        return vehicleModels;
    }

    private ArrayList<String> extractYears(String make, String model) {
        Set<String> yearsSet = new HashSet<>();
        for(TransportationModel v : readVehicles){
            if(v.getMake().equals(make) && v.getModel().equals(model)){
                yearsSet.add(v.getYear());
            }
        }
        ArrayList<String> vehicleYears = new ArrayList<>(yearsSet);
        Collections.sort(vehicleYears);
        return vehicleYears;
    }

    private ArrayList<String> extractTransmission(String make, String model, String year) {
        Set<String> transmissionSet = new HashSet<>();
        for(TransportationModel v : readVehicles){
            if(v.getMake().equals(make) && v.getModel().equals(model) && v.getYear().equals(year)){
                transmissionSet.add(v.getTransmisson());
            }
        }
        ArrayList<String> vehicleTransmissions = new ArrayList<>(transmissionSet);
        Collections.sort(vehicleTransmissions);
        return vehicleTransmissions;
    }
    private ArrayList<String> extractEngineDisplacement(String make, String model, String year, String Transmission) {
        Set<String> engineDisplacementSet = new HashSet<>();
        for(TransportationModel v : readVehicles){
            if(v.getMake().equals(make) && v.getModel().equals(model) && v.getYear().equals(year) && v.getTransmisson().equals(Transmission)){
                engineDisplacementSet.add(v.getEngineDisplacment());
            }
        }
        ArrayList<String> vehicleEngineDisplacements = new ArrayList<>(engineDisplacementSet);
        Collections.sort(vehicleEngineDisplacements);
        return vehicleEngineDisplacements;
    }

    public void populateCarFuelData(TransportationModel transportationModel){
        for(TransportationModel v : readVehicles){
            if(v.getMake().equals(transportationModel.getMake()) && v.getModel().equals(transportationModel.getModel()) &&
                v.getYear().equals(transportationModel.getYear())){
                transportationModel.copyFuelData(v);
                return;
            }
        }
    }
}
