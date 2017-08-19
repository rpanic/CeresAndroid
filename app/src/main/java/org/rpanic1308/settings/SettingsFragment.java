package org.rpanic1308.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import rpanic1308.ceres.R;

/**
 * Created by Team_ on 17.04.2017.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }
}
