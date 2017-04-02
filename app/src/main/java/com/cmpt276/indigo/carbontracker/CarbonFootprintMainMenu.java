package com.cmpt276.indigo.carbontracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Implements MainMenu Activity
 */
public class CarbonFootprintMainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbon_footprint_main_menu);
        //Allows for backbutton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        journeyDataBtn();
        periodicDataBtn();
    }

    private void journeyDataBtn() {
        Button btn = (Button) findViewById(R.id.carbon_footprint_main_menu_journey_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarbonFootprintMainMenu.this, CarbonFootprintJourneyFootprintMenu.class);
                startActivity(intent);
            }
        });
    }
//
    private void periodicDataBtn() {
        Button btn = (Button) findViewById(R.id.carbon_footprint_main_menu_periodic_data_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarbonFootprintMainMenu.this, CarbonFootprintPeriodicDataMenu.class);
                startActivity(intent );
            }
        });
    }

    public static Intent makeIntent(Context packageContext) {
        return new Intent(packageContext, CarbonFootprintMainMenu.class);
    }
}
