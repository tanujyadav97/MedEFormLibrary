package com.hackslash.medeformlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        generateForm();
        doQuery();
    }

    private void generateForm() {
        GenerateForm gf = new GenerateForm(MainActivity.this, this,
                "Android/data/com.android.hackslash.openehr",
                "openEHR-EHR-OBSERVATION.body_temperature.v2.adl", "OBSERVATION",
                R.id.llroot, "patientid");
        gf.makeForm();
    }

    private void doQuery() {
        Queries qry = new Queries();
        ArrayList<QueryObj> queries1 = new ArrayList<>();
        queries1.add(new QueryObj(queryType.WHERE_GREATER_THAN, "Hb Count", 0));
        queries1.add(new QueryObj(queryType.WHERE_LESS_THAN, "Hb Count", 700));
        qry.selectArchetypeQuery("openEHR-EHR-OBSERVATION.soap_investigations.v8.adl", queries1,
                new QueryCallback() {
                    public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
                        if (data == null)
                            Log.e(TAG, exception);
                        else
                            Log.d(TAG, data.toString());
                    }
                });

        ArrayList<QueryObj> queries2 = new ArrayList<>();
        queries2.add(new QueryObj(queryType.WHERE_EQUAL_TO, "Source of Referral", "Mohit yadav"));
//        queries2.add(new QueryObj(queryType.WHERE_EQUAL_TO,"Grade","B"));
        qry.selectPatientQuery("1", queries2,
                new QueryCallback() {
                    public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
                        if (data == null)
                            Log.e(TAG, exception);
                        else
                            Log.d(TAG, data.toString());
                    }
                });
    }
}
