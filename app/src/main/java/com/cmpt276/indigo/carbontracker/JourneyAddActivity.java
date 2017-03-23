package com.cmpt276.indigo.carbontracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.DuplicateComponentException;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.UtilityModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.VehicleModel;

import java.util.Calendar;

import java.util.ArrayList;
import java.util.List;

/*
    implments journey UI
 */

public class JourneyAddActivity extends AppCompatActivity {
    public static final int RESULT_DELETE = 15;
    ArrayList<JourneyModel> journies;
    public static final int DATE_SELECT = 52;
    JourneyModel newJourney;
    public static final int TRANSPORTATION_SELECT = 56;
    public static final int ROUTE_SELECT = 57;
    boolean isRouteSelected, isVehicleSelected;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    List<Integer> journey_positionList;
    public double carbonEmission;

    private boolean isEdit = false;// if in edit mode or not
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_menu);
        //Allows for backbutton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        newJourney = new JourneyModel(); // The journey the user is creating
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        journies = carbonFootprintInterface.getJournies(this);
        populateUIFromIntent();
        transportSelectBtn();
        routeSelectBtn();
        selectCreate();
        gettingDate();
        deleteBtn();
    }

    private void populateUIFromIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.getSerializableExtra("journey") != null) {
            // if intent.getSerializableExtra("journey") != null when first in
            // it mean in edit mode
            newJourney = (JourneyModel) intent.getSerializableExtra("journey");
            isEdit = true;//set the isEdit is true
            isVehicleSelected = true;
            isRouteSelected = true;
            // update the view
            fillJourneyTexts();
            fillCarbonFootprintText();
        }
    }

    //sets date to one journey including year month and day
    private void gettingDate() {

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                newJourney.setCreationDate(myCalendar);
            }

        };
        Button txt = (Button) findViewById(R.id.journey_menu_select_date_btn);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(JourneyAddActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    /**
     * create or edit
     */
    private void selectCreate() {
        Button btn = (Button) findViewById(R.id.journey_menu_create_btn);
        final Context context = this;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRouteSelected && isVehicleSelected) {
                    // is in edit mode
                    if (isEdit) {
                        Intent intent = getIntent();
                        intent.putExtra("journey", newJourney);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else if (!addJourney(newJourney)) {
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("journey", newJourney);
                    setResult(Activity.RESULT_OK, intent);
                    Toast.makeText(JourneyAddActivity.this, "Journey Created!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(JourneyAddActivity.this,"Please select Route and Vehicle",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    boolean addJourney(JourneyModel journeyModel){
        try{
            carbonFootprintInterface.add(this,journeyModel);
        }
        catch(DuplicateComponentException e){
            if(!isEdit) {
                Toast.makeText(JourneyAddActivity.this, "This Journey already exists.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

    //display co2emission
    private void fillCarbonFootprintText() {
       newJourney.calculateEmissions();
        carbonEmission = newJourney.getCo2Emission();
        if (isRouteSelected && isVehicleSelected){
            TextView footprintDisplay = (TextView) findViewById(R.id.journey_menu_text_current_footprint);
            String value = String.format("%." + 2 + "f", carbonEmission);
            footprintDisplay.setText(value + " Kg" + "");
        }
    }

    //display vehicle and route
    private void fillJourneyTexts() {

        if (isVehicleSelected) {
            TextView carDisplay = (TextView) findViewById(R.id.journey_menu_text_current_vehicle);
            carDisplay.setText(newJourney.getVehicleModel().getName() + "");
        }
        if (isRouteSelected) {
            TextView routeDisplay = (TextView) findViewById(R.id.journey_menu_text_current_route);
            routeDisplay.setText(newJourney.getRouteModel().getName() + "");
        }
    }

    //Launch Transport select activity
    private void transportSelectBtn() {
        Button btn = (Button) findViewById(R.id.journey_menu_select_transport_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JourneyAddActivity.this, TransportationSelectActvitiy.class);
                startActivityForResult(intent, TRANSPORTATION_SELECT );

            }
        });
    }

    //Go to go to route selection activity
    private void routeSelectBtn() {
        Button btn = (Button) findViewById(R.id.journey_menu_select_route_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JourneyAddActivity.this, RouteSelectActivity.class);
                startActivityForResult(intent,ROUTE_SELECT);

            }
        });
    }

    /**
     * delete the journey
     */
    private void deleteBtn() {
        Button btn = (Button) findViewById(R.id.journey_menu_delete_btn);
        if(!isEdit){
            btn.setEnabled(false);
        }
        final Context context = this;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newJourney != null) {
                    // delete the select journey
                    carbonFootprintInterface.remove(context, newJourney);
                    setResult(RESULT_DELETE);
                    // finish this activity and back
                    finish();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TRANSPORTATION_SELECT:
                    VehicleModel vehicle = (VehicleModel) data.getSerializableExtra("vehicle");
                    newJourney.setVehicleModel(vehicle);
                    isVehicleSelected = true;
                    break;
                case ROUTE_SELECT:
                    RouteModel route = (RouteModel) data.getSerializableExtra("route");
                    newJourney.setRouteModel(route);
                    isRouteSelected = true;
            }
            fillJourneyTexts();
            fillCarbonFootprintText();
        }
    }

    public static Intent makeIntentForNewJourney(Context packageContext) {
        return new Intent(packageContext, JourneyAddActivity.class);
    }

    public static Intent makeIntentForEditJourney(Context packageContext, JourneyModel journeyModel) {
        Intent intent = makeIntentForNewJourney(packageContext);
        intent.putExtra("journey", journeyModel);
        return intent;
    }
}








