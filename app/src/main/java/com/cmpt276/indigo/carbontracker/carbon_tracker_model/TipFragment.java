package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cmpt276.indigo.carbontracker.JourneyMenu;
import com.cmpt276.indigo.carbontracker.R;
import com.cmpt276.indigo.carbontracker.TransportationSelectActvitiy;

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

        messageList[0]= "You had"+ vehicles.size()+ "car trips in total. You might be able to cut that down by combining trips tomorrow.";
        float CO2usage=0;
        int gasolineNumber=0;
        for(JourneyModel v: journies){
            if (v.getVehicleModel().getEngineDisplacment() == "")
//            v.getRouteModel().getHighwayDistance()
            if (v.getCreationDate() == Calendar.getInstance().getTime()){
                CO2usage = CO2usage + v.getCo2Emission();
            }

        }
        messageList[1]= "You generate "+ CO2usage  + " of CO2 today from natural gas use. Shorter showers might help cut down emissions from hot water heater.";
        messageList[2]= "this is my 3rd";
        messageList[3]= "what is love baby dont hurt me";
        messageList[4]= "baby are u down down";
        messageList[5]= "baby dont worry u ar my only";
        messageList[6]= "lonely I have nobody";
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


