package com.gonali.crawler.pipeline.impl;

import com.alibaba.fastjson.JSON;
import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.pipeline.dbclient.EsClient;
import com.gonali.crawler.pipeline.dbclient.HbaseClient;
import com.gonali.crawler.utils.RandomUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TianyuanPan on 5/18/16.
 */
public class HbaseEsPipeline extends BaseDBPipeline {

    private EsClient esClient;
    private HbaseClient hbaseClient;

    private List<CrawlerData> dataList;


    public HbaseEsPipeline() {

        this.esClient = new EsClient();
        this.hbaseClient = new HbaseClient();
        dataList = new ArrayList<>();
    }

    @Override
    public int insertRecord(Object obj) {

        int i = 0, j = 0;

        String rowkey = RandomUtils.getRandomString(50) + "_" + new Date().getTime();

        try {

            this.esClient.doSetInsert(this.esClient.getRequestUrl() + rowkey, JSON.toJSONString(obj));
            ++i;
        } catch (Exception ex) {

            logger.warn("HbaseEsPipeline EsClient.doPut Exception!!! Message:" + ex.getMessage());
//            ex.printStackTrace();
        }

        try {

            this.hbaseClient.insertRecord(hbaseClient.getTableName(),
                    rowkey, hbaseClient.getColumnFamilyName(), (CrawlerData) obj);
            ++j;

        } catch (Exception ex) {

            logger.warn("HbaseEsPipeline HbaseClient.insertRecord Exception!!! Message:" + ex.getMessage());
//            ex.printStackTrace();
        }


        return (i & j);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

        System.out.println("HbaseEsPipeline resultItems size: " + resultItems.getAll().size() +
                "\n\tTask uuid: " + task.getUUID());

        logger.debug("HbaseEsPipeline resultItems size: " + resultItems.getAll().size() +
                "\n\tTask uuid: " + task.getUUID());

        CrawlerData crawlerData = resultItems.get("crawlerData");

        if (crawlerData != null) {

            int i = this.insertRecord(crawlerData);
            System.out.println("HbaseEsPipeline doInsert Successful number: " + i);
            logger.debug("HbaseEsPipeline doInsert Successful number: " + i);
            return;
        }

        System.out.println("at HbaseEsPipeline, crawler data IS NULL !!!");
        logger.debug("at HbaseEsPipeline, crawler data IS NULL !!!");

    }
}
