package com.cmpt276.indigo.carbontracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.TransportationModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.UtilityModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Arya on 2017-03-16.
 * Implements Yearly Pie Graph Tab
 */

public class CarbonFootprintYearlyPieTab extends Fragment {
    public static final int NUMBEROFMONTHS = 12;
    public static final int NUMBEROFDAYS = 365;
    public static final double MIN_PERCENTAGE = 0.01;
    ArrayList<JourneyModel> journeys;
    ArrayList<UtilityModel> utilities;
    Calendar last365 = Calendar.getInstance();
    Calendar tomorrow = Calendar.getInstance();
    Calendar today = Calendar.getInstance();

    CarbonFootprintComponentCollection carbonInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_carbon_footprint_yearly_pie_tab, container, false);

        carbonInterface = CarbonFootprintComponentCollection.getInstance();

        journeys = carbonInterface.getJournies(getActivity());
        utilities = carbonInterface.getUtilities(getActivity());
        last365.add(Calendar.DAY_OF_MONTH,-365);
        tomorrow.add(Calendar.DAY_OF_MONTH,1);


        createGraph(rootView,journeys,utilities);

        return rootView;
    }
    //creates graph
    private void createGraph(View rootView ,
                             ArrayList<JourneyModel> journeys,
                             ArrayList<UtilityModel> utilities) {

        PieChart pieChart = (PieChart) rootView.findViewById(R.id.yearly_pie_graph);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        //Gets Values
        float totalElectrcityEmissions = getTotalElectrcityEmissions(utilities);
        float totalNaturalGasEmissions = getTotalNaturalGasEmissions(utilities);
        float totalBusEmissions = (float) getTotalBusEmissions(journeys);
        float totalSkytrainEmissions = (float) getTotalSkytrainEmissions(journeys);
        float totalCarEmissions = (float) getTotalCarEmissions(journeys);

        //populates graph
        populateGraph(
                totalElectrcityEmissions,
                totalNaturalGasEmissions,
                totalBusEmissions,
                totalSkytrainEmissions,
                totalCarEmissions,
                pieEntries
        );


        PieDataSet dataSets = new PieDataSet(pieEntries, null);
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
                pieEntries.add(new PieEntry(electricity, getString(R.string.electricity_pie_tab)));
            }
            if(naturalGas / total > MIN_PERCENTAGE) {
                pieEntries.add(new PieEntry(naturalGas, getString(R.string.natural_gas_pie_tab)));
            }
            if(car / total > MIN_PERCENTAGE) {
                pieEntries.add(new PieEntry(car, getString(R.string.car__pie_tab)));
            }
            if(bus / total > MIN_PERCENTAGE) {
                pieEntries.add(new PieEntry(bus, getString(R.string.bus__pie_tab)));
            }
            if(skytrain / total > MIN_PERCENTAGE) {
                pieEntries.add(new PieEntry(skytrain, getString(R.string.skytrain__pie_tab)));
            }
        }
    }


    //TODO: Modify these to get PIE chart data
    public float getTotalElectrcityEmissions(ArrayList<UtilityModel> utilities) {
        float totalElectrcity = 0;//sample data
        for (UtilityModel utility : utilities){
            if (utility.getStartDate().before(tomorrow) && utility.getStartDate().after(last365)) {
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
            if (utility.getStartDate().before(tomorrow) && utility.getStartDate().after(last365)) {
                if (utility.getCompanyName() == UtilityModel.Company.FORTISBC) {
                    float total_emission = (float) (utility.getNumberOfOccupants() * utility.getTotalEmissionsPerOccupant());
                    totalNaturalGasEmissions = totalNaturalGasEmissions + total_emission;
                }
            }
        }
        return totalNaturalGasEmissions;
    }

    public double getTotalBusEmissions(ArrayList<JourneyModel> journeys) {
        double totalBusEmissions = 0;//sample
        for (JourneyModel journey : journeys) {
            if (journey.getCreationDate().after(last365) && journey.getCreationDate().before(tomorrow)) {
                if (journey.getTransportationModel().getTransportaionMode() == TransportationModel.TransportationMode.BUS) {
                    totalBusEmissions = totalBusEmissions + journey.getCo2Emission();
                }
            }
        }
        return totalBusEmissions;
    }

    public double getTotalSkytrainEmissions(ArrayList<JourneyModel> journeys) {
        double totalSkytrainEmissions = 0;//sample
        for (JourneyModel journey : journeys) {
            if (journey.getCreationDate().after(last365) && journey.getCreationDate().before(tomorrow)) {
                if (journey.getTransportationModel().getTransportaionMode() == TransportationModel.TransportationMode.SKYTRAIN) {
                    totalSkytrainEmissions = totalSkytrainEmissions + journey.getCo2Emission();
                }
            }
        }
        return totalSkytrainEmissions;
    }


    public double getTotalCarEmissions(ArrayList<JourneyModel> journeys) {
        double totalCarEmissions = 0; //
        for (JourneyModel journey : journeys) {
            if (journey.getCreationDate().after(last365) && journey.getCreationDate().before(tomorrow)) {
                if (journey.getTransportationModel().getTransportaionMode() == TransportationModel.TransportationMode.CAR) {
                    totalCarEmissions = totalCarEmissions + journey.getCo2Emission();
                }
            }
        }
        return totalCarEmissions;
    }
}