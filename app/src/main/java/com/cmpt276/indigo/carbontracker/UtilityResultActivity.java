package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.UtilityModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.round;
/**
 * Created by Arya on 2017-03-16.
 * Implements Result Activity for Utility
 */

public class UtilityResultActivity extends AppCompatActivity {
    UtilityModel newUtilities;
    CarbonFootprintComponentCollection carbonFootprintInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);// The Utility the user is creating
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        Intent intent = getIntent();
        if (intent != null && intent.getSerializableExtra("utility")!=null) {
            newUtilities = (UtilityModel) intent.getSerializableExtra("utility");
            fillTexts();
        }
    }

    private void fillBillInformation() {
        TextView nameDisplay = (TextView) findViewById(R.id.utility_result_name);
        nameDisplay.setText(newUtilities.getName() + "");

        TextView companyDisplay = (TextView) findViewById(R.id.utility_result_company);
        companyDisplay.setText(newUtilities.getCompanyName() + "");

        TextView daysDisplay = (TextView) findViewById(R.id.utility_result_billing_days);
        daysDisplay.setText(newUtilities.calculateBillingPeriodInDays() + " days");

        TextView energyDisplay = (TextView) findViewById(R.id.utility_result__total_energy_consumption);
        if(newUtilities.getCompanyName() == UtilityModel.Company.BCHYDRO) {
            energyDisplay.setText(round(newUtilities.getTotalEnergyConsumptionInKWh(),2) + " KWh");
        }
        else {
            energyDisplay.setText(round(newUtilities.getTotalEnergyConsumptionInGJ(),2) + " GJ");
        }

        newUtilities.calculateTotalEmissions();
        TextView emissionsDisplay = (TextView) findViewById(R.id.utility_result_total_emission);
        emissionsDisplay.setText(round(newUtilities.getTotalCO2EmissionsInKg(),2) + " Kg");

        TextView occupantsDisplay = (TextView) findViewById(R.id.utility_result_occupants);
        occupantsDisplay.setText(newUtilities.getNumberOfOccupants() + "");
    }


    private void fillPerDayUsageTexts() {
        TextView energyDisplay = (TextView) findViewById(R.id.utility_result_per_day_energy_consumption);
        if (newUtilities.getCompanyName() == UtilityModel.Company.BCHYDRO) {
            energyDisplay.setText(round(newUtilities.calculateDailyEnergyConsumptionInKWh(),2) + " KWh");
        }
        else{
            energyDisplay.setText(newUtilities.calculateDailyEnergyConsumptionInGJ() + " GJ");
        }

        TextView emissionsDisplay = (TextView) findViewById(R.id.utility_result_per_day_emissions);
        emissionsDisplay.setText(round(newUtilities.calculateDailyCO2EmissionsInKg(),2) + " Kg");
    }

    private void fillPerPersonTexts() {
        TextView energyDisplay = (TextView) findViewById(R.id.utility_result_per_person_energy_consumption);
        if (newUtilities.getCompanyName() == UtilityModel.Company.BCHYDRO) {
            energyDisplay.setText(round(newUtilities.getTotalEnergyConsumptionPerOccupantInKWh(),2) + " KWh");
        }
        else{
            energyDisplay.setText(round(newUtilities.getTotalEnergyConsumptionPerOccupantInGJ(),2) + " GJ");
        }


        TextView emissionsDisplay = (TextView) findViewById(R.id.utility_result_per_person_emissions);
        emissionsDisplay.setText(round(newUtilities.getTotalEmissionsPerOccupant(),2) + " Kg");
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void fillTexts() {
        fillBillInformation();
        fillPerDayUsageTexts();
        fillPerPersonTexts();
    }
}
