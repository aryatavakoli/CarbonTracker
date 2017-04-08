package com.cmpt276.indigo.carbontracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.UtilityModel;

import java.io.InputStream;
import java.util.Calendar;

/*
    implments main menu activity
 */
public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final int JOURNEY_SELECT = 300;
    public static final int REQUEST_CODE = 100;
    public static final String CHECK_BOX_STATUS = "CheckBoxStatus";
    public static final String CHECK_STATUS = "CheckStatus";
    private CheckBox checkBox;

    @Override
    protected void onPause() {
        super.onPause();
        SavePreferences(CHECK_BOX_STATUS, checkBox.isChecked());
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkBox.setChecked(LoadPreferences());
        if(LoadPreferences()) {
            UtilityModel.setUnits(UtilityModel.Units.BREATHS);
            JourneyModel.setUnits(JourneyModel.Units.BREATHS);
        }
        else{
            UtilityModel.setUnits(UtilityModel.Units.KILOGRAMS);
            JourneyModel.setUnits(JourneyModel.Units.KILOGRAMS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        checkBox = (CheckBox) findViewById(R.id.main_menu_checkbox);
        // process might be killed off while paused and the app is in the background.
        // In that case, the flag will be false when the activity is recreated by
        // the system when the user tries to bring the app back to the foreground/
        //use shared preference to get aroundt his
        loadDataFile();
        carbonFootprintSelectBtn();
        // add journey view button
        journeyViewBtn();
        utilitesCreateBtn();
        showNotification();
        setCheckboxCallBack();
        getSoftButtonsBarSizePort(this);
    }

    private void SavePreferences(String key, Boolean bool){
        SharedPreferences sharedPreferences = getSharedPreferences(CHECK_STATUS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, bool);
        editor.apply();
    }

    public boolean LoadPreferences(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(CHECK_STATUS,MODE_PRIVATE);
        boolean value = preferences.getBoolean(CHECK_BOX_STATUS, false);
        return value;
    }

    //TODO: SWTICH UNITS
    private void setCheckboxCallBack() {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("Should be True", " " + isChecked);
                SavePreferences(CHECK_BOX_STATUS,isChecked);
                if(isChecked) {
                    UtilityModel.setUnits(UtilityModel.Units.BREATHS);
                    JourneyModel.setUnits(JourneyModel.Units.BREATHS);
                }
                else{
                    UtilityModel.setUnits(UtilityModel.Units.KILOGRAMS);
                    JourneyModel.setUnits(JourneyModel.Units.KILOGRAMS);
                }

            }
        });
    }

    private void showNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        Intent intent = new Intent(getApplicationContext(),Notification_receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
    }


    //Go to carbon footprint activity
    private void carbonFootprintSelectBtn() {
        Button btn = (Button) findViewById(R.id.main_menu_carbonfootprint_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(MainMenu.this, CarbonFootprintJourneyFootprintMenu.class);
            startActivity(intent);
            }
        });
    }

    private void loadDataFile() {
        CarbonFootprintComponentCollection carbonFootprint = CarbonFootprintComponentCollection.getInstance();
        InputStream is = getResources().openRawResource(R.raw.vehicles);
        carbonFootprint.loadDataFile(is);
    }

    /**
     * jump to JourneySelectActivity
     */
    private void journeyViewBtn() {
        Button btn = (Button) findViewById(R.id.main_menu_view_journey_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, JourneySelectActivity.class);
                startActivityForResult(intent,23 );
            }
        });
    }

    //Launch Create a utitliy activity
    private void utilitesCreateBtn() {
        Button btn = (Button) findViewById(R.id.main_menu_create_utilities_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, UtilitySelectActivity.class);
                //startActivityForResult(intent, JOURNEY_SELECT );
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

        }
    }

    public static Intent makeIntent(Context packageContext) {
        return new Intent(packageContext, MainMenu.class);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return SideNavigationManager.handleSideNavigationSelection(this, item);
    }

    public static void setupMargin(Activity activity, int contentID){
        RelativeLayout r = (RelativeLayout)activity.findViewById(contentID);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT
        );
        int margin = MainMenu.getSoftButtonsBarSizePort(activity);
        params.setMargins(0, margin, 0, margin);
        r.setLayoutParams(params);
    }

    public static int getSoftButtonsBarSizePort(Activity activity) {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }
}



