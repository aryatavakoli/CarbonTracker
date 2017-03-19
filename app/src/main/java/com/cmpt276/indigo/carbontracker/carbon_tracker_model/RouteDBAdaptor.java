package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RouteDBAdaptor {

    // For logging:
    private static final String TAG = "RouteDBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    public static final String KEY_NAME = "name";
    public static final String KEY_CITY_DISTANCE = "city_distance";
    public static final String KEY_HIGHWAY_DISTANCE = "highway_distance";
    public static final String KEY_TOTAL_DISTANCE = "total_distance";
    public static final String KEY_IS_DELETED = "is_deleted";

    public static final int COL_NAME = 1;
    public static final int COL_CITY_DISTANCE = 2;
    public static final int COL_HIGHWAY_DISTANCE = 3;
    public static final int COL_TOTAL_DISTANCE = 4;
    public static final int COL_IS_DELETED = 5;

    public static final String[] ALL_KEYS = new String[] {
            KEY_ROWID, KEY_NAME, KEY_CITY_DISTANCE, KEY_HIGHWAY_DISTANCE, KEY_TOTAL_DISTANCE, KEY_IS_DELETED
    };

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "CarbonTrackerDb";
    public static final String DATABASE_TABLE = "routeTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_NAME + " text not null unique, "
                    + KEY_CITY_DISTANCE + " double not null, "
                    + KEY_HIGHWAY_DISTANCE + " double not null, "
                    + KEY_TOTAL_DISTANCE + " double not null, "
                    + KEY_IS_DELETED + " boolean not null"
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private RouteDBAdaptor.DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    public RouteDBAdaptor(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public RouteDBAdaptor open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertRow(RouteModel route) {
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, route.getName());
        initialValues.put(KEY_CITY_DISTANCE, route.getCityDistance());
        initialValues.put(KEY_HIGHWAY_DISTANCE, route.getHighwayDistance());
        initialValues.put(KEY_TOTAL_DISTANCE, route.getTotalDistance());
        initialValues.put(KEY_IS_DELETED, route.getIsDeleted());
        // Insert it into the database.
        route.setId(db.insert(DATABASE_TABLE, null, initialValues));
        return route.getId();
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

    // Get a specific route by name
    public Cursor getName(String routeName) {
        String where = KEY_NAME + "='" + routeName + "'";
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(RouteModel route) {
        String where = KEY_ROWID + "=" + route.getId();
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAME, route.getName());
        newValues.put(KEY_CITY_DISTANCE, route.getCityDistance());
        newValues.put(KEY_HIGHWAY_DISTANCE, route.getHighwayDistance());
        newValues.put(KEY_TOTAL_DISTANCE, route.getTotalDistance());
        newValues.put(KEY_IS_DELETED, route.getIsDeleted());
        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    public void dropTable() {
        db.execSQL("DROP TABLE " + DATABASE_TABLE);
    }

    public void createTable() {
        db.execSQL(DATABASE_CREATE_SQL);
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
