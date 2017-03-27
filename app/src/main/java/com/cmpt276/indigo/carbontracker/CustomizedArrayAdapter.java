package com.cmpt276.indigo.carbontracker;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by faranakpouya on 2017-03-26.
 */

public class CustomizedArrayAdapter extends ArrayAdapter<String>{
    Context context;
    CustomizedArrayAdapterItem items[];
    int selectedItem;

    CustomizedArrayAdapter(Context c, CustomizedArrayAdapterItem items[], String titles[]){
        super(c, R.layout.customized_list_row, R.id.text, titles);
        this.context = c;
        this.items = items;
        selectedItem = -1;
    }

    void setSelected(int selectedItem){
        this.selectedItem = selectedItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInFlater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInFlater.inflate(R.layout.customized_list_row, parent, false);
        ImageView images = (ImageView) row.findViewById(R.id.icon);
        TextView titleTextView = (TextView) row.findViewById(R.id.text);
        TextView descriptionTextView = (TextView) row.findViewById(R.id.text2);
        images.setImageResource(items[position].getImage());
        titleTextView.setText(items[position].getText1());
        descriptionTextView.setText(items[position].getText2());
        if(position == selectedItem) {
            LinearLayout rowLayout = (LinearLayout)row.findViewById(R.id.list_item_layout);
            if(Build.VERSION.SDK_INT >= 16){
                rowLayout.setBackground(context.getResources().getDrawable(R.drawable.list_item_selection_bg));
            }
            else {
                rowLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.list_item_selection_bg));
            }
        }
        return row;
    }
}
