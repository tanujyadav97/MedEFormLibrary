package com.hackslash.medeformlibrary;

import java.util.HashMap;
import java.util.Map;

public interface QueryCallback {
    void onCallback(HashMap<String, HashMap<String, Map<String, Object>>> data, String exception);
}
