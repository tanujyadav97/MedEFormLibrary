package com.hackslash.medeformlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    LinearLayout queryLL, conditions;
    Button submit, addcond;
    Spinner queryTypeSpinner;
    LinearLayout patientIDLL, archetypeNameLL, startTimestampLL, endTimestampLL, cond;
    EditText patientID, archetypeName, startTimestamp, endTimestamp;
    TextView out, output;

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
        queryLL = findViewById(R.id.query);
        queryLL.setVisibility(View.VISIBLE);
        patientIDLL = findViewById(R.id.patientidLL);
        archetypeNameLL = findViewById(R.id.archetypenameLL);
        startTimestampLL = findViewById(R.id.starttimestampLL);
        endTimestampLL = findViewById(R.id.endtimestampLL);
        submit = findViewById(R.id.submit);
        conditions = findViewById(R.id.conditions);
        patientID = findViewById(R.id.patientid);
        archetypeName = findViewById(R.id.archetypename);
        startTimestamp = findViewById(R.id.starttimestamp);
        endTimestamp = findViewById(R.id.endtimestamp);
        queryTypeSpinner = findViewById(R.id.query_type);
        cond = findViewById(R.id.cond);
        addcond = findViewById(R.id.addcond);
        out = findViewById(R.id.out);
        output = findViewById(R.id.output);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.querytype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        queryTypeSpinner.setAdapter(adapter);

        queryTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                patientIDLL.setVisibility(View.GONE);
                archetypeNameLL.setVisibility(View.GONE);
                conditions.setVisibility(View.GONE);
                startTimestampLL.setVisibility(View.GONE);
                endTimestampLL.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                out.setVisibility(View.VISIBLE);
                output.setVisibility(View.VISIBLE);
                output.setText("");

                cond.removeViews(1, cond.getChildCount() - 1);

                switch (i) {
                    case 0:
                        patientIDLL.setVisibility(View.VISIBLE);
                        conditions.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        archetypeNameLL.setVisibility(View.VISIBLE);
                        conditions.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        patientIDLL.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        archetypeNameLL.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        patientIDLL.setVisibility(View.VISIBLE);
                        startTimestampLL.setVisibility(View.VISIBLE);
                        endTimestampLL.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        archetypeNameLL.setVisibility(View.VISIBLE);
                        startTimestampLL.setVisibility(View.VISIBLE);
                        endTimestampLL.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        patientIDLL.setVisibility(View.VISIBLE);
                        archetypeNameLL.setVisibility(View.VISIBLE);
                        startTimestampLL.setVisibility(View.VISIBLE);
                        endTimestampLL.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addcond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        3.0f
                );
                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2.0f
                );

                LinearLayout ll = new LinearLayout(getApplicationContext());
                ll.setOrientation(LinearLayout.HORIZONTAL);

                Spinner sp = new Spinner(getApplicationContext());
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.conds, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp.setAdapter(adapter);
                sp.setLayoutParams(param);

                EditText etkey = new EditText(getApplicationContext());
                etkey.setLayoutParams(param1);
                etkey.setHint("Key");

                EditText etvalue = new EditText(getApplicationContext());
                etvalue.setLayoutParams(param1);
                etvalue.setHint("Value");

                ll.addView(sp);
                ll.addView(etkey);
                ll.addView(etvalue);

                cond.addView(ll);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Queries qry = new Queries();
                switch (queryTypeSpinner.getSelectedItemPosition()) {
                    case 0:
                        ArrayList<QueryObj> queries = new ArrayList<>();
                        int count = cond.getChildCount();
                        View v = null;
                        for (int i = 0; i < count; i++) {
                            v = cond.getChildAt(i);
                            queryType qrytype = getQueryType(((Spinner) ((LinearLayout) v).getChildAt(0)).getSelectedItemPosition());
                            String key = ((EditText) ((LinearLayout) v).getChildAt(1)).getText().toString();
                            String value = ((EditText) ((LinearLayout) v).getChildAt(2)).getText().toString();

                            queries.add(new QueryObj(qrytype, key, value));
                        }
                        qry.selectPatientQuery(patientID.getText().toString(), queries,
                                new QueryCallback() {
                                    public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
                                        if (data == null) {
                                            Log.e(TAG, exception);
                                            output.setText(exception);
                                        } else {
                                            Log.d(TAG, data.toString());
                                            output.setText(data.toString());
                                        }
                                    }
                                });
                        break;
                    case 1:
                        ArrayList<QueryObj> queries1 = new ArrayList<>();
                        int count1 = cond.getChildCount();
                        View v1 = null;
                        for (int i = 0; i < count1; i++) {
                            v1 = cond.getChildAt(i);
                            queryType qrytype = getQueryType(((Spinner) ((LinearLayout) v1).getChildAt(0)).getSelectedItemPosition());
                            String key = ((EditText) ((LinearLayout) v1).getChildAt(1)).getText().toString();
                            String value = ((EditText) ((LinearLayout) v1).getChildAt(2)).getText().toString();

                            queries1.add(new QueryObj(qrytype, key, Integer.parseInt(value)));
                            //TODO: to solve this, can add one more spinner with int/bool/string/float
                        }
                        qry.selectArchetypeQuery(archetypeName.getText().toString(), queries1,
                                new QueryCallback() {
                                    public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
                                        if (data == null) {
                                            Log.e(TAG, exception);
                                            output.setText(exception);
                                        } else {
                                            Log.d(TAG, data.toString());
                                            output.setText(data.toString());
                                        }
                                    }
                                });
                        break;
                    case 2:
                        qry.getPatientQuery(patientID.getText().toString(), new QueryCallback() {
                            @Override
                            public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
                                if (data == null) {
                                    Log.e(TAG, exception);
                                    output.setText(exception);
                                } else {
                                    Log.d(TAG, data.toString());
                                    output.setText(data.toString());
                                }
                            }
                        });
                        break;
                    case 3:
                        qry.getArchetypeQuery(archetypeName.getText().toString(), new QueryCallback() {
                            @Override
                            public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
                                if (data == null) {
                                    Log.e(TAG, exception);
                                    output.setText(exception);
                                } else {
                                    Log.d(TAG, data.toString());
                                    output.setText(data.toString());
                                }
                            }
                        });
                        break;
                    case 4:
                        qry.getPatientTimestampQuery(patientID.getText().toString(), startTimestamp.getText().toString(),
                                endTimestamp.getText().toString(), new QueryCallback() {
                                    @Override
                                    public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
                                        if (data == null) {
                                            Log.e(TAG, exception);
                                            output.setText(exception);
                                        } else {
                                            Log.d(TAG, data.toString());
                                            output.setText(data.toString());
                                        }
                                    }
                                });
                        break;
                    case 5:
                        qry.getArchetypeTimestampQuery(archetypeName.getText().toString(),
                                startTimestamp.getText().toString(), endTimestamp.getText().toString(), new QueryCallback() {
                                    @Override
                                    public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
                                        if (data == null) {
                                            Log.e(TAG, exception);
                                            output.setText(exception);
                                        } else {
                                            Log.d(TAG, data.toString());
                                            output.setText(data.toString());
                                        }
                                    }
                                });
                        break;
                    case 6:
                        qry.getPatientArchetypeTimestampQuery(patientID.getText().toString(), archetypeName.getText().toString(),
                                startTimestamp.getText().toString(), endTimestamp.getText().toString(), new QueryCallback() {
                                    @Override
                                    public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
                                        if (data == null) {
                                            Log.e(TAG, exception);
                                            output.setText(exception);
                                        } else {
                                            Log.d(TAG, data.toString());
                                            output.setText(data.toString());
                                        }
                                    }
                                });
                        break;
                }
            }
        });


