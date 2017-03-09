package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;

import java.io.InputStream;
/*

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
        journeySelectBtn();
        carbonFootprintSelectBtn();

    }
    //Go to carbon footprint activity
    private void carbonFootprintSelectBtn() {
        Button btn = (Button) findViewById(R.id.main_menu_carbonfootprint_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, CarbonFootprintMenu.class);
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
    private void journeySelectBtn() {
        Button btn = (Button) findViewById(R.id.main_menu_create_Journey_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, JourneyMenu.class);
                startActivityForResult(intent, JOURNEY_SELECT );
            }
        });
    }
}



