package com.cmpt276.indigo.carbontracker.viewjourney;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.cmpt276.indigo.carbontracker.JourneyMenu;
import com.cmpt276.indigo.carbontracker.R;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.TipFragment;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.VehicleModel;

import java.util.ArrayList;

/**
 * the activity to display all the journey into a listview
 */
public class ViewJourneyActivity extends AppCompatActivity {
    ListView tableListView;//listview
    ArrayList<VehicleModel> vehicles;
    ArrayList<RouteModel> routes;
    ArrayList<JourneyModel> journies;
    CarbonFootprintComponentCollection carbonFootprintInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journey);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // init the view
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        vehicles = carbonFootprintInterface.getVehicles(this);
        routes = carbonFootprintInterface.getRoutes(this);
        journies = carbonFootprintInterface.getJournies(this);
        createBtn();
        tableListView = (ListView) findViewById(R.id.list);
        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // to edit the journey
                Intent intent = new Intent(getApplicationContext(), JourneyMenu.class);
                intent.putExtra("journey", (JourneyModel)tableListView.getAdapter().getItem(i));
                // start to edit
                startActivity(intent);

            }
        });
    }


    private void showTipDialog() {
        FragmentManager manager = getSupportFragmentManager();
        TipFragment dialog = new TipFragment();
        dialog.setCancelable(false);
        dialog.show(manager,"message dialog");
        dialog.setVehicles(vehicles);
        dialog.setJournies(journies);
        dialog.setRoutes(routes);


    }

    private void createBtn() {
        Button createBtn = (Button) findViewById(R.id.newJourneyBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                showTipDialog();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // update the list view in order to show the news
        ViewJourneyAdapter adapter = new ViewJourneyAdapter(this);
        tableListView.setAdapter(adapter);
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
