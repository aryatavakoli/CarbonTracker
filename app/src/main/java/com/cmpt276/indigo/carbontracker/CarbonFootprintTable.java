package com.cmpt276.indigo.carbontracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import android.widget.ListView;
public class CarbonFootprintTable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbon_footprint_table);
        //Allows for backbutton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        List<Goods> list = new ArrayList<Goods>();
        list.add(new Goods("01", "2010", "982323423232",34,23,23));
        list.add(new Goods("02", "2011", "31312323223",34,23,23));
        list.add(new Goods("03", "2012", "12",34,23,23));
        list.add(new Goods("04", "2013", "12333435445",34,23,23));
        list.add(new Goods("05", "2014", "34523",34,23,23));
        list.add(new Goods("06", "2015", "345456",34,23,23));
        list.add(new Goods("07", "2016", "2344",34,23,23));
        list.add(new Goods("08", "2017", "23445",34,23,23));
        list.add(new Goods("09", "2018", "3234345",34,23,23));

        ListView tableListView = (ListView) findViewById(R.id.list);

        TableAdapter adapter = new TableAdapter(this, list);

        tableListView.setAdapter(adapter);

    }
}
