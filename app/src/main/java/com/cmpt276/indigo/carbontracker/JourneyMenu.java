package com.cmpt276.indigo.carbontracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.TipFragment;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.VehicleModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*

 */

public class JourneyMenu extends AppCompatActivity {
    public static final int DATE_SELECT = 52;
    static int messageIndex = 0;
    JourneyModel newJourney;
    public static final int TRANSPORTATION_SELECT = 56;
    public static final int ROUTE_SELECT = 57;
    boolean isRouteSelected, isVehicleSelected;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    List<Integer> journey_positionList;
    public float carbonEmission;

    private boolean isEdit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_menu);
        //Allows for backbutton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        newJourney = new JourneyModel(); // The journey the user is creating
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        transportSelectBtn();
        routeSelectBtn();
        selectCreate();
        gettingDate();
        deleteBtn();
        Intent intent = getIntent();
        if (intent != null && intent.getSerializableExtra("journey")!=null) {
            newJourney = (JourneyModel) intent.getSerializableExtra("journey");
            isEdit = true;
            isVehicleSelected = true;
            isRouteSelected = true;
            fillJourneyTexts();
            fillCarbonFootprintText();
        }
    }



    private void gettingDate() {

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                newJourney.setCreationDate(myCalendar.getTime());
            }

        };
        Button txt = (Button) findViewById(R.id.journey_menu_select_date_btn);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(JourneyMenu.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }


    private void selectCreate() {
        Button btn = (Button) findViewById(R.id.journey_menu_create_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRouteSelected && isVehicleSelected) {
                    if (isEdit) {
                        carbonFootprintInterface.edit(newJourney);
                        Toast.makeText(JourneyMenu.this, "Journey Save!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        carbonFootprintInterface.add(newJourney);
                        Toast.makeText(JourneyMenu.this, "Journey Created!", Toast.LENGTH_SHORT).show();
                        showTipDialog();
                    }

                    //finish();

                }
                else{

                    Toast.makeText(JourneyMenu.this,"Please select Route and Vehicle",Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });



}

    private String getMessageFromArray(){
        String[] messageArray = getResources().getStringArray(R.array.messageArray);
        if (messageIndex == messageArray.length ) messageIndex = 0;
        String message = messageArray[messageIndex];

        return message;
    }

    private void showTipDialog() {
        FragmentManager manager = getSupportFragmentManager();
        TipFragment dialog = new TipFragment();
        dialog.setCancelable(false);
        dialog.show(manager,"message dialog");
        String message = getMessageFromArray();
        dialog.setMessage(message);


    }

    private void fillCarbonFootprintText() {
       newJourney.calculateEmissions();
        carbonEmission = newJourney.getCo2Emission();
        if (isRouteSelected && isVehicleSelected){
            TextView footprintDisplay = (TextView) findViewById(R.id.journey_menu_text_current_footprint);
            footprintDisplay.setText( carbonEmission + " Kg" + "");
        }
    }

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
                Intent intent = new Intent(JourneyMenu.this, TransportationSelectActvitiy.class);
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
                Intent intent = new Intent(JourneyMenu.this, RouteSelectActivity.class);
                startActivityForResult(intent,ROUTE_SELECT);

            }
        });
    }

    private void deleteBtn() {
        Button btn = (Button) findViewById(R.id.journey_menu_delete_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newJourney != null) {
                    carbonFootprintInterface.remove(newJourney);
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

}









