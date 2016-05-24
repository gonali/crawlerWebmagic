package com.gonali.crawler.model;

/**
 * Created by TianyuanPan on 5/20/16.
 */
public class FengBirdModel {

    private String topicTaskID;
    private String title;
    private long   labelTime;
    private String url;
    private String fromUrl;
    private String rootUrl;
    private long   crawlTime;
    private int    deleteflag;

    public FengBirdModel() {

        this.deleteflag = 0;
    }

    public FengBirdModel(CrawlData crawlData) {

        this.topicTaskID = crawlData.getTid();
        this.title = crawlData.getTitle();
        this.labelTime = crawlData.getPublishTime();
        this.url = crawlData.getUrl();
        this.fromUrl = crawlData.getFromUrl();
        this.rootUrl = crawlData.getRootUrl();
        this.crawlTime = crawlData.getCrawlTime();
        this.deleteflag = 0;

    }

    public String getTopicTaskID() {
        return topicTaskID;
    }

    public void setTopicTaskID(String topicTaskID) {
        this.topicTaskID = topicTaskID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getLabelTime() {
        return labelTime;
    }

    public void setLabelTime(long labelTime) {
        this.labelTime = labelTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public long getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(long crawlTime) {
        this.crawlTime = crawlTime;
    }

    public int getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(int deleteflag) {
        this.deleteflag = deleteflag;
    }
}
