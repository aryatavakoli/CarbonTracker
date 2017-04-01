package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Implements Dialouge box for tips
 */

public class TipFragment extends AppCompatDialogFragment {
    CarbonFootprintComponentCollection carbonFootprintInterface;

    public ArrayList<TransportationModel> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<TransportationModel> vehicles) {
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

    ArrayList<TransportationModel> vehicles;
    ArrayList<RouteModel> routes;
    ArrayList<JourneyModel> journies;
    String[] messageList = new String[15];

    public static int getMessageIndex() {
        return messageIndex;
    }

    public static void setMessageIndex(int messageIndex) {
        TipFragment.messageIndex = messageIndex;
    }

    static int messageIndex =0; //this our couner through messageArray

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
        if (messageIndex == 15)messageIndex = 0; //if we have reached the end of the arraylist go back to index 0
        builder.setTitle("New Tip");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getActivity().finish();

            }

        });
        builder.setPositiveButton("next tip", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
//                if the user wants to see another tip we call anotheer dialod
                FragmentManager manager = getFragmentManager();
                TipFragment dialog2 = new TipFragment();
                dialog2.setCancelable(false);
                dialog2.show(manager,"message dialog");
                dialog2.setMessage(messageList[messageIndex]);
                dialog2.setJournies(journies); // pass data to tip fragment
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
//        populate each element of message list with the data user has provided us with
        final Calendar today = Calendar.getInstance();
        double CO2usage=0;
        double CO2Car= 0;
        int carNUm = 0;
        int walkNum= 0;
        float distanceWalked = 0;
        int shortDistanceTrips=0;
        int cityNum = 0;
        double mostCarbonEmission= 0;
        int todayJournies =0;
        if (journies.size() > 0)   mostCarbonEmission = journies.get(0).getCo2EmissionInSpecifiedUnits();
        JourneyModel mostCo2Journey = new JourneyModel();
        for(JourneyModel v: journies){
            if (v.getCreationDate().equals(today)){
                todayJournies ++;
            }
            if (v.getCo2EmissionInSpecifiedUnits() >  mostCarbonEmission && v.getTransportationModel().getTransportaionMode() == TransportationModel.TransportationMode.CAR){
                 mostCarbonEmission = v.getCo2EmissionInSpecifiedUnits();
                 mostCo2Journey = v;
            }

            CO2usage =  CO2usage + v.getCo2EmissionInSpecifiedUnits();
            if (v.getTransportationModel().getTransportaionMode()== TransportationModel.TransportationMode.CAR) {
                carNUm++;
                CO2Car =  CO2Car + v.getCo2EmissionInSpecifiedUnits();
            }
            if (v.getRouteModel().getTotalDistance() <10 && v.getTransportationModel().getTransportaionMode() != TransportationModel.TransportationMode.WALK_BIKE)  {
                shortDistanceTrips ++;
            }
            if (v.getTransportationModel().getTransportaionMode() == TransportationModel.TransportationMode.WALK_BIKE){
                walkNum++;
                distanceWalked += v.getRouteModel().getTotalDistance();

            }
            if ((v.getRouteModel().getHighwayDistance() < v.getRouteModel().getCityDistance()) &&
                    v.getTransportationModel().getTransportaionMode() == TransportationModel.TransportationMode.CAR){
                cityNum ++;
            }
        }
        messageList[0]= "You had "+ carNUm+ " car trips today. You might be able to cut that down by combining trips tomorrow.";
        messageList[1]= "You generate "+ CO2usage  + " of CO2 . Shorter showers might help cut down emissions from hot water heater.";
        messageList[2]= "Overall you had " + CO2usage + " kgs of carbon which " + CO2Car + " of it belongs to your car trips";
        messageList[3]= "You have " + shortDistanceTrips +" trips with total distance of less than 10km you can try walking or biking these routes";
        messageList[4]= "You have Walked/Biked " + distanceWalked +  "kms in total of " + walkNum +" walk/bike trips you had" ;
        messageList[5]= "In " + cityNum + " of your car journeys highway distance was less than city distance" +
                " if it is not necessary try to avoid driving in city and traffics because it produces more CO2 ";
        messageList[6]= "during taking each of your " +  carNUm+
        " car tips try to Drive at an appropriate speed -" +
        " staying within the 70mph limit can bring savings of 10 per cent for your fuel bill compared to driving at 80mph.";
        messageList[7]= "today your co2 emission was " + CO2usage + " CO2 emission per person in Canada" +
                " is on average 14.68 metric tons per day";
        messageList[8]= "Remove unnecessary weight from your " + carNUm+" cars; this will cut down fuel consumption and carbon dioxide emissions";
        if(mostCo2Journey.getTransportationModel() != null) {
            messageList[9] = "your journey " + mostCo2Journey + " uses the most amount of CO2 with the" + mostCo2Journey.getTransportationModel().getName() +
                    ". Try to avoid this journey if it is possible";
        }
        else
        {
            messageList[9] = "";
        }


        messageList[10]="Today you had " + todayJournies + " journeys plan to do a number of errands in one trip rather than several trips and save both time and fuel";
        messageList[11]= "Your number of walking trip is" + walkNum + " and your car trip is " + carNUm +
        "You can try to walk more";
        messageList[12]= "if you are using one of your " + carNUm+ " cars you have created today Minimise fuel wasted in idling by stopping the engine whenever your car is stopped or held up for an extended period of time.";
        messageList[13]= "while driving one of your " + carNUm+ " cars listen to the radio for traffic slowdown warnings";
        messageList[14]= " If you are taking one of your " + carNUm + " trips, don't keep it on idle. Appliances running on standby power consume a great deal of energy, unnecessarily.";
    }
}


