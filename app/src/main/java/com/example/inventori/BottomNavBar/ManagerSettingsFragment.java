package com.example.inventori.BottomNavBar;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.inventori.R;

public class ManagerSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}