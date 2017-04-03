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
    private int selectedItemIndex;
    private long idOfJourneyEditing = -1;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    private static final int ACTIVITY_RESULT_ADD = 50;
    private static final int ACTIVITY_RESULT_EDIT = 100;

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
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        createListView();
    }

    private void setBottomNavigationItemsStatus() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem edit = menu.findItem(R.id.action_edit);
        MenuItem cancel = menu.findItem(R.id.action_cancel);
        cancel.setEnabled(true);
        if(selectedItemIndex < 0){
            edit.setEnabled(false);
        }
        else{
            edit.setEnabled(true);
        }
    }

    private void setupList(){
        Resources res = getResources();
        journies = carbonFootprintInterface.getJournies(this);
        int journiesSize = journies.size();
        arrayAdapterItems = new CustomizedArrayAdapterItem[journiesSize];
        for (int i = 0; i < journiesSize; i++){
            int imageID = TransportationImageSelect.getImageResource(journies.get(i).getTransportationModel().getImageName());
            if(imageID == -1){
                imageID = R.drawable.car;
            }
            arrayAdapterItems[i] = new CustomizedArrayAdapterItem(
                    imageID,
                    journies.get(i).getTransportationModel().getName(),
                    journies.get(i).getRouteModel().getName(),
                    journies.get(i).getCreationDateString());
        }
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
                    case R.id.action_cancel:
                        finish();
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
        adapter.setSelected(selectedItemIndex);
        journeyList.setAdapter(adapter);
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
            selectedItemIndex = -1;
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
