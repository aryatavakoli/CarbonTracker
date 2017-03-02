package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.FuelDataInputStream;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.VehicleModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    public MainMenu() {
        // this method fires only once per application start.
        // getApplicationContext returns null here
        Log.d("main", "Constructor fired");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        carbonFootprintSelectbtn();
        journeySelectbtn();
        //readVehicleData();
    }



    //TODO: get this method to run only once on runtime
    //Reads data from CSV not called in onCreate
    private void readVehicleData() {
        FuelDataInputStream data = new FuelDataInputStream(this);

        data.readDataFile();
    }

    //Launch Create a jounrney activity
    private void journeySelectbtn() {
        Button btn = (Button) findViewById(R.id.main_menu_create_Journey_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, JourneyMenu.class);
                startActivity(intent);

            }
        });


    }

    //Go to carbon footprint activity
    private void carbonFootprintSelectbtn() {
        Button btn = (Button) findViewById(R.id.main_menu_carbonfootprint_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, CarbonFootprintMenu.class);
                startActivity(intent);

            }
        });


    }
}
