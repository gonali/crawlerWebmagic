package com.gonali.crawler.model.rdb;

import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.model.InsertSqlModel;
import com.gonali.crawler.utils.CrawlerDataUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 5/20/16.
 */
public class FengBirdModel implements RdbModel{

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

    public FengBirdModel(CrawlerData crawlerData) {

        this.topicTaskID = crawlerData.getTid();
        this.title = crawlerData.getTitle();
        this.labelTime = crawlerData.getPublishTime();
        this.url = crawlerData.getUrl();
        this.fromUrl = crawlerData.getFromUrl();
        this.rootUrl = crawlerData.getRootUrl();
        this.crawlTime = crawlerData.getCrawlTime();
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

    @Override
    public Object setThisModelFields(CrawlerData data) {

        List<Map<String, Object>> fieldList = CrawlerDataUtils.getCrawlerDataUtils(data).getAttributeInfoList();

        for (int i = 0; i < fieldList.size(); ++i) {

            String field = (String) fieldList.get(i).get("name");

            switch (field) {
                case "tid":
                    this.setTopicTaskID((String) fieldList.get(i).get("value"));
                    break;
                case "url":
                    this.setUrl((String) fieldList.get(i).get("value"));
                    break;
                case "crawlTime":
                    this.setCrawlTime((long) fieldList.get(i).get("value"));
                    break;
                case "publishTime":
                    this.setLabelTime((long) fieldList.get(i).get("value"));
                    break;
                case "title":
                    this.setTitle((String) fieldList.get(i).get("value"));
                    break;
                case "rootUrl":
                    this.setRootUrl((String) fieldList.get(i).get("value"));
                    break;
                case "fromUrl":
                    this.setFromUrl((String) fieldList.get(i).get("value"));
                    break;
                default:
                    break;
            }

        }

        return this;

    }

    @Override
    public InsertSqlModel insertSqlModelBuilder(String tableName) {

        InsertSqlModel model = new InsertSqlModel(tableName);

        List<Map<String, Object>> fieldList = CrawlerDataUtils.getCrawlerDataUtils(this).getAttributeInfoList();

        for (int i = 0; i < fieldList.size(); ++i) {

            String type = (String) fieldList.get(i).get("type");

            switch (type) {
                case "long":
                    String dateString = "'" +
                            new SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
                                    .format(new Date((long) fieldList.get(i).get("value"))) + "'";

                    model.addKeyValue((String) fieldList.get(i).get("name"), dateString);
                    break;
                case "int":
                    model.addKeyValue((String) fieldList.get(i).get("name"), fieldList.get(i).get("value"));
                    break;
                default:
                    model.addKeyValue((String) fieldList.get(i).get("name"), "'" + fieldList.get(i).get("value") + "'");
                    break;
            }


        }
        return model;
    }

}
