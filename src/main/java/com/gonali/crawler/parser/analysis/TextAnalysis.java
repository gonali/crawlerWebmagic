package com.gonali.crawler.parser.analysis;


import com.gonali.crawler.model.CrawlerData;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 15-12-22.
 */
public class TextAnalysis implements Serializable {

    private WholeSiteAnalysis wholeSiteAnalysis;

    public TextAnalysis(WholeSiteAnalysis wholeSiteAnalysis) {
        this.wholeSiteAnalysis = wholeSiteAnalysis;
    }

    public List<CrawlerData> analysisHtml(CrawlerData crawlerData)   {
        List<CrawlerData> crawlerDataList = new ArrayList<>();

        String html = crawlerData.getHtml();

        if(html != null || html.length() > 0) {
            String rootUrl = crawlerData.getRootUrl();
            long depth = crawlerData.getDepthfromSeed();
            if(depth < 6 )  {
                String fromUrl = crawlerData.getUrl();
                try {
                    List<BaseURL> baseURLList = wholeSiteAnalysis.getUrlList(fromUrl,html);
                    for (BaseURL baseURL : baseURLList) {
                        CrawlerData newCrawlerData = createNewCrawlData(baseURL,rootUrl,depth,fromUrl, crawlerData.getPass(), crawlerData.getTid(), crawlerData.getStartTime());
                        crawlerDataList.add(newCrawlerData);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            crawlerData.setText(wholeSiteAnalysis.getText());
        }
        crawlerData.setFetched(true);
        crawlerDataList.add(crawlerData);
        return crawlerDataList;
    }

    public CrawlerData createNewCrawlData(BaseURL baseURL, String rootURL, long depth, String fromUrl, int pass, String tid, String startTime)   {
        CrawlerData crawlerData = new CrawlerData();
        if(baseURL != null) {
            String url = baseURL.getUrl();
            crawlerData.setTid(tid);
            crawlerData.setStartTime(startTime);
            crawlerData.setRootUrl(rootURL);
            crawlerData.setDepthfromSeed(depth + 1);
            crawlerData.setFromUrl(fromUrl);
            crawlerData.setPublishTime(baseURL.getDate());
            crawlerData.setUrl(url);
            crawlerData.setPass(pass);
            crawlerData.setTitle(baseURL.getTitle());
            crawlerData.setFetched(false);
        }
        return crawlerData;
    }

}
