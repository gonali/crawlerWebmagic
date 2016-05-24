package com.gonali.crawler.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 5/18/16.
 */
public class CrawlerDataUtils {

    private List<Map<String, Object>> fieldList;

    private CrawlerDataUtils() {

        fieldList = new ArrayList();
    }


    public static CrawlerDataUtils getCrawlerDataUtils(Object data) {

        CrawlerDataUtils utils = new CrawlerDataUtils();
        Map<String, Object> infoMap = null;

        Field[] fields = data.getClass().getDeclaredFields();
//        String[] fieldNames = new String[fields.length];


//        System.out.println("########################################");
        for (int i = 0; i < fields.length; i++) {
            infoMap = new HashMap();
            infoMap.put("type", fields[i].getType().toString());

//            System.out.println("type is: " + fields[i].getType().toString());

            infoMap.put("name", fields[i].getName());

            if (fields[i].getType().toString().equals("boolean"))
                infoMap.put("value", utils.getBooleanFieldValueByName(fields[i].getName(), data));
            else
                infoMap.put("value", utils.getFieldValueByName(fields[i].getName(), data));

            utils.fieldList.add(infoMap);
        }
//        System.out.println("########################################");

        return utils;
    }


    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
//            System.out.println("getter is: " + getter);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Object getBooleanFieldValueByName(String fieldName, Object o) {

        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "is" + firstLetter + fieldName.substring(1);
//            System.out.println("getter is: " + getter);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public List<Map<String, Object>> getAttributeInfoList() {

        return this.fieldList;
    }

}
