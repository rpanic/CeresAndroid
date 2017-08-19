package org.rpanic1308.settings;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.preference.PreferenceActivity;
import android.os.Bundle;

import org.rpanic1308.main.CeresController;

public class SettingsActivity extends PreferenceActivity/*extends AppCompatActivity */{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addPreferencesFromResource(R.xml.userprefs);
        //setContentView(R.layout.activity_settings);
        //getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();

        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                .beginTransaction();
        SettingsFragment mPrefsFragment = new SettingsFragment();
        mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
        mFragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CeresController.SERVER = CeresController.getServerAddress();
    }

    /*private Preference pref;
    private String summaryStr;
    String prefixStr;

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        //Get the current summary
        pref = findPreference(key);
        summaryStr = (String)pref.getSummary();

        //Get the user input data
        prefixStr = sharedPreferences.getString(key, "");

        //Update the summary with user input data
        pref.setSummary(summaryStr.concat(": [").concat(prefixStr).concat("]"));
    }*/
}
