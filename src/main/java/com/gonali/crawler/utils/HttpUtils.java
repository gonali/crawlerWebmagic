package com.gonali.crawler.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by TianyuanPan on 5/25/16.
 */
public class HttpUtils {

    private static final String REQUEST_POST = "POST";
    private static final String REQUEST_GET = "GET";
    private static final String REQUEST_DELETE = "DELETE";
    private static final String REQUEST_PUT = "PUT";

    public static String doPost(String urlStr, String data) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(REQUEST_POST);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(30 * 1000);// timeout in 30 s

        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        printWriter.write(data);
        printWriter.flush();
        printWriter.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }
        br.close();

        return result;
    }


    public static String doGet(String urlStr, Map<String, String> paramter) throws Exception {
        String keyValues = "";
        for (Map.Entry<String, String> entry : paramter.entrySet()) {
            String kv = entry.getKey() + "=" + entry.getValue() + "&";
            keyValues += kv;
        }
        keyValues = keyValues.substring(0, keyValues.length() - 1);
        urlStr = urlStr + "?" + keyValues;
        URL url = new URL(urlStr + paramter);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(REQUEST_GET);
        conn.setConnectTimeout(30 * 1000);// timeout in 30 s
        conn.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result += line;
        }
        br.close();
        return result;
    }


    public static String doPut(String urlStr, String data) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(REQUEST_PUT);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(30 * 1000);// timeout in 30 s

        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        printWriter.write(data);
        printWriter.flush();
        printWriter.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result += line;
        }
        br.close();
        return result;

    }


    public static String doDelete(String urlStr, Map<String, String> paramter) throws Exception {
        String keyValues = "";
        for (Map.Entry<String, String> entry : paramter.entrySet()) {
            String kv = entry.getKey() + "=" + entry.getValue() + "&";
            keyValues += kv;
        }
        keyValues = keyValues.substring(0, keyValues.length() - 1);
        urlStr = urlStr + "?" + keyValues;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setRequestMethod(REQUEST_DELETE);
        conn.setConnectTimeout(30*1000);// timeout in 30 s

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result += line;
        }
        br.close();

        return result;

    }
}
