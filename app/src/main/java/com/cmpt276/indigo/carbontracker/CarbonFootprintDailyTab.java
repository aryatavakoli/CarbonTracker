package com.cmpt276.indigo.carbontracker;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CarbonFootprintDailyTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_carbon_footprint_daily_tab, container, false);

        Button btn = (Button) rootView.findViewById(R.id.refresh_on_date_pick);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPieGraph();
            }
        });
        return rootView;
    }

    private void createPieGraph() {
    }
}
