package com.kfidria.sweethome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.util.Log;

/**
 * A placeholder fragment containing a simple view.
 */
public class PrefFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
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

    private void updateSummaries() {
        final String STNG_USERNAME = "Username";

        getPreferenceScreen().findPreference(STNG_USERNAME).setSummary("Username currently set to " +
                ((EditTextPreference)getPreferenceScreen().findPreference(STNG_USERNAME)).getText());

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateSummaries();


    }
}

