package com.cmpt276.indigo.carbontracker.viewjourney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.cmpt276.indigo.carbontracker.R;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;

import java.util.ArrayList;

public class EditJourneyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journey);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final int position = getIntent().getIntExtra("position", -1);
        final CarbonFootprintComponentCollection carbonInterface = CarbonFootprintComponentCollection.getInstance();
        final ArrayList<JourneyModel> journies = carbonInterface.getJournies();
        findViewById(R.id.delete_journey_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                journies.remove(position);
                onBackPressed();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
