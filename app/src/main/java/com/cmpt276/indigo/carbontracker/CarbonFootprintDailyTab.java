package com.cmpt276.indigo.carbontracker;

import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.UtilityModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class CarbonFootprintDailyTab extends Fragment {
    ArrayList<JourneyModel> journeys;
    ArrayList<UtilityModel> utilities;
    CarbonFootprintComponentCollection carbonInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_carbon_footprint_daily_tab, container, false);

        carbonInterface = CarbonFootprintComponentCollection.getInstance();

        journeys = carbonInterface.getJournies();
        utilities = carbonInterface.getUtilities(getActivity());

        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        float totalElectrcityEmissionsToday = getTotalElectrcityEmissionsToday(utilities);
        float totalNaturalGasEmissionsToday = getTotalNaturalGasEmissionsToday(utilities);
        float totalBusEmissionsToday = getTotalBusEmissionsToday(journeys);
        float totalSkytrainEmissionsToday = getTotalSkytrainEmissionsToday(journeys);
        float totalCarEmissionsToday = getTotalCarEmissionsToday(journeys);

        populateGraph(
                totalElectrcityEmissionsToday,
                totalNaturalGasEmissionsToday,
                totalBusEmissionsToday,
                totalSkytrainEmissionsToday,
                totalCarEmissionsToday,
                pieEntries);

        createGraph(rootView, pieEntries);


        return rootView;
    }

    private void createGraph(View rootView, ArrayList<PieEntry> pieEntries) {
        PieChart pieChart = (PieChart) rootView.findViewById(R.id.daily_pie_graph);
        PieDataSet dataSets = new PieDataSet(pieEntries,null);
        PieData data = new PieData(dataSets);
        dataSets.setColors(ColorTemplate.MATERIAL_COLORS);
        pieChart.setDescription(null);
        pieChart.setData(data);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateY(2000);
        pieChart.invalidate();
    }

    private void populateGraph(float electricity,
                               float naturalGas,
                               float bus,
                               float skytrain,
                               float car,
                               ArrayList<PieEntry> pieEntries) {
        pieEntries.add(new PieEntry(electricity,"Electricity"));
        pieEntries.add(new PieEntry(naturalGas,"Natural Gas"));
        pieEntries.add(new PieEntry(car,"Car"));
        pieEntries.add(new PieEntry(bus,"Bus"));
        pieEntries.add(new PieEntry(skytrain,"Skytrain"));
        pieEntries.add(new PieEntry(0f,"Walk/bike"));
    }

    private float getTotalElectrcityEmissionsToday(ArrayList<UtilityModel> utilities) {
        float totalElectrcityEmissionsToday = 5;
        return totalElectrcityEmissionsToday;
    }

    private float getTotalNaturalGasEmissionsToday(ArrayList<UtilityModel> utilities) {
        float totalNaturalGasEmissionsToday = 10;
        return totalNaturalGasEmissionsToday;
    }

    private float getTotalBusEmissionsToday(ArrayList<JourneyModel> journeys) {
        float totalBusEmissionsToday = 15;
        return totalBusEmissionsToday;
    }

    private float getTotalSkytrainEmissionsToday(ArrayList<JourneyModel> journeys) {
        float totalSkytrainEmissionsToday = 20;
        return totalSkytrainEmissionsToday;
    }

    private float getTotalCarEmissionsToday(ArrayList<JourneyModel> journeys) {
        float totalCarEmissionsToday = 25;
        return totalCarEmissionsToday;
    }
}