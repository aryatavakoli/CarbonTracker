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
 * Implements adapter for Utility table
 */

public class UtilityDBAdapter {

    // For logging:
    private static final String TAG = "UtilityDBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    public static final String KEY_NAME = "name";
    public static final String KEY_COMPANY = "company";
    public static final String KEY_TOTAL_ENERGY_CONSUMPTION_IN_GWH = "total_energy_consumption_in_GWH";
    public static final String KEY_NUMBER_OF_OCCUPANTS = "number_of_occupants";
    public static final String KEY_START_DATE = "start_date";
    public static final String KEY_END_DATE = "end_date";
    public static final String KEY_IS_DELETED = "is_deleted";
    public static final String KEY_UNITS = "units";
    public static final String KEY_EMISSION_IN_UNITS = "emission_in_units";

    public static final int COL_NAME = 1;
    public static final int COL_COMPANY = 2;
    public static final int COL_TOTAL_ENERGY_CONSUMPTION_IN_GWH = 3;
    public static final int COL_NUMBER_OF_OCCUPANTS = 4;
    public static final int COL_START_DATE = 5;
    public static final int COL_END_DATE = 6;
    public static final int COL_IS_DELETED = 7;
    public static final int COL_UNITS= 8;
    public static final int COL_EMISSION_IN_UNITS = 9;

    public static final String[] ALL_KEYS = new String[] {
            KEY_ROWID,
            KEY_NAME,
            KEY_COMPANY,
            KEY_TOTAL_ENERGY_CONSUMPTION_IN_GWH,
            KEY_NUMBER_OF_OCCUPANTS,
            KEY_START_DATE,
            KEY_END_DATE,
            KEY_IS_DELETED,
            KEY_UNITS,
            KEY_EMISSION_IN_UNITS
    };

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "CarbonTrackerDb";
    public static final String DATABASE_TABLE = "utilityTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 3;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_NAME + " text not null, "
                    + KEY_COMPANY + " integer not null, "
                    + KEY_TOTAL_ENERGY_CONSUMPTION_IN_GWH + " double not null, "
                    + KEY_NUMBER_OF_OCCUPANTS + " integer not null, "
                    + KEY_START_DATE + " date not null, "
                    + KEY_END_DATE + " date not null, "
                    + KEY_IS_DELETED + " boolean not null,"
                    + KEY_UNITS + " Units not null"
                    + KEY_EMISSION_IN_UNITS + "double not null"
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private UtilityDBAdapter.DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    public UtilityDBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public UtilityDBAdapter open() {
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
        initialValues.put(KEY_COMPANY, UtilityModel.CompanyToInt(utility.getCompanyName()));
        initialValues.put(KEY_TOTAL_ENERGY_CONSUMPTION_IN_GWH, utility.getTotalEnergyConsumptionInKWh());
        initialValues.put(KEY_NUMBER_OF_OCCUPANTS, utility.getNumberOfOccupants());
        initialValues.put(KEY_START_DATE, utility.getStartDateString());
        initialValues.put(KEY_END_DATE, utility.getEndDateString());
        initialValues.put(KEY_IS_DELETED, utility.getIsDeleted());
        initialValues.put(KEY_UNITS, UtilityModel.UnitsToInt(utility.getUnits()));
        initialValues.put(KEY_EMISSION_IN_UNITS, UtilityModel.UnitsToInt(utility.getUnits()));
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

    private UtilityModel makeUtility(Cursor cursor){
        boolean isDeleted = cursor.getInt(UtilityDBAdapter.COL_IS_DELETED) > 0;
        long id = (long) cursor.getInt(UtilityDBAdapter.COL_ROWID);
        String name = cursor.getString(UtilityDBAdapter.COL_NAME);
        UtilityModel.Company company = UtilityModel.IntToCompany(cursor.getInt(UtilityDBAdapter.COL_COMPANY));
        double totalEnergyConsumptionInGWh = cursor.getDouble(UtilityDBAdapter.COL_TOTAL_ENERGY_CONSUMPTION_IN_GWH);
        int numberOfOccupants = cursor.getInt((UtilityDBAdapter.COL_NUMBER_OF_OCCUPANTS));
        UtilityModel.Units units = UtilityModel.IntToUnits(cursor.getInt(UtilityDBAdapter.COL_UNITS));
        double totalEmissionInUnits = cursor.getDouble(UtilityDBAdapter.COL_EMISSION_IN_UNITS);

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(UtilityModel.DATE_FORMAT);
        try
        {
            startDate.setTime(formatter.parse(cursor.getString(UtilityDBAdapter.COL_START_DATE)));
            endDate.setTime(formatter.parse(cursor.getString(UtilityDBAdapter.COL_END_DATE)));
        }
        catch(Exception e){

        }
        return new UtilityModel(
                id,
                company,
                name,
                totalEnergyConsumptionInGWh,
                numberOfOccupants,
                startDate,
                endDate,
                isDeleted,
                units,
                totalEmissionInUnits);
    }

    public ArrayList<UtilityModel> getAllUtilities() {
        open();
        Cursor cursor = getAllRows();
        ArrayList<UtilityModel> utilityModels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                UtilityModel utilityModel = makeUtility(cursor);
                if(utilityModel != null && !utilityModel.getIsDeleted()) {
                    utilityModels.add(utilityModel);
                }
            } while(cursor.moveToNext());
        }
        close();
        return utilityModels;
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

    // Get a specific utility by name
    public Cursor getName(String utilityName) {
        String where = KEY_NAME + "='" + utilityName + "'" +
                " AND " + KEY_IS_DELETED + "=" + 0;
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
        newValues.put(KEY_COMPANY, UtilityModel.CompanyToInt(utility.getCompanyName()));
        newValues.put(KEY_TOTAL_ENERGY_CONSUMPTION_IN_GWH, utility.getTotalEnergyConsumptionInKWh());
        newValues.put(KEY_NUMBER_OF_OCCUPANTS, utility.getNumberOfOccupants());
        newValues.put(KEY_START_DATE, utility.getStartDateString());
        newValues.put(KEY_END_DATE, utility.getEndDateString());
        newValues.put(KEY_IS_DELETED, utility.getIsDeleted());
        newValues.put(KEY_UNITS, UtilityModel.UnitsToInt(utility.getUnits()));
        newValues.put(KEY_EMISSION_IN_UNITS, utility.getDailyCO2EmissionsInSpecifiedUnits());
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
