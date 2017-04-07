package com.cmpt276.indigo.carbontracker;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cmpt276.indigo.carbontracker.R;

/**
 * This class handles side navigation events
 */

public class SideNavigationManager {
    public static boolean handleSideNavigationSelection(AppCompatActivity activity, MenuItem item){
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            switchToMainMenu(activity);
        }
        else if (id == R.id.nav_back) {
            switchToPreviousActivity(activity);
        }
        else if (id == R.id.nav_carbon_footprint) {
            switchToCarbonFootprint(activity);
        }
        else if (id == R.id.nav_journey_list) {
            switchToJourneyList(activity);
        }
        else if (id == R.id.nav_route_list) {
            switchToRouteList(activity);
        }
        else if (id == R.id.nav_transportation_list) {
            switchToTransportationList(activity);
        }
        else if (id == R.id.nav_utility_list) {
            switchToUtilityList(activity);
        }
        else if (id == R.id.nav_about) {
            switchToAboutUs(activity);
        }
        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private static void switchToMainMenu(AppCompatActivity activity) {
        Intent intent = MainMenu.makeIntent(activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    private static void switchToPreviousActivity(AppCompatActivity activity) {
        activity.finish();
    }

    private static void switchToCarbonFootprint(AppCompatActivity activity) {
        if(activity instanceof CarbonFootprintMainMenu){
            return;
        }
        activity.startActivity(CarbonFootprintMainMenu.makeIntent(activity));
    }

    private static void switchToJourneyList(AppCompatActivity activity) {
        if(activity instanceof JourneySelectActivity){
            return;
        }
        activity.startActivity(JourneySelectActivity.makeIntent(activity));
    }

    private static void switchToJourneyAdd(AppCompatActivity activity) {
        if(activity instanceof JourneyAddActivity){
            return;
        }
        activity.startActivity(JourneyAddActivity.makeIntentForNewJourney(activity));
    }

    private static void switchToRouteList(AppCompatActivity activity) {
        if(activity instanceof RouteSelectActivity){
            return;
        }
        activity.startActivity(RouteSelectActivity.makeIntent(activity));
    }

    private static void switchToRouteAdd(AppCompatActivity activity) {
        if(activity instanceof RouteAddActivity){
            return;
        }
        activity.startActivity(RouteAddActivity.makeIntentForNewRoute(activity));
    }

    private static void switchToTransportationList(AppCompatActivity activity) {
        if(activity instanceof TransportationSelectActvitiy){
            return;
        }
        activity.startActivity(TransportationSelectActvitiy.makeIntent(activity));
    }

    private static void switchToTransportationAdd(AppCompatActivity activity) {
        if(activity instanceof TransportationAddActivity){
            return;
        }
        activity.startActivity(TransportationAddActivity.makeIntentForNewVehicle(activity));
    }

    private static void switchToUtilityList(AppCompatActivity activity) {
        if(activity instanceof UtilitySelectActivity){
            return;
        }
        activity.startActivity(UtilitySelectActivity.makeIntent(activity));
    }

    private static void switchToUtilityAdd(AppCompatActivity activity) {
        if(activity instanceof UtilityAddActivity){
            return;
        }
        activity.startActivity(UtilityAddActivity.makeIntentForNewUtility(activity));
    }

    private static void switchToAboutUs(AppCompatActivity activity){
        if(activity instanceof AboutActivity){
            return;
        }
        activity.startActivity(AboutActivity.makeIntentForAbout(activity));
    }
}
