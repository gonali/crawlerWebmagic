package com.gonali.crawler.parser.tool;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.Serializable;

/**
 * Created by root on 15-12-16.
 */
public class GetHtml implements Serializable,PageProcessor
{
    private String htmlSrc;
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        htmlSrc = page.getHtml().toString();
        System.out.println(htmlSrc);

    }

    @Override
    public Site getSite() {
        return site;
    }


    public static String getHtmlFromUrl(String url){
        GetHtml th = new GetHtml();
        Spider.create(th)
                .addUrl(url)
                .run();
        return th.htmlSrc;
    }
}