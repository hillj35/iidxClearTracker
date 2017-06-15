package com.example.jordan.assignment1;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Settings");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PrefsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_general);

            ListPreference difficultyPref = (ListPreference)findPreference("default_difficulty");
            difficultyPref.setSummary(difficultyPref.getEntry());

            ListPreference sortPref = (ListPreference)findPreference("default_sort");
            sortPref.setSummary(sortPref.getEntry());
        }

        @Override
        public void onResume() {
            super.onResume();
            // Set up a listener whenever a key changes
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            // Set up a listener whenever a key changes
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("default_difficulty")) {
                ListPreference difficultyPref = (ListPreference)findPreference(key);
                difficultyPref.setSummary(difficultyPref.getEntry());
            }

            else if (key.equals("default_sort")) {
                ListPreference sortPref = (ListPreference)findPreference("default_sort");
                sortPref.setSummary(sortPref.getEntry());
            }

        }

    }
}
