package com.gonali.crawler.queue;

import com.gonali.crawler.model.CrawlData;
import com.gonali.crawler.utils.JSONUtil;
import com.gonali.crawler.utils.LogManager;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class RedisToCrawlQue {
    private transient static LogManager logger = new LogManager(RedisToCrawlQue.class);

    public void putNextUrls(List<CrawlData> crawlData,Jedis jedis, String tid ) {

        for (CrawlData nextCrawlData : crawlData) {
            String crawlDataJson = JSONUtil.object2JacksonString(nextCrawlData);
            jedis.hset("webmagicCrawler::ToCrawl::" + tid, nextCrawlData.getUrl(), crawlDataJson);
        }
    }

}
