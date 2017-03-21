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

    private ArrayList<VehicleModel> vehicles;
    private ArrayList<RouteModel> routes;
    private ArrayList<JourneyModel> journies;
    private ArrayList<String> vehicleMakes;
    private ArrayList<VehicleModel> readVehicles;
    private ArrayList<UtilityModel> utilities;
    private static CarbonFootprintComponentCollection instance = new CarbonFootprintComponentCollection();

    public static CarbonFootprintComponentCollection getInstance(){
        return instance;
    }

    private CarbonFootprintComponentCollection(){
        vehicles = new ArrayList<>();
        routes = new ArrayList<>();
        journies = new ArrayList<>();
        vehicleMakes = new ArrayList<>();
        utilities = new ArrayList<>();
    }

    public ArrayList<VehicleModel> getVehicles(Context context) {
        VehicleDBAdapter vehicleDBAdapter = new VehicleDBAdapter(context);
        vehicleDBAdapter.open();
        Cursor cursor = vehicleDBAdapter.getAllRows();
        ArrayList<VehicleModel> vehicles = new ArrayList<>();
        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                long id = (long)cursor.getInt(vehicleDBAdapter.COL_ROWID);
                String name = cursor.getString(VehicleDBAdapter.COL_NAME);
                String make = cursor.getString(VehicleDBAdapter.COL_MAKE);
                String model = cursor.getString(VehicleDBAdapter.COL_MODEL);
                String year = cursor.getString(VehicleDBAdapter.COL_YEAR);
                String transmission = cursor.getString(VehicleDBAdapter.COL_TRANSMISSION);
                String engineDisplacement = cursor.getString(VehicleDBAdapter.COL_ENGINE_DISPLACEMENT);
                double cityMileage = cursor.getDouble(VehicleDBAdapter.COL_CITY_MILEAGE);
                double highwayMileage = cursor.getDouble(VehicleDBAdapter.COL_HIGHWAY_MILEAGE);
                String primaryFuelType = cursor.getString(VehicleDBAdapter.COL_PRIMARY_FUEL_TYPE);
                boolean isDeleted = cursor.getInt(VehicleDBAdapter.COL_IS_DELETED) > 0;
                if(isDeleted) {
                    continue;
                }
                VehicleModel vehicle = new VehicleModel(id, name, make, model, year, transmission, engineDisplacement, cityMileage, highwayMileage, primaryFuelType, isDeleted);
                vehicles.add(vehicle);
            } while(cursor.moveToNext());
        }
        // Close the cursor to avoid a resource leak.
        cursor.close();
        return vehicles;
    }

    public ArrayList<RouteModel> getRoutes(Context context) {
        //TODO: read from Route table
        RouteDBAdapter routeDBAdapter = new RouteDBAdapter(context);
        routeDBAdapter.open();
        Cursor cursor = routeDBAdapter.getAllRows();
        ArrayList<RouteModel> routes = new ArrayList<>();
        //Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()){
            do {
                //Process the data:
                long id = (long) cursor.getInt(routeDBAdapter.COL_ROWID);
                String name = cursor.getString(RouteDBAdapter.COL_NAME);
                double cityDistance = cursor.getDouble(RouteDBAdapter.COL_CITY_DISTANCE);
                double highwayDistance = cursor.getDouble(RouteDBAdapter.COL_HIGHWAY_DISTANCE);
                double totalDistance = cursor.getDouble((RouteDBAdapter.COL_TOTAL_DISTANCE));
                boolean isDeleted = cursor.getInt(RouteDBAdapter.COL_IS_DELETED) > 0;
                if (isDeleted){
                    continue;
                }
                RouteModel route = new RouteModel(id, name ,cityDistance, highwayDistance, totalDistance, isDeleted);
                routes.add(route);
            }while(cursor.moveToNext());
        }
        //Close the cursor to avoid a resource leak.
        cursor.close();
        return routes;
    }

    public ArrayList<JourneyModel> getJournies() {
        //TODO: read from Journie table
        return journies;
    }

    public ArrayList<UtilityModel> getUtilities() {return utilities;}

    //Adding component to one of arrayList based on its underlying type
    //Throw an exception if component cannot be casted to a valid type
    public void add(Context context, CarbonFootprintComponent component){
        //TODO: add to database instead of arraylist
        validateComponentDuplication(context, component);
        if (component instanceof VehicleModel){
            VehicleDBAdapter vehicleDBAdapter = new VehicleDBAdapter(context);
            vehicleDBAdapter.open();
            vehicleDBAdapter.insertRow((VehicleModel)component);
            vehicleDBAdapter.close();
        }
        else if (component instanceof RouteModel){
            RouteDBAdapter routeDBAdapter = new RouteDBAdapter(context);
            routeDBAdapter.open();
            routeDBAdapter.insertRow((RouteModel)component);
            routeDBAdapter.close();
        }
        else if (component instanceof JourneyModel){
            journies.add((JourneyModel) component);
        }
        else if (component instanceof UtilityModel){
            //TODO: adding UtilityDBAdapted and uncommenting followings
            /*
            UtilityDBAdapter utilityDBAdapter = new UtilityDBAdapter(context);
            utilityDBAdapter.open();
            utilityDBAdapter.insertRow((RouteModel)component);
            utilityDBAdapter.close();
            */
            utilities.add((UtilityModel) component);
        }
        else{
            throw new IllegalArgumentException("Input component is not valid.");
        }
    }

    //Edit an existing component to replace it with the passed argument
    private <E extends CarbonFootprintComponent> void edit(ArrayList<E> list, CarbonFootprintComponent component, Context context){
        if (component instanceof VehicleModel){
//            VehicleModel vehicle = (VehicleModel) component;
//            VehicleDBAdapter vehicleDBAdapter = new VehicleDBAdapter(context);
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
        //TODO: edit database entries
        if (component instanceof VehicleModel){
            VehicleModel vehicle = (VehicleModel) component;
            VehicleDBAdapter vehicleDBAdapter = new VehicleDBAdapter(context);
            vehicleDBAdapter.open();
            vehicleDBAdapter.updateRow(vehicle);
            vehicleDBAdapter.close();
        }
        else if (component instanceof RouteModel){
            RouteModel route = (RouteModel) component;
            RouteDBAdapter routeDBAdapter = new RouteDBAdapter((context));
            routeDBAdapter.open();
            routeDBAdapter.updateRow(route);
            routeDBAdapter.close();
        }
        else if (component instanceof JourneyModel){
            int index = journies.indexOf((JourneyModel)component);
            if (index > -1) {
                journies.set(index, (JourneyModel) component);
            }
        }
        else if (component instanceof UtilityModel){
//            edit(utilities, component, context);
            int index = utilities.indexOf((UtilityModel)component);
            if (index > -1) {       //Index has a problem. It will fixed in database
                utilities.set(index, (UtilityModel) component);
            }
        }
        else{
            throw new IllegalArgumentException("Input component is not valid.");
        }
    }

    //deleting component from one of arrayList based on its underlying type
    //Throw an exception if component cannot be casted to a valid type
    public void delete(CarbonFootprintComponent component){
        // delete from database
        if (component instanceof VehicleModel){
            int index = vehicles.indexOf((VehicleModel)component);
            if(index > -1){
                vehicles.remove(index);
            }
            else
            {
                throw new IllegalArgumentException("Input component could not be found in the list.");
            }
        }
        else if (component instanceof RouteModel){
            int index = routes.indexOf((RouteModel)component);
            if(index > -1){
                routes.remove(index);
            }
            else
            {
                throw new IllegalArgumentException("Input component could not be found in the list.");
            }
        }
        else if (component instanceof UtilityModel){
            int index = utilities.indexOf((UtilityModel)component);
            if(index > -1){
                utilities.remove(index);
            }
            else
            {
                throw new IllegalArgumentException("Input component could not be found in the list.");
            }
        }
        else{
            throw new IllegalArgumentException("Input component is not valid.");
        }
    }

    //removing(hide) component from one of arrayList based on its underlying type
    //Throw an exception if component cannot be casted to a valid type
    public void remove(Context context, CarbonFootprintComponent component){
        // TODO: hide in database
        if (component instanceof VehicleModel){
            VehicleModel vehicle = (VehicleModel) component;
            if(vehicle.getId() > -1){
                vehicle.setIsDeleted(true);
                VehicleDBAdapter vehicleDBAdapter = new VehicleDBAdapter(context);
                vehicleDBAdapter.open();
                vehicleDBAdapter.updateRow(vehicle);
                vehicleDBAdapter.close();
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
                routeDBAdapter.updateRow(route);
                routeDBAdapter.close();
            }
            else
            {
                throw new IllegalArgumentException("Input component could not be found in the list.");
            }
        } else if (component instanceof JourneyModel) {
            int index = journies.indexOf(component);
            if (index > -1) {
                journies.get(index).setDeleted(true);
                journies.remove(index);
            }
        }
        else if (component instanceof UtilityModel) {
            UtilityModel utility = (UtilityModel) component;
            if(utility.getId() > -1){
                utility.setIsDeleted(true);
                UtilityDBAdapter utilityDBAdapter = new UtilityDBAdapter(context);
                utilityDBAdapter.open();
                utilityDBAdapter.updateRow(utility);
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
        //TODO: Check the database
        if (component instanceof VehicleModel){
            VehicleModel vehicleModel = (VehicleModel)component;
            VehicleDBAdapter vehicleDBAdapter = new VehicleDBAdapter(context);
            vehicleDBAdapter.open();
            Cursor c = vehicleDBAdapter.getName(vehicleModel.getName());
            if(c.getCount() > 0)
            {
                vehicleDBAdapter.close();
                throw new DuplicateComponentException();
            }
            vehicleDBAdapter.close();
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
        for(VehicleModel v : readVehicles){
            makesSet.add(v.getMake());
        }
        vehicleMakes = new ArrayList<>(makesSet);
        Collections.sort(vehicleMakes);
    }

    private ArrayList<String> extractModels(String make) {
        Set<String> modelsSet = new HashSet<>();
        for(VehicleModel v : readVehicles){
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
        for(VehicleModel v : readVehicles){
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
        for(VehicleModel v : readVehicles){
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
        for(VehicleModel v : readVehicles){
            if(v.getMake().equals(make) && v.getModel().equals(model) && v.getYear().equals(year) && v.getTransmisson().equals(Transmission)){
                engineDisplacementSet.add(v.getEngineDisplacment());
            }
        }
        ArrayList<String> vehicleEngineDisplacements = new ArrayList<>(engineDisplacementSet);
        Collections.sort(vehicleEngineDisplacements);
        return vehicleEngineDisplacements;
    }

    public void populateCarFuelData(VehicleModel vehicleModel){
        for(VehicleModel v : readVehicles){
            if(v.getMake().equals(vehicleModel.getMake()) && v.getModel().equals(vehicleModel.getModel()) &&
                v.getYear().equals(vehicleModel.getYear())){
                vehicleModel.copyFuelData(v);
                return;
            }
        }
    }
}
