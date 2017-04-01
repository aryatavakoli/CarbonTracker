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
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.TransportationModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Arya on 2017-03-16.
 * Implements Daily Pie Graph Tab
 */

public class CarbonFootprintDailyTab extends Fragment {
    public static final double MIN_PERCENTAGE = 0.01;
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
            journeys = carbonInterface.getJournies(getActivity());
            utilities = carbonInterface.getUtilities(getActivity());

            double totalElectrcityEmissionsToday = getTotalElectrcityEmissionsToday(utilities,myCalendar);
            double totalNaturalGasEmissionsToday = getTotalNaturalGasEmissionsToday(utilities,myCalendar);
            double totalBusEmissionsToday = getTotalBusEmissionsToday(journeys,myCalendar);
            double totalSkytrainEmissionsToday = getTotalSkytrainEmissionsToday(journeys,myCalendar);
            double totalCarEmissionsToday = getTotalCarEmissionsToday(journeys,myCalendar);

            populateGraph(
                    (float)totalElectrcityEmissionsToday,
                    (float)totalNaturalGasEmissionsToday,
                    (float)totalBusEmissionsToday,
                    (float)totalSkytrainEmissionsToday,
                    (float)totalCarEmissionsToday,
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
        float total = electricity + naturalGas + bus + skytrain + car;
        if(total > 1e-6) {
            if(electricity / total > MIN_PERCENTAGE) {
                pieEntries.add(new PieEntry(electricity, "Electricity"));
            }
            if(naturalGas / total > MIN_PERCENTAGE) {
                pieEntries.add(new PieEntry(naturalGas, "Natural Gas"));
            }
            if(car / total > MIN_PERCENTAGE) {
                pieEntries.add(new PieEntry(car, "Car"));
            }
            if(bus / total > MIN_PERCENTAGE) {
                pieEntries.add(new PieEntry(bus, "Bus"));
            }
            if(skytrain / total > MIN_PERCENTAGE) {
                pieEntries.add(new PieEntry(skytrain, "Skytrain"));
            }
        }
    }

    private float getTotalElectrcityEmissionsToday(ArrayList<UtilityModel> utilities, Calendar today) {
        float totalElectrcityEmissionsToday = 0;
        for (UtilityModel utility : utilities) {
            if (isSameDay(utility.getStartDate(), today)) {
                Log.d("Hello", "Emission true");
                if (utility.getCompanyName() == UtilityModel.Company.BCHYDRO) {
                    float total_emission = (float) (utility.getDailyCO2EmissionsInSpecifiedUnits());
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
            if (isSameDay(utility.getStartDate(), today)) {
                if (utility.getCompanyName() == UtilityModel.Company.FORTISBC) {
                    float total_emission = (float) (utility.getDailyCO2EmissionsInSpecifiedUnits());
                    totalNaturalGasEmissionsToday += total_emission;
                }
            }
        }
        return totalNaturalGasEmissionsToday;
    }


    private double getTotalBusEmissionsToday(ArrayList<JourneyModel> journeys, Calendar today) {
        double totalBusEmissionsToday = 0;
        for (JourneyModel journey : journeys) {
            if (isSameDay(journey.getCreationDate(), today)) {
                if (journey.getTransportationModel().getTransportaionMode() == TransportationModel.TransportationMode.BUS) {
                    totalBusEmissionsToday = totalBusEmissionsToday + journey.getCo2EmissionInSpecifiedUnits();
                }
            }
        }
        return totalBusEmissionsToday;
    }

    private double getTotalSkytrainEmissionsToday(ArrayList<JourneyModel> journeys, Calendar today) {
        double totalSkytrainEmissionsToday = 0;
        for (JourneyModel journey : journeys) {
            if (isSameDay(journey.getCreationDate(), today)) {
                if (journey.getTransportationModel().getTransportaionMode() == TransportationModel.TransportationMode.SKYTRAIN) {
                    totalSkytrainEmissionsToday = totalSkytrainEmissionsToday + journey.getCo2EmissionInSpecifiedUnits();
                }
            }
        }
        return totalSkytrainEmissionsToday;
    }

    private double getTotalCarEmissionsToday(ArrayList<JourneyModel> journeys, Calendar today) {
        double totalCarEmissionsToday = 0;
        for (JourneyModel journey : journeys) {
            if (isSameDay(journey.getCreationDate(), today)) {
                if (journey.getTransportationModel().getTransportaionMode() == TransportationModel.TransportationMode.CAR) {
                    totalCarEmissionsToday = totalCarEmissionsToday + journey.getCo2EmissionInSpecifiedUnits();
                }
            }
        }
        return totalCarEmissionsToday;
    }

    public boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null)
            return false;
        return (//cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                 cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
}