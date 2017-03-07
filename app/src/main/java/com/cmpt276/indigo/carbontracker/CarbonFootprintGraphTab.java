package com.cmpt276.indigo.carbontracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arya on 05/03/17.
 */

public class CarbonFootprintGraphTab extends Fragment {
    ArrayList<JourneyModel> journies;
    CarbonFootprintComponentCollection carbonInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_carbon_footprint_graph, container, false);

        carbonInterface = CarbonFootprintComponentCollection.getInstance();

        journies = carbonInterface.getJournies();

        PieChart pieChart = (PieChart) rootView.findViewById(R.id.bar_graph);

        ArrayList<String> pieEntrieslabels = new ArrayList<>();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        for (int i = 0; i < journies.size(); i++) {
            pieEntrieslabels.add(journies.get(i).getRouteModel().getName());
            pieEntries.add(new PieEntry(journies.get(i).getCo2Emission(),journies.get(i).getRouteModel().getName()));
        }

        PieDataSet dataSets = new PieDataSet(pieEntries,null);
        PieData data = new PieData(dataSets);
        dataSets.setColors(ColorTemplate.MATERIAL_COLORS);
        pieChart.setDescription(null);
        pieChart.setData(data);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateY(2000);
        pieChart.invalidate();
        return rootView;
    }
}
