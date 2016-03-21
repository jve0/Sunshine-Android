package com.audioguides.sunshine.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(this, SettingsActivity.class));
        }
        if (id == R.id.action_map){
            openPreferredLocationInMap();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap (){
        //get the location from SharedPreferences. It's saved as ID, but we need the name of the city
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String location_name = getLocationNameFromID(settings
                .getString(getString(R.string.pref_location_key), getString(R.string.pref_units_default)));

        //build the URI scheme for send the location
        Uri geolocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location_name)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geolocation);

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            Log.d("OpenMap", "couldn't open "+ location_name);
            Toast.makeText(this, "couldn't open "+ location_name , Toast.LENGTH_SHORT).show();
        }


    }

    //translate the city ID for the city name
    private String getLocationNameFromID (String location_id){
        int location_index = Arrays.asList(getResources().getStringArray(R.array.forecast_ids_entries))
                .indexOf(location_id);
        return getResources()
                .getStringArray(R.array.forecast_ids_entry_values)[location_index];
    }


}
