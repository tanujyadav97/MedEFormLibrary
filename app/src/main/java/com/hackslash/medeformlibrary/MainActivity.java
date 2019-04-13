package com.hackslash.medeformlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GenerateForm gf = new GenerateForm(MainActivity.this, this,
                "Android/data/com.android.hackslash.openehr",
                "openEHR-EHR-OBSERVATION.body_temperature.v2.adl", "OBSERVATION",
                R.id.llroot, "patientid");
        gf.makeForm();
    }
}
