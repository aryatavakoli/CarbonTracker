package com.cmpt276.indigo.carbontracker;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.DuplicateComponentException;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.UtilityModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
    implements Utility Add activity
 */

public class UtilityAddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final int RESULT_DELETE = 15;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    Calendar startCalendar = Calendar.getInstance(); //getting 2 calender instances one for start one for end
    Calendar endCalendar = Calendar.getInstance();
    UtilityModel currentUtility;
    boolean editing = false; //we are not in the editing mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        loadSpinner();
        populateUIFromIntent();
        gettingDate(R.id.StartDatebtn, startCalendar);
        gettingDate(R.id.endDateBtn, endCalendar);
        setupBottomNavigation();
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
            addItem.setTitle(R.string.update_utility);
            addItem.setIcon(R.drawable.ic_update);
        }
        else{
            MenuItem deleteItem = menu.findItem(R.id.action_delete);
            deleteItem.setEnabled(false);
        }
    }

    private void onClickDelete(){
        //Removing vehicle from collection if it is on the list
        removeUtility(currentUtility);
        setResult(RESULT_DELETE);
        Toast.makeText(UtilityAddActivity.this, R.string.utility_deleted, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void onAddClick() {
        //Try to get data from utility add UI and create a utility object
        UtilityModel newUtility = createUtility();
        if(newUtility == null){
            return;
        }
        //adding and replacing route when a user is editing
        if (editing){
            Intent intent = getIntent();
            //Passing the utility object to the TransportationActivity
            intent.putExtra("utility", newUtility);
            setResult(RESULT_OK, intent);
            finish();
        }
        //adding Utility to collection if it is not duplicate
        else if (!addUtility(newUtility)) {
            return;
        }
        else {
            Toast.makeText(UtilityAddActivity.this, R.string.utility_added, Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent();
        intent.putExtra("utility", newUtility);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void displayDate(Button btn, Calendar c) {
        String date = calendarToString(c);
        btn.setText(date);
    }

    private String calendarToString(Calendar c) {
        //change date type to sting
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
        return formatter.format(c.getTime());
    }

    private void gettingDate(final int id , final Calendar myCalendar){
//        using a date picker for start and end of utility

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Button txt = (Button) findViewById(id);
                displayDate(txt, myCalendar);
            }

        };
        final Button txt = (Button) findViewById(id);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UtilityAddActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                displayDate(txt, myCalendar);
            }
        });
        displayDate(txt, myCalendar);
    }

    private void populateUIFromIntent(){
//        if we are in the editing mode we have to show the data is already in the utility
        Intent intent = getIntent();
        currentUtility = (UtilityModel) intent.getSerializableExtra("utility");
        if (currentUtility != null){
            editing = true;
            EditText editName = (EditText) findViewById(R.id.utility_add_editText_name);
            editName.setText(currentUtility.getName());
            UtilityModel.Company company = currentUtility.getCompanyName();
            setSpinnerSelection(R.id.utility_add_spinner_company, company);
            EditText editConsumption = (EditText) findViewById(R.id.utility_add_editText_energy_consumption);
            editConsumption.setText("" + currentUtility.getTotalEnergyConsumptionInKWh());
            EditText editNumberOfOccupents = (EditText) findViewById(R.id.utility_add_editText_num_ppl);
            editNumberOfOccupents.setText("" + currentUtility.getNumberOfOccupants());
            startCalendar = currentUtility.getStartDate();
            endCalendar = currentUtility.getEndDate();
        }
    }

    private void setSpinnerSelection(int resourceId, UtilityModel.Company item){
        Spinner itemSpinner = (Spinner) findViewById(resourceId);
        UtilityModel.Company values[] = UtilityModel.Company.values();
        int positionOfItem = 0;
        for(int i = 0; i < values.length; i++) {
            if(values[i] == item) {
                positionOfItem = i;
                break;
            }
        }
        itemSpinner.setSelection(positionOfItem);
    }

    private void loadSpinner() {
        Spinner spinnerCompany  = (Spinner) findViewById(R.id.utility_add_spinner_company);
        spinnerCompany.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, UtilityModel.Company.values()));
    }

    //remove(hide) utility from the list
    void removeUtility(UtilityModel utility){
        carbonFootprintInterface.remove(this, utility);
    }

    private UtilityModel createUtility() {
//        the following code checks if the user has entered all the required datas and they are all
//        valid
        EditText editTextName = (EditText) findViewById(R.id.utility_add_editText_name);
        String name = editTextName.getText().toString();

        if (editTextName.getText().toString().length() == 0) {
            Toast.makeText(UtilityAddActivity.this, R.string.please_enter_a_utility_name, Toast.LENGTH_SHORT).show();
            return null;
        }

        Spinner spinnerCompany = (Spinner) findViewById(R.id.utility_add_spinner_company);
        UtilityModel.Company company = (UtilityModel.Company) spinnerCompany.getSelectedItem();

        if (company == null) {
            Toast.makeText(UtilityAddActivity.this, R.string.company_should_be_selected, Toast.LENGTH_SHORT).show();
            return null;
        }
        if (startCalendar == null || endCalendar == null) {
            Toast.makeText(UtilityAddActivity.this, R.string.please_select_start_and_end_days, Toast.LENGTH_SHORT).show();
            return null;
        } else if (endCalendar.getTimeInMillis() < startCalendar.getTimeInMillis()) {
            Toast.makeText(UtilityAddActivity.this, R.string.end_date_cannot_be_before_start_date, Toast.LENGTH_SHORT).show();
            return null;
        }

        EditText editTextEnergy = (EditText) findViewById(R.id.utility_add_editText_energy_consumption);
        if (editTextEnergy.getText().toString().length() == 0) {
            Toast.makeText(UtilityAddActivity.this, R.string.please_enter_energy_consumption, Toast.LENGTH_SHORT).show();
            return null;
        }
        double energy = Double.parseDouble(editTextEnergy.getText().toString());

        EditText editTextPeople = (EditText) findViewById(R.id.utility_add_editText_num_ppl);
        if (editTextPeople.getText().toString().length() == 0) {
            Toast.makeText(UtilityAddActivity.this, R.string.please_enter_occupants, Toast.LENGTH_SHORT).show();
            return null;
        }
        int occupants = Integer.parseInt(editTextPeople.getText().toString());

        return new UtilityModel(-1, company, name, energy, occupants, startCalendar, endCalendar, false, UtilityModel.Units.KILOGRAMS);
    }

    boolean addUtility(UtilityModel utility){
        try{
            carbonFootprintInterface.add(this,utility);
        }
        catch(DuplicateComponentException e){
            if(!editing) {
                Toast.makeText(UtilityAddActivity.this, R.string.this_utility_already_exists, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

    public static Intent makeIntentForNewUtility(Context packageContext) {
        return new Intent(packageContext, UtilityAddActivity.class);
    }

    public static Intent makeIntentForEditUtility(Context packageContext, UtilityModel utility) {
        Intent intent = makeIntentForNewUtility(packageContext);
        intent.putExtra("utility", utility);
        return intent;
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