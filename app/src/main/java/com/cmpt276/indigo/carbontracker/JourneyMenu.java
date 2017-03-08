package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.DuplicateComponentException;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.VehicleModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.sql.Types.NULL;

public class JourneyMenu extends AppCompatActivity {
    JourneyModel newJourney;
    public static final int TRANSPORTATION_SELECT = 56;
    public static final int ROUTE_SELECT = 57;
    boolean isRouteSelected, isVehicleSelected;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    List<Integer> journey_positionList;
    public float carbonEmission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_menu);
        //Allows for backbutton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        newJourney = new JourneyModel(); // The journey the user is creating
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        transportSelectbtn();
        routeSelectbtn();
        carbonFootprintSelectbtn();
    }

    private void fillCarbonfootprintText() {
       newJourney.calculateEmissions();
        carbonEmission = newJourney.getCo2Emission();
        if (isRouteSelected && isVehicleSelected){
            TextView footprintDisplay = (TextView) findViewById(R.id.journey_menu_text_current_footprint);
            footprintDisplay.setText( carbonEmission + " Kg" + "");
        }
    }

    private void fillJourneyTexts() {

        if (isVehicleSelected) {
            TextView CarDisplay = (TextView) findViewById(R.id.journey_menu_text_current_vehicle);
            CarDisplay.setText(newJourney.getVehicleModel().getName() + "");
        }
        if (isRouteSelected) {
            TextView RouteDisplay = (TextView) findViewById(R.id.journey_menu_text_current_route);
            RouteDisplay.setText(newJourney.getRouteModel().getName() + "");
        }



    }

    //Launch Transport select activity
    private void transportSelectbtn() {
        Button btn = (Button) findViewById(R.id.journey_menu_select_transport_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JourneyMenu.this, TransportationSelectActvitiy.class);
                startActivityForResult(intent, TRANSPORTATION_SELECT );

            }
        });
    }

    //Go to carbon footprint activity
    private void carbonFootprintSelectbtn() {
        Button btn = (Button) findViewById(R.id.journey_menu_carbonfootprint_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JourneyMenu.this, CarbonFootprintMenu.class);
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
                startActivityForResult(intent,ROUTE_SELECT);

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TRANSPORTATION_SELECT:
                    VehicleModel Vehicle = (VehicleModel) data.getSerializableExtra("vehicle");
                    newJourney.setVehicleModel(Vehicle);
                    isVehicleSelected = true;
                    break;
                case ROUTE_SELECT:
                    RouteModel Route = (RouteModel) data.getSerializableExtra("route");
                    newJourney.setRouteModel(Route);
                    isRouteSelected = true;
            }
            fillJourneyTexts();
            fillCarbonfootprintText();

        }
        if (isRouteSelected && isVehicleSelected) {
            carbonFootprintInterface.add(newJourney);


        }

    }

}







