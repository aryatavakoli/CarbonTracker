package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.viewjourney.ViewJourneyActivity;

import java.io.InputStream;
/*
    implments main menu activity
 */
public class MainMenu extends AppCompatActivity {
    public static final int JOURNEY_SELECT = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        // process might be killed off while paused and the app is in the background.
        // In that case, the flag will be false when the activity is recreated by
        // the system when the user tries to bring the app back to the foreground/
        //use shared preference to get aroundt his
        loadDataFile();
        journeyCreateBtn();
        carbonFootprintSelectBtn();
        journeyViewBtn();
        utilitesCreateBtn();
    }
    //Go to carbon footprint activity
    private void carbonFootprintSelectBtn() {
        Button btn = (Button) findViewById(R.id.main_menu_carbonfootprint_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, CarbonFootprintMainMenu.class);
                startActivity(intent);
            }
        });
    }

    private void loadDataFile() {
        CarbonFootprintComponentCollection carbonFootprint = CarbonFootprintComponentCollection.getInstance();
        InputStream is = getResources().openRawResource(R.raw.vehicles);
        carbonFootprint.loadDataFile(is);
    }

    //Launch Create a jounrney activity
    private void journeyCreateBtn() {
        Button btn = (Button) findViewById(R.id.main_menu_create_Journey_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, JourneyMenu.class);
                startActivityForResult(intent, JOURNEY_SELECT );
            }
        });
    }

    private void journeyViewBtn() {
        Button btn = (Button) findViewById(R.id.main_menu_view_journey_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, ViewJourneyActivity.class);
                startActivity(intent );
            }
        });
    }

    //Launch Create a utitliy activity
    private void utilitesCreateBtn() {
        Button btn = (Button) findViewById(R.id.main_menu_create_utilities_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, UtilitySelectActivity.class);
                //startActivityForResult(intent, JOURNEY_SELECT );
                startActivity(intent);
            }
        });
    }
}



