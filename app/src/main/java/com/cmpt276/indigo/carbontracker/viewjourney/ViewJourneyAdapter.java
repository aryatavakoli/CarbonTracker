package com.cmpt276.indigo.carbontracker.viewjourney;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cmpt276.indigo.carbontracker.R;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.VehicleModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/*
the adapter to View all Journey
 */

class ViewJourneyAdapter extends BaseAdapter {

    private List<JourneyModel> journeyList;
    private LayoutInflater inflater;
    private TextView carId;
    private TextView carName;
    private TextView routeName;
    private TextView date;
    // will be format the date.
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    ViewJourneyAdapter(Context context) {
        this.journeyList = new ArrayList<>();
        CarbonFootprintComponentCollection carbonInterface = CarbonFootprintComponentCollection.getInstance();
        //display all journey
        for (JourneyModel model : carbonInterface.getJournies()) {
            if (!model.getIsDeleted()) {
                journeyList.add(model);
            }
        }
        // init the LayoutInflater
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (journeyList != null) {
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
        // init the convert view
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_carbon_view_table, null);
            carId = (TextView) convertView.findViewById(R.id.list_item_carbon_footprint_table_id_col);
            carName = (TextView) convertView.findViewById(R.id.list_item_carbon_footprint_table_vehicle_col);
            routeName = (TextView) convertView.findViewById(R.id.list_item_carbon_footprint_table_route_col);
            date = (TextView) convertView.findViewById(R.id.list_item_carbon_footprint_table_date_col);
        }
        // set the data
        carId.setText((position + 1) + "");
        carName.setText(vehicles.getName());
        carName.setTextSize(13);
        routeName.setText(route.getName());
        routeName.setTextSize(13);
        routeName.setTextSize(13);
        // format the date.
        date.setText(SIMPLE_DATE_FORMAT.format(journey.getCreationDate()));
        date.setTextSize(13);
        return convertView;
    }

}
