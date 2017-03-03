package com.cmpt276.indigo.carbontracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponent;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;


import java.util.ArrayList;
import java.util.List;

public class RouteSelectActivity extends AppCompatActivity {

    private int indexOfRouteEditing = -1;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    private static final int ACTIVITY_RESULT_ADD = 40;
    private static final int ACTIVITY_RESULT_EDIT = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_route_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Allows for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        startAddActivity();
        createListView();
        setupEditRouteLongPress();
    }

    // FAB button to launch add activity
    private void startAddActivity() {
        //set reference to fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RouteSelectActivity.this,RouteAddActivity.class);
                startActivityForResult(intent, ACTIVITY_RESULT_ADD);
            }
        });
    }


    //sample for demonstartion purposes
    private void createListView() {
        //set reference to listview
        ListView routeList = (ListView) findViewById(R.id.route_select_list);
        populateRoutesList();

        //handle click for each element in listview
        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                // Passing selected route to the caller activity
                RouteModel selectedRoute = carbonFootprintInterface.getRoutes().get(position);
                intent.putExtra("route", selectedRoute);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void populateRoutesList() {
        ListView routeList = (ListView) findViewById(R.id.route_select_list);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        ArrayList<RouteModel> routes = carbonFootprintInterface.getRoutes();
        // putting routes in list
        List<String> route_nameList = new ArrayList<>();
        //Add elements
        for(RouteModel v: routes){
            route_nameList.add(v.getName());
        }

        //Create array adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, //context
                android.R.layout.simple_list_item_1,
                route_nameList //arrayList
        );

        //apply adapter to listview
        routeList.setAdapter(arrayAdapter);
    }

    private void setupEditRouteLongPress() {
        final ArrayList<RouteModel> routes = carbonFootprintInterface.getRoutes();
        ListView list = (ListView) findViewById(R.id.route_select_list);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                indexOfRouteEditing = position;
                RouteModel route = routes.get(position);

                Intent intent = RouteAddActivity.makeIntentForEditRoute(RouteSelectActivity.this, route);
                startActivityForResult(intent, ACTIVITY_RESULT_EDIT);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case ACTIVITY_RESULT_ADD:
                    populateRoutesList();
                    break;
                case ACTIVITY_RESULT_EDIT:
                    RouteModel modifiedRoute = (RouteModel) data.getSerializableExtra("route");
                    RouteModel route = carbonFootprintInterface.getRoutes().get(indexOfRouteEditing);
                    route.setName(modifiedRoute.getName());
                    route.setHighwayDistance(modifiedRoute.getHighwayDistance());
                    route.setCityDistance(modifiedRoute.getCityDistance());
                    populateRoutesList();
            }

        }

    }

}
