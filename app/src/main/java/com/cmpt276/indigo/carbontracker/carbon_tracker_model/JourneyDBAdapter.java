package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Implements adapter for Journey table
 */

public class JourneyDBAdapter {
    // For logging:
    private static final String TAG = "JourneyDBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    public static final String KEY_TRANSPORTATION_ID = "transportation_id";
    public static final String KEY_ROUTE_id = "route_id";
    public static final String KEY_CO2_EMISSION = "co2_emission";
    public static final String KEY_CREATE_DATE = "create_date";
    public static final String KEY_IS_DELETED = "is_deleted";

    public static final int COL_TRANSPORTATION_ID = 1;
    public static final int COL_ROUTE_id = 2;
    public static final int COL_CO2_EMISSION = 3;
    public static final int COL_CREATE_DATE = 4;
    public static final int COL_IS_DELETED = 5;

    public static final String[] ALL_KEYS = new String[] {
            KEY_ROWID, KEY_TRANSPORTATION_ID, KEY_ROUTE_id, KEY_CO2_EMISSION, KEY_CREATE_DATE, KEY_IS_DELETED
    };

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "CarbonTrackerDb";
    public static final String DATABASE_TABLE = "journeyTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 3;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_TRANSPORTATION_ID + " integer not null, "
                    + KEY_ROUTE_id + " integer not null, "
                    + KEY_CO2_EMISSION + " double not null, "
                    + KEY_CREATE_DATE + " date not null, "
                    + KEY_IS_DELETED + " boolean not null"
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private JourneyDBAdapter.DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    public JourneyDBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new JourneyDBAdapter.DatabaseHelper(context);
    }

    // Open the database connection.
    public JourneyDBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        ensureTableExists();
        return this;
    }

    private void ensureTableExists(){
        if(!tableExists()) {
            createTable();
        }
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertRow(JourneyModel journey) {
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TRANSPORTATION_ID, journey.getTransportationModel().getId());
        initialValues.put(KEY_ROUTE_id, journey.getRouteModel().getId());
        initialValues.put(KEY_CO2_EMISSION, journey.getCo2EmissionInKG());
        initialValues.put(KEY_CREATE_DATE, journey.getCreationDateString());
        initialValues.put(KEY_IS_DELETED, journey.getIsDeleted());
        // Insert it into the database.
        journey.setId(db.insert(DATABASE_TABLE, null, initialValues));
        return journey.getId();
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    private JourneyModel makeJourney(Cursor cursor, TransportationDBAdapter transportationDBAdapter, RouteDBAdapter routeDBAdapter){
        boolean isDeleted = cursor.getInt(JourneyDBAdapter.COL_IS_DELETED) > 0;
        long id = (long) cursor.getInt(JourneyDBAdapter.COL_ROWID);
        long transportationID = (long)cursor.getInt(JourneyDBAdapter.COL_TRANSPORTATION_ID);
        long routeID = (long)cursor.getInt(JourneyDBAdapter.COL_ROUTE_id);
        float co2Emission = cursor.getFloat(JourneyDBAdapter.COL_CO2_EMISSION);
        Calendar createDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(UtilityModel.DATE_FORMAT);
        try
        {
            createDate.setTime(formatter.parse(cursor.getString(JourneyDBAdapter.COL_CREATE_DATE)));
        }
        catch(Exception e){

        }
        TransportationModel transportationModel = transportationDBAdapter.getVehicle(transportationID);
        RouteModel routeModel = routeDBAdapter.getRoute(routeID);
        if(transportationModel == null) {
            throw new IllegalArgumentException("Vehicle could not be found in database");
        }
        if(routeModel == null) {
            throw new IllegalArgumentException("Route could not be found in database");
        }
        return new JourneyModel(id, transportationModel, routeModel, co2Emission, createDate, isDeleted);
    }

    public ArrayList<JourneyModel> getAllJournies() {
        open();
        TransportationDBAdapter transportationDBAdapter = new TransportationDBAdapter(context);
        RouteDBAdapter routeDBAdapter = new RouteDBAdapter(context);
        transportationDBAdapter.open();
        routeDBAdapter.open();

        Cursor cursor = getAllRows();
        ArrayList<JourneyModel> journies = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                JourneyModel journeyModel = makeJourney(cursor, transportationDBAdapter, routeDBAdapter);
                if(journeyModel != null && !journeyModel.getIsDeleted()) {
                    journies.add(journeyModel);
                }
            } while(cursor.moveToNext());
        }
        transportationDBAdapter.close();
        routeDBAdapter.close();
        close();
        return journies;
    }

    // Return all data in the database.
    private Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    //TODO: need to identify journey. Journey can be duplicate
    // Get a specific journey by vehicle's id and route's id
    public Cursor getName(String vehicleId, String routeId) {
        String where = KEY_TRANSPORTATION_ID + "='" + vehicleId + "' && " +
                        KEY_ROUTE_id + "='" + routeId + "' ";
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(JourneyModel journey) {
        String where = KEY_ROWID + "=" + journey.getId();
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_TRANSPORTATION_ID, journey.getTransportationModel().getId());
        newValues.put(KEY_ROUTE_id, journey.getRouteModel().getId());
        newValues.put(KEY_CO2_EMISSION, journey.getCo2EmissionInKG());
        newValues.put(KEY_CREATE_DATE, journey.getCreationDateString());
        newValues.put(KEY_IS_DELETED, journey.getIsDeleted());
        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    public void dropTable() {
        db.execSQL("DROP TABLE " + DATABASE_TABLE);
    }

    public void createTable() {
        db.execSQL(DATABASE_CREATE_SQL);
    }

    // this methods checks existance of journey table and returns true if it exists
    private boolean tableExists() {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + DATABASE_TABLE + "'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
