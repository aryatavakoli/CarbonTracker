package com.cmpt276.indigo.carbontracker;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.TransportationModel;

import java.util.ArrayList;
import java.util.List;

/*
    Implements TransportationSelectActvitiy UI for displaying list of added vehicles
 */

public class TransportationSelectActvitiy extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private long idOfVehicleEditing = -1;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    private static final int ACTIVITY_RESULT_ADD = 50;
    private static final int ACTIVITY_RESULT_EDIT = 100;
    private int selectedItemIndex;
    CustomizedArrayAdapter adapter;
    CustomizedArrayAdapterItem arrayAdapterItems[];

    ArrayList<TransportationModel> vehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedItemIndex = -1;
        setContentView(R.layout.activity_transportation_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupBottomNavigation();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        createListView();
        MainMenu.setupMargin(this, R.id.content_transportation_select);
    }

    private void setupList(){
        vehicles = carbonFootprintInterface.getVehicles(this);
        int vehicleSize = vehicles.size();
        arrayAdapterItems = new CustomizedArrayAdapterItem[vehicleSize];
        for (int i = 0; i < vehicleSize; i++){
            int imageID = TransportationImageSelect.getImageResource(vehicles.get(i).getImageName());
            if(imageID == -1){
                imageID = R.drawable.car;
            }
            arrayAdapterItems[i] = new CustomizedArrayAdapterItem(imageID, vehicles.get(i).getName(), "", "");
        }
        adapter = new CustomizedArrayAdapter(this, arrayAdapterItems, getTitles(arrayAdapterItems));
    }

    private String[] getTitles(CustomizedArrayAdapterItem items[]){
        if (items.length == 0){
            return new String[0];
        }
        String[] titles = new String[items.length];
        for (int i = 0; i < items.length; i++){
            titles[i] = items[i].getText1();
        }
        return titles;
    }

    private void setupBottomNavigation(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                Intent intent = new Intent(TransportationSelectActvitiy.this, TransportationAddActivity.class);
                switch(item.getItemId())
                {
                    case R.id.action_add:
                        startActivityForResult(intent, ACTIVITY_RESULT_ADD);
                        break;
                    case R.id.action_edit:
                        editItem();
                        break;
                    case R.id.action_select:
                        onSelectTransportation();
                        break;
                }
                return true;
            }
        });
        setBottomNavigationItemsStatus();
    }

    private void editItem(){
        if(selectedItemIndex > -1) {
            TransportationModel vehicle = vehicles.get(selectedItemIndex);
            idOfVehicleEditing = vehicle.getId();
            Intent intent = TransportationAddActivity.makeIntentForEditVehicle(TransportationSelectActvitiy.this, vehicle);
            startActivityForResult(intent, ACTIVITY_RESULT_EDIT); //open the edit activity
        }
    }

    private void onSelectTransportation(){
        Intent intent = getIntent();
        // Passing selected vehicle to the caller activity
        TransportationModel selectedVehicle = vehicles.get(selectedItemIndex);
        intent.putExtra("vehicle", selectedVehicle);
        setResult(RESULT_OK, intent);
        finish();
    }

    //sample for demonstartion purposes
    private void createListView() {
        //set reference to listview
        ListView carList = (ListView) findViewById(R.id.transportation_select_list);
        populateVehiclesList();
        //handle click for each element in listview
        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItemIndex = position;
                adapter.setSelected(position);
                adapter.notifyDataSetChanged();
                setBottomNavigationItemsStatus();
            }
        });
    }

    private void setBottomNavigationItemsStatus() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem edit = menu.findItem(R.id.action_edit);
        MenuItem select = menu.findItem(R.id.action_select);
        if(selectedItemIndex < 0){
            edit.setEnabled(false);
            select.setEnabled(false);
        }
        else{
            edit.setEnabled(true);
            select.setEnabled(true);
        }
    }

    private void populateVehiclesList() {
        setupList();
        ListView carList = (ListView) findViewById(R.id.transportation_select_list);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        vehicles = carbonFootprintInterface.getVehicles(this);
        // putting vehicles in list
        List<String> vehicle_nameList = new ArrayList<>();
        //Add elements
        int counter = 0;
        for(TransportationModel v: vehicles){
            vehicle_nameList.add(v.getName());
            counter++;
        }
        adapter.setSelected(selectedItemIndex);
        //apply adapter ro listview
        carList.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case ACTIVITY_RESULT_ADD:
                    populateVehiclesList();
                    break;
                case ACTIVITY_RESULT_EDIT:
                    TransportationModel modifiedVehicle = (TransportationModel) data.getSerializableExtra("vehicle");
                    modifiedVehicle.setId(idOfVehicleEditing);
                    carbonFootprintInterface.edit(this, modifiedVehicle);
                    populateVehiclesList();
                    break;
            }

        }
        else if (resultCode == TransportationAddActivity.RESULT_DELETE){
            selectedItemIndex = -1;
            populateVehiclesList();
        }
        setBottomNavigationItemsStatus();
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

    public static Intent makeIntent(Context packageContext) {
        return new Intent(packageContext, TransportationSelectActvitiy.class);
    }
}
