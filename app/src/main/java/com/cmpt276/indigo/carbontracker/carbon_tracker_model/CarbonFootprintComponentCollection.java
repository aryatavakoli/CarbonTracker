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
            new DuplicateComponentException();
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
}
