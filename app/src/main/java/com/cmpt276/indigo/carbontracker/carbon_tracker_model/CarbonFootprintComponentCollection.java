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
    private static CarbonFootprintComponentCollection instance = new CarbonFootprintComponentCollection();

    public static CarbonFootprintComponentCollection getInstance(){
        return instance;
    }

    private CarbonFootprintComponentCollection(){
        vehicles = new ArrayList<>();
        routes = new ArrayList<>();
        journies = new ArrayList<>();
        vehicleMakes = new ArrayList<>();
    }

    public ArrayList<VehicleModel> getVehicles(Context context) {
        VehicleDBAdapter vehicleDBAdapter = new VehicleDBAdapter(context);
        vehicleDBAdapter.open();
        Cursor cursor = vehicleDBAdapter.getAllRows();
        ArrayList<VehicleModel> vehicleModels = new ArrayList<>();
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
                VehicleModel vehicleModel = new VehicleModel(id, name, make, model, year, transmission, engineDisplacement, cityMileage, highwayMileage, primaryFuelType, isDeleted);
                vehicleModels.add(vehicleModel);
            } while(cursor.moveToNext());
        }
        // Close the cursor to avoid a resource leak.
        cursor.close();
        return vehicleModels;
    }

    public ArrayList<RouteModel> getRoutes() {
        //TODO: read from Route table
        return routes;
    }

    public ArrayList<JourneyModel> getJournies() {
        //TODO: read from Journie table
        return journies;
    }

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
            routes.add((RouteModel) component);
        }
        else if (component instanceof JourneyModel){
            journies.add((JourneyModel) component);
        }
        else{
            throw new IllegalArgumentException("Input component is not valid.");
        }
    }

    //Edit an existing component to replace it with the passed argument
    private <E extends CarbonFootprintComponent> void edit(ArrayList<E> list, CarbonFootprintComponent component, Context context){
        if (component instanceof VehicleModel){
            VehicleModel vehicle = (VehicleModel) component;
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
            edit(routes, component, context);
        }
        else if (component instanceof JourneyModel){
            edit(journies, component, context);
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
            int index = routes.indexOf(component);
            if(index > -1){
                routes.get(index).setIsDeleted(true);
            }
            else
            {
                throw new IllegalArgumentException("Input component could not be found in the list.");
            }
        } else if (component instanceof JourneyModel) {
            int index = journies.indexOf(component);
            if (index > -1) {
                journies.get(index).setDeleted(true);
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
            validateComponentDuplication(routes, component);
        }
        else if (component instanceof JourneyModel){
            validateComponentDuplication(journies, component);
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
