package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponent;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;

import java.io.InputStream;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        carbonFootprintSelectbtn();
        journeySelectbtn();
        loadDataFile();
    }

    private void loadDataFile() {
        CarbonFootprintComponentCollection carbonFootprint = CarbonFootprintComponentCollection.getInstance();
        InputStream is = getResources().openRawResource(R.raw.vehicles);
        carbonFootprint.loadDataFile(is);
    }

    //Launch Create a jounrney activity
    private void journeySelectbtn() {
        Button btn = (Button) findViewById(R.id.main_menu_create_Journey_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, JourneyMenu.class);
                startActivity(intent);
            }
        });
    }

    //Go to carbon footprint activity
    private void carbonFootprintSelectbtn() {
        Button btn = (Button) findViewById(R.id.main_menu_carbonfootprint_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, CarbonFootprintMenu.class);
                startActivity(intent);
            }
        });
    }
}
