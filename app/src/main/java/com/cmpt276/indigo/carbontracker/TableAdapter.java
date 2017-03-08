package com.cmpt276.indigo.carbontracker;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.VehicleModel;

public class TableAdapter extends BaseAdapter {

    private List<JourneyModel> journeyList;
    private LayoutInflater inflater;
    private TextView carName;
    private TextView routeName;
    private TextView totalDistance;
    private TextView date;
    private TextView co2;

    public TableAdapter(Context context){
        CarbonFootprintComponentCollection carbonInterface = CarbonFootprintComponentCollection.getInstance();
        this.journeyList = carbonInterface.getJournies();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if(journeyList!=null){
            ret = journeyList.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return journeyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        JourneyModel journey = (JourneyModel) this.getItem(position);

        VehicleModel vehicles = journey.getVehicleModel();

        RouteModel route = journey.getRouteModel();

        if(convertView == null){

            convertView = inflater.inflate(R.layout.list_item_carbon_footprint_table, null);

            carName = (TextView) convertView.findViewById(R.id.list_item_carbon_footprint_table_vehicle_col);
            routeName = (TextView) convertView.findViewById(R.id.list_item_carbon_footprint_table_route_col);
            totalDistance = (TextView) convertView.findViewById(R.id.list_item_carbon_footprint_table_distance_col);
            date = (TextView) convertView.findViewById(R.id.list_item_carbon_footprint_table_date_col);
            co2 = (TextView) convertView.findViewById(R.id.list_item_carbon_footprint_table_co2_col);
        }


        carName.setText(vehicles.getName());
        carName.setTextSize(13);
        routeName.setText(route.getName());
        routeName.setTextSize(13);
        totalDistance.setText((route.getCityDistance()+ route.getHighwayDistance()) + " km" + "");
        routeName.setTextSize(13);
        date.setText(journey.getCreationDate()+ "");
        date.setTextSize(13);
        co2.setText(journey.getCo2Emission()+ " Kg" +"");
        co2.setTextSize(13);

        return convertView;
    }



}
