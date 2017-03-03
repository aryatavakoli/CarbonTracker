package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import java.util.ArrayList;

public class CarbonFootprintComponentCollection {

    ArrayList<VehicleModel> vehicles;
    ArrayList<RouteModel> routes;
    ArrayList<JourneyModel> journies;
    private static CarbonFootprintComponentCollection instance = new CarbonFootprintComponentCollection();

    public static CarbonFootprintComponentCollection getInstance(){
        return instance;
    }

    private CarbonFootprintComponentCollection(){
        vehicles = new ArrayList<>();
        routes = new ArrayList<>();
        journies = new ArrayList<>();

        // FOR TEST, SHOULD BE REMOVED
        vehicles.add(new VehicleModel("Vehicle1", "Mazda", "3", "2010"));
        vehicles.add(new VehicleModel("Vehicle2", "Toyota", "Corola", "1998"));
        vehicles.add(new VehicleModel("Vehicle3", "Hyndai", "Sonata", "2000"));
        vehicles.add(new VehicleModel("Vehicle4", "Benz", "S240", "2014"));
        vehicles.add(new VehicleModel("Vehicle5", "BMW", "Class5", "2008"));
        vehicles.add(new VehicleModel("Vehicle6", "Lexus", "Next", "2002"));
        vehicles.add(new VehicleModel("Vehicle7", "Ferrari", "Tiger", "2001"));
        vehicles.add(new VehicleModel("Vehicle8", "Lamborgini", "CL159", "1999"));
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
        // FOR TESTING
        ArrayList<String> makes = new ArrayList<>();
        makes.add("Mazda");
        makes.add("Toyota");
        makes.add("Benz");
        makes.add("BMW");
        makes.add("Hyndai");
        makes.add("Lexus");
        makes.add("Ferrari");
        makes.add("Lamborgini");
        return makes;
    }

    public ArrayList<String> getVehicleModel(){
        // FOR TESTING
        ArrayList<String> models = new ArrayList<>();
        models.add("3");
        models.add("Corola");
        models.add("Sonata");
        models.add("S240");
        models.add("Class5");
        models.add("Next");
        models.add("Tiger");
        models.add("CL159");
        return models;
    }

    public ArrayList<String> getVehicleYear(){
        // FOR TESTING
        ArrayList<String> years = new ArrayList<>();
        for(int i = 1990; i < 2020; i++){
            years.add("" + i);
        }
        return years;
    }
}