//        Queries qry = new Queries();
//
//        ArrayList<QueryObj> queries1 = new ArrayList<>();
//        queries1.add(new QueryObj(queryType.WHERE_GREATER_THAN, "Hb Count", 0));
//        queries1.add(new QueryObj(queryType.WHERE_LESS_THAN, "Hb Count", 300));
//        qry.selectArchetypeQuery("openEHR-EHR-OBSERVATION.soap_investigations.v8.adl", queries1,
//                new QueryCallback() {
//                    public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
//                        if (data == null)
//                            Log.e(TAG, exception);
//                        else
//                            Log.d(TAG, data.toString());
//                    }
//                });
//
//        ArrayList<QueryObj> queries2 = new ArrayList<>();
//        queries2.add(new QueryObj(queryType.WHERE_EQUAL_TO, "Source of Referral", "Hospital"));
////        queries2.add(new QueryObj(queryType.WHERE_EQUAL_TO,"Grade","B"));
//        qry.selectPatientQuery("1", queries2,
//                new QueryCallback() {
//                    public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
//                        if (data == null)
//                            Log.e(TAG, exception);
//                        else
//                            Log.d(TAG, data.toString());
//                    }
//                });
//
//        qry.getPatientQuery("1", new QueryCallback() {
//            @Override
//            public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
//                if (data == null)
//                    Log.e(TAG, exception);
//                else
//                    Log.d(TAG, data.toString());
//            }
//        });
//
//        qry.getArchetypeQuery("openEHR-EHR-EVALUATION.SOAP_Assessment_RCP.v3.adl", new QueryCallback() {
//            @Override
//            public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
//                if (data == null)
//                    Log.e(TAG, exception);
//                else
//                    Log.d(TAG, data.toString());
//            }
//        });
//
//        qry.getPatientTimestampQuery("1", "1555404623", "1555404624", new QueryCallback() {
//            @Override
//            public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
//                if (data == null)
//                    Log.e(TAG, exception);
//                else
//                    Log.d(TAG, data.toString());
//            }
//        });
//
//        qry.getArchetypeTimestampQuery("openEHR-EHR-EVALUATION.SOAP_Assessment_RCP.v3.adl",
//                "1555404771", "1555404780", new QueryCallback() {
//                    @Override
//                    public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
//                        if (data == null)
//                            Log.e(TAG, exception);
//                        else
//                            Log.d(TAG, data.toString());
//                    }
//                });
//
//        qry.getPatientArchetypeTimestampQuery("1", "openEHR-EHR-EVALUATION.SOAP_Assessment_RCP.v3.adl",
//                "1555404622", "1555404623", new QueryCallback() {
//                    @Override
//                    public void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception) {
//                        if (data == null)
//                            Log.e(TAG, exception);
//                        else
//                            Log.d(TAG, data.toString());
//                    }
//                });
    }

    private queryType getQueryType(int i) {
        switch (i) {
            case 0:
                return queryType.WHERE_LESS_THAN;
            case 1:
                return queryType.WHERE_LESS_THAN_OR_EQUAL_TO;
            case 2:
                return queryType.WHERE_EQUAL_TO;
            case 3:
                return queryType.WHERE_GREATER_THAN;
            case 4:
                return queryType.WHERE_GREATER_THAN_OR_EQUAL_TO;
            case 5:
                return queryType.WHERE_ARRAY_CONTAINS;
        }
        return null;
    }
}
