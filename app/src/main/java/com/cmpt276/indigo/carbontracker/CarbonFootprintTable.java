package com.cmpt276.indigo.carbontracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.*;
import java.util.ArrayList;
import java.util.List;

import android.widget.ListView;
public class CarbonFootprintTable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbon_footprint_table);
        //Allows for backbutton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ListView tableListView = (ListView) findViewById(R.id.list);

        TableAdapter adapter = new TableAdapter(this, FuelDataInputStream.getSampleData());

        tableListView.setAdapter(adapter);

    }
}
