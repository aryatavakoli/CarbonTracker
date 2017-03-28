package com.cmpt276.indigo.carbontracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.UtilityModel;

import java.util.ArrayList;
import java.util.Calendar;

import static com.cmpt276.indigo.carbontracker.MainMenu.REQUEST_CODE;


/**
 * Created by parmis on 2017-03-27.
 */

class Notification_receiver extends BroadcastReceiver{


    public static final int OneMonthAndHalf = 45;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    ArrayList<JourneyModel> journeys;
    ArrayList<UtilityModel> utilities;
    boolean utilityAddedRecently;
    Calendar last45Days = Calendar.getInstance();
    @Override
    public void onReceive(Context context, Intent intent) {
        utilityAddedRecently = false;
        last45Days.add(Calendar.DAY_OF_MONTH, -OneMonthAndHalf);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        journeys = carbonFootprintInterface.getJournies(context);
        utilities = carbonFootprintInterface.getUtilities(context);
        for (UtilityModel utility : utilities){
            if (utility.getStartDate().after(last45Days.getTime() )){
                utilityAddedRecently = true;
            }
        }


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                context.NOTIFICATION_SERVICE);
        Intent repeating_intent = new Intent(context,JourneyAddActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                REQUEST_CODE,
                repeating_intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.car)
//                .setTicker("Hearty365")
                .setContentTitle("Carbon Footprint")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentInfo("Info");
        if (utilityAddedRecently){
            Calendar today = Calendar.getInstance();
            int journeysMadeToday =0;
            for (JourneyModel journey : journeys){
                if (journey.getCreationDate().equals(today)){
                    journeysMadeToday ++;

                }
            }

            builder.setContentText("You entered " + journeysMadeToday+ " journeys today; want to enter more?");
        }
        else{
            builder.setContentText(
                    "You have not entered {hydro, natural gas} in over a month and a half;" +
                            " want to enter one now?");
        }
        notificationManager.notify(REQUEST_CODE,builder.build());



    }
}
