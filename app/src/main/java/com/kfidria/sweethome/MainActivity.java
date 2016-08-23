package com.kfidria.sweethome;

import android.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.preference.PreferenceManager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private int currentTheme;
    private boolean change;

    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

         //Yellow Tigger
        setTheme(R.style.AppThemeYellow);
        int color = getResources().getColor(R.color.yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            MainFragment mainFragment = new MainFragment();
            mainFragment.setArguments(getIntent().getExtras());
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mainFragment)
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_custom);
        toolbar.setTitleTextColor(color);
        toolbar.setSubtitleTextColor(color);
        setSupportActionBar(toolbar);
        //setActionBar(toolbar);
     //   mPrefs.registerOnSharedPreferenceChangeListener(mSharedPreferencesListener);
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

        switch (item.getItemId()) {

            case R.id.action_settings:
       //         PrefFragment preferenceFragment = new PrefFragment();
        //        FragmentManager fragmentManager = getFragmentManager();
       //         fragmentManager.beginTransaction()
        //                .replace(R.id.fragment_container, preferenceFragment, "Preferences")
        //                .addToBackStack("Fragment")
        //                .commit();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().findFragmentByTag("One");
            //getFragmentManager().popBackStack();
        }

    }

    public void getPrefs() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String msg = "username = "
                + mPrefs.getString("Username", "NULL")
                + ", theme = "
                + mPrefs.getString("Theme", "Null")
                + ", self_destruct = "
                + mPrefs.getBoolean("self_destruct", false);

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    private SharedPreferences.OnSharedPreferenceChangeListener mSharedPreferencesListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {

                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

                          getPrefs();

                }
            };
}