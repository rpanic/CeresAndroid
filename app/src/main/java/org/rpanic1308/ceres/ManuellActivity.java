package org.rpanic1308.ceres;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import rpanic1308.ceres.R;

public class ManuellActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manuell);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("blabla");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);

    }



}
