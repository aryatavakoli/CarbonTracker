package com.cmpt276.indigo.carbontracker;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TableAdapter extends BaseAdapter {

    private List<ListItem> list;
    private LayoutInflater inflater;

    public TableAdapter(Context context, List<ListItem> list){
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if(list!=null){
            ret = list.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListItem items = (ListItem) this.getItem(position);

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.list_item_carbon_footprint_table, null);

            viewHolder.carName = (TextView) convertView.findViewById(R.id.car);
            viewHolder.routeName = (TextView) convertView.findViewById(R.id.route);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.col = (TextView) convertView.findViewById(R.id.co2);


            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.carName.setText(items.getCarName());
        viewHolder.carName.setTextSize(13);
        viewHolder.routeName.setText(items.getRouteName());
        viewHolder.routeName.setTextSize(13);
        viewHolder.date.setText(items.getDate()+"");
        viewHolder.date.setTextSize(13);
        viewHolder.col.setText(items.getCol()+"");
        viewHolder.col.setTextSize(13);

        return convertView;
    }

    public static class ViewHolder{

        public TextView carName;
        public TextView routeName;
        public TextView date;
        public TextView col;
    }

}
