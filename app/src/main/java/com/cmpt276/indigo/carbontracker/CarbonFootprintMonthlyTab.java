package com.cmpt276.indigo.carbontracker;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.UtilityModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


import java.util.ArrayList;
import java.util.StringTokenizer;

public class CarbonFootprintMonthlyTab extends Fragment {
    public static final int NUMBEROFDAYS = 28;
    ArrayList<JourneyModel> journeys;
    ArrayList<UtilityModel> utilities;
    CarbonFootprintComponentCollection carbonInterface;
    private BarChart mChart;
    ArrayList<Entry> dailyElectricityEntry;
    ArrayList<Entry> dailyNaturalGasEntry;
    ArrayList<Entry> dailyJourneyEntry;
    ArrayList<BarEntry> barEntries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_carbon_footprint_monthly_tab, container, false);

        carbonInterface = CarbonFootprintComponentCollection.getInstance();

        journeys = carbonInterface.getJournies();
        utilities = carbonInterface.getUtilities(getActivity());

        ArrayList<String> barLabels = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        mChart = (BarChart) rootView.findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setScaleEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setGranularityEnabled(true);

        BarDataSet set1;
        set1 = new BarDataSet(barEntries, "The year 2017");

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);


        return rootView;
    }


    public static int generateRandomPositiveValue(int max, int min) {
        //Random rand = new Random();
        int ii = min + (int) (Math.random() * ((max - (min)) + 1));
        return ii;
    }
}