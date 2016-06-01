package com.gonali.crawler.entry;


import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.utils.CommandLineParser;
import com.gonali.crawler.utils.JedisPoolUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class Crawler {

    public static void kick(CommandLineParser cmdParser) throws Exception {
        //tid_startTime作为appname，即作为这个爬虫的任务名称
        InitCrawlerConfig crawlerConfig = new InitCrawlerConfig(cmdParser.getTid() + cmdParser.getStartTime(),
                cmdParser.getRecallDepth(), cmdParser.getTemplateDir(), cmdParser.getClickRegexDir(),
                cmdParser.getProtocolDir(), cmdParser.getPostRegexDir());

        InstanceFactory.getInstance(crawlerConfig);

        ConfigLoader configLoader = new ConfigLoader();
        List<CrawlerData> crawlerDataList = configLoader.load(cmdParser.getDepth(), cmdParser.getTid(),
                cmdParser.getStartTime(), cmdParser.getPass(), cmdParser.getSeedPath(), cmdParser.getType());

        CrawlerWorkflowManager workflow = new CrawlerWorkflowManager(cmdParser.getTid(), "appname");
        workflow.crawl(crawlerDataList, cmdParser.getTid(), cmdParser.getStartTime(), cmdParser.getPass());
    }

    public static void main(String[] args) {

        CommandLineParser cmdParser = CommandLineParser.getCommandLineParser(args);

        try {

            kick(cmdParser);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            JedisPoolUtils.cleanAll();
        }

        long end_time = System.currentTimeMillis();
        System.out.println("time elapse:" + (end_time - cmdParser.getExecStartTime()));
    }
}
