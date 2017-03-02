package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.VehicleModel;

public class JourneyMenu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_menu);
        //Allows for backbutton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        transportSelectbtn();
        routeSelectbtn();
    }


    //Launch Transport select activity
    private void transportSelectbtn() {
        Button btn = (Button) findViewById(R.id.journey_menu_select_transport_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JourneyMenu.this, TransportationSelectActvitiy.class);
                startActivity(intent);

            }
        });


    }

    //Go to go to route selection activity
    private void routeSelectbtn() {
        Button btn = (Button) findViewById(R.id.journey_menu_select_route_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JourneyMenu.this, RouteSelectActivity.class);
                startActivity(intent);

            }
        });


    }
}
