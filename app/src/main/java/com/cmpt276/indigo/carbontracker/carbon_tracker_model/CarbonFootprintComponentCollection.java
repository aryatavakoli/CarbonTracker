package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CarbonFootprintComponentCollection {

    ArrayList<VehicleModel> vehicles;
    ArrayList<RouteModel> routes;
    ArrayList<JourneyModel> journies;
    ArrayList<String> vehicleMakes;
    ArrayList<String> vehicleModels;
    ArrayList<String> vehicleYears;
    private static CarbonFootprintComponentCollection instance = new CarbonFootprintComponentCollection();

    public static CarbonFootprintComponentCollection getInstance(){
        return instance;
    }

    private CarbonFootprintComponentCollection(){
        vehicles = new ArrayList<>();
        routes = new ArrayList<>();
        journies = new ArrayList<>();
        vehicleMakes = new ArrayList<>();
        vehicleModels = new ArrayList<>();
    }

    public ArrayList<VehicleModel> getVehicles() {
        return vehicles;
    }

    public ArrayList<RouteModel> getRoutes() {
        return routes;
    }

    public ArrayList<JourneyModel> getJournies() {return journies;}

    //Adding component to one of arrayList based on its underlying type
    //Throw an exception if component cannot be casted to a valid type
    public void add(CarbonFootprintComponent component){
        validateComponentDuplication(component);
        if (component instanceof VehicleModel){
            vehicles.add((VehicleModel)component);
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

    //deleting component from one of arrayList based on its underlying type
    //Throw an exception if component cannot be casted to a valid type
    public void delete(CarbonFootprintComponent component){
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

    //removing component from one of arrayList based on its underlying type
    //Throw an exception if component cannot be casted to a valid type
    public void remove(CarbonFootprintComponent component){
        if (component instanceof VehicleModel){
            int index = vehicles.indexOf((VehicleModel)component);
            if(index > -1){
                vehicles.get(index).setIsDeleted(true);
            }
            else
            {
                throw new IllegalArgumentException("Input component could not be found in the list.");
            }
        }
        else if (component instanceof RouteModel){
            int index = routes.indexOf((RouteModel)component);
            if(index > -1){
                routes.get(index).setIsDeleted(true);
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

    //Edit an existing compoent to replace it with the passed argument
    private <E extends CarbonFootprintComponent> void edit(ArrayList<E> list, CarbonFootprintComponent component){
        int index = list.indexOf(component);
        if(index > -1){
            list.set(index, (E)component);
        }
        else
        {
            throw new IllegalArgumentException("Input component could not be found in the list.");
        }
    }

    //edit component from one of arrayList based on its underlying type
    //Throw an exception if component cannot be casted to a valid type or found
    public void edit(CarbonFootprintComponent component){
        if (component instanceof VehicleModel){
            edit(vehicles, component);
        }
        else if (component instanceof RouteModel){
            edit(routes, component);
        }
        else if (component instanceof JourneyModel){
            edit(journies, component);
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
    private void validateComponentDuplication(CarbonFootprintComponent component){
        if (component instanceof VehicleModel){
            validateComponentDuplication(vehicles, component);
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

    public void changeComponent(CarbonFootprintComponent component, int indexOfComponentEditting){

    }

    //Validating index of component
    private void validateComponentIndexWithException(CarbonFootprintComponent component,int index) {
        if (component instanceof VehicleModel){
            if (index < 0 || index >= instance.getVehicles().size()) {
                throw new IllegalArgumentException();
            }
        }
        else if (component instanceof RouteModel){
            if (index < 0 || index >= instance.getRoutes().size()) {
                throw new IllegalArgumentException();
            }
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    // Followings are sample data for testing, need to modified to properly get data from fueldatainputstream class
    public ArrayList<String> getVehicleMakes(){
        return vehicleMakes;
    }

    public ArrayList<String> getVehicleModel(){
        return vehicleModels;
    }

    public ArrayList<String> getVehicleYear(){
        // FOR TESTING
//        ArrayList<String> years = new ArrayList<>();
//        for(int i = 1980; i < 2017; i++){
//            years.add("" + i);
//        }
//        return years;

        return vehicleYears;
    }

    public void loadDataFile(InputStream is){
        ArrayList<VehicleModel> readVehicles = FuelDataInputStream.getInstance().readDataFile(is);
        extractModels(readVehicles);
        extractMakes(readVehicles);
        extractYears(readVehicles);
    }

    private void extractMakes(ArrayList<VehicleModel> readVehicles) {
        Set<String> makesSet = new HashSet<>();
        for(VehicleModel v : readVehicles){
            makesSet.add(v.getMake());
        }
        vehicleMakes = new ArrayList<>(makesSet);
        Collections.sort(vehicleMakes);
    }

    private void extractModels(ArrayList<VehicleModel> readVehicles) {
        Set<String> modelsSet = new HashSet<>();
        for(VehicleModel v : readVehicles){
            modelsSet.add(v.getModel());
        }
        vehicleModels = new ArrayList<>(modelsSet);
        Collections.sort(vehicleModels);
    }

    private void extractYears(ArrayList<VehicleModel> readVehicles) {
        Set<String> yearsSet = new HashSet<>();
        for(VehicleModel v : readVehicles){
            yearsSet.add(v.getYear());
        }
        vehicleYears = new ArrayList<>(yearsSet);
        Collections.sort(vehicleYears);
    }
}
