package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;

import java.util.ArrayList;
import java.util.List;

public class RouteSelectActivity extends AppCompatActivity {
    private static final int ACTIVITY_ROUTE_ADD = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_route_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Allows for backbutton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        launchAddActivity();

        createListView();
    }

    //Launch add route activity
    private void launchAddActivity() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = RouteAddActivity.makeIntentForNewPot(RouteSelectActivity.this);
                startActivityForResult(intent,ACTIVITY_ROUTE_ADD);
            }
        });
    }

    //Create a sample list view
    private void createListView() {
        //Reference to listview
        ListView routeList = (ListView) findViewById(R.id.route_select_list);
        //create a sample array for test
        List<String> sample_route_list = new ArrayList<>();
        //add elements
        sample_route_list.add("Route 1");
        sample_route_list.add("Route 2");
        //Create array adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, //context
                android.R.layout.simple_list_item_1,
                sample_route_list //array
        );
        //Apply array adapter to listview
        routeList.setAdapter(arrayAdapter);

        //handle callblack for each list element
        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO: implement on item click
            }
        });
    }

}
