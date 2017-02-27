package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RouteSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Allows for backbutton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        launchAddActivity();

        createListView();
    }

    private void launchAddActivity() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RouteSelectActivity.this, RouteAddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createListView() {
        ListView routeList = (ListView) findViewById(R.id.route_select_list);
        //create a sample array for test
        List<String> sample_route_list = new ArrayList<>();
        sample_route_list.add("Route 1");
        sample_route_list.add("Route 2");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, //context
                android.R.layout.simple_list_item_1,
                sample_route_list //array
        );

        routeList.setAdapter(arrayAdapter);

        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RouteSelectActivity.this,ResultActivity.class);
                //based on item add info to intent
                startActivity(intent);
            }
        });
    }

}
