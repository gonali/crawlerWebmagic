package com.gonali.crawler.entry;


import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.utils.JedisPoolUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class Crawler {
    public static void kick(int depth, int pass, String tid, String starttime, String seedpath, String protocolDir,
                            String postregexDir, String type, int recalldepth, String templatesDir, String clickregexDir, String configpath) throws Exception {
        //tid_startTime作为appname，即作为这个爬虫的任务名称
        InitCrawlerConfig crawlerConfig = new InitCrawlerConfig(tid + starttime, recalldepth, templatesDir, clickregexDir, protocolDir, postregexDir);
        InstanceFactory.getInstance(crawlerConfig);

        ConfigLoader configLoader = new ConfigLoader();
        List<CrawlerData> crawlerDataList = configLoader.load(depth, tid, starttime, pass, seedpath, type);

        CrawlerWorkflowManager workflow = new CrawlerWorkflowManager(tid, "appname");
        workflow.crawl(crawlerDataList, tid, starttime, pass);
    }

    public static void main(String[] args) {
        long start_time = System.currentTimeMillis();
        if (args.length < 24) {
            System.out.println("Usage:\n" +
                    "\t  -depth <退出深度>\n" +
                    "\t  -pass <遍数>\n" +
                    "\t  -tid <实例ID>\n" +
                    "\t  -starttime <实例启动时间>" +
                    "\t  -seedpath <种子路径>\n" +
                    "\t  -protocolDir <协议过滤目录>\n" +
                    "\t  -type <实例类型>\n" +
                    "\t  -recalldepth <回溯点击层数>\n" +
                    "\t  -templateDir <模板目录>\n" +
                    "\t  -clickregexDir <点击正则表达式目录>\n" +
                    "\t  -postregexDir <后缀过滤目录>\n" +
                    "\t -configpath <配置文件路径>");
            System.out.println(args.length);
            System.exit(-1);
        }

        int depth = 1;
        int pass = 1;//遍数
        String tid = "SparkCrawler";
        String starttime = "";
        String seedpath = "/opt/IdeaProjects/gengyun/SparkWebCrawler/data/inputConfig.json";
        String protocolDir = "";
        String type = "topic";
        int recalldepth = 3;
        String templateDir = "/SparkCrawler/templates";
        String clickregexDir = "/SparkCrawler/clickregex";
        String postregexDir = "";
        String configpath = "";

        for (int i = 0; i < args.length; i++) {
            if ("-depth".equals(args[i])) {
                depth = Integer.valueOf(args[i + 1]);
                i++;
            } else if ("-pass".equals(args[i])) {
                pass = Integer.valueOf(args[i + 1]);
                i++;
            } else if ("-tid".equals(args[i])) {
                tid = args[i + 1];
                i++;
            } else if ("-starttime".equals(args[i])) {
                starttime = args[i + 1];
                i++;
            } else if ("-seedpath".equals(args[i])) {
                seedpath = args[i + 1];
                i++;
            } else if ("-protocolDir".equals(args[i])) {
                protocolDir = args[i + 1];
                i++;
            } else if ("-type".equals(args[i])) {
                type = args[i + 1];
                i++;
            } else if ("-recalldepth".equals(args[i])) {
                recalldepth = Integer.valueOf(args[i + 1]);
                i++;
            } else if ("-templateDir".equals(args[i])) {
                templateDir = args[i + 1];
                i++;
            } else if ("-clickregexDir".equals(args[i])) {
                clickregexDir = args[i + 1];
                i++;
            } else if ("-postregexDir".equals(args[i])) {
                postregexDir = args[i + 1];
                i++;
            } else if ("-configpath".equals(args[i])) {
                configpath = args[i + 1];
                i++;
            }

        }

        try {
            kick(depth, pass, tid, starttime, seedpath, protocolDir, postregexDir, type, recalldepth, templateDir, clickregexDir, configpath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisPoolUtils.cleanAll();
        }

        long end_time = System.currentTimeMillis();
        System.out.println("time elapse(FenZhong):" + (end_time - start_time) / 1000 / 60);
    }
}
