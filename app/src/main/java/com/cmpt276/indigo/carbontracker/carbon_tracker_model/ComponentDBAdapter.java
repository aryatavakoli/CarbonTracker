package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import android.database.Cursor;

/**
 * Created by faranakpouya on 2017-03-22.
 */

public interface ComponentDBAdapter {

    void close();
    ComponentDBAdapter open();
    Cursor getRow(long rowId);
}
