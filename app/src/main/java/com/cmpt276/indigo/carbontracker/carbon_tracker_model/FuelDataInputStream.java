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

import com.cmpt276.indigo.carbontracker.Goods;
import com.cmpt276.indigo.carbontracker.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FuelDataInputStream {
    private Context context;
    private List<VehicleModel> vehicledata = new ArrayList<>();

    public FuelDataInputStream(Context current) {
        this.context = current;
    }

    public static List<Goods> getSampleData()
    {
        List<Goods> list = new ArrayList<Goods>();
        list.add(new Goods("01", "2010", "982323423232",34,23,23));
        list.add(new Goods("02", "2011", "31312323223",34,23,23));
        list.add(new Goods("03", "2012", "12",34,23,23));
        list.add(new Goods("04", "2013", "12333435445",34,23,23));
        list.add(new Goods("05", "2014", "34523",34,23,23));
        list.add(new Goods("06", "2015", "345456",34,23,23));
        list.add(new Goods("07", "2016", "2344",34,23,23));
        list.add(new Goods("08", "2017", "23445",34,23,23));
        list.add(new Goods("09", "2018", "3234345",34,23,23));
        return list;
    }

    public void readDataFile(){
        InputStream is = context.getResources().openRawResource(R.raw.vehicles);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";
        try {
            reader.readLine();
            //int counter = 0;
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
                data.setTransmisson(token[57]);
                //Log.d("MainMenu", "No Error on setTransmisson");
                if(token[23].length() > 0 ){
                    data.setEngineDisplacment(Double.parseDouble(token[23]));
                }
                else if (token[23].length() == 0){
                    data.setEngineDisplacment(0);
                }
                //Log.d("MainMenu", "No Error on EngineDisplacment");
                data.setCityMileage(Double.parseDouble(token[58]));
                data.setHighwayMileage(Double.parseDouble(token[60]));
                //Log.d("MainMenu", "No Error on SetCarbon");
                vehicledata.add(data);
                //counter++;

//                Log.d("Row Number: " +counter, "Just created: " +
//                     "make: " + data.getMake() + ", "
//                 + "model:" + data.getModel() + ", "
//                 + "Year: " + data.getYear() +", "
//                 + "Highway Mileage: " + data.getHighwayMileage() + ", "
//                 + "City Mileage: " + data.getCityMileage() + ", "
//                 + "Transmission: " + data.getTransmisson() + ", "
//                 + "Engine Displacement: " + data.getEngineDisplacment() );
            }

        } catch (IOException e){
            Log.wtf("MainMenu", "Error reading datafile on Line: " + line, e);
            e.printStackTrace();

        }

    }
}
