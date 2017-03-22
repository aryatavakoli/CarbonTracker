package com.cmpt276.indigo.carbontracker.viewjourney;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.cmpt276.indigo.carbontracker.JourneyMenu;
import com.cmpt276.indigo.carbontracker.R;
import com.cmpt276.indigo.carbontracker.TransportationAddActivity;
import com.cmpt276.indigo.carbontracker.TransportationSelectActvitiy;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.VehicleModel;

import java.util.ArrayList;

/**
 * the activity to select the transport type
 */
public class SelectTransActivity extends AppCompatActivity {

    private Spinner spinner;
    public static final int TRANSPORTATION_SELECT = 56;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journey);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        spinner = (Spinner) findViewById(R.id.spinner);//the spinner to list the transport types
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = (String) spinner.getSelectedItem();
                //select the cat type
                if (item.equals("Car")) {
                    Intent intent = new Intent(SelectTransActivity.this, TransportationSelectActvitiy.class);
                    startActivityForResult(intent, TRANSPORTATION_SELECT );
                } else {
                    //select the other type
                    Intent intent = new Intent();
                    VehicleModel selectedVehicle = new VehicleModel();
                    selectedVehicle.setName(item);
                    intent.putExtra("vehicle", selectedVehicle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TRANSPORTATION_SELECT:
                    // when select the cat type ,TransportationSelectActvitiy will return the the vehicle
                    Intent intent = getIntent();
                    intent.putExtra("vehicle", (VehicleModel) data.getSerializableExtra("vehicle"));
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // to back
            onBackPressed();
        }
        return true;
    }
}
