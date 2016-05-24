package com.gonali.crawler.pipeline.dbclient;

import com.alibaba.fastjson.JSON;
import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.utils.ConfigUtils;
import com.gonali.crawler.utils.RandomUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TianyuanPan on 5/9/16.
 */
public class EsClient extends AbstractDBClient {

    private static final String REQUEST_POST = "POST";
    private static final String REQUEST_GET = "GET";
    private static final String REQUEST_DELETE = "DELETE";
    private static final String REQUEST_PUT = "PUT";

    private String hostname;
    private int port;
    private String indexName;
    private String typeName;


    private List<CrawlerData> dataList;

    private String requestUrl;


    public EsClient() {

        this.hostname = ConfigUtils.getResourceBundle().getString("ES_HOSTNAME");
        this.port = Integer.parseInt(ConfigUtils.getResourceBundle().getString("ES_PORT"));
        this.indexName = ConfigUtils.getResourceBundle().getString("ES_INDEX_NAME");
        this.typeName = ConfigUtils.getResourceBundle().getString("ES_TYPE_NAME");

        this.requestUrl = "http://" + this.hostname + ":" +
                this.port + "/" + this.indexName + "/" + this.typeName + "/";

        this.dataList = new ArrayList<>();

        this.connOpen = false;
    }


    public Object getConnection() {

        return null;
    }

    public void closeConnection() {


    }


    public int doSetInsert() {

        int i = 0;

        for (CrawlerData o : dataList) {

            String dataJson = JSON.toJSONString(o);

            try {

                this.doPut(this.requestUrl + RandomUtils.getRandomString(50) + "_" + new Date().getTime(), dataJson);
                ++i;

            } catch (Exception ex) {
                logger.warn("EsClient doSetInsert Exception!!! Message: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        this.dataList.clear();

        return i;
    }


    public boolean isConnOpen() {
        return this.connOpen;
    }

    public void add(CrawlerData data) {

        this.dataList.add(data);
    }

    public String getRequestUrl() {

        return requestUrl;
    }

    public String doPost(String urlStr, String data) throws Exception {
        System.out.println("EsClient.doPost Request Url: " + urlStr);
        logger.debug("EsClient.doPost Request Url: + urlStr");
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(REQUEST_POST);
        conn.setDoInput(true);
        conn.setDoOutput(true);

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

        logger.debug("EsClient.doPost result:\n" + result + "\n==========");
        br.close();
        return result;
    }


    public String doGet(String urlStr) throws Exception {
        System.out.println("EsClient.doGet Request Url: " + urlStr);
        logger.debug("EsClient.doGet Request Url: + urlStr");
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(REQUEST_GET);

        conn.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result += line;
        }
        System.out.println(result);
        logger.debug("EsClient.doGet result:\n" + result + "\n==========");
        br.close();
        return result;
    }


    public String doPut(String urlStr, String data) throws Exception {
        System.out.println("EsClient.doPut Request Url: " + urlStr);
        logger.debug("EsClient.doPut Request Url: + urlStr");
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(REQUEST_PUT);
        conn.setDoInput(true);
        conn.setDoOutput(true);
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
        logger.debug("EsClient.doPut result:\n" + result + "\n==========");
        br.close();
        return result;

    }


    public String doDelete(String urlStr) throws Exception {

        System.out.println("EsClient.doDelete Request Url: " + urlStr);
        logger.debug("EsClient.doDelete Request Url: + urlStr");
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setRequestMethod(REQUEST_DELETE);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result += line;
        }
        br.close();
        logger.debug("EsClient.doDelete ResponseCode: " + conn.getResponseCode());
        logger.debug("EsClient.doDelete result:\n" + result + "\n==========");
        System.out.println("EsClient.doDelete ResponseCode: " + conn.getResponseCode());

        return result;

    }

/*    public static void main(String[] args) {

        EsClient esClient = new EsClient();
        try {
            esClient.doGet("http://127.0.0.1:9200/doctest/a01/1");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/


}
