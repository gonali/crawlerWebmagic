package com.gonali.crawler.queue;

import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.utils.JSONUtil;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class RedisCrawledQue {

    public void putCrawledQue(List<CrawlerData> crawlerData, /*JedisPoolUtils jedisPoolUtils*/Jedis jedis, String taskid) {

        try {
//            Jedis jedis = JedisPoolUtils.getJedis();//jedisPoolUtils.getJedisPool().getResource();

            for (CrawlerData data : crawlerData) {
                String crawlDataJson = JSONUtil.object2JacksonString(data);
                jedis.hset("webmagicCrawler::Crawled::" + taskid, data.getUrl(), crawlDataJson);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
