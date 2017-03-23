package com.cmpt276.indigo.carbontracker;

import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.UtilityModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CarbonFootprintDailyTab extends Fragment {
    ArrayList<JourneyModel> journeys;
    ArrayList<UtilityModel> utilities;
    CarbonFootprintComponentCollection carbonInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.activity_carbon_footprint_daily_tab, container, false);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            ArrayList<PieEntry> pieEntries = new ArrayList<>();

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                final Button txt = (Button) rootView.findViewById(R.id.refresh_on_date_pick);
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                displayDate(txt, myCalendar);
                pieEntries.clear();

                carbonInterface = CarbonFootprintComponentCollection.getInstance();
                journeys = carbonInterface.getJournies();
                utilities = carbonInterface.getUtilities(getActivity());

                float totalElectrcityEmissionsToday = getTotalElectrcityEmissionsToday(utilities,myCalendar);
                float totalNaturalGasEmissionsToday = getTotalNaturalGasEmissionsToday(utilities,myCalendar);
                float totalBusEmissionsToday = getTotalBusEmissionsToday(journeys,myCalendar);
                float totalSkytrainEmissionsToday = getTotalSkytrainEmissionsToday(journeys,myCalendar);
                float totalCarEmissionsToday = getTotalCarEmissionsToday(journeys,myCalendar);

                populateGraph(
                        totalElectrcityEmissionsToday,
                        totalNaturalGasEmissionsToday,
                        totalBusEmissionsToday,
                        totalSkytrainEmissionsToday,
                        totalCarEmissionsToday,
                        pieEntries);

                createGraph(rootView, pieEntries);
            }
        };
        final Button txt = (Button) rootView.findViewById(R.id.refresh_on_date_pick);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return rootView;
    }

    private void displayDate(Button btn, Calendar c) {
        String date = calendarToString(c);
        btn.setText(date);
    }

    private String calendarToString(Calendar c) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
        return formatter.format(c.getTime());
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

    private float getTotalElectrcityEmissionsToday(ArrayList<UtilityModel> utilities, Calendar today) {
        float totalElectrcityEmissionsToday = 0;
        for (UtilityModel utility : utilities) {
            if (utility.getStartDate().before(today) && utility.getEndDate().after(today)) {
                Log.d("Je", "If statement = true");
                if (utility.getCompanyName() == UtilityModel.Company.BCHYDRO) {
                    float total_emission = (float) (utility.calculateDailyCO2EmissionsInKg());
                    totalElectrcityEmissionsToday += total_emission;
                }
            }
        }
        Log.d("Hello", "Emission: " + totalElectrcityEmissionsToday);
        return totalElectrcityEmissionsToday;
    }

    private float getTotalNaturalGasEmissionsToday(ArrayList<UtilityModel> utilities, Calendar today) {
        float totalNaturalGasEmissionsToday = 0;
        for (UtilityModel utility : utilities) {
            if (utility.getStartDate().before(today) && utility.getEndDate().after(today)) {
                if (utility.getCompanyName() == UtilityModel.Company.FORTISBC) {
                    float total_emission = (float)  utility.calculateDailyCO2EmissionsInKg();
                    totalNaturalGasEmissionsToday = totalNaturalGasEmissionsToday + total_emission;
                }
            }
        }
        return totalNaturalGasEmissionsToday;
    }

    private float getTotalBusEmissionsToday(ArrayList<JourneyModel> journeys, Calendar today) {
        float totalBusEmissionsToday = 15;
        return totalBusEmissionsToday;
    }

    private float getTotalSkytrainEmissionsToday(ArrayList<JourneyModel> journeys, Calendar today) {
        float totalSkytrainEmissionsToday = 20;
        return totalSkytrainEmissionsToday;
    }

    private float getTotalCarEmissionsToday(ArrayList<JourneyModel> journeys, Calendar today) {
        float totalCarEmissionsToday = 25;
        return totalCarEmissionsToday;
    }
}