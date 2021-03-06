package com.gonali.crawler.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 5/31/16.
 */
public class InsertSqlModel {

    private String tableName;
    private Map<String, Object> keyValuePair;
    private List<String> keys;

    public InsertSqlModel() {
        this.keyValuePair = new HashMap<>();
        this.keys = new ArrayList<>();
        this.tableName = "";
    }

    public InsertSqlModel(String tableName) {
        this.keyValuePair = new HashMap<>();
        this.keys = new ArrayList<>();
        this.tableName = tableName;
    }

    public Object addKeyValue(String key, Object value) {

        keys.add(key);
        return this.keyValuePair.put(key, value);
    }

    public Object deleKeyValue(String key) {
        int i = 0;
        for (String k : keys) {
            if (k.equals(key)) {
                keys.remove(i);
                break;
            }
            ++i;
        }
        return this.keyValuePair.remove(key);
    }

    public Object getKeyValue(String key) {

        return this.keyValuePair.get(key);
    }

    public String getInsertSql() {

        String prefix = "INSERT INTO " + this.tableName + " ";
        String attr = new String();
        String value = new String();

        int i = 0;
        for (String k : keys) {
            ++i;
            attr += k;
            value += keyValuePair.get(k);

            if (i != keys.size()) {
                attr += ",";
                value += ",";
            }
        }

        return prefix + "(" + attr + ")" + " VALUES (" + value + ")";
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
