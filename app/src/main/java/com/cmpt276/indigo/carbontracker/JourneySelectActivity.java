package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * the activity to display all the journey into a listview
 */
public class JourneySelectActivity extends AppCompatActivity {
    ArrayList<JourneyModel> journies;
    private long idOfJourneyEditing = -1;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    private static final int ACTIVITY_RESULT_ADD = 50;
    private static final int ACTIVITY_RESULT_EDIT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_journey_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        startAddActivity();
        createListView();
        setupEditVehicleLongPress();
    }

    private void startAddActivity() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JourneySelectActivity.this, JourneyAddActivity.class);
                startActivityForResult(intent, ACTIVITY_RESULT_ADD);
            }
        });
    }

    private void createListView() {
        //set reference to listview
        ListView carList = (ListView) findViewById(R.id.journey_select_list);
        populateJourneyList();
        //handle click for each element in listview
        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                // Passing selected vehicle to the caller activity
                JourneyModel selectedJourney = journies.get(position);
                intent.putExtra("journey", selectedJourney);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void populateJourneyList() {
        ListView journeyList = (ListView) findViewById(R.id.journey_select_list);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        journies = carbonFootprintInterface.getJournies(this);
        // putting vehicles in list
        List<String> journey_nameList = new ArrayList<>();
        for(JourneyModel v: journies){
            journey_nameList.add("Car: " + v.getVehicleModel().getName() +
                                "   Route: " + v.getRouteModel().getName() +
                                "   Date: " + v.getCreationDateString());
        }

        //Create array adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, //context
                android.R.layout.simple_list_item_1,
                journey_nameList //arrayList
        );
        journeyList.setAdapter(arrayAdapter);
    }

    private void setupEditVehicleLongPress() {
        ListView list = (ListView) findViewById(R.id.journey_select_list);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                JourneyModel journeyModel  = journies.get(position);
                idOfJourneyEditing = journeyModel.getId();
                Intent intent = JourneyAddActivity.makeIntentForEditJourney(JourneySelectActivity.this, journeyModel);
                startActivityForResult(intent, ACTIVITY_RESULT_EDIT);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

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
            populateJourneyList();
        }

    }

}
