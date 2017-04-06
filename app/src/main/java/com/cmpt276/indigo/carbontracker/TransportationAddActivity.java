package com.cmpt276.indigo.carbontracker;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.*;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.TransportationModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

/*
    Implements TransportationAddActivity UI for adding a new vehicle
 */

public class TransportationAddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final int RESULT_DELETE = 200;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    private boolean editing = false;
    TransportationModel currentVehicle;
    TransportationModel.TransportationMode transportationModes[];
    private String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setupDropdownList();
        populateUIFromIntent();
        setupBottomNavigation();
        imageName = "";
    }

    private void setupBottomNavigation(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch(item.getItemId())
                {
                    case R.id.action_add:
                        onAddClick();
                        break;
                    case R.id.action_cancel:
                        finish();
                        break;
                    case R.id.action_delete:
                        onClickDelete();
                        break;
                }
                return true;
            }
        });
        Menu menu = bottomNavigationView.getMenu();
        if(editing){
            MenuItem addItem = menu.findItem(R.id.action_add);
            addItem.setTitle(R.string.update_transportation);
            addItem.setIcon(R.drawable.ic_update);
        }
        else{
            MenuItem deleteItem = menu.findItem(R.id.action_delete);
            deleteItem.setEnabled(false);
        }
    }

    private void populateUIFromIntent() {
        Intent intent = getIntent();
        currentVehicle = (TransportationModel) intent.getSerializableExtra("vehicle");
        if (currentVehicle != null){
            editing = true;
            EditText editName = (EditText) findViewById(R.id.add_transport_editText_nickname);
            editName.setText(currentVehicle.getName());

            String make = currentVehicle.getMake();
            String model = currentVehicle.getModel();
            String year =  currentVehicle.getYear();
            String transmission = currentVehicle.getTransmisson();
            String engineDisplacement = currentVehicle.getEngineDisplacment();
            TransportationModel.TransportationMode transportationMode = currentVehicle.getTransportaionMode();

            setSpinnerSelection(R.id.add_transport_dropdown_make, carbonFootprintInterface.getVehicleMakes(), make);
            setSpinnerSelection(R.id.add_transport_dropdown_model, carbonFootprintInterface.getVehicleModel(make), model);
            setSpinnerSelection(R.id.add_transport_dropdown_year, carbonFootprintInterface.getVehicleYear(make, model), year);
            setSpinnerSelection(R.id.add_transport_dropdown_transmission, carbonFootprintInterface.getVehicleTransmission(make,model,year), transmission);
            setSpinnerSelection(R.id.add_transport_dropdown_engine_displacement, carbonFootprintInterface.getVehicleEngineDisplacement(make,model,year,transmission), engineDisplacement);

            int modeIndex = 0;
            for(int i = 0; i < transportationModes.length; i++){
                if(transportationModes[i] == transportationMode){
                    modeIndex = i;
                    break;
                }
            }
            setSpinnerSelection(R.id.add_transport_dropdown_transportation_mode, modeIndex);
        }
    }

    private void setSpinnerSelection(int resourceId, int index){
        Spinner makeSpinner = (Spinner) findViewById(resourceId);
        makeSpinner.setSelection(index);
    }

    private void setSpinnerSelection(int resourceId, ArrayList<String> items, String item){
        Spinner makeSpinner = (Spinner) findViewById(resourceId);
        int positionOfMake = items.indexOf(item);
        makeSpinner.setSelection(positionOfMake);
    }

    private void onAddClick(){
        //Try to get data from transportation add UI and create a vehicle object
        TransportationModel vehicle = createVehicleObject();
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
            Toast.makeText(TransportationAddActivity.this, R.string.transportation_added, Toast.LENGTH_SHORT).show();
        }
        Intent intent = getIntent();
        //Passing the vehicle object to the TransportationActivity
        intent.putExtra("vehicle", vehicle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onClickDelete(){
        //Removing vehicle from collection if it is on the list
        removeVehicle(currentVehicle);
        setResult(RESULT_DELETE);
        Toast.makeText(TransportationAddActivity.this, R.string.transportation_deleted, Toast.LENGTH_SHORT).show();
        finish();
    }

    private TransportationModel createVehicleObject(){
        //Try to get data from transportation add UI
        EditText nickNameEditText = (EditText) findViewById(R.id.add_transport_editText_nickname);
        String name = nickNameEditText.getText().toString();

        if (name.length() == 0) {
            Toast.makeText(TransportationAddActivity.this, R.string.please_enter_a_transportation_name, Toast.LENGTH_SHORT)
                    .show();
            return null;
        }

        Spinner transportationModelSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_transportation_mode);
        int selectedTransportationModeIndex = transportationModelSpinner.getSelectedItemPosition();
        String make = null;
        String model = null;
        String year = null;
        String transmission = null;
        String engineDisplacement = null;
        if(transportationModes[selectedTransportationModeIndex] == TransportationModel.TransportationMode.CAR){
            Spinner vehicleMakeSpinner = (Spinner) findViewById(R.id.add_transport_dropdown_make);
            make = vehicleMakeSpinner.getSelectedItem().toString();
            if (make == null){
                Toast.makeText(TransportationAddActivity.this, R.string.vehicle_make_shoud_be_selected, Toast.LENGTH_SHORT).show();
            }

            Spinner vehicleModelSpinner = (Spinner) findViewById(R.id.add_transport_dropdown_model);
            model = vehicleModelSpinner.getSelectedItem().toString();
            if (model == null){
                Toast.makeText(TransportationAddActivity.this, R.string.vehicle_model_should_be_selected, Toast.LENGTH_SHORT).show();
            }

            Spinner vehicleYearSpinner = (Spinner) findViewById(R.id.add_transport_dropdown_year);
            year = vehicleYearSpinner.getSelectedItem().toString();
            if (year == null){
                Toast.makeText(TransportationAddActivity.this, R.string.vehicle_year_should_be_selected, Toast.LENGTH_SHORT).show();
            }

            Spinner vehicleTransmissionSpinner = (Spinner) findViewById(R.id.add_transport_dropdown_transmission);
            transmission = vehicleTransmissionSpinner.getSelectedItem().toString();
            if (transmission == null){
                Toast.makeText(TransportationAddActivity.this, R.string.vehicle_transmission_should_be_selected, Toast.LENGTH_SHORT).show();
            }

            Spinner vehicleEngineDisplacementSpinner = (Spinner) findViewById(R.id.add_transport_dropdown_engine_displacement);
            engineDisplacement = (String) vehicleEngineDisplacementSpinner.getSelectedItem();
            if (engineDisplacement == null){
                Toast.makeText(TransportationAddActivity.this, R.string.engine_displacement_should_be_selected, Toast.LENGTH_SHORT).show();
            }
        }
        //Creating vehicle object to pass it to vehicle activity to be added to the list.
        TransportationModel vehicle = new TransportationModel(-1, name, make, model, year, transmission , engineDisplacement, 0, 0, "", transportationModes[selectedTransportationModeIndex], imageName, false);

        // setting fuel efficiency data
        carbonFootprintInterface.populateCarFuelData(vehicle);

        return vehicle;
    }

    public static Intent makeIntentForNewVehicle(Context packageContext) {
        return new Intent(packageContext, TransportationAddActivity.class);
    }

    public static Intent makeIntentForEditVehicle(Context packageContext, TransportationModel vehicle) {
        Intent intent = makeIntentForNewVehicle(packageContext);
        intent.putExtra("vehicle", vehicle);
        return intent;
    }

    boolean addVehicle(TransportationModel vehicle){
        try{
            carbonFootprintInterface.add(this, vehicle);
        }
        catch(DuplicateComponentException e){
            if(!editing) {
                Toast.makeText(TransportationAddActivity.this, "This transportation already exist.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

    //remove(hide) vehicle from the list
    void removeVehicle(TransportationModel vehicle){
        carbonFootprintInterface.remove(this, vehicle);
    }

    //Set all the values for dropdown lists
    private void setupDropdownList() {
        setupTransportationModelDropdownList();
        setupMakeDropdownList();
    }

    private void enableNonCarFields(boolean enable) {
        int visible = enable ? View.VISIBLE : View.INVISIBLE;

        TextView makeText = (TextView) findViewById(R.id.add_transportation_make_tv);
        makeText.setVisibility(visible);
        TextView modelText = (TextView)findViewById(R.id.add_transportation_model_tv);
        modelText.setVisibility(visible);
        TextView yearText = (TextView)findViewById(R.id.add_transportation_year_tv);
        yearText.setVisibility(visible);
        TextView transmissionText = (TextView)findViewById(R.id.add_transportation_transmission_tv);
        transmissionText.setVisibility(visible);
        TextView engineDisplacementText = (TextView)findViewById(R.id.add_transportation_engine_tv);
        engineDisplacementText.setVisibility(visible);

        Spinner makeSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_make);
        makeSpinner.setVisibility(visible);
        Spinner modelSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_model);
        modelSpinner.setVisibility(visible);
        Spinner yearSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_year);
        yearSpinner.setVisibility(visible);
        Spinner transmissionSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_transmission);
        transmissionSpinner.setVisibility(visible);
        Spinner engineDisplacementSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_engine_displacement);
        engineDisplacementSpinner.setVisibility(visible);
    }

    private void setupTransportationModelDropdownList() {
        transportationModes = TransportationModel.TransportationMode.values();
        Spinner transportationModelSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_transportation_mode);
        transportationModelSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, transportationModes));
        transportationModelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(transportationModes[position] != TransportationModel.TransportationMode.CAR){
                    // disable all other fields
                    enableNonCarFields(false);
                }
                else{
                    enableNonCarFields(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        transportationModelSpinner.setSelection(2);
        enableNonCarFields(true);
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
        if(editing) {
            int index = makes.indexOf(currentVehicle.getMake());
            if(index > -1){
                makeSpinner.setSelection(index);
            }
        }
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
        if(editing) {
            int index = models.indexOf(currentVehicle.getModel());
            if(index > -1){
                modelSpinner.setSelection(index);
            }
        }
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
        if(editing) {
            int index = years.indexOf(currentVehicle.getYear());
            if(index > -1){
                yearSpinner.setSelection(index);
            }
        }
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
        if(editing) {
            int index = transmissions.indexOf(currentVehicle.getYear());
            if(index > -1){
                transmissionSpinner.setSelection(index);
            }
        }
    }

    private void setupEngineDisplacmentDropdownList(String selectedMake, String selectedModel, String selectedYear, String selectedTransmission) {
        ArrayList<String> engineDisplacement = carbonFootprintInterface.getVehicleEngineDisplacement(selectedMake, selectedModel, selectedYear, selectedTransmission);
        final Spinner engineDisplacementSpinner = (Spinner)findViewById(R.id.add_transport_dropdown_engine_displacement);
        fillSpinner(engineDisplacementSpinner, engineDisplacement);
        if(editing) {
            int index = engineDisplacement.indexOf(currentVehicle.getEngineDisplacment());
            if(index > -1){
                engineDisplacementSpinner.setSelection(index);
            }
        }
    }

    private void fillSpinner(Spinner spinner, ArrayList<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.add_transportation_add_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_image) {
            startActivityForResult(TransportationImageSelect.makeIntent(TransportationAddActivity.this), TransportationImageSelect.TRANSPORTATION_IMAGE_SELECT_CALL_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            String image = data.getStringExtra(TransportationImageSelect.IMAGE_EXCHANGE_NAME);
            if(image != null){
                imageName = image;
                if(currentVehicle != null){
                    currentVehicle.setImageName(image);
                }
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return SideNavigationManager.handleSideNavigationSelection(this, item);
    }
}
