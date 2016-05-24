package com.gonali.crawler.pipeline.impl;

import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.pipeline.dbclient.HbaseClient;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

/**
 * Created by TianyuanPan on 5/18/16.
 */
public class HbasePipeline extends BaseDBPipeline {


    private HbaseClient hbaseClient;


    public HbasePipeline() {

        this.hbaseClient = new HbaseClient();
    }

    @Override
    public int insertRecord(Object obj) {
        return 0;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

        System.out.println("HbasePipeline resultItems size: " + resultItems.getAll().size() +
                "\n\tTask uuid: " + task.getUUID());

        logger.debug("HbasePipeline resultItems size: " + resultItems.getAll().size() +
                "\n\tTask uuid: " + task.getUUID());

        CrawlerData crawlerData = resultItems.get("crawlerData");

        if (crawlerData != null) {

            this.hbaseClient.add(crawlerData);
            int i = this.hbaseClient.doSetInsert();
            System.out.println("HbasePipeline doInsert Successful number: " + i);
            logger.debug("HbasePipeline doInsert Successful number: " + i);
            return;
        }

        System.out.println("at HbasePipeline, crawler data IS NULL !!!");
        logger.debug("at HbasePipeline, crawler data IS NULL !!!");

    }

}
