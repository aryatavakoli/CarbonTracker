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
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.UtilityModel;

import java.util.ArrayList;
import java.util.List;

/*
    implements Utility Select activity
 */
public class UtilitySelectActivity extends AppCompatActivity {

    private int indexOfUtilityEditing = -1;
    private static final int ACTIVITY_RESULT_ADD = 30;
    private static final int ACTIVITY_RESULT_EDIT = 90;

    CarbonFootprintComponentCollection carbonFootprintInterface;
    List<Integer> utilityPositionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_utility_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Allows for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();

        startAddActivity();
        createListView();
        setupEditUtilityLongPress();
    }

    private void startAddActivity() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UtilitySelectActivity.this,UtilityAddActivity.class);
                startActivityForResult(intent, ACTIVITY_RESULT_ADD);
            }
        });
    }

    //sample for demonstartion purposes
    private void createListView() {
        //set reference to listview
        ListView utilitiesList = (ListView) findViewById(R.id.utilities_select_list);
        populateUtilitiesList();

        //handle click for each element in listview
        utilitiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), UtilityResultActivity.class);
                // Passing selected Utilities to the caller activity
                int realPosition = utilityPositionList.get(position);
                UtilityModel selectedUtility = carbonFootprintInterface.getUtilities().get(realPosition);
                intent.putExtra("utility", selectedUtility);
                setResult(RESULT_OK, intent);
                startActivity(intent);
            }
        });
    }

    private void populateUtilitiesList() {
        ListView utilitiesList = (ListView) findViewById(R.id.utilities_select_list);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        ArrayList<UtilityModel> utilities = carbonFootprintInterface.getUtilities();
        // putting Utilities in list
        List<String> utilityNameList = new ArrayList<>();
        utilityPositionList = new ArrayList<>();
        //Add elements
        int counter = 0;
        for(UtilityModel v: utilities){
            if(!v.getIsDeleted()) {
                utilityNameList.add(v.getName());
                utilityPositionList.add(counter);
            }
            counter++;
        }

        //Create array adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, //context
                android.R.layout.simple_list_item_1,
                utilityNameList //arrayList
        );

        //apply adapter ro listview
        utilitiesList.setAdapter(arrayAdapter);
    }

    private void setupEditUtilityLongPress() {
        final ArrayList<UtilityModel> utilities = carbonFootprintInterface.getUtilities();
        ListView list = (ListView) findViewById(R.id.utilities_select_list);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int realPosition = utilityPositionList.get(position);
                indexOfUtilityEditing = realPosition;
                UtilityModel utility = utilities.get(realPosition);
                Intent intent = UtilityAddActivity.makeIntentForEditUtility(UtilitySelectActivity.this, utility);
                startActivityForResult(intent, ACTIVITY_RESULT_EDIT);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == RESULT_OK){
            switch (requestCode) {
                case ACTIVITY_RESULT_ADD:
                    populateUtilitiesList();
                    break;
                case ACTIVITY_RESULT_EDIT:
                    UtilityModel modifiedUtility = (UtilityModel) data.getSerializableExtra("utility");
                    modifiedUtility.setId(indexOfUtilityEditing);
                    populateUtilitiesList();
                    break;
            }
        }
        else if (resultCode == UtilityAddActivity.RESULT_DELETE){
            populateUtilitiesList();
        }

    }


}
