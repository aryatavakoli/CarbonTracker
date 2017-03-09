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
import android.widget.Spinner;
import android.widget.Toast;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.VehicleModel;

import java.util.ArrayList;

/*

 */

public class TransportationAddActivity extends AppCompatActivity {

    public static final int RESULT_DELETE = 200;
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
        setupDeleteButton();
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

            String make = vehicle.getMake();
            String model = vehicle.getModel();
            String year =  vehicle.getYear();
            String transmission = vehicle.getTransmisson();

            setSpinnerSelection(R.id.add_transport_dropdown_make, carbonFootprintInterface.getVehicleMakes(), make);
            setSpinnerSelection(R.id.add_transport_dropdown_model, carbonFootprintInterface.getVehicleModel(make), model);
            setSpinnerSelection(R.id.add_transport_dropdown_year, carbonFootprintInterface.getVehicleYear(make, model), vehicle.getYear());
            setSpinnerSelection(R.id.add_transport_dropdown_transmission, carbonFootprintInterface.getVehicleTransmission(make,model,year), vehicle.getTransmisson());
            setSpinnerSelection(R.id.add_transport_dropdown_engine_displacement, carbonFootprintInterface.getVehicleEngineDisplacement(make,model,year,transmission),vehicle.getEngineDisplacment());
        }
    }

    private void setSpinnerSelection(int resourceId, ArrayList<String> items, String item){
        Spinner makeSpinner = (Spinner) findViewById(resourceId);
        int positionOfMake = items.indexOf(item);
        makeSpinner.setSelection(positionOfMake);
    }

    private void setupOkButton() {
        Button btnOK = (Button) findViewById(R.id.add_transport_ok_btn);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Try to get data from transportation add UI and create a vehicle object
                VehicleModel vehicle = createVehicleObject();
                if (vehicle == null){
                    return;
                }
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
                else{
                    Toast.makeText(TransportationAddActivity.this, "Vehicle Added!", Toast.LENGTH_SHORT).show();
                }
                Intent intent = getIntent();
                //Passing the vehicle object to the TransportationActivity
                intent.putExtra("vehicle", vehicle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setupDeleteButton(){
        Button btnDelete = (Button) findViewById(R.id.add_transport_delete_btn);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Try to get data from transportation add UI
                VehicleModel vehicle = createVehicleObject();
                if(!editing){
                    if (vehicle == null){
                        return;
                    }
                    Toast.makeText(TransportationAddActivity.this, "This vehicle does not exit.", Toast.LENGTH_SHORT)
                            .show();
                }
                else{
                    //Removing vehicle from collection if it is on the list
                    removeVehicle(vehicle);
                    setResult(RESULT_DELETE);
                    Toast.makeText(TransportationAddActivity.this, "Vehicle Deleted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private VehicleModel createVehicleObject(){
        //Try to get data from transportation add UI
        EditText nickNameEditText = (EditText) findViewById(R.id.add_transport_editText_nickname);
        String name = nickNameEditText.getText().toString();

        if (name.length() == 0) {
            Toast.makeText(TransportationAddActivity.this, "Please enter a vehicle name.", Toast.LENGTH_SHORT)
                    .show();
            return null;
        }

        Spinner vehicleMakeSpinner = (Spinner) findViewById(R.id.add_transport_dropdown_make);
        String make = vehicleMakeSpinner.getSelectedItem().toString();
        if (make == null){
            Toast.makeText(TransportationAddActivity.this, "Vehicle make should be selected", Toast.LENGTH_SHORT).show();
        }

        Spinner vehicleModelSpinner = (Spinner) findViewById(R.id.add_transport_dropdown_model);
        String model = vehicleModelSpinner.getSelectedItem().toString();
        if (model == null){
            Toast.makeText(TransportationAddActivity.this, "Vehicle model should be selected", Toast.LENGTH_SHORT).show();
        }

        Spinner vehicleYearSpinner = (Spinner) findViewById(R.id.add_transport_dropdown_year);
        String year = vehicleYearSpinner.getSelectedItem().toString();
        if (year == null){
            Toast.makeText(TransportationAddActivity.this, "Vehicle year should be selected", Toast.LENGTH_SHORT).show();
        }

        Spinner vehicleTransmissionSpinner = (Spinner) findViewById(R.id.add_transport_dropdown_transmission);
        String transmission = vehicleTransmissionSpinner.getSelectedItem().toString();
        if (transmission == null){
            Toast.makeText(TransportationAddActivity.this, "Vehicle transmission should be selected", Toast.LENGTH_SHORT).show();
        }

        Spinner vehicleEngineDisplacementSpinner = (Spinner) findViewById(R.id.add_transport_dropdown_engine_displacement);
        String engineDisplacement = (String) vehicleEngineDisplacementSpinner.getSelectedItem();
        if (engineDisplacement == null){
            Toast.makeText(TransportationAddActivity.this, "Engine Displacement should be selected", Toast.LENGTH_SHORT).show();
        }

        //Creating vehicle object to pass it to vehicle activity to be added to the list.
        VehicleModel vehicle = new VehicleModel(name, make, model, year, transmission , engineDisplacement);

        // setting fuel efficiency data
        carbonFootprintInterface.populateCarFuelData(vehicle);

        return vehicle;
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
    //remove(hide) vehicle from the list
    void removeVehicle(VehicleModel vehicle){
        carbonFootprintInterface.remove(vehicle);
    }

    //Set all the values for dropdown lists
    private void setupDropdownList() {
        setupMakeDropdownList();
    }

    private void setupMakeDropdownList() {
        ArrayList<String> makes = carbonFootprintInterface.getVehicleMakes();
        final Spinner makeSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_make);
        fillSpinner(makeSpinner, makes);
        makeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMake = makeSpinner.getSelectedItem().toString();
                setupModelDropdownList(selectedMake);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupModelDropdownList(final String selectedMake) {
        ArrayList<String> models = carbonFootprintInterface.getVehicleModel(selectedMake);
        final Spinner modelSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_model);
        fillSpinner(modelSpinner, models);
        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedModel = modelSpinner.getSelectedItem().toString();
                setupYearDropdownList(selectedMake, selectedModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupYearDropdownList(final String selectedMake, final String selectedModel) {
        ArrayList<String> years = carbonFootprintInterface.getVehicleYear(selectedMake, selectedModel);
        final Spinner yearSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_year);
        fillSpinner(yearSpinner, years);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = yearSpinner.getSelectedItem().toString();
                setupTransmissionDropdownList(selectedMake, selectedModel, selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupTransmissionDropdownList(final String selectedMake, final String selectedModel, final String selectedYear) {
        ArrayList<String> transmissions = carbonFootprintInterface.getVehicleTransmission(selectedMake, selectedModel, selectedYear);
        final Spinner transmissionSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_transmission);
        fillSpinner(transmissionSpinner, transmissions);
        transmissionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTransmission = transmissionSpinner.getSelectedItem().toString();
                setupEngineDisplacmentDropdownList(selectedMake, selectedModel, selectedYear, selectedTransmission);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupEngineDisplacmentDropdownList(String selectedMake, String selectedModel, String selectedYear, String selectedTransmission) {
        ArrayList<String> engineDisplacement = carbonFootprintInterface.getVehicleEngineDisplacement(selectedMake, selectedModel, selectedYear, selectedTransmission);
        final Spinner transmissionSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_engine_displacement);
        fillSpinner(transmissionSpinner, engineDisplacement);
    }

    private void fillSpinner(Spinner spinner, ArrayList<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
    }

}
