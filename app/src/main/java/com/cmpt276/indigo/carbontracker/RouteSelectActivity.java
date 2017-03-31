package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.UtilityModel;


import java.util.ArrayList;
import java.util.List;

/*
    implments Route selection activity
 */
public class RouteSelectActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private long indexOfRouteEditing = -1;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    private static final int ACTIVITY_RESULT_ADD = 40;
    private static final int ACTIVITY_RESULT_EDIT = 60;
    List<Integer> routePositionList;
    ArrayList<RouteModel> routes;
    int selectItem;
    private int selectedItemIndex;
    int image = R.drawable.route;
    CustomizedArrayAdapter adapter;
    CustomizedArrayAdapterItem arrayAdapterItems[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedItemIndex = -1;
        setContentView(R.layout.activity_route_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupBottomNavigation();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Allows for back button
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        //startAddActivity();
        createListView();
        setupEditRouteLongPress();
    }

    private void setupList(){
        Resources res = getResources();
        ArrayList<RouteModel> routes = carbonFootprintInterface.getRoutes(this);
        int routeSize = routes.size();
        arrayAdapterItems = new CustomizedArrayAdapterItem[routeSize];
        for (int i = 0; i < routeSize; i++){
            arrayAdapterItems[i] = new CustomizedArrayAdapterItem(image, routes.get(i).getName(), "", "");
        }
        selectItem = -1;
        adapter = new CustomizedArrayAdapter(this, arrayAdapterItems, getTitles(arrayAdapterItems));
    }

    private String[] getTitles(CustomizedArrayAdapterItem items[]){
        if (items.length == 0){
            return new String[0];
        }
        String[] titles = new String[items.length];
        for (int i = 0; i < items.length; i++){
            titles[i] = items[i].getText1();
        }
        return titles;
    }

    private void setupBottomNavigation(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                Intent intent = new Intent(RouteSelectActivity.this, RouteAddActivity.class);
                switch(item.getItemId())
                {
                    case R.id.action_add:
                        startActivityForResult(intent, ACTIVITY_RESULT_ADD);
                        break;
                    case R.id.action_edit:
                        editItem();
                        break;
                    case R.id.action_delete:
                        removeItem();
                        break;
                    case R.id.action_select:
                        onSelectRoute();
                        break;
                }
                return true;
            }
        });
        setBottomNavigationItemsStatus();
    }

    private void editItem(){
        if(selectedItemIndex > -1) {
            RouteModel route = routes.get(selectedItemIndex);
            indexOfRouteEditing = route.getId();
            Intent intent = RouteAddActivity.makeIntentForEditRoute(RouteSelectActivity.this, route);
            startActivityForResult(intent, ACTIVITY_RESULT_EDIT); //open the edit activity
        }
    }

    private void removeItem() {
        if(selectedItemIndex > -1){
            carbonFootprintInterface.remove(this, routes.get(selectedItemIndex));
            selectedItemIndex = -1;
            setBottomNavigationItemsStatus();
            populateRoutesList();
        }
    }

    private void onSelectRoute(){
        Intent intent = getIntent();
        // Passing selected vehicle to the caller activity
        RouteModel route = routes.get(selectedItemIndex);
        intent.putExtra("route", route);
        setResult(RESULT_OK, intent);
        finish();
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
                selectedItemIndex = position;
                adapter.setSelected(position);
                adapter.notifyDataSetChanged();
                setBottomNavigationItemsStatus();
//                Intent intent = getIntent();
//                // Passing selected route to the caller activity
//                int realPosition = routePositionList.get(position);
//                RouteModel selectedRoute = routes.get(realPosition);
//                intent.putExtra("route", selectedRoute);
//                setResult(RESULT_OK, intent);
//                finish();
            }
        });
    }

    private void populateRoutesList() {
        setupList();
        ListView routeList = (ListView) findViewById(R.id.route_select_list);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        routes = carbonFootprintInterface.getRoutes(this);
        // putting routes in list
        List<String> routeNameList = new ArrayList<>();
        routePositionList = new ArrayList<>();
        //Add elements
        int counter = 0;
        for(RouteModel v: routes){
            if(!v.getIsDeleted()) {
                routeNameList.add(v.getName());
                routePositionList.add(counter);
            }
            counter++;
        }

//        //Create array adapter
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
//                this, //context
//                android.R.layout.simple_list_item_1,
//                routeNameList //arrayList
//        );

        //apply adapter ro listview
        routeList.setAdapter(adapter);
    }

    private void setBottomNavigationItemsStatus() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem edit = menu.findItem(R.id.action_edit);
        MenuItem remove = menu.findItem(R.id.action_delete);
        MenuItem select = menu.findItem(R.id.action_select);
        if(selectedItemIndex < 0){
            edit.setEnabled(false);
            remove.setEnabled(false);
            select.setEnabled(false);
        }
        else{
            edit.setEnabled(true);
            remove.setEnabled(true);
            select.setEnabled(true);
        }
    }

    private void setupEditRouteLongPress() {
        routes = carbonFootprintInterface.getRoutes(this);
        ListView list = (ListView) findViewById(R.id.route_select_list);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                RouteModel route = routes.get(position);
                indexOfRouteEditing = route.getId();
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
                    modifiedRoute.setId(indexOfRouteEditing);
                    carbonFootprintInterface.edit(this, modifiedRoute);
                    populateRoutesList();
                    break;
            }

        }
        else if (resultCode == RouteAddActivity.RESULT_DELETE){
            populateRoutesList();
        }
        setBottomNavigationItemsStatus();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
