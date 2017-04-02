package com.cmpt276.indigo.carbontracker;

import android.app.ActionBar;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  Implements array adapter to customized each item in the list.
 */

public class ImageArrayAdapter extends BaseAdapter{

    public static final int IMAGE_SIZE = 500;
    int imageResources[];
    Context context;

    ImageArrayAdapter(Context c, int imageResources[]){
        this.imageResources = imageResources;
        this.context = c;
    }

    @Override
    public int getCount(){
        return imageResources.length;
    }

    @Override
    public Object getItem(int position){
        return null;
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(IMAGE_SIZE, IMAGE_SIZE));
            imageView.setVisibility(View.VISIBLE);
        }
        else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(imageResources[position]);
        return imageView;
    }
}
