package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

public class UtilitySelectActivity extends AppCompatActivity {

    private int indexOfUtilitiesEditing = -1;
    private static final int ACTIVITY_RESULT_ADD = 30;
    private static final int ACTIVITY_RESULT_EDIT = 90;

    List<Integer> utility_positionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_utility_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Allows for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        startAddActivity();
    }

    private void startAddActivity() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UtilitySelectActivity.this,UtilityAddActivity.class);
                startActivity(intent);
                startActivityForResult(intent, ACTIVITY_RESULT_ADD);
            }
        });
    }


}
