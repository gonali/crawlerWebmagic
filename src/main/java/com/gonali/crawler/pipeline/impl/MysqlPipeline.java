package com.gonali.crawler.pipeline.impl;

import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.model.rdb.RdbModel;
import com.gonali.crawler.pipeline.dbclient.MysqlClient;
import org.apache.http.annotation.ThreadSafe;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

/**
 * Created by TianyuanPan on 5/4/16.
 */

@ThreadSafe
public class MysqlPipeline extends BaseDBPipeline {

    MysqlClient dbClient;
    String tableName;
    RdbModel rdbModel;

    public MysqlPipeline(String tableName, RdbModel rdbModel) {

        this.dbClient = new MysqlClient();
        this.tableName = tableName;
        this.rdbModel = rdbModel;
    }

    @Override
    public int insertRecord(Object obj) {
        return 0;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

        System.out.println("MysqlPipeline resultItems size: " + resultItems.getAll().size() +
                "\n\tTask uuid: " + task.getUUID());

        logger.debug("MysqlPipeline resultItems size: " + resultItems.getAll().size() +
                "\n\tTask uuid: " + task.getUUID());

        CrawlerData crawlerData = resultItems.get("crawlerData");

        if (crawlerData == null) {
            System.out.println("MysqlPipeline crwalerData is NULL");
            logger.warn("MysqlPipeline crwalerData is NULL !!!");
            return;
        }


        add(tableName, crawlerData);

        int sum = doInsert();
        System.out.println("MysqlPipeline doInsert Successful number: " + sum);
        logger.debug("MysqlPipeline doInsert Successful number: " + sum);

    }


    public void add(String tablename, CrawlerData data) {
        this.dbClient.addItem(tablename, this.rdbModel,data);
    }

    public int doInsert() {
        this.dbClient.getConnection();
        int sum = this.dbClient.doSetInsert();
        this.dbClient.closeConnection();
        return sum;
    }


/*    public static void main(String[] args) {

        MysqlPipeline mysqlPipeline = new MysqlPipeline();
        mysqlPipeline.dbClient.getConnection();
        System.out.println("connection Status: " + mysqlPipeline.dbClient.isConnOpen());
        if (mysqlPipeline.dbClient.isConnOpen())
            mysqlPipeline.dbClient.closeConnection();

    }*/
}
