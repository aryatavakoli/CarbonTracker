package com.cmpt276.indigo.carbontracker;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Calendar;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.UtilityModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.VehicleModel;
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


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Arya on 2017-03-16.
 * Implements Daily monthly Graph Tab
 */
//change this
public class CarbonFootprintMonthlyTab extends Fragment {
    public static final int NUMBEROFDAYS = 28;
    ArrayList<JourneyModel> journeys;
    ArrayList<UtilityModel> utilities;
    CarbonFootprintComponentCollection carbonInterface;
    ArrayList<JourneyModel> journey28;
    ArrayList<UtilityModel> utility28;
    Calendar today = Calendar.getInstance();
    Calendar last28 = Calendar.getInstance();
    Calendar tomorrow = Calendar.getInstance();
    private BarChart mChart;

    ArrayList<String> barLabels;
    ArrayList<BarEntry> barEntries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_carbon_footprint_monthly_tab, container, false);

        carbonInterface = CarbonFootprintComponentCollection.getInstance();

        journeys = carbonInterface.getJournies();
        utilities = carbonInterface.getUtilities(getActivity());
        last28.add(Calendar.DAY_OF_MONTH,-28);
        tomorrow.add(Calendar.DAY_OF_MONTH,1);




        createGraph(rootView,journeys,utilities);

        return rootView;
    }
    //creates graph
    private void createGraph(View rootView ,
                             ArrayList<JourneyModel> journeys,
                             ArrayList<UtilityModel> utilities) {

        mChart = (BarChart) rootView.findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setScaleEnabled(false);

        ArrayList<String> barLabels = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        //Gets Values
        float totalElectrcityEmissions = getTotalElectrcityEmissions(utilities);
        float totalNaturalGasEmissions = getTotalNaturalGasEmissions(utilities);
        float totalBusEmissions = getTotalBusEmissions(journeys);
        float totalSkytrainEmissions = getTotalSkytrainEmissions(journeys);
        float totalCarEmissions = getTotalCarEmissions(journeys);

        //populates graph
        populateUtilityEntries(
                totalElectrcityEmissions,
                totalNaturalGasEmissions,
                barLabels,
                barEntries);

        populateJourneyEntries(
                totalBusEmissions,
                totalSkytrainEmissions,
                totalCarEmissions,
                barLabels,
                barEntries);

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

        set1 = new BarDataSet(barEntries, "EMISSION TYPE");
        set1.setColors(ColorTemplate.MATERIAL_COLORS);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        mChart.setData(data);
        mChart.setFitBars(true);
    }

    private void populateUtilityEntries(float totalElectrcity,
                                        float totalNaturalGas,
                                        ArrayList<String> barLabels,
                                        ArrayList<BarEntry> barEntries) {
            barEntries.add(new BarEntry(0, totalElectrcity));
            barLabels.add("Electric");
            barEntries.add(new BarEntry(1, totalNaturalGas));
            barLabels.add("GAS");
    }

    private void populateJourneyEntries(float totalBusEmissions,
                                        float totalSkytrainEmissions,
                                        float totalCarEmissions,
                                        ArrayList<String> barLabels,
                                        ArrayList<BarEntry> barEntries){
        barEntries.add(new BarEntry(2, totalBusEmissions));
        barLabels.add("Bus");
        barEntries.add(new BarEntry(3, totalSkytrainEmissions));
        barLabels.add("Skytrain");
        barEntries.add(new BarEntry(4, totalCarEmissions));
        barLabels.add("Car");
        barEntries.add(new BarEntry(5, 0));
        barLabels.add("Walk/Bike");

    }

    //TODO: Modify these to get bar chart data
    public float getTotalElectrcityEmissions(ArrayList<UtilityModel> utilities) {
        float totalElectrcity = 0;//sample data
        for (UtilityModel utility : utilities){
            if (utility.getStartDate().before(tomorrow) && utility.getStartDate().after(last28)) {
                if (utility.getCompanyName() == UtilityModel.Company.BCHYDRO) {
                    float total_emission = (float) (utility.getNumberOfOccupants() * utility.getTotalEmissionsPerOccupant());
                    totalElectrcity = totalElectrcity + total_emission;
                }
            }
        }
        return totalElectrcity;
    }

    public float getTotalNaturalGasEmissions(ArrayList<UtilityModel> utilities) {
        float totalNaturalGasEmissions = 0;//sample
        for (UtilityModel utility : utilities) {
            if (utility.getStartDate().before(tomorrow) && utility.getStartDate().after(last28)) {
                if (utility.getCompanyName() == UtilityModel.Company.FORTISBC) {
                    float total_emission = (float) (utility.getNumberOfOccupants() * utility.getTotalEmissionsPerOccupant());
                    totalNaturalGasEmissions = totalNaturalGasEmissions + total_emission;
                }
            }
        }
        return totalNaturalGasEmissions;
    }

    public float getTotalBusEmissions(ArrayList<JourneyModel> journeys) {
        float totalBusEmissions = 0;//sample
        for (JourneyModel journey : journeys) {
            if (journey.getCreationDate().after(last28.getTime()) && journey.getCreationDate().before(tomorrow.getTime())) {
                if (journey.getVehicleModel().getTransportaionMode() == VehicleModel.TransportationMode.BUS) {
                    totalBusEmissions = totalBusEmissions + journey.getCo2Emission();
                }
            }
        }
        return totalBusEmissions;
    }

    public float getTotalSkytrainEmissions(ArrayList<JourneyModel> journeys) {
        float totalSkytrainEmissions = 0;//sample
        for (JourneyModel journey : journeys) {
            if (journey.getCreationDate().after(last28.getTime()) && journey.getCreationDate().before(tomorrow.getTime())) {
                if (journey.getVehicleModel().getTransportaionMode() == VehicleModel.TransportationMode.SKYTRAIN) {
                    totalSkytrainEmissions = totalSkytrainEmissions + journey.getCo2Emission();
                }
            }
        }
            return totalSkytrainEmissions;

    }


    public float getTotalCarEmissions(ArrayList<JourneyModel> journeys) {
        float totalCarEmissions = 0; //
        for (JourneyModel journey : journeys) {
            if (journey.getCreationDate().after(last28.getTime()) && journey.getCreationDate().before(tomorrow.getTime())) {
                if (journey.getVehicleModel().getTransportaionMode() == VehicleModel.TransportationMode.CAR) {
                    totalCarEmissions = totalCarEmissions + journey.getCo2Emission();
                }
            }
        }
        return totalCarEmissions;
    }
}