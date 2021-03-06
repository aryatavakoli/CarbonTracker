package com.cmpt276.indigo.carbontracker;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.UtilityModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Arya on 2017-03-16.
 * Implements Yearly Bar Graph Tab
 */

public class CarbonFootprintYearlyBarTab extends Fragment {
    public static final int NUMBEROFMONTHS = 12;
    public static final double AVERAGE_YEARLY_CO2_PER_PERSON_IN_KG = 564;
    public static final double AVERAGE_YEARLY_CO2_PER_PERSON_TO_MEET_PARIS_ACCORD = 506;
    ArrayList<JourneyModel> journeys;
    ArrayList<UtilityModel> utilities;
    Calendar today = Calendar.getInstance();

    CarbonFootprintComponentCollection carbonInterface;
    private BarChart mChart;

    ArrayList<String> barLabels;
    ArrayList<BarEntry> barEntries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_carbon_footprint_yearly_bar_tab, container, false);
        carbonInterface = CarbonFootprintComponentCollection.getInstance();

        journeys = carbonInterface.getJournies(getActivity());
        utilities = carbonInterface.getUtilities(getActivity());

        createGraph(rootView, journeys, utilities);

        return rootView;
    }

    private double getMonthCo2(int i) {
//        for each month we get the total co2 of journies in that month + c02 of utilities in that month
        double totalJourneyCo2 = 0;
        for (JourneyModel j : journeys){
            Calendar c = j.getCreationDate();
            if (c.get(Calendar.YEAR) == today.get(Calendar.YEAR) && (c.get(Calendar.MONTH) == i)){
                totalJourneyCo2 = totalJourneyCo2 + j.getCo2EmissionInSpecifiedUnits();
            }

        }
        double totalUtilityCo2 = 0;
        for (UtilityModel u : utilities){
            Calendar c = Calendar.getInstance();
            if ( u.getStartDate().get(Calendar.MONTH) == i && c.get(Calendar.YEAR) == today.get(Calendar.YEAR)){
                totalUtilityCo2 = totalUtilityCo2 + u.getDailyCO2EmissionsInSpecifiedUnits();
            }
        }


        return totalJourneyCo2 + (float) totalUtilityCo2;
    }


    //creates graph
    private void createGraph(View rootView,
                             ArrayList<JourneyModel> journeys,
                             ArrayList<UtilityModel> utilities) {

        mChart = (BarChart) rootView.findViewById(R.id.bargraph2);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setScaleEnabled(false);

        ArrayList<String> barLabels = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        //TODO: POPULATE ARRAY
        double[] emissionsData = new double[NUMBEROFMONTHS];
//        make an array of 12 for 12 months of a year
//        set each month to its own C02 emission which has been calculated
        emissionsData[0] = getMonthCo2(0);
        emissionsData[1] = getMonthCo2(1);
        emissionsData[2] = getMonthCo2(2);
        emissionsData[3] = getMonthCo2(3);
        emissionsData[4] = getMonthCo2(4);
        emissionsData[5] = getMonthCo2(5);
        emissionsData[6] = getMonthCo2(6);
        emissionsData[7] = getMonthCo2(7);
        emissionsData[8] = getMonthCo2(8);
        emissionsData[9] = getMonthCo2(9);
        emissionsData[10] = getMonthCo2(10);
        emissionsData[11] = getMonthCo2(11);


        //populates graph
        populateBarLabels(barLabels);
        populateEntries(barEntries,emissionsData);

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


        LimitLine limitLine = new LimitLine((float) AVERAGE_YEARLY_CO2_PER_PERSON_IN_KG, "Average Canadian consumption");
        LimitLine limitLine2 = new LimitLine((float) AVERAGE_YEARLY_CO2_PER_PERSON_TO_MEET_PARIS_ACCORD, "Paris Accord Goal");
        limitLine.setLineWidth(1f);
        limitLine.setTextSize(10f);
        limitLine.setTextColor(Color.RED);
        limitLine.setLineColor(Color.RED);

        limitLine2.setLineWidth(1f);
        limitLine2.setTextSize(10f);
        limitLine2.setTextColor(Color.GREEN);
        limitLine2.setLineColor(Color.GREEN);
        yAxis.addLimitLine(limitLine);
        yAxis.addLimitLine(limitLine2);

        mChart.setData(data);
        mChart.setFitBars(true);
    }

    private void populateEmissionsDataAtIndex(ArrayList<Float> emissionsData, int index, float value) {
        emissionsData.add(value);
    }

    private void populateBarLabels(ArrayList<String> barLabels) {
//        label the bars for the graph
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

    private void populateEntries(ArrayList<BarEntry> barEntries, double[] emissionsData) {
        for (int i = 0; i < emissionsData.length; i++ ) {
            barEntries.add(new BarEntry(i, (float) emissionsData[i]));
        }
    }
}