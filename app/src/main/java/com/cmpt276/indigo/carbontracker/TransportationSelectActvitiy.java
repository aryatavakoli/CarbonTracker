package com.cmpt276.indigo.carbontracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponent;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.VehicleModel;

import java.util.ArrayList;
import java.util.List;

/*
    Implements TransportationSelectActvitiy UI for displaying list of added vehicles
 */

public class TransportationSelectActvitiy extends AppCompatActivity {

    private long idOfVehicleEditing = -1;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    private static final int ACTIVITY_RESULT_ADD = 50;
    private static final int ACTIVITY_RESULT_EDIT = 100;
    int selectItem;
    int image = R.drawable.car;
    CustomizedArrayAdapter adapter;
    CustomizedArrayAdapterItem arrayAdapterItems[];

    ArrayList<VehicleModel> vehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_transportation_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Allows for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        startAddActivity();
        createListView();
        setupEditVehicleLongPress();
    }

    // FAB button to launch add activity
    private void startAddActivity() {
        //set reference to fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransportationSelectActvitiy.this,TransportationAddActivity.class);
                startActivityForResult(intent, ACTIVITY_RESULT_ADD);
            }
        });
    }

    private void setupList(){
        Resources res = getResources();
        vehicles = carbonFootprintInterface.getVehicles(this);
        int vehicleSize = vehicles.size();
        arrayAdapterItems = new CustomizedArrayAdapterItem[vehicleSize];
        for (int i = 0; i < vehicleSize; i++){
            arrayAdapterItems[i] = new CustomizedArrayAdapterItem(image, vehicles.get(i).getName(), "", "");
        }
        selectItem = -1;
        adapter = new CustomizedArrayAdapter(this, arrayAdapterItems, getTitles(arrayAdapterItems));
    }

    private String[] getTitles(CustomizedArrayAdapterItem items[]){
        if (items.length == 0){
            return null;
        }
        String[] titles = new String[items.length];
        for (int i = 0; i < items.length; i++){
            titles[i] = items[i].getText1();
        }
        return titles;
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
                Intent intent = getIntent();
                // Passing selected vehicle to the caller activity
                VehicleModel selectedVehicle = vehicles.get(position);
                intent.putExtra("vehicle", selectedVehicle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
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
        for(VehicleModel v: vehicles){
            vehicle_nameList.add(v.getName());
            counter++;
        }

//        //Create array adapter
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
//                this, //context
//                android.R.layout.simple_list_item_1,
//                vehicle_nameList //arrayList
//        );

        //apply adapter ro listview
        carList.setAdapter(adapter);
    }

    private void setupEditVehicleLongPress() {
        ListView list = (ListView) findViewById(R.id.transportation_select_list);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                VehicleModel vehicle = vehicles.get(position);
                idOfVehicleEditing = vehicle.getId();
                Intent intent = TransportationAddActivity.makeIntentForEditVehicle(TransportationSelectActvitiy.this, vehicle);
                startActivityForResult(intent, ACTIVITY_RESULT_EDIT);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case ACTIVITY_RESULT_ADD:
                    populateVehiclesList();
                    break;
                case ACTIVITY_RESULT_EDIT:
                    VehicleModel modifiedVehicle = (VehicleModel) data.getSerializableExtra("vehicle");
                    modifiedVehicle.setId(idOfVehicleEditing);
                    carbonFootprintInterface.edit(this, modifiedVehicle);
                    populateVehiclesList();
                    break;
            }

        }
        else if (resultCode == TransportationAddActivity.RESULT_DELETE){
            populateVehiclesList();
        }

    }

}
