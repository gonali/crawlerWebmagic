package com.gonali.crawler.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/29.
 */
public class CrawlData implements Serializable {
//    private final static long serialVersionUID = -2344403674643228206L;

    private String tid;
    private String url;
    private int statusCode;
    private int pass;//遍数;
    private String type;
    private String rootUrl;
    private String fromUrl;
    private String text;
    private String html;
    private String title;
    private String startTime;
    private long crawlTime;
    private long publishTime;
    private long depthfromSeed;//层数
    private long count;
    private boolean tag;//true：文章，fallse：导航
    private boolean fetched;


    public CrawlData() {}

    public CrawlData(String url, int statusCode, int pass, String type, String rootUrl, String fromUrl, String text, String html, String title,
                     String startTime, long crawlTime, long publishTime, long depthfromSeed, boolean tag, long count, boolean fetched)  {
        this.url = url;
        this.statusCode = statusCode;
        this.pass = pass;
        this.type = type;
        this.rootUrl = rootUrl;
        this.fromUrl = fromUrl;
        this.text = text;
        this.html = html;
        this.title = title;
        this.startTime = startTime;
        this.crawlTime = crawlTime;
        this.publishTime = publishTime;
        this.depthfromSeed = depthfromSeed;
        this.tag = tag;
        this.count = count;
        this.fetched = fetched;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public long getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(long crawlTime) {
        this.crawlTime = crawlTime;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public long getDepthfromSeed() {
        return depthfromSeed;
    }

    public void setDepthfromSeed(long depthfromSeed) {
        this.depthfromSeed = depthfromSeed;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public boolean isFetched() {
        return fetched;
    }

    public void setFetched(boolean fetched) {
        this.fetched = fetched;
    }


}
