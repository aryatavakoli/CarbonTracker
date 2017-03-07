package com.cmpt276.indigo.carbontracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.FuelDataInputStream;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;

/**
 * Created by arya on 05/03/17.
 */

public class CarbonFootprintTableTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_carbon_footprint_table, container, false);

        ListView tableListView = (ListView) rootView.findViewById(R.id.list);

        TableAdapter adapter = new TableAdapter(getActivity(), JourneyModel.getListItems());

        tableListView.setAdapter(adapter);
        return rootView;
    }
}
