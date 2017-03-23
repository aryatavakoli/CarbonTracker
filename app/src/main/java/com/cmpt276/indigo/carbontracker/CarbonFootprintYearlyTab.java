package com.cmpt276.indigo.carbontracker;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.UtilityModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class CarbonFootprintYearlyTab extends Fragment {
    public static final int NUMBEROFDAYS = 28;
    ArrayList<JourneyModel> journeys;
    ArrayList<UtilityModel> utilities;
    CarbonFootprintComponentCollection carbonInterface;
    private BarChart mChart;

    ArrayList<String> barLabels;
    ArrayList<BarEntry> barEntries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_carbon_footprint_yearly_tab, container, false);
        carbonInterface = CarbonFootprintComponentCollection.getInstance();

        journeys = carbonInterface.getJournies();
        utilities = carbonInterface.getUtilities(getActivity());

        createGraph(rootView,journeys,utilities);

        return rootView;
    }
    //creates graph
    private void createGraph(View rootView ,
                             ArrayList<JourneyModel> journeys,
                             ArrayList<UtilityModel> utilities) {

        mChart = (BarChart) rootView.findViewById(R.id.chart2);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setScaleEnabled(false);

        ArrayList<String> barLabels = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        //populates graph
        populateBarLabels(barLabels);

        populateEntries(barEntries);

        //Xaxis properties
        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(barLabels));

        //Yaxis properties
        YAxis yAxis = mChart.getAxisLeft();
        mChart.getAxisRight().setEnabled(false);
        yAxis.setAxisMinimum(0);

        BarDataSet set1;

        set1 = new BarDataSet(barEntries, "MONTH");
        set1.setColors(ColorTemplate.MATERIAL_COLORS);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        mChart.setData(data);
        mChart.setFitBars(true);
    }

    private void populateBarLabels( ArrayList<String> barLabels) {
        barLabels.add("Jan");
        barLabels.add("Feb");
        barLabels.add("Mar");
        barLabels.add("Apr");
        barLabels.add("May");
        barLabels.add("Jun");
        barLabels.add("Jul");
        barLabels.add("Aug");
        barLabels.add("Sep");
        barLabels.add("Oct");
        barLabels.add("Nov");
        barLabels.add("Dec");
    }

    private void populateEntries(ArrayList<BarEntry> barEntries){
        barEntries.add(new BarEntry(0, 50));
        barEntries.add(new BarEntry(1, 60));
        barEntries.add(new BarEntry(2, 70));
        barEntries.add(new BarEntry(3, 80));
        barEntries.add(new BarEntry(4, 90));
        barEntries.add(new BarEntry(5, 100));
        barEntries.add(new BarEntry(6, 110));
        barEntries.add(new BarEntry(7, 120));
        barEntries.add(new BarEntry(8, 130));
        barEntries.add(new BarEntry(9, 140));
        barEntries.add(new BarEntry(10, 150));
        barEntries.add(new BarEntry(11, 160));


    }

    //TODO: Modify these to get bar chart data
    public float getTotalElectrcityEmissions(ArrayList<UtilityModel> utilities) {
        float totalElectrcity = 5;//sample data
        return totalElectrcity;
    }

    public float getTotalNaturalGasEmissions(ArrayList<UtilityModel> utilities) {
        float totalNaturalGasEmissions = 10;//sample
        return totalNaturalGasEmissions;
    }

    public float getTotalBusEmissions(ArrayList<JourneyModel> journeys) {
        float totalBusEmissions = 15;//sample
        return totalBusEmissions;
    }

    public float getTotalSkytrainEmissions(ArrayList<JourneyModel> journeys) {
        float totalSkytrainEmissions = 20;//sample
        return totalSkytrainEmissions;
    }

    public float getTotalCarEmissions(ArrayList<JourneyModel> journeys) {
        float totalCarEmissions = 25; //
        return totalCarEmissions;
    }
}
