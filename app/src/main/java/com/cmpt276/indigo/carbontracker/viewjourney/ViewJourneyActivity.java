package com.cmpt276.indigo.carbontracker.viewjourney;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cmpt276.indigo.carbontracker.JourneyMenu;
import com.cmpt276.indigo.carbontracker.R;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;

public class ViewJourneyActivity extends AppCompatActivity {
    ListView tableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journey);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tableListView = (ListView) findViewById(R.id.list);
        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), JourneyMenu.class);
                intent.putExtra("journey", (JourneyModel)tableListView.getAdapter().getItem(i));
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TableAdapter adapter = new TableAdapter(this);
        tableListView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
