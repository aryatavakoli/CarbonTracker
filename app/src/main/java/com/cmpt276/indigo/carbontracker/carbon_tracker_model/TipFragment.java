package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.cmpt276.indigo.carbontracker.R;

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
        builder.setTitle(R.string.new_tip);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getActivity().finish();

            }

        });
        builder.setPositiveButton(R.string.next_tip, new DialogInterface.OnClickListener() {
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
        messageList[0]= getString(R.string.you_had)+ carNUm+ getString(R.string.car_trips_today_you_might_be_able_to_cut);
        messageList[1]= getString(R.string.you_generate)+ CO2usage  + getString(R.string.of_co2_shorter_shower_might_help);
        messageList[2]= getString(R.string.overall_you_had) + CO2usage + getString(R.string.kgs_of_carbon_which) + CO2Car + getString(R.string.of_it_belongs_to_your_car_trips);
        messageList[3]= getString(R.string.you_have) + shortDistanceTrips +getString(R.string.trips_with_total_distance_of_less_than);
        messageList[4]= getString(R.string.you_have_walked_biked) + distanceWalked +  getString(R.string.kms_in_total_of) + walkNum +getString(R.string.walk_or_bike_trips_you_had) ;
        messageList[5]= getString(R.string.in_) + cityNum + getString(R.string.of_your_car_journeys_highway_diatance_was_less_than_city) +
                getString(R.string.if_it_is_not_necessary_try_to_avoid);
        messageList[6]= getString(R.string.during_taking_each_of_your) +  carNUm+
        getString(R.string.car_tips_try_to_drive_at_an_appropriate_speed) +
        getString(R.string.staying_within_the_70mph_limit_can_bring_saving);
        messageList[7]= getString(R.string.today_your_co2_emission_was) + CO2usage + getString(R.string.co2_emission_per_person_in_canada) +
                getString(R.string.is_on_average_);
        messageList[8]= getString(R.string.remove_unnecessary_weight_from_your) + carNUm+getString(R.string.cars_this_will_cut_down_fuel_consumption);
        if(mostCo2Journey.getTransportationModel() != null) {
            messageList[9] = getString(R.string.your_journey_) + mostCo2Journey + getString(R.string.uses_the_most_amount_of_co2_with_the) + mostCo2Journey.getTransportationModel().getName() +
                    getString(R.string.try_to_avoid_this_journey_if);
        }
        else
        {
            messageList[9] = "";
        }


        messageList[10]=getString(R.string.today_you_had_) + todayJournies + getString(R.string.journeys_plan_to_do_a_number_of_errands);
        messageList[11]= getString(R.string.your_number_of_walking_trip_is) + walkNum + getString(R.string.and_your_car_trip_is) + carNUm +
        getString(R.string.you_can_try_to_walk_more);
        messageList[12]= getString(R.string.if_you_are_using_one_of_your) + carNUm+ getString(R.string.cars_you_have_created_today_minimise);
        messageList[13]= getString(R.string.while_driving_one_of_your) + carNUm+ getString(R.string.cars_listen_to_the_radio_for_traffic_slowdown);
        messageList[14]= getString(R.string.if_you_are_taking_one_of_your) + carNUm + getString(R.string.trips_dont_keep_it_on_idle);
    }
}


