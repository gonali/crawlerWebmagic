package com.gonali.crawler.queue;

import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.utils.JSONUtil;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class RedisCrawledQue {

    public void putCrawledQue(List<CrawlerData> crawlerData, Jedis jedis, String taskid) {

        try {

            for (CrawlerData data : crawlerData) {

                try {

                    String crawlDataJson = JSONUtil.object2JacksonString(data);
                    jedis.hset("webmagicCrawler::Crawled::" + taskid, data.getUrl(), crawlDataJson);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }
}
