package com.cmpt276.indigo.carbontracker;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.HashMap;
import java.util.Map;

public class TransportationImageSelect extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static String IMAGE_EXCHANGE_NAME = "image_id";
    private int resourceIDs[];
    public static int TRANSPORTATION_IMAGE_SELECT_CALL_CODE = 50;
    private static Map<Integer, String> imageMap = getImageMaps();

    private static Map<Integer, String> getImageMaps(){
        Map<Integer, String> maps = new HashMap<>();
        maps.put(R.mipmap.ic_transportation_bike, "transportation_bike");
        maps.put(R.mipmap.ic_transportation_bus, "transportation_bus");
        maps.put(R.mipmap.ic_transportation_sedan, "transportation_sedan");
        maps.put(R.mipmap.ic_transportation_suv, "transportation_suv");
        return maps;
    }

    public static int getImageResource(String imageName){
        for(Map.Entry<Integer, String> entry : imageMap.entrySet()){
            if(entry.getValue().equals(imageName)){
                return entry.getKey();
            }
        }
        return -1;
    }

    private static String getImageResource(int imageResource){
        if(imageMap.containsKey(new Integer(imageResource))){
            return imageMap.get(new Integer(imageResource));
        }
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_image_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        populateResourceIDs();
        loadImages();
        setImageClickListener();
    }

    private void populateResourceIDs() {
        resourceIDs = new int[imageMap.size()];
        int counter = 0;
        for(Map.Entry<Integer, String> entry : imageMap.entrySet()){
            resourceIDs[counter] = entry.getKey();
            counter++;
        }
    }

    private void setImageClickListener() {
        GridView view = (GridView)findViewById(R.id.content_transportation_image_select);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(IMAGE_EXCHANGE_NAME, getImageResource(resourceIDs[position]));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void loadImages() {
        ImageArrayAdapter adapter = new ImageArrayAdapter(this, resourceIDs);
        GridView view = (GridView)findViewById(R.id.content_transportation_image_select);
        view.setAdapter(adapter);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return SideNavigationManager.handleSideNavigationSelection(this, item);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TransportationImageSelect.class);
    }
}
