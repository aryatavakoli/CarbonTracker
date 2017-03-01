package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

/*
FuelDataInputStream provides an interface to extract required data from CSV file.
Following methods need to be implemented in FuelDataInputStream:
 readDataFile
 getJourneyEmission

This class can be potentially be a Singleton class because we have only one CSV file to read

This class will be used by following UIs: CarUI, JourneyUI
 */

import android.content.Context;
import android.util.Log;

import com.cmpt276.indigo.carbontracker.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FuelDataInputStream {
    private Context context;
    private List<VehicleModel> vehicledata = new ArrayList<>();

    public FuelDataInputStream(Context current) {
        this.context = current;
    }

    public void readDataFile(){
        InputStream is = context.getResources().openRawResource(R.raw.vehicles);
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
                data.setCityMileage(Double.parseDouble(token[58]));
                data.setHighwayMileage(Double.parseDouble(token[60]));
                //Log.d("MainMenu", "No Error on SetCarbon");
                vehicledata.add(data);

                Log.d("MainMenu", "Just created: " +
                     "make: " + data.getMake() + ", "
                 + "model:" + data.getModel() + ", "
                 + "Year: " + data.getYear() +", "
                 + "Highway Mileage: " + data.getHighwayMileage() + ", "
                 + "City Mileage: " + data.getCityMileage() );
            }

        } catch (IOException e){
            Log.wtf("MainMenu", "Error reading datafile on Line: " + line, e);
            e.printStackTrace();

        }

    }
}
