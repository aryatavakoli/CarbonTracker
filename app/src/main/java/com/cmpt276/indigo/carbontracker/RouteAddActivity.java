package com.cmpt276.indigo.carbontracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.DuplicateComponentException;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;

import java.net.NoRouteToHostException;
import java.util.ArrayList;

import static java.sql.Types.NULL;

/*
    implements Route Add activity
 */

public class RouteAddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final int RESULT_DELETE = 12;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    private boolean editing = false;
    RouteModel currentRoute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        populateUIFromIntent();
        calculate();
        recalculateOnChange(R.id.add_route_editText_city_distance);
        recalculateOnChange(R.id.add_route_editText_highway_distance);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
            addItem.setTitle(R.string.update_route);
            addItem.setIcon(R.drawable.ic_update);
        }
        else{
            MenuItem deleteItem = menu.findItem(R.id.action_delete);
            deleteItem.setEnabled(false);
        }
    }

    private void onClickDelete(){
        //Removing vehicle from collection if it is on the list
        removeRoute(currentRoute);
        setResult(RESULT_DELETE);
        Toast.makeText(RouteAddActivity.this, R.string.route_deleted, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void populateUIFromIntent() { //If the user wants to edit the pot this function gets the value from the
        Intent intent = getIntent();   //selected pot and shows it to the user
        currentRoute = (RouteModel) intent.getSerializableExtra("route");
        if (currentRoute != null) {
            editing = true;
            EditText editName = (EditText) findViewById(R.id.add_route_editText_nickname);
            editName.setText(currentRoute.getName());
            EditText editHighway = (EditText) findViewById(R.id.add_route_editText_highway_distance);
            editHighway.setText((currentRoute.getHighwayDistance() + ""));
            EditText editCity = (EditText) findViewById(R.id.add_route_editText_city_distance);
            editCity.setText((currentRoute.getCityDistance() + ""));

        }
    }

    private void calculate() {
        double cityDistanceInKm = getNumberFromEditTextOrZeroForFail(R.id.add_route_editText_city_distance);
        double highwayDistanceInKm = getNumberFromEditTextOrZeroForFail(R.id.add_route_editText_highway_distance);
        double total = cityDistanceInKm + highwayDistanceInKm;
        displayNumberIfPositive(R.id.add_route_textview_total_distance, total);
    }

    private void recalculateOnChange(int textFieldID) {
        TextView textView = (TextView) findViewById(textFieldID);
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
    }

    private void displayNumberIfPositive(int id, double data) {
        TextView textView = (TextView) findViewById(id);
        if (data >= 0) {
            textView.setText("" + data);
        } else {
            textView.setText("");
        }
    }

    private double getNumberFromEditTextOrZeroForFail(int id) {
        EditText data = (EditText) findViewById(id);
        String text = data.getText().toString();
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private void onAddClick() {
        EditText editTextName = (EditText) findViewById(R.id.add_route_editText_nickname);
        if (editTextName.getText().toString().length() == 0) {
            Toast.makeText(RouteAddActivity.this, R.string.please_enter_a_route_name, Toast.LENGTH_SHORT)
                    .show();
            return;

        }

        double highway = 0.0;
        double city = 0.0;
        EditText editTextHighway = (EditText) findViewById(R.id.add_route_editText_highway_distance);
        String highwayText = editTextHighway.getText().toString();
        if (highwayText.length() > 0) {
            highway = Double.parseDouble(highwayText);
        }

        EditText editTextCity = (EditText) findViewById(R.id.add_route_editText_city_distance);
        String cityText = editTextCity.getText().toString();
        if (editTextCity.getText().toString().length() > 0) {
            city = Double.parseDouble(cityText);
        }
        if(city == 0.0 && highway == 0.0){
            Toast.makeText(RouteAddActivity.this, R.string.highway_city_not_zero, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        RouteModel newRoute = createRoute();

        if (editing) {
            Intent intent = getIntent();
            //Passing the route object to the RouteActivity
            intent.putExtra("route", newRoute);
            setResult(RESULT_OK, intent);
            finish();
        }

        //adding route to collection if it is not duplicate and user is not editing
        else if (!addRoute(newRoute)) {
            return;
        }
        else{
            Toast.makeText(RouteAddActivity.this, R.string.route_added, Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent();
        intent.putExtra("route", newRoute);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    private RouteModel createRoute() {
        // Get values from UI:
        EditText editTextName = (EditText) findViewById(R.id.add_route_editText_nickname);
        String name = editTextName.getText().toString();

        EditText editTextHighway = (EditText) findViewById(R.id.add_route_editText_highway_distance);
        String highwayText = editTextHighway.getText().toString();
        double highway = 0.0;
        if(highwayText.length() > 0) {
            highway = Double.parseDouble(highwayText);
        }

        EditText editTextCity = (EditText) findViewById(R.id.add_route_editText_city_distance);
        String cityText = editTextCity.getText().toString();
        double city = 0.0;
        if(cityText.length() > 0) {
            city = Double.parseDouble(cityText);
        }
        RouteModel newRoute = new RouteModel();
        newRoute.setName(name);
        newRoute.setHighwayDistance(highway);
        newRoute.setCityDistance(city);
        return  newRoute;

    }

    boolean addRoute(RouteModel route){
        try{
            carbonFootprintInterface.add(this, route);
        }
        catch(DuplicateComponentException e){
            if(!editing) {
                Toast.makeText(RouteAddActivity.this, R.string.this_route_already_exists, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

    boolean removeRoute(RouteModel route){
        if(!carbonFootprintInterface.remove(this, route)) {
            Toast.makeText(RouteAddActivity.this, R.string.failed_to_delete, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }



    public static Intent makeIntentForNewRoute(Context packageContext) {
        return new Intent(packageContext, RouteAddActivity.class);
    }

    public static Intent makeIntentForEditRoute(Context packageContext, RouteModel route) {
        Intent intent = makeIntentForNewRoute(packageContext);
        intent.putExtra("route", route);
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







