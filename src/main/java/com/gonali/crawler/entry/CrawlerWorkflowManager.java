package com.gonali.crawler.entry;

import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.model.rdb.FengBirdModel;
import com.gonali.crawler.parser.analysis.TextAnalysis;
import com.gonali.crawler.pipeline.impl.EsPipeline;
import com.gonali.crawler.pipeline.impl.HbasePipeline;
import com.gonali.crawler.pipeline.impl.MysqlPipeline;
import com.gonali.crawler.queue.RedisCrawledQue;
import com.gonali.crawler.queue.RedisToCrawlQue;
import com.gonali.crawler.scheuler.RedisScheduler;
import com.gonali.crawler.service.WholesitePageProcessor;
import com.gonali.crawler.utils.BloomFilter;
import com.gonali.crawler.utils.JedisPoolUtils;
import com.gonali.crawler.utils.LogManager;
import redis.clients.jedis.Jedis;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class CrawlerWorkflowManager {
    private LogManager logger = new LogManager(CrawlerWorkflowManager.class);
    //初始化爬虫工厂是，用于解析的模板文件
    private TextAnalysis textAnalysis = InstanceFactory.getTextAnalysis();

    //待爬取队列
    private RedisToCrawlQue nextQueue = InstanceFactory.getRedisToCrawlQue();
    //已爬取队列
    private RedisCrawledQue crawledQueue = InstanceFactory.getRedisCrawledQue();

    private String tid;

    private String appname;

    public CrawlerWorkflowManager(String tid, String appname) {
        this.tid = tid;
        this.appname = appname;
    }

    public void crawl(List<CrawlerData> seeds, String tid, String starttime, int pass) throws IOException {

        //JedisPoolUtils jedisPoolUtils = new JedisPoolUtils();
        Jedis jedis = JedisPoolUtils.getJedis();//jedisPoolUtils.getJedis();
        nextQueue.putNextUrls(seeds, jedis, tid);
        //初始化布隆过滤hash表
        BloomFilter bloomFilter = new BloomFilter(jedis, 1000, 0.001f, (int) Math.pow(2, 31));
        for (CrawlerData seed : seeds) {
            bloomFilter.add("redis:bloomfilter", seed.getUrl());
        }
        //初始化webMagic的Spider程序
        initSpider(seeds, textAnalysis);
    }

    protected void initSpider(List<CrawlerData> seeds, TextAnalysis textAnalysis) {
        String tempUrl = "";
        for (CrawlerData crawlerData : seeds) {
            tempUrl += crawlerData.getUrl() + ",";
        }
        String urls = tempUrl.substring(0, tempUrl.length() - 1);

        Spider.create(new WholesitePageProcessor(tid, textAnalysis))
                .setScheduler(new RedisScheduler())
                        //从seed开始抓
                .addUrl(urls)
                        //存入mysql
                .addPipeline(new MysqlPipeline("tb_fbird", new FengBirdModel()))
                        //存入elasticSearch
//                .addPipeline(new EsPipeline())
//                .addPipeline(new ConsolePipeline())
//                .addPipeline(new HbaseEsPipeline())
//                .addPipeline(new HbasePipeline())
                        //开启5个线程抓取
                .thread(5)
//                .thread(1)
                        //启动爬虫
                .run();
    }
}
