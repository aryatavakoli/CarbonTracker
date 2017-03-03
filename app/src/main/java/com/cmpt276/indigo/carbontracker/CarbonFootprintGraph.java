package com.cmpt276.indigo.carbontracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class CarbonFootprintGraph extends AppCompatActivity {

    float carbonfootprint[] = {10.0f, 20.0f ,30.0f, 40.0f,50.0f,60.0f ,43.0f};

    String journeyName[] = {"Journey 1", "Journey 2" , "Journey 3","Journey 4","Journey 5", "Journey 6","Journey 7"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbon_footprint_graph);
        //Allows for backbutton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupPieGraph();
    }

    private void setupPieGraph() {
        PieChart pieChart = (PieChart) findViewById(R.id.bar_graph);
        ArrayList<String> pieEntrieslabels = new ArrayList<>();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        for (int i = 0; i < carbonfootprint.length; i++) {
            pieEntrieslabels.add(journeyName[i]);
            pieEntries.add(new PieEntry(carbonfootprint[i],journeyName[i]));
        }


        PieDataSet dataSets = new PieDataSet(pieEntries,null);
        PieData data = new PieData(dataSets);
        dataSets.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieChart.setDescription(null);
        pieChart.setData(data);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateY(3000);
        pieChart.invalidate();
    }
}
