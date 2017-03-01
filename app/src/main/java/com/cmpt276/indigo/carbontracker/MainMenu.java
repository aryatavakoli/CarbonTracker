package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
        readVehicleData();
    }

    private List<VehicleModel> vehicledata = new ArrayList<>();

    //TODO: get this method to run only once on runtime
    //Reads data from CSV not called in onCreate
    private void readVehicleData() {

        InputStream is = getResources().openRawResource(R.raw.vehicles);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";
        try {
            reader.readLine();
            while ((line = reader.readLine()) != null){
                //Log.d("MainMenu", "Error on Line: " + line);
                //split by columns
                String[] token = line.split(",");
                //read the data
                VehicleModel data = new VehicleModel();
                data.setMake(token[46]);
                //Log.d("MainMenu", "No Error on SetMake");
                data.setModel(token[47]);
                //Log.d("MainMenu", "No Error on SetModel");
                data.setYear(token[63]);
                //Log.d("MainMenu", "No Error on SetYear");
                data.setCarbonFootprintGpm(Double.parseDouble(token[14]));
                //Log.d("MainMenu", "No Error on SetCarbon");
                vehicledata.add(data);

                Log.d("MainMenu", "Just created: " + "make: " + data.getMake() + ", " + "model:" + data.getModel() + ", " + "Year: " + data.getYear() +", " + "CarbonFootPrint: " + data.getCarbonFootprintGpm());
            }

        } catch (IOException e){
            Log.wtf("MainMenu", "Error reading datafile on Line: " + line, e);
            e.printStackTrace();

        }


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
