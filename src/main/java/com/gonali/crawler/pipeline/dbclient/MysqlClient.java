package com.gonali.crawler.pipeline.dbclient;


import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.model.InsertSqlModel;
import com.gonali.crawler.model.rdb.RdbModel;
import com.gonali.crawler.utils.ConfigUtils;
import com.gonali.crawler.utils.MySqlPoolUtils;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 5/4/16.
 */
public class MysqlClient extends AbstractDBClient {

    private Statement myStatement;
    private ConfigUtils configUtils;
    private MySqlPoolUtils pool;


    private List<InsertSqlModel> insertSqlModelList;

    public MysqlClient() {

        this.insertSqlModelList = new ArrayList<>();
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
        int size = this.insertSqlModelList.size();
        for (int i = 0; i < size; ++i) {

            try {

                String sql = this.insertSqlModelList.get(i).getInsertSql();
                lineSum += this.myStatement.executeUpdate(sql);

            } catch (Exception ex) {
                System.out.println("SQL excute Exception ...");
                logger.warn("SQL excute Exception ...");
                //ex.printStackTrace();

            }
        }
        this.insertSqlModelList = new ArrayList<>();
        return lineSum;
    }

    @Override
    public boolean isConnOpen() {
        return this.connOpen;
    }


    public Object addItem(String tableName, RdbModel rdbModel, CrawlerData data) {

        rdbModel.setThisModelFields(data);
        InsertSqlModel model = rdbModel.insertSqlModelBuilder(tableName);
        insertSqlModelList.add(model);
        return model;
    }

}
