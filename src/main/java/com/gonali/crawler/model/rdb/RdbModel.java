package com.gonali.crawler.model.rdb;

import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.model.InsertSqlModel;

/**
 * Created by TianyuanPan on 5/31/16.
 */
public interface RdbModel {

    public Object setThisModelFields(CrawlerData data);
    public InsertSqlModel insertSqlModelBuilder(String tableName);
}
