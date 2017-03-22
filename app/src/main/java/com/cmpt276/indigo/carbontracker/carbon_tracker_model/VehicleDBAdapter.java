package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class VehicleDBAdapter {

    // For logging:
    private static final String TAG = "VechicleDBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    public static final String KEY_NAME = "name";
    public static final String KEY_MAKE = "make";
    public static final String KEY_MODEL = "model";
    public static final String KEY_YEAR = "year";
    public static final String KEY_TRANSMISSION = "transmission";
    public static final String KEY_ENGINE_DISPLACEMENT = "engine_displacement";
    public static final String KEY_CITY_MILEAGE = "city_mileage";
    public static final String KEY_HIGHWAY_MILEAGE = "highway_mileage";
    public static final String KEY_PRIMARY_FUEL_TYPE = "primary_fuel_type";
    public static final String KEY_IS_DELETED = "is_deleted";

    public static final int COL_NAME = 1;
    public static final int COL_MAKE = 2;
    public static final int COL_MODEL = 3;
    public static final int COL_YEAR = 4;
    public static final int COL_TRANSMISSION = 5;
    public static final int COL_ENGINE_DISPLACEMENT = 6;
    public static final int COL_CITY_MILEAGE = 7;
    public static final int COL_HIGHWAY_MILEAGE = 8;
    public static final int COL_PRIMARY_FUEL_TYPE = 9;
    public static final int COL_IS_DELETED = 10;

    public static final String[] ALL_KEYS = new String[] {
            KEY_ROWID, KEY_NAME, KEY_MAKE, KEY_MODEL, KEY_YEAR, KEY_TRANSMISSION, KEY_ENGINE_DISPLACEMENT, KEY_CITY_MILEAGE, KEY_HIGHWAY_MILEAGE, KEY_PRIMARY_FUEL_TYPE, KEY_IS_DELETED
    };

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "CarbonTrackerDb";
    public static final String DATABASE_TABLE = "vehicleTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 3;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_NAME + " text not null, "
                    + KEY_MAKE + " text not null, "
                    + KEY_MODEL + " text not null, "
                    + KEY_YEAR + " text not null, "
                    + KEY_TRANSMISSION + " text not null, "
                    + KEY_ENGINE_DISPLACEMENT + " text not null, "
                    + KEY_CITY_MILEAGE + " double not null, "
                    + KEY_HIGHWAY_MILEAGE + " double not null, "
                    + KEY_PRIMARY_FUEL_TYPE + " text not null, "
                    + KEY_IS_DELETED + " boolean not null"
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    public VehicleDBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public VehicleDBAdapter open() {
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
    public long insertRow(VehicleModel vehicle) {
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, vehicle.getName());
        initialValues.put(KEY_MODEL, vehicle.getModel());
        initialValues.put(KEY_MAKE, vehicle.getMake());
        initialValues.put(KEY_YEAR, vehicle.getYear());
        initialValues.put(KEY_TRANSMISSION, vehicle.getTransmisson());
        initialValues.put(KEY_ENGINE_DISPLACEMENT, vehicle.getEngineDisplacment());
        initialValues.put(KEY_CITY_MILEAGE, vehicle.getCityMileage());
        initialValues.put(KEY_HIGHWAY_MILEAGE, vehicle.getHighwayMileage());
        initialValues.put(KEY_PRIMARY_FUEL_TYPE, vehicle.getPrimaryFuelType());
        initialValues.put(KEY_IS_DELETED, vehicle.getIsDeleted());
        // Insert it into the database.
        vehicle.setId(db.insert(DATABASE_TABLE, null, initialValues));
        return vehicle.getId();
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

    // Return all data in the database.
    public Cursor getAllRows() {
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

    // Get a specific vehicle by name
    public Cursor getName(String vehicleName) {
        String where = KEY_NAME + "='" + vehicleName + "'";
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(VehicleModel vehicle) {
        String where = KEY_ROWID + "=" + vehicle.getId();
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAME, vehicle.getName());
        newValues.put(KEY_MODEL, vehicle.getModel());
        newValues.put(KEY_YEAR, vehicle.getYear());
        newValues.put(KEY_TRANSMISSION, vehicle.getTransmisson());
        newValues.put(KEY_ENGINE_DISPLACEMENT, vehicle.getEngineDisplacment());
        newValues.put(KEY_CITY_MILEAGE, vehicle.getCityMileage());
        newValues.put(KEY_HIGHWAY_MILEAGE, vehicle.getHighwayMileage());
        newValues.put(KEY_PRIMARY_FUEL_TYPE, vehicle.getPrimaryFuelType());
        newValues.put(KEY_IS_DELETED, vehicle.getIsDeleted());
        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    public void dropTable() {
        db.execSQL("DROP TABLE " + DATABASE_TABLE);
    }

    public void createTable() {
        db.execSQL(DATABASE_CREATE_SQL);
    }

    // this methods checks existance of vehicle table and returns true if it exists
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
