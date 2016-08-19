package com.kfidria.sweethome;


import android.content.SharedPreferences;

import android.os.Bundle;

import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class PrefFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = PreferenceFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
       // updateSummaries();
    }

    @Override
    public void onPause() {
        super.onPause();
        //updateSummaries();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateSummaries();

    }

    private void updateSummaries() {
        final String STNG_USERNAME = "Username";

        getPreferenceScreen().findPreference(STNG_USERNAME)
                .setSummary("Username currently set to " +
                ((EditTextPreference)getPreferenceScreen().findPreference(STNG_USERNAME))
                        .getText());

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

    }


}

