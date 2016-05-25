package com.gonali.crawler.pipeline.dbclient;


import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.model.FengBirdModel;
import com.gonali.crawler.utils.ConfigUtils;
import com.gonali.crawler.utils.CrawlerDataUtils;
import com.gonali.crawler.utils.MySqlPoolUtils;

import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 5/4/16.
 */
public class MysqlClient extends AbstractDBClient {

    private Statement myStatement;
    private ConfigUtils configUtils;
    private MySqlPoolUtils pool;


    private List<InsertSqlModel> insertSqlModels;

    public MysqlClient() {

        this.insertSqlModels = new ArrayList<>();
        this.configUtils = ConfigUtils.getConfigUtils("MYSQL_");
        this.pool = MySqlPoolUtils.getMySqlPoolUtils(configUtils);
        this.connection = null;
        this.myStatement = null;
        this.connOpen = false;

    }


    @Override
    public Object getConnection() {

        try {
            logger.debug("get Mysql connection ...");
            this.connection = this.pool.getConnection();
            this.myStatement = this.connection.createStatement();
            if (this.connection != null)
                this.setConnOpen(true);

        } catch (Exception ex) {

            logger.error("get mysql connection error!! Exception Message: " + ex.getMessage());
            ex.printStackTrace();
            this.setConnOpen(false);
            return null;
        }

        return this.connection;
    }

    @Override
    public void closeConnection() {

        try {

            this.pool.releaseConnection(this.connection);

            this.setConnOpen(false);

        } catch (Exception ex) {

            this.setConnOpen(false);
            logger.error("connection.close() exception!! Message:" + ex.getMessage());
            ex.printStackTrace();

        }


    }

    @Override
    public int doSetInsert() {
        int lineSum = 0;
        if (!this.connOpen) {
            System.out.println("Warning: the connection is NOT open!!!");
            logger.warn("Warning: the connection is NOT open!!!");
            return lineSum;
        }
        for (InsertSqlModel model : this.insertSqlModels) {

            try {

                String sql = model.getInsertSql();
                lineSum += this.myStatement.executeUpdate(sql);

            } catch (Exception ex) {
                System.out.println("SQL excute Exception ...");
                logger.warn("SQL excute Exception ...");
                //ex.printStackTrace();

            }
        }
        this.insertSqlModels.clear();
        return lineSum;
    }

    @Override
    public boolean isConnOpen() {
        return this.connOpen;
    }


    public Object addItem(String tableName, CrawlerData data) {

        InsertSqlModel model = new InsertSqlModel(tableName);

        FengBirdModel fengBirdModel = new FengBirdModel();

        List<Map<String, Object>> fieldList = CrawlerDataUtils.getCrawlerDataUtils(data).getAttributeInfoList();

        for (Map<String, Object> m : fieldList) {

            String field = (String) m.get("name");

            switch (field) {
                case "tid":
                    fengBirdModel.setTopicTaskID((String) m.get("value"));
                    break;
                case "url":
                    fengBirdModel.setUrl((String) m.get("value"));
                    break;
                case "crawlTime":
                    fengBirdModel.setCrawlTime((long) m.get("value"));
                    break;
                case "publishTime":
                    fengBirdModel.setLabelTime((long) m.get("value"));
                    break;
                case "title":
                    fengBirdModel.setTitle((String) m.get("value"));
                    break;
                case "rootUrl":
                    fengBirdModel.setRootUrl((String) m.get("value"));
                    break;
                case "fromUrl":
                    fengBirdModel.setFromUrl((String) m.get("value"));
                    break;
                default:
                    break;
            }

        }

        fieldList = CrawlerDataUtils.getCrawlerDataUtils(fengBirdModel).getAttributeInfoList();

        for (Map<String, Object> m : fieldList) {

            String type = (String) m.get("type");

            switch (type) {
                case "long":
                    String dateString = "'" +
                            new SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
                                    .format(new Date((long) m.get("value"))) + "'";

                    model.addKeyValue((String) m.get("name"), dateString);
                    break;
                case "int":
                    model.addKeyValue((String) m.get("name"), m.get("value"));
                    break;
                default:
                    model.addKeyValue((String) m.get("name"), "'" + m.get("value") + "'");
                    break;
            }


        }
/*
        model.addKeyValue("title", "'" + data.getTitle() + "'");
        Long time = data.getPublishTime();
        if (time == null) {
            model.addKeyValue("publicTime", "'" + new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date().getTime()) + "'");
        } else
            model.addKeyValue("publicTime", "'" + new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(data.getPublishTime())) + "'");

        model.addKeyValue("url", "'" + data.getUrl() + "'");
        model.addKeyValue("text", "'" + data.getText() + "'");
        model.addKeyValue("fetched", data.isFetched());
        model.addKeyValue("html", "'" + data.getHtml().replace("\\\'","\'").replace("\'", "\\\'") + "'");
*/

        insertSqlModels.add(model);
        return model;
    }

    public Object addItem(CrawlerData data) {

        return null;
    }

}
