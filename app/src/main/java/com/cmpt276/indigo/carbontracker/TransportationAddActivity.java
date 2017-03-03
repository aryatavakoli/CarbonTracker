package com.cmpt276.indigo.carbontracker;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.*;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.VehicleModel;

import java.util.ArrayList;
import java.util.List;

public class TransportationAddActivity extends AppCompatActivity {

    CarbonFootprintComponentCollection carbonFootprintInterface;
    private boolean editing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_add);
        //Allows for backbutton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        setupOkButton();
        setupDropdownList();
        populateUIFromIntent();
    }

    private void populateUIFromIntent() {
        Intent intent = getIntent();
        VehicleModel vehicle = (VehicleModel) intent.getSerializableExtra("vehicle");
        if (vehicle != null){
            editing = true;
            EditText editName = (EditText) findViewById(R.id.add_transport_editText_nickname);
            editName.setText(vehicle.getName());

            setSpinnerSelection(R.id.add_transport_dropdown_make, carbonFootprintInterface.getVehicleMakes(), vehicle.getMake());
            setSpinnerSelection(R.id.add_transport_dropdown_model, carbonFootprintInterface.getVehicleModel(), vehicle.getModel());
            setSpinnerSelection(R.id.add_transport_dropdown_year, carbonFootprintInterface.getVehicleYear(), vehicle.getYear());
        }
    }

    private void setSpinnerSelection(int resourceId, ArrayList<String> items, String item){
        Spinner makeSpinner = (Spinner) findViewById(resourceId);
        int makePosition = items.indexOf(item);
        makeSpinner.setSelection(makePosition);
    }

    private void setupOkButton() {
        Button btnOK = (Button) findViewById(R.id.add_transport_ok_btn);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Try to get data from transportation add UI
                EditText nickName = (EditText) findViewById(R.id.add_transport_editText_nickname);
                String name = nickName.getText().toString();

                if (name.length() == 0) {
                    Toast.makeText(TransportationAddActivity.this, "Please enter a vehicle name.", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                Spinner vehicleMake = (Spinner) findViewById(R.id.add_transport_dropdown_make);
                String make = vehicleMake.getSelectedItem().toString();
                if (make == null){
                    Toast.makeText(TransportationAddActivity.this, "Vehicle make should be selected", Toast.LENGTH_SHORT).show();
                }

                Spinner vehicleModel = (Spinner) findViewById(R.id.add_transport_dropdown_model);
                String model = vehicleModel.getSelectedItem().toString();
                if (make == null){
                    Toast.makeText(TransportationAddActivity.this, "Vehicle model should be selected", Toast.LENGTH_SHORT).show();
                }

                Spinner vehicleYear = (Spinner) findViewById(R.id.add_transport_dropdown_year);
                String year = vehicleYear.getSelectedItem().toString();
                if (make == null){
                    Toast.makeText(TransportationAddActivity.this, "Vehicle year should be selected", Toast.LENGTH_SHORT).show();
                }

                //Creating vehicle object to pass it to vehicle activity to be added to the list.
                VehicleModel vehicle = new VehicleModel(name, make, model, year);
                //adding and replacing vehicle when a user is editing
                if(editing){
                    Intent intent = getIntent();
                    //Passing the vehicle object to the TransportationActivity
                    intent.putExtra("vehicle", vehicle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                //adding vehicle to collection if it is not duplicate and user is not editing
                else if(!addVehicle(vehicle)){
                    return;
                }
                Intent intent = getIntent();
                //Passing the vehicle object to the TransportationActivity
                intent.putExtra("vehicle", vehicle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public static Intent makeIntentForNewVehicle(Context packageContext) {
        return new Intent(packageContext, TransportationAddActivity.class);
    }

    public static Intent makeIntentForEditVehicle(Context packageContext, VehicleModel vehicle) {
        Intent intent = makeIntentForNewVehicle(packageContext);
        intent.putExtra("vehicle", vehicle);
        return intent;
    }

    boolean addVehicle(VehicleModel vehicle){
        try{
            carbonFootprintInterface.add(vehicle);
        }
        catch(DuplicateComponentException e){
            if(!editing) {
                Toast.makeText(TransportationAddActivity.this, "This vehicle already exist.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

    //Set all the values for dropdown lists
    private void setupDropdownList() {
        setupMakeDropdownList();
        setupModelDropdownList();
        setupYearDropdownList();
    }

    private void setupYearDropdownList() {
        ArrayList<String> years = carbonFootprintInterface.getVehicleYear();
        Spinner yearSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_year);
        fillSpinner(yearSpinner, R.id.add_transport_dropdown_year, years);
    }

    private void setupModelDropdownList() {
        ArrayList<String> models = carbonFootprintInterface.getVehicleModel();
        Spinner modelSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_model);
        fillSpinner(modelSpinner, R.id.add_transport_dropdown_model, models);
    }

    private void setupMakeDropdownList() {
        ArrayList<String> makes = carbonFootprintInterface.getVehicleMakes();
        Spinner makeSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_make);
        fillSpinner(makeSpinner, R.id.add_transport_dropdown_make, makes);
    }

    private void fillSpinner(Spinner spinner, int resourceID, ArrayList<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
    }

}
