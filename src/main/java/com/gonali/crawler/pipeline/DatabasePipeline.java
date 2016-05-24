package com.gonali.crawler.pipeline;

import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * Created by TianyuanPan on 5/4/16.
 */
public interface DatabasePipeline<T> extends Pipeline {

    public int insertRecord(T obj);
}
