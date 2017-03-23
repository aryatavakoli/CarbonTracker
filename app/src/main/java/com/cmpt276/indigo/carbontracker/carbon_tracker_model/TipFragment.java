package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class TipFragment extends AppCompatDialogFragment {
    CarbonFootprintComponentCollection carbonFootprintInterface;

    public ArrayList<VehicleModel> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<VehicleModel> vehicles) {
        this.vehicles = vehicles;
    }

    public CarbonFootprintComponentCollection getCarbonFootprintInterface() {
        return carbonFootprintInterface;
    }

    public void setCarbonFootprintInterface(CarbonFootprintComponentCollection carbonFootprintInterface) {
        this.carbonFootprintInterface = carbonFootprintInterface;
    }

    public ArrayList<RouteModel> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<RouteModel> routes) {
        this.routes = routes;
    }

    public ArrayList<JourneyModel> getJournies() {
        return journies;
    }

    public void setJournies(ArrayList<JourneyModel> journies) {
        this.journies = journies;
    }

    public String[] getMessageList() {
        return messageList;
    }

    public void setMessageList(String[] messageList) {
        this.messageList = messageList;
    }

    ArrayList<VehicleModel> vehicles;
    ArrayList<RouteModel> routes;
    ArrayList<JourneyModel> journies;
    String[] messageList = new String[15];

    public static int getMessageIndex() {
        return messageIndex;
    }

    public static void setMessageIndex(int messageIndex) {
        TipFragment.messageIndex = messageIndex;
    }

    static int messageIndex =0;

    public String getMessage() {
        return message;
    }

    private String message;



    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        populateMessageList();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(messageList[messageIndex]);
        messageIndex ++;
        if (messageIndex == 15)messageIndex = 0;
        builder.setTitle("New Tip");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getActivity().finish();

            }

        });
        builder.setPositiveButton("next tip", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FragmentManager manager = getFragmentManager();
                TipFragment dialog2 = new TipFragment();
                dialog2.setCancelable(false);
                dialog2.show(manager,"message dialog");
                dialog2.setMessage(messageList[messageIndex]);
                dialog2.setJournies(journies);
                dialog2.setRoutes(routes);
                dialog2.setVehicles(vehicles);
                messageIndex ++;
                if (messageIndex == 15)messageIndex = 0;
            }

        });


        AlertDialog alert = builder.show();
        alert.setCancelable(false);
        return alert;

    }



    private void populateMessageList() {
        final Calendar today = Calendar.getInstance();
        double CO2usage=0;
        double CO2Car= 0;
        int carNUm = 0;
        int walkNum= 0;
        float distanceWalked = 0;
        int shortDistanceTrips=0;
        int cityNum = 0;
        int highwayNum = 0;
        for(JourneyModel v: journies){
            CO2usage =  CO2usage + v.getCo2Emission();
            if (v.getVehicleModel().getTransportaionMode()== VehicleModel.TransportationMode.CAR && v.getCreationDate().equals(today)) {
                carNUm++;
                CO2Car =  CO2Car + v.getCo2Emission();
            }
            if (v.getRouteModel().getTotalDistance() <10 && v.getVehicleModel().getTransportaionMode() != VehicleModel.TransportationMode.WALK_BIKE)  {
                shortDistanceTrips ++;
            }
            if (v.getVehicleModel().getTransportaionMode() == VehicleModel.TransportationMode.WALK_BIKE){
                walkNum++;
                distanceWalked += v.getRouteModel().getTotalDistance();

            }
            if ((v.getRouteModel().getHighwayDistance() < v.getRouteModel().getCityDistance()) &&
                    v.getVehicleModel().getTransportaionMode() == VehicleModel.TransportationMode.CAR){
                cityNum ++;
            }




        }
        messageList[0]= "You had"+ carNUm+ "car trips today. You might be able to cut that down by combining trips tomorrow.";
        messageList[1]= "You generate "+ CO2usage  + " of CO2 . Shorter showers might help cut down emissions from hot water heater.";
        messageList[2]= "Overall you had " + CO2usage + " kgs of carbon which " + CO2Car+" of it belongs to your car trips";
        messageList[3]= "You have " + shortDistanceTrips +" trips with total distance of less than 10km you can try walking or biking these routes";
        messageList[4]= "You have Walked/Biked" + distanceWalked +  "kms in total of " + walkNum +" walk/bike trips you had" ;
        messageList[5]= "In " + cityNum + " of your car journeys highway distance was less than city distance" +
                " if it is not necessary try to avoid driving in city and traffics because it produces more CO2 ";
        messageList[6]= "during taking each of your " +  carNUm+
        " car tips try to Drive at an appropriate speed -" +
        " staying within the 70mph limit can bring savings of 10 per cent for your fuel bill compared to driving at 80mph.";
        messageList[7]= "u dont know ur beautiful";
        messageList[8]= "I feel it coming";
        messageList[9]= "mimiram barat";
        messageList[10]="akhe dooooset daram";
        messageList[11]= "dooos draam zendegiro";
        messageList[12]= "we dont talk anymore";
        messageList[13]= "I dont wanna live forever";
        messageList[14]= "to pishie manio mi00";
    }






}


