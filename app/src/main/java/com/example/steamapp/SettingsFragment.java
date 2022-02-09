package com.example.steamapp;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstancestate, String rootKey){
        addPreferencesFromResource(R.xml.prefs);
    }
}
