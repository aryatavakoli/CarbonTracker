package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.VehicleModel;

import java.util.ArrayList;
import java.util.List;

public class TransportationSelectActvitiy extends AppCompatActivity {

    private VehicleModel addedVehicle;
    CarbonFootprintComponentCollection carbonFootprintInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_transportation_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Allows for backbutton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        startAddActivity();
        createListView();
        addedVehicle = null;
    }

    // FAB button to launch add activity
    private void startAddActivity() {
        //set reference to fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransportationSelectActvitiy.this,TransportationAddActivity.class);
                // if user has previously added a vehicle, we delete it here before moving to add another one
                if(addedVehicle != null){
                    carbonFootprintInterface.delete(addedVehicle);
                }
                //intent.putExtra("vehicle", vehicle);
                startActivityForResult(intent, 50);
            }
        });
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
                // if selected vehicle is not the same as the one recently added, we need to delete added vehicle
                VehicleModel selectedVehicle = carbonFootprintInterface.getVehicles().get(position);
                if(addedVehicle != null && !addedVehicle.equals(selectedVehicle)){
                    carbonFootprintInterface.delete(addedVehicle);
                }
                intent.putExtra("vehicle", selectedVehicle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void populateVehiclesList() {
        ListView carList = (ListView) findViewById(R.id.transportation_select_list);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        ArrayList<VehicleModel> vehicles = carbonFootprintInterface.getVehicles();
        // putting vehicles in list
        List<String> vehicle_nameList = new ArrayList<>();
        //Add elements
        for(VehicleModel v: vehicles){
            vehicle_nameList.add(v.getName());
        }

        //Create array adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, //context
                android.R.layout.simple_list_item_1,
                vehicle_nameList //arrayList
        );

        //apply adapter ro listview
        carList.setAdapter(arrayAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            addedVehicle = (VehicleModel) data.getSerializableExtra("vehicle");
            populateVehiclesList();
        }
    }

}
