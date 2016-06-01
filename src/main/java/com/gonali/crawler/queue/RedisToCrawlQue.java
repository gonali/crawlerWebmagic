package com.gonali.crawler.queue;

import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.utils.JSONUtil;
import com.gonali.crawler.utils.LogManager;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class RedisToCrawlQue {

    public void putNextUrls(List<CrawlerData> crawlerData, Jedis jedis, String tid) {

        for (CrawlerData nextCrawlerData : crawlerData) {
            try {

                String crawlDataJson = JSONUtil.object2JacksonString(nextCrawlerData);
                jedis.hset("webmagicCrawler::ToCrawl::" + tid, nextCrawlerData.getUrl(), crawlDataJson);

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }
    }

}
