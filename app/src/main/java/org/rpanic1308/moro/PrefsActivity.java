package org.rpanic1308.moro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

import rpanic1308.ceres.R;

/**
 * Created by morot on 06.06.2017.
 */

public class PrefsActivity extends PreferenceActivity{
    static SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
            ListPreference hotword = (ListPreference) findPreference("pmdl");
            SwitchPreference switchPref = (SwitchPreference) findPreference("notification");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("value", hotword.getValue());
            editor.apply();

            switchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    Intent intent = new Intent("ai.kitt.snowboy.Notification");
                    intent.putExtra("Notification", (boolean)o);
                    getActivity().sendBroadcast(intent);
                    preference.setSummary(o.toString());
                    return true;
                }
            });

            hotword.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {

                    Intent intent = new Intent("ai.kitt.snowboy.HOTWORD");
                    intent.putExtra("HOTWORD", o.toString());
                    getActivity().sendBroadcast(intent);
                    preference.setSummary(o.toString());
                    return true;
                }
            });


        }
    }
}
