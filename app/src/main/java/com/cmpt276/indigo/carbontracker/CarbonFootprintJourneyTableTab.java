package com.cmpt276.indigo.carbontracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by arya on 05/03/17.
 * Implements the Table for each journey
 */

public class CarbonFootprintJourneyTableTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_carbon_footprint_journey_table, container, false);

        ListView tableListView = (ListView) rootView.findViewById(R.id.list);

        TableAdapter adapter = new TableAdapter(getActivity());

        tableListView.setAdapter(adapter);
        return rootView;
    }
}
