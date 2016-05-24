package com.gonali.crawler.pipeline.impl;

import com.gonali.crawler.pipeline.DatabasePipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

/**
 * Created by TianyuanPan on 5/19/16.
 */
public class BaseDBPipeline implements DatabasePipeline {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public int insertRecord(Object obj) {
        return 0;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

    }
}
