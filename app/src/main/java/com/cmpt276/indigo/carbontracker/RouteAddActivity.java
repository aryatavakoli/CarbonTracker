package com.cmpt276.indigo.carbontracker;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import static java.sql.Types.NULL;


public class RouteAddActivity extends AppCompatActivity {
    RouteModel newRoute = new RouteModel();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        calculate();
        recalculateOnChange(R.id.add_route_editText_city_distance);
        recalculateOnChange(R.id.add_route_editText_highway_distance);
        setupOKButton();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void calculate() {
        int cityDistance = getNumberFromEditTextOrZeroForFail(R.id.add_route_editText_city_distance);
        int highwayDistance = getNumberFromEditTextOrZeroForFail(R.id.add_route_editText_highway_distance);
        int total = cityDistance + highwayDistance;
        displayNumberIfPositive(R.id.add_route_textview_total_distance, total);
    }
    private void recalculateOnChange(int textFieldID) {
        TextView tv = (TextView) findViewById(textFieldID);
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
    }


    private void displayNumberIfPositive(int id, int data) {
        TextView tv = (TextView) findViewById(id);
        if (data >= 0 ) {
            tv.setText("" + data);
        } else {
            tv.setText("");
        }
    }

    private int getNumberFromEditTextOrZeroForFail(int id) {
        EditText data = (EditText) findViewById(id);
        String text = data.getText().toString();
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private void setupOKButton() {
        Button btn = (Button) findViewById(R.id.add_route_ok_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    // Get values from UI:
                    EditText etName = (EditText) findViewById(R.id.add_route_editText_nickname);
                    String name = etName.getText().toString();

                    if (name.length() == 0) {
                        Toast.makeText(RouteAddActivity.this, "Please enter a route name.", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }

                    EditText etHighway = (EditText) findViewById(R.id.add_route_editText_highway_distance);

                    if (etHighway.getText().toString().length() == 0){
                        Toast.makeText(RouteAddActivity.this, "Please enter a highway distance.", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    int highway = Integer.parseInt(etHighway.getText().toString());

                EditText etCity = (EditText) findViewById(R.id.add_route_editText_city_distance);
                if (etCity.getText().toString().length() == 0){
                    Toast.makeText(RouteAddActivity.this, "Please enter a city distance.", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                int city = Integer.parseInt(etCity.getText().toString());



                    newRoute.setName(name);
                    newRoute.setHighwayDistance(highway);
                    newRoute.setCityDistance(city);





                    finish();

            }
        });
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("RouteAdd Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


}