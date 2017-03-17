package com.cmpt276.indigo.carbontracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.DuplicateComponentException;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.UtilityModel;

public class UtilityAddActivity extends AppCompatActivity {
    public static final int RESULT_DELETE = 15;
    CarbonFootprintComponentCollection carbonFootprintInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();

        setupOKButton();
        loadSpinner();
        //setupDeleteButton();
    }

    private void loadSpinner() {
        Spinner spinnerCompany  = (Spinner) findViewById(R.id.utility_add_spinner_company);
        spinnerCompany.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, UtilityModel.Company.values()));
    }

    private void setupOKButton() {
        Button btn = (Button) findViewById(R.id.utility_add_button_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinnerCompany  = (Spinner) findViewById(R.id.utility_add_spinner_company);
                UtilityModel.Company company = (UtilityModel.Company) spinnerCompany.getSelectedItem();

                if (company == null) {
                    Toast.makeText(UtilityAddActivity.this, "Company should be selected", Toast.LENGTH_SHORT).show();
                }

                EditText editTextName = (EditText) findViewById(R.id.utility_add_editText_name);

                if (editTextName.getText().toString().length() == 0) {
                    Toast.makeText(UtilityAddActivity.this, "Please enter a Utility name.", Toast.LENGTH_SHORT)
                            .show();
                    return;

                }

                EditText editTextDays = (EditText) findViewById(R.id.utility_add_editText_num_days);

                if (editTextDays.getText().toString().length() == 0) {
                    Toast.makeText(UtilityAddActivity.this, "Please enter a Billing Period.", Toast.LENGTH_SHORT)
                            .show();
                    return;

                }

                EditText editTextEnergy = (EditText) findViewById(R.id.utility_add_editText_energy_consumption);
                if (editTextEnergy.getText().toString().length() == 0) {
                    Toast.makeText(UtilityAddActivity.this, "Please enter Energy Consumption", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                UtilityModel newUtility = createUtility();

                //adding Utility to collection if it is not duplicate
                if (!addUtility(newUtility)) {
                    return;
                }
                else {
                    Toast.makeText(UtilityAddActivity.this, "Utility Added!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra("utility", newUtility);
                    setResult(Activity.RESULT_OK, intent);

                    finish();
                }
            }

        });
    }

    private UtilityModel createUtility() {
        // Get values from UI:
        Spinner spinnerCompany  = (Spinner) findViewById(R.id.utility_add_spinner_company);
        UtilityModel.Company company = (UtilityModel.Company) spinnerCompany.getSelectedItem();

        EditText editTextName = (EditText) findViewById(R.id.utility_add_editText_name);
        String name = editTextName.getText().toString();

        EditText editTextDays = (EditText) findViewById(R.id.utility_add_editText_num_days);
        int days = Integer.parseInt(editTextDays.getText().toString());

        EditText editTextEnergy = (EditText) findViewById(R.id.utility_add_editText_energy_consumption);
        int energy = Integer.parseInt(editTextEnergy.getText().toString());
        UtilityModel newUtility = new UtilityModel(company);
        newUtility.setName(name);
        newUtility.setBillingPeriodInDays(days);
        newUtility.setTotalEnergyConsumptionInGWH(energy);
        return  newUtility;
    }

    boolean addUtility(UtilityModel utility){
        try{
            carbonFootprintInterface.add(this,utility);
        }
        catch(DuplicateComponentException e){
                Toast.makeText(UtilityAddActivity.this, "This Utility already exists.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static Intent makeIntentForNewUtility(Context packageContext) {
        return new Intent(packageContext, UtilityAddActivity.class);
    }

    public static Intent makeIntentForEditUtility(Context packageContext, UtilityModel utility) {
        Intent intent = makeIntentForNewUtility(packageContext);
        intent.putExtra("utility", utility);
        return intent;
    }
}
