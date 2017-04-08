package com.cmpt276.indigo.carbontracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.DuplicateComponentException;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.TipFragment;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.TransportationModel;

import java.util.Calendar;

import java.util.ArrayList;

/*
    implments journey UI
 */

public class JourneyAddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final int RESULT_DELETE = 15;
    ArrayList<JourneyModel> journies;
    ArrayList<TransportationModel> vehicles;
    ArrayList<RouteModel> routes;
    JourneyModel newJourney;
    public static final int TRANSPORTATION_SELECT = 56;
    public static final int ROUTE_SELECT = 57;
    boolean isRouteSelected, isVehicleSelected;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    public double carbonEmission;

    private boolean isEdit = false;// if in edit mode or not
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        newJourney = new JourneyModel(); // The journey the user is creating
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        journies = carbonFootprintInterface.getJournies(this);
        routes = carbonFootprintInterface.getRoutes(this);
        vehicles = carbonFootprintInterface.getVehicles(this);
        populateUIFromIntent();
        transportSelectBtn();
        routeSelectBtn();
        gettingDate();
        setupBottomNavigation();
        MainMenu.setupMargin(this, R.id.content_journey_add);
    }

    private void setupBottomNavigation(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch(item.getItemId())
                {
                    case R.id.action_add:
                        if (isRouteSelected && isVehicleSelected) {
                            // is in edit mode
                            if (isEdit) {
                                Intent intent = getIntent();
                                intent.putExtra("journey", newJourney);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else if (!addJourney(newJourney)) {
                                break;
                            }

                            Intent intent = new Intent();
                            intent.putExtra("journey", newJourney);
                            setResult(Activity.RESULT_OK, intent);
                            Toast.makeText(JourneyAddActivity.this, R.string.journey_created, Toast.LENGTH_SHORT).show();
                            showTipDialog();
                        }
                        else{
                            Toast.makeText(JourneyAddActivity.this, R.string.please_select_route_and_transportation,Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.action_cancel:
                        finish();
                        break;
                    case R.id.action_delete:
                        removeItem();
                        break;
                }
                return true;
            }
        });
        Menu menu = bottomNavigationView.getMenu();
        if(isEdit){
            MenuItem addItem = menu.findItem(R.id.action_add);
            addItem.setTitle(R.string.update_journey);
            addItem.setIcon(R.drawable.ic_update);
        }
        else{
            MenuItem deleteItem = menu.findItem(R.id.action_delete);
            deleteItem.setEnabled(false);
        }
    }

    private void removeItem() {
        if (newJourney != null) {
            // delete the select journey
            carbonFootprintInterface.remove(this, newJourney);
            setResult(RESULT_DELETE);
            // finish this activity and back
            finish();
        }
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
            gettingDate();
        }
    }

    //sets date to one journey including year month and day
    private void gettingDate() {

        Calendar myCalendar = Calendar.getInstance();
        if (isEdit){
            myCalendar =  newJourney.getCreationDate();
        }

        final Calendar finalMyCalendar = myCalendar;
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                finalMyCalendar.set(Calendar.YEAR, year);
                finalMyCalendar.set(Calendar.MONTH, monthOfYear);
                finalMyCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }

        };
        Button txt = (Button) findViewById(R.id.journey_menu_select_date_btn);
//        if the user select the date button a date picker will pop up
        final Calendar finalMyCalendar1 = myCalendar;
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(JourneyAddActivity.this, date, finalMyCalendar1
                        .get(Calendar.YEAR), finalMyCalendar1.get(Calendar.MONTH),
                        finalMyCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        newJourney.setCreationDate(finalMyCalendar);
    }

    boolean addJourney(JourneyModel journeyModel){
        try{
            carbonFootprintInterface.add(this,journeyModel);
        }
        catch(DuplicateComponentException e){
            if(!isEdit) {
                Toast.makeText(JourneyAddActivity.this, R.string.this_journey_already_exists, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

    //display co2emission
    private void fillCarbonFootprintText() {
        TextView footprintDisplay = (TextView) findViewById(R.id.journey_menu_text_current_footprint);
        if (isRouteSelected && isVehicleSelected){
            carbonEmission = newJourney.getCo2EmissionInSpecifiedUnits();
            String value = String.format("%." + 2 + "f", carbonEmission);
            footprintDisplay.setText(value + newJourney.getSpecifiedUnits() + "");
        }
    }

    //display vehicle and route
    private void fillJourneyTexts() {

        if (isVehicleSelected) {
            TextView carDisplay = (TextView) findViewById(R.id.journey_menu_text_current_vehicle);
            carDisplay.setText(newJourney.getTransportationModel().getName() + "");
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TRANSPORTATION_SELECT:
                    TransportationModel vehicle = (TransportationModel) data.getSerializableExtra("vehicle");
                    newJourney.setTransportationModel(vehicle);
                    isVehicleSelected = true;
                    break;
                case ROUTE_SELECT:
                    RouteModel route = (RouteModel) data.getSerializableExtra("route");
                    newJourney.setRouteModel(route);
                    isRouteSelected = true;
            }
            fillJourneyTexts();
            fillCarbonFootprintText();
            gettingDate();
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
    private void showTipDialog() { //open a dialog and pass the arraylist to the tipfragment to use them
        FragmentManager manager = getSupportFragmentManager();
        TipFragment dialog = new TipFragment();
        dialog.setCancelable(false);
        dialog.show(manager,"message dialog");
        dialog.setVehicles(vehicles);
        dialog.setJournies(journies);
        dialog.setRoutes(routes);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return SideNavigationManager.handleSideNavigationSelection(this, item);
    }

}

