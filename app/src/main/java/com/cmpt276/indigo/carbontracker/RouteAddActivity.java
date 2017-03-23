package com.cmpt276.indigo.carbontracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.DuplicateComponentException;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;

import java.net.NoRouteToHostException;

import static java.sql.Types.NULL;

/*
    implements Route Add activity
 */

public class RouteAddActivity extends AppCompatActivity {
    public static final int RESULT_DELETE = 12;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    private boolean editing = false;
    RouteModel currentRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        populateUIFromIntent();
        calculate();
        recalculateOnChange(R.id.add_route_editText_city_distance);
        recalculateOnChange(R.id.add_route_editText_highway_distance);
        setupOKButton();
        setupDeleteButton();
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

    private int getNumberFromEditTextOrZeroForFail(int id) {
        EditText data = (EditText) findViewById(id);
        String text = data.getText().toString();
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private void setupOKButton() {
        Button btn = (Button) findViewById(R.id.add_route_ok_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextName = (EditText) findViewById(R.id.add_route_editText_nickname);

                if (editTextName.getText().toString().length() == 0) {
                    Toast.makeText(RouteAddActivity.this, "Please enter a route name.", Toast.LENGTH_SHORT)
                            .show();
                    return;

                }

                EditText editTextHighway = (EditText) findViewById(R.id.add_route_editText_highway_distance);

                if (editTextHighway.getText().toString().length() == 0) {
                    Toast.makeText(RouteAddActivity.this, "Please enter a highway distance.", Toast.LENGTH_SHORT)
                            .show();
                    return;

                }

                EditText editTextCity = (EditText) findViewById(R.id.add_route_editText_city_distance);
                if (editTextCity.getText().toString().length() == 0) {
                    Toast.makeText(RouteAddActivity.this, "Please enter a city distance.", Toast.LENGTH_SHORT)
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
                    Toast.makeText(RouteAddActivity.this, "Route Added!", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent();
                intent.putExtra("route", newRoute);
                setResult(Activity.RESULT_OK, intent);

                finish();

            }

        });
    }

    private RouteModel createRoute() {
        // Get values from UI:
        EditText editTextName = (EditText) findViewById(R.id.add_route_editText_nickname);
        String name = editTextName.getText().toString();

        EditText editTextHighway = (EditText) findViewById(R.id.add_route_editText_highway_distance);
        double highway = Double.parseDouble(editTextHighway.getText().toString());

        EditText editTextCity = (EditText) findViewById(R.id.add_route_editText_city_distance);
        double city = Double.parseDouble(editTextCity.getText().toString());
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
                Toast.makeText(RouteAddActivity.this, "This route already exists.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }


    private void setupDeleteButton(){
        Button btnDelete = (Button) findViewById(R.id.add_route_delete_btn);
        if(!editing){
            btnDelete.setEnabled(false);
        }
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRoute(currentRoute);
                setResult(RESULT_DELETE);
                Toast.makeText(RouteAddActivity.this, "Route Deleted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    boolean removeRoute(RouteModel route){
        if(!carbonFootprintInterface.remove(this, route)) {
            Toast.makeText(RouteAddActivity.this, "Failed to delete!!", Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(RouteAddActivity.this, "deleting completed", Toast.LENGTH_LONG).show();
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

}







