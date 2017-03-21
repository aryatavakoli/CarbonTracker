package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by faranakpouya on 2017-03-18.
 */

public class UtilityDBAdaptor {

    // For logging:
    private static final String TAG = "UtilityDBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    public static final String KEY_NAME = "name";
    public static final String KEY_BILLING_PERIOD_IN_DAY = "billing_period_in_day";
    public static final String KEY_TOTAL_ENERGY_CONSUMPTION_IN_GWH = "total_energy_consumption_in_GWH";
    public static final String KEY_TOTAL_CO2_EMISSION_IN_KG= "total_CO2_emissions_in_Kg";
    public static final String KEY_DAILY_ENERGY_CONSUMPTION_IN_GWH = "daily_energy_consumption_in_GWH";
    public static final String KEY_DAILY_CO2_EMISSION_IN_KG = "daily_CO2_emissions_in_Kg";
    public static final String KEY_NUMBER_OF_OCCUPANTS = "number_of_occupants";
    public static final String KEY_IS_DELETED = "is_deleted";

    public static final int COL_NAME = 1;
    public static final int COL_BILLING_PERIOD_IN_DAY = 2;
    public static final int COL_TOTAL_ENERGY_CONSUMPTION_IN_GWH = 3;
    public static final int COL_TOTAL_CO2_EMISSION_IN_KG = 4;
    public static final int COL_DAILY_ENERGY_CONSUMPTION_IN_GWH = 5;
    public static final int COL_DAILY_CO2_EMISSION_IN_KG = 6;
    public static final int COL_NUMBER_OF_OCCUPANTS = 7;
    public static final int COL_IS_DELETED = 8;

    public static final String[] ALL_KEYS = new String[] {
            KEY_ROWID, KEY_NAME, KEY_BILLING_PERIOD_IN_DAY, KEY_TOTAL_ENERGY_CONSUMPTION_IN_GWH, KEY_TOTAL_CO2_EMISSION_IN_KG, KEY_DAILY_ENERGY_CONSUMPTION_IN_GWH, KEY_DAILY_CO2_EMISSION_IN_KG, KEY_NUMBER_OF_OCCUPANTS, KEY_IS_DELETED
    };

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "CarbonTrackerDb";
    public static final String DATABASE_TABLE = "utilityTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_NAME + " text not null unique, "
                    + KEY_BILLING_PERIOD_IN_DAY + " integer not null, "
                    + KEY_TOTAL_ENERGY_CONSUMPTION_IN_GWH + " double not null, "
                    + KEY_TOTAL_CO2_EMISSION_IN_KG + " double not null, "
                    + KEY_DAILY_ENERGY_CONSUMPTION_IN_GWH + " double not null, "
                    + KEY_DAILY_CO2_EMISSION_IN_KG + " double not null, "
                    + KEY_NUMBER_OF_OCCUPANTS + " integer not null, "
                    + KEY_IS_DELETED + " boolean not null"
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private UtilityDBAdaptor.DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    public UtilityDBAdaptor(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public UtilityDBAdaptor open() {
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
    public long insertRow(UtilityModel utility) {
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, utility.getName());
        initialValues.put(KEY_BILLING_PERIOD_IN_DAY, utility.getBillingPeriodInDay());
        initialValues.put(KEY_TOTAL_ENERGY_CONSUMPTION_IN_GWH, utility.getDailyEnergyConsumptionInGWH());
        initialValues.put(KEY_TOTAL_CO2_EMISSION_IN_KG, utility.getDailyCO2EmissionsInKg());
        initialValues.put(KEY_DAILY_ENERGY_CONSUMPTION_IN_GWH, utility.getDailyEnergyConsumptionInGWH());
        initialValues.put(KEY_DAILY_CO2_EMISSION_IN_KG, utility.getDailyCO2EmissionsInKg());
        initialValues.put(KEY_NUMBER_OF_OCCUPANTS, utility.getNumberOfOccupants());
        initialValues.put(KEY_IS_DELETED, utility.getIsDeleted());
        // Insert it into the database.
        utility.setId(db.insert(DATABASE_TABLE, null, initialValues));
        return utility.getId();
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

    // Get a specific utility by name
    public Cursor getName(String utilityName) {
        String where = KEY_NAME + "='" + utilityName + "'";
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(UtilityModel utility) {
        String where = KEY_ROWID + "=" + utility.getId();
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAME, utility.getName());
        newValues.put(KEY_BILLING_PERIOD_IN_DAY, utility.getBillingPeriodInDay());
        newValues.put(KEY_TOTAL_ENERGY_CONSUMPTION_IN_GWH, utility.getDailyEnergyConsumptionInGWH());
        newValues.put(KEY_TOTAL_CO2_EMISSION_IN_KG, utility.getDailyCO2EmissionsInKg());
        newValues.put(KEY_DAILY_ENERGY_CONSUMPTION_IN_GWH, utility.getDailyEnergyConsumptionInGWH());
        newValues.put(KEY_DAILY_CO2_EMISSION_IN_KG, utility.getDailyCO2EmissionsInKg());
        newValues.put(KEY_NUMBER_OF_OCCUPANTS, utility.getNumberOfOccupants());
        newValues.put(KEY_IS_DELETED, utility.getIsDeleted());
        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    public void dropTable() {
        db.execSQL("DROP TABLE " + DATABASE_TABLE);
    }

    public void createTable() {
        db.execSQL(DATABASE_CREATE_SQL);
    }

    //This method checks existance of monthly utility and returns true if it exists
    private boolean tableExists(){
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
