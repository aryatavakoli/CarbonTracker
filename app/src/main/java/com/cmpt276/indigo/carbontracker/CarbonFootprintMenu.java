package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CarbonFootprintMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbon_footprint_menu);
        //Allows for backbutton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        launchGraphbtn();
        launchTablebtn();
    }


    //Launch graph activity
    private void launchGraphbtn() {
        Button btn = (Button) findViewById(R.id.carbon_footprint_menu_graph_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarbonFootprintMenu.this, CarbonFootprintGraph.class);
                startActivity(intent);

            }
        });


    }

    //Go to go to table  activity
    private void launchTablebtn() {
        Button btn = (Button) findViewById(R.id.carbon_footprint_menu_table_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarbonFootprintMenu.this, CarbonFootprintTable.class);
                startActivity(intent);

            }
        });


    }
}
