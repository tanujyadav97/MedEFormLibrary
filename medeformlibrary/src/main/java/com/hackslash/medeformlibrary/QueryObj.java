package com.hackslash.medeformlibrary;

public class QueryObj {
    public queryType querytype;
    public String key;
    public Object value;

    public QueryObj(queryType querytype, String key, Object value) {
        this.querytype = querytype;
        this.key = key;
        this.value = value;
    }
}
