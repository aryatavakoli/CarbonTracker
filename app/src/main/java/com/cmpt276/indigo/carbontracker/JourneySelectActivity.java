package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.ListView;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.TransportationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * the activity to display all the journey into a listview
 */
public class JourneySelectActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ArrayList<JourneyModel> journies;
    ArrayList<TransportationModel> vehicles;
    ArrayList<RouteModel> routes;
    private int selectedItemIndex;
    private long idOfJourneyEditing = -1;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    private static final int ACTIVITY_RESULT_ADD = 50;
    private static final int ACTIVITY_RESULT_EDIT = 100;

    int selectItem;
    int image = R.drawable.skytrain;
    CustomizedArrayAdapter adapter;
    CustomizedArrayAdapterItem arrayAdapterItems[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedItemIndex = -1;

        setContentView(R.layout.activity_journey_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setupBottomNavigation();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
//        startAddActivity();
        createListView();
//        setupEditVehicleLongPress();
    }

//    private void startAddActivity() { //launching the add activity
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(JourneySelectActivity.this, JourneyAddActivity.class);
//                startActivityForResult(intent, ACTIVITY_RESULT_ADD);
//            }
//        });
//    }

    private void setBottomNavigationItemsStatus() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem edit = menu.findItem(R.id.action_edit);
        MenuItem delete = menu.findItem(R.id.action_delete);
        if(selectedItemIndex < 0){
            edit.setEnabled(false);
            delete.setEnabled(false);
        }
        else{
            edit.setEnabled(true);
            delete.setEnabled(true);
        }
    }

    private void setupList(){
        Resources res = getResources();
        journies = carbonFootprintInterface.getJournies(this);
        int journiesSize = journies.size();
        arrayAdapterItems = new CustomizedArrayAdapterItem[journiesSize];
        for (int i = 0; i < journiesSize; i++){
            arrayAdapterItems[i] = new CustomizedArrayAdapterItem(
                    image,
                    journies.get(i).getTransportationModel().getName(),
                    journies.get(i).getRouteModel().getName(),
                    journies.get(i).getCreationDateString());
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
                Intent intent = new Intent(JourneySelectActivity.this, JourneyAddActivity.class);
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
                }
                return true;
            }
        });
        setBottomNavigationItemsStatus();
    }

    private void editItem(){
        if(selectedItemIndex > -1) {
            JourneyModel journeyModel = journies.get(selectedItemIndex);
            idOfJourneyEditing = journeyModel.getId();
            Intent intent = JourneyAddActivity.makeIntentForEditJourney(JourneySelectActivity.this, journeyModel);
            startActivityForResult(intent, ACTIVITY_RESULT_EDIT); //open the edit activity
        }
    }

    private void removeItem() {
        if(selectedItemIndex > -1){
            carbonFootprintInterface.remove(this, journies.get(selectedItemIndex));
            selectedItemIndex = -1;
            setBottomNavigationItemsStatus();
            populateJourneyList();
        }
    }

    private void createListView() {
        //set reference to listview
        final ListView journeyList = (ListView) findViewById(R.id.journey_select_list);
        populateJourneyList();
        //handle click for each element in listview
        journeyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItemIndex = position;
                adapter.setSelected(position);
                adapter.notifyDataSetChanged();
                setBottomNavigationItemsStatus();
                /*
                Intent intent = getIntent();
                // Passing selected vehicle to the caller activity
                JourneyModel selectedJourney = journies.get(position);
                intent.putExtra("journey", selectedJourney);
                setResult(RESULT_OK, intent);
                finish();
                */
            }
        });
    }

    private void populateJourneyList() {
        setupList();
        ListView journeyList = (ListView) findViewById(R.id.journey_select_list);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        journies = carbonFootprintInterface.getJournies(this);
        // putting vehicles in list
        List<String> journey_nameList = new ArrayList<>();
        for(JourneyModel v: journies){
            journey_nameList.add("Car: " + v.getTransportationModel().getName() +
                    "   Route: " + v.getRouteModel().getName() +
                    "   Date: " + v.getCreationDateString());
        }

        //Create array adapter
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
//                this, //context
//                android.R.layout.simple_list_item_1,
//                journey_nameList //arrayList
//        );
        journeyList.setAdapter(adapter);
    }

    private void setupEditVehicleLongPress() { //we can edit the vehicle or delte
        ListView list = (ListView) findViewById(R.id.journey_select_list);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                JourneyModel journeyModel  = journies.get(position);
                idOfJourneyEditing = journeyModel.getId();
                Intent intent = JourneyAddActivity.makeIntentForEditJourney(JourneySelectActivity.this, journeyModel);
                startActivityForResult(intent, ACTIVITY_RESULT_EDIT); //open the edit activity
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//get the result from add journey activity if it was for creating or adding
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case ACTIVITY_RESULT_ADD:
                    populateJourneyList();
                    break;
                case ACTIVITY_RESULT_EDIT:
                    JourneyModel modifiedJourney = (JourneyModel) data.getSerializableExtra("journey");
                    modifiedJourney.setId(idOfJourneyEditing);
                    carbonFootprintInterface.edit(this, modifiedJourney);
                    populateJourneyList();
                    break;
            }

        }
        else if (resultCode == JourneyAddActivity.RESULT_DELETE){
            populateJourneyList();
        }

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
