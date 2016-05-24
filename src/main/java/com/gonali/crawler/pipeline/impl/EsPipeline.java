package com.gonali.crawler.pipeline.impl;

import com.gonali.crawler.model.CrawlData;
import com.gonali.crawler.pipeline.dbclient.EsClient;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

/**
 * Created by TianyuanPan on 5/9/16.
 */
public class EsPipeline extends BaseDBPipeline {

    private EsClient esClient;


    public EsPipeline() {

        this.esClient = new EsClient();

    }


    @Override
    public int insertRecord(Object obj) {
        return 0;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {


        System.out.println("EsPipeline resultItems size: " + resultItems.getAll().size() +
                "\n\tTask uuid: " + task.getUUID());

        logger.debug("EsPipeline resultItems size: " + resultItems.getAll().size() +
                "\n\tTask uuid: " + task.getUUID());

        CrawlData crawlerData = resultItems.get("crawlerData");

        if (crawlerData != null) {

            this.esClient.add(crawlerData);
            int i = this.esClient.doSetInsert();
            System.out.println("EsPipeline doInsert Successful number: " + i);
            logger.debug("EsPipeline doInsert Successful number: " + i);
            return;
        }

        System.out.println("at EsPipeline, crawler data IS NULL !!!");
        logger.debug("at EsPipeline, crawler data IS NULL !!!");

    }

}
