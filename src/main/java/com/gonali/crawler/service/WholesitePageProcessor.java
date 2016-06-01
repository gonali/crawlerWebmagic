package com.gonali.crawler.service;

import com.gonali.crawler.entry.InstanceFactory;
import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.parser.analysis.TextAnalysis;
import com.gonali.crawler.queue.RedisCrawledQue;
import com.gonali.crawler.queue.RedisToCrawlQue;
import com.gonali.crawler.scheuler.RedisBloomFilter;
import com.gonali.crawler.utils.BloomFilter;
import com.gonali.crawler.utils.JSONUtil;
import com.gonali.crawler.utils.JedisPoolUtils;
import redis.clients.jedis.Jedis;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 网页抓取器。
 *
 * @author yinlei
 *         2014-3-5 下午4:27:51
 */
public class WholesitePageProcessor implements PageProcessor {
    private String tid;
    private TextAnalysis textAnalysis;

    public WholesitePageProcessor(String tid, TextAnalysis textAnalysis) {
        this.tid = tid;
        this.textAnalysis = textAnalysis;
    }

    private Site site = Site.me()
            .setDomain(/*"http://blog.ifeng.com/"*//*"http://www.gog.cn"*/"http://china.huanqiu.com/")
            .setCharset("utf-8")
            .setRetryTimes(3)
            .setSleepTime(1000);

    @Override
    public void process(Page page) {
        //JedisPoolUtils jedisPoolUtils = null;
        Jedis jedis = null;
        String url = page.getRequest().getUrl();

        try {
            // jedisPoolUtils = new JedisPoolUtils();
            jedis = JedisPoolUtils.getJedis();//jedisPoolUtils.getJedis();
            String json_crawlData = jedis.hget("webmagicCrawler::ToCrawl::" + tid, page.getRequest().getUrl());
            CrawlerData page_crawlerData = (CrawlerData) JSONUtil.jackson2Object(json_crawlData, CrawlerData.class);
            jedis.hdel("webmagicCrawler::ToCrawl::" + tid, page.getRequest().getUrl());

            int statusCode = page.getStatusCode();
            String html = page.getHtml().toString();

            //对源码和访问状态码进行赋值
            page_crawlerData.setHtml(html);
            page_crawlerData.setStatusCode(statusCode);

            //解析过程
            List<CrawlerData> perPageCrawlDateList = this.getTextAnalysis().analysisHtml(page_crawlerData);

            List<CrawlerData> nextCrawlerData = new ArrayList<>();
            List<CrawlerData> crawledData = new ArrayList<>();

            BloomFilter bloomFilter = new BloomFilter(jedis, 1000, 0.001f, (int) Math.pow(2, 31));
            for (CrawlerData crawlerData : perPageCrawlDateList) {
                if (linkFilter(crawlerData) == true) {
                    if (crawlerData.isFetched() == false) {
                        //链接fetched为false,即导航页,bloomFilter判断待爬取队列没有记录
                        boolean isNew = RedisBloomFilter.notExistInBloomHash(crawlerData.getUrl(), jedis, bloomFilter);
                        if (isNew) {
                            nextCrawlerData.add(crawlerData);
                        }
                    } else {
                        //链接fetched为true,即文章页，添加到redis的已爬取队列
                        crawledData.add(crawlerData);
                        page.putField("crawlerData", crawlerData);
                    }
                }
            }

            RedisToCrawlQue nextQueue = InstanceFactory.getRedisToCrawlQue();

            //加入到待爬取队列
            nextQueue.putNextUrls(nextCrawlerData, jedis, tid);

            //添加到待爬取的targetRequest中
            for (CrawlerData crawlerData : nextCrawlerData) {
                page.addTargetRequest(crawlerData.getUrl());
            }

            //加入到已爬取队列
            new RedisCrawledQue().putCrawledQue(crawledData, jedis, this.tid);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            JedisPoolUtils.cleanJedis(jedis);
        }

    }


    @Override
    public Site getSite() {
        return site;
    }

    public boolean linkFilter(CrawlerData crawlerData) {
        if (!crawlerData.getUrl().endsWith(".css") && !crawlerData.getUrl().endsWith(".js")&& !crawlerData.getUrl().endsWith(".gif")&& !crawlerData.getUrl().endsWith(".png") && !crawlerData.getUrl().endsWith(".jpg") && crawlerData.getUrl().contains("http://china.huanqiu.com/"/*"http://www.gog.cn/"*/)) {
            return true;
        } else {
            return false;
        }
    }

    public TextAnalysis getTextAnalysis() {
        return textAnalysis;
    }

    public void setTextAnalysis(TextAnalysis textAnalysis) {
        this.textAnalysis = textAnalysis;
    }


}

