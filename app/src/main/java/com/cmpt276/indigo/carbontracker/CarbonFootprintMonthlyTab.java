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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


import java.util.ArrayList;

public class CarbonFootprintMonthlyTab extends Fragment {
    public static final int NUMBEROFDAYS = 28;
    ArrayList<JourneyModel> journeys;
    ArrayList<UtilityModel> utilities;
    CarbonFootprintComponentCollection carbonInterface;
    private LineChart mChart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_carbon_footprint_monthly_tab, container, false);

        carbonInterface = CarbonFootprintComponentCollection.getInstance();

        journeys = carbonInterface.getJournies();
        utilities = carbonInterface.getUtilities(getActivity());


        mChart = (LineChart) rootView.findViewById(R.id.chart1);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(true);

        mChart.setDrawBorders(true);

        // no description text
        mChart.getDescription().setEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        Legend l = mChart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setAxisMinimum(0f);
        xAxis.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setDrawGridLines(true);

        mChart.getAxisRight().setEnabled(false);

        // add data
        setData(28);

        mChart.invalidate();
        return rootView;
    }


    private void setupChart(LineChart chart, LineData data) {


        // no description text
        chart.getDescription().setEnabled(false);

        // mChart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background

//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(10, 0, 10, 0);

        // add data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        chart.getXAxis().setEnabled(false);

        // animate calls invalidate()...
        chart.animateX(2500);
    }


    private void setData(int count) {

        double[] dailyElectricityEmissions = new double[NUMBEROFDAYS];
        double[] dailyNaturalGasEmissions = new double[NUMBEROFDAYS];
        double[] dailyJourneyEmissions = new double[NUMBEROFDAYS];

        //TODO: Get Data From Model

        ArrayList<Entry> dailyElectricityEntry = new ArrayList<Entry>();

        for (int day = 1; day < count; day++) {
            dailyElectricityEntry.add(new Entry(day, (float) dailyElectricityEmissions[day]));
        }

        ArrayList<Entry> dailyNaturalGasEntry = new ArrayList<Entry>();

        for (int day = 1; day < count; day++) {
            dailyNaturalGasEntry.add(new Entry(day, (float) dailyNaturalGasEmissions[day]));
        }

        ArrayList<Entry> dailyJourneyEntry = new ArrayList<Entry>();

        for (int day = 1; day < count; day++) {
            dailyJourneyEntry.add(new Entry(day, (float) dailyJourneyEmissions[day]));
        }

        LineDataSet set1, set2 , set3;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet)mChart.getData().getDataSetByIndex(1);
            set1.setValues(dailyElectricityEntry);
            set2.setValues(dailyNaturalGasEntry);
            set1.setValues(dailyJourneyEntry);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(dailyElectricityEntry, "DataSet 1");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set1.setColor(Color.rgb(255, 0, 0));
            set1.setDrawCircles(true);
            set1.setLineWidth(4f);

            // create a dataset and give it a type
            set2 = new LineDataSet(dailyNaturalGasEntry, "DataSet 2");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set2.setColor(Color.rgb(0, 0, 255));
            set2.setDrawCircles(true);
            set2.setLineWidth(4f);

            // create a dataset and give it a type
            set3 = new LineDataSet(dailyJourneyEntry, "DataSet 3");
            set3.setAxisDependency(YAxis.AxisDependency.LEFT);
            set3.setColor(Color.rgb(0, 255 , 0 ));
            set3.setDrawCircles(true);
            set2.setLineWidth(4f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            dataSets.add(set2);
            dataSets.add(set3);

            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            data.setDrawValues(false);

            // set data
            mChart.setData(data);
        }
    }
}




