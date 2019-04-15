package com.hackslash.medeformlibrary;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hackslash.medeformlibrary.queryType;

public class Queries {
    private String TAG = "Queries";
    private FirebaseFirestore mFirestore;

    Queries() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    public void selectPatientQuery(String patientId, ArrayList<QueryObj> queries, QueryCallback queryCallback) {
        CollectionReference ref = mFirestore.collection("EHR1").document("EHR1")
                .collection(patientId);
        selectQuery(ref, queries, queryCallback);
    }

    public void selectArchetypeQuery(String archetypeName, ArrayList<QueryObj> queries, QueryCallback queryCallback) {
        CollectionReference ref = mFirestore.collection("EHR2").document("EHR2")
                .collection(archetypeName);
        selectQuery(ref, queries, queryCallback);
    }

    public void selectQuery(CollectionReference ref, ArrayList<QueryObj> queries, final QueryCallback queryCallback) {
        Query query = null;
        boolean fl = false;

        for (QueryObj obj : queries) {
            try {

                switch (obj.querytype) {
                    case WHERE_EQUAL_TO:
                        if (fl)
                            query = query.whereEqualTo(obj.key, obj.value);
                        else {
                            query = ref.whereEqualTo(obj.key, obj.value);
                            fl = true;
                        }
                        break;
                    case WHERE_LESS_THAN:
                        if (fl)
                            query = query.whereLessThan(obj.key, obj.value);
                        else {
                            query = ref.whereLessThan(obj.key, obj.value);
                            fl = true;
                        }
                        break;
                    case WHERE_GREATER_THAN:
                        if (fl)
                            query = query.whereGreaterThan(obj.key, obj.value);
                        else {
                            query = ref.whereGreaterThan(obj.key, obj.value);
                            fl = true;
                        }
                        break;
                    case WHERE_LESS_THAN_OR_EQUAL_TO:
                        if (fl)
                            query = query.whereLessThanOrEqualTo(obj.key, obj.value);
                        else {
                            query = ref.whereLessThanOrEqualTo(obj.key, obj.value);
                            fl = true;
                        }
                        break;
                    case WHERE_GREATER_THAN_OR_EQUAL_TO:
                        if (fl)
                            query = query.whereGreaterThanOrEqualTo(obj.key, obj.value);
                        else {
                            query = ref.whereGreaterThanOrEqualTo(obj.key, obj.value);
                            fl = true;
                        }
                        break;
                    case WHERE_ARRAY_CONTAINS:
                        if (fl)
                            query = query.whereArrayContains(obj.key, obj.value);
                        else {
                            query = ref.whereArrayContains(obj.key, obj.value);
                            fl = true;
                        }
                        break;
                }
            } catch (Exception e) {
                queryCallback.onCallback(null, e.toString());
                return;
            }
        }

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                queryCallback.onCallback(extractData(docs), null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.toString());
                queryCallback.onCallback(null, e.toString());
            }
        });
    }

    public void getPatientQuery(String patientID) {

    }

    public void getArchetypeQuery(String archetypeName) {

    }

    private HashMap<String, HashMap<String, Map<String, Object>>> extractData(List<DocumentSnapshot> docs) {
        final HashMap<String, HashMap<String, Map<String, Object>>> data = new HashMap<>();
        for (DocumentSnapshot doc : docs) {
            final Map<String, Object> map = doc.getData();
            String[] key = doc.getId().split("@");
            String pidOrArche = key[0];
            final String timestamp = key[1];

            if (data.containsKey(pidOrArche)) {
                data.get(pidOrArche).put(timestamp, map);
            } else {
                data.put(pidOrArche, new HashMap() {{
                    put(timestamp, map);
                }});
            }
        }
        return data;
    }
}
