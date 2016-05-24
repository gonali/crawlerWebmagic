package com.gonali.crawler.parser.analysis;


import com.gonali.crawler.model.CrawlData;

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

    public List<CrawlData> analysisHtml(CrawlData crawlData)   {
        List<CrawlData> crawlDataList = new ArrayList<>();

        String html = crawlData.getHtml();

        if(html != null || html.length() > 0) {
            String rootUrl = crawlData.getRootUrl();
            long depth = crawlData.getDepthfromSeed();
            if(depth < 6 )  {
                String fromUrl = crawlData.getUrl();
                try {
                    List<BaseURL> baseURLList = wholeSiteAnalysis.getUrlList(fromUrl,html);
                    for (BaseURL baseURL : baseURLList) {
                        CrawlData newCrawlData = createNewCrawlData(baseURL,rootUrl,depth,fromUrl,crawlData.getPass(),crawlData.getTid(),crawlData.getStartTime());
                        crawlDataList.add(newCrawlData);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            crawlData.setText(wholeSiteAnalysis.getText());
        }
        crawlData.setFetched(true);
        crawlDataList.add(crawlData);
        return crawlDataList;
    }

    public CrawlData createNewCrawlData(BaseURL baseURL, String rootURL, long depth, String fromUrl, int pass, String tid, String startTime)   {
        CrawlData crawlData = new CrawlData();
        if(baseURL != null) {
            String url = baseURL.getUrl();
            crawlData.setTid(tid);
            crawlData.setStartTime(startTime);
            crawlData.setRootUrl(rootURL);
            crawlData.setDepthfromSeed(depth + 1);
            crawlData.setFromUrl(fromUrl);
            crawlData.setPublishTime(baseURL.getDate());
            crawlData.setUrl(url);
            crawlData.setPass(pass);
            crawlData.setTitle(baseURL.getTitle());
            crawlData.setFetched(false);
        }
        return crawlData;
    }

}
