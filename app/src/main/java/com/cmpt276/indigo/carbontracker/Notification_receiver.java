package com.cmpt276.indigo.carbontracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

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
    Calendar today = Calendar.getInstance();
    @Override
    public void onReceive(Context context, Intent intent) {
        utilityAddedRecently = false;
        last45Days.add(Calendar.DAY_OF_MONTH, -OneMonthAndHalf);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        journeys = carbonFootprintInterface.getJournies(context);
        utilities = carbonFootprintInterface.getUtilities(context);
        Log.i("utility siiiiiiiize",utilities.size() + "");
        for (UtilityModel utility : utilities){
            Log.i("utility staaaaaaaaaart",utility.getStartDateString());
            Log.i("utility eeeeeeeeeeend",utility.getEndDateString());
            Log.i("4555555555555555",last45Days.getTime() + "");
            if (utility.getStartDate().after(last45Days.getTime()) && utility.getEndDate().before(today.getTime())){
                utilityAddedRecently = true;
            }
        }


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                context.NOTIFICATION_SERVICE);
        Intent repeating_intent;
        if(utilityAddedRecently){
        repeating_intent = new Intent(context,JourneyAddActivity.class);}
        else{
            repeating_intent = new Intent(context,UtilityAddActivity.class);
        }
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
                .setContentTitle(context.getString(R.string.carbon_noti))
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentInfo("Info");
        if (utilityAddedRecently == false){
            Calendar today = Calendar.getInstance();
            int journeysMadeToday = 0;
            Log.i("jooooourneyyyyy size",journeys.size() + "");
            for (JourneyModel journey : journeys){
                Log.i("journey daaaaaaate",journey.getCreationDateString());
                if (isSameDay( journey.getCreationDate(), today)){
                    journeysMadeToday ++;

                }
           }

            builder.setContentText(context.getString(R.string.you_entered_noti) + journeysMadeToday+ context.getString(R.string.journeys_today_want_noti));
        }
        else{
            builder.setContentText(
                    context.getString(R.string.you_have_not_entered_noti) +
                            context.getString(R.string.want_to_enter_noti));
        }
        notificationManager.notify(REQUEST_CODE,builder.build());



    }


    public boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null)
            return false;
        return (//cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                        && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
}
