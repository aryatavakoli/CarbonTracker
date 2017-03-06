package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

/*
FuelDataInputStream provides an interface to extract required data from CSV file.
Following methods need to be implemented in FuelDataInputStream:
 readDataFile
 getJourneyEmission

This class can be potentially be a Singleton class because we have only one CSV file to read

This class will be used by following UIs: CarUI, JourneyUI
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FuelDataInputStream {

    static private FuelDataInputStream instance = new FuelDataInputStream();

    static public FuelDataInputStream getInstance(){
        return instance;
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

    public ArrayList<VehicleModel> readDataFile(InputStream is){
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        ArrayList<VehicleModel> vehicleData = new ArrayList<>();
        String line = "";
        try {
            reader.readLine();
            while ((line = reader.readLine()) != null){
                //split by columns
                String[] token = line.split(",");
                //read the data
                VehicleModel data = new VehicleModel();
                data.setMake(token[46]);
                data.setModel(token[47]);
                data.setYear(token[63]);
                data.setTransmisson(token[57]);
                if(token[23].length() > 0 ){
                    try{
                        data.setEngineDisplacment(Double.parseDouble(token[23]));
                    }
                    catch(Exception e){
                        data.setEngineDisplacment(0.0);
                    }
                }
                else if (token[23].length() == 0){
                    data.setEngineDisplacment(0);
                }
                data.setCityMileage(Double.parseDouble(token[4]));
                data.setHighwayMileage(Double.parseDouble(token[34]));
                data.setPrimaryFuelType(token[30]);
                vehicleData.add(data);

                // Scaffolding statments
//                Log.d("MainMenu: ", "Just created: " +
//                     "make: " + data.getMake() + ", "
//                 + "model:" + data.getModel() + ", "
//                 + "Year: " + data.getYear() +", "
//                 + "Highway Mileage: " + data.getHighwayMileage() + ", "
//                 + "City Mileage: " + data.getCityMileage() + ", "
//                 + "Transmission: " + data.getTransmisson() + ", "
//                 + "Engine Displacement: " + data.getEngineDisplacment() + ", "
//                 + "Primary Fuel Type: " + data.getPrimaryFuelType() );
            }
        } catch (IOException e){
            Log.wtf("MainMenu", "Error reading datafile on Line: " + line, e);
            e.printStackTrace();
        }
        return vehicleData;
    }
}
