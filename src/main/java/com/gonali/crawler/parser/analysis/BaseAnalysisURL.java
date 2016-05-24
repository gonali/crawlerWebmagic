package com.gonali.crawler.parser.analysis;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2015/11/12.
 */
public class BaseAnalysisURL
{
    String url = null;
    String title = null;
    long date = 0;
    String html = null;
    String text;

    public void setDate(long date)
    {
        this.date = date;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    BaseAnalysisURL(String url, String title, long date, String html)
    {
        this.url = url;
        this.title = title;
        this.date = date;
        this.html = html;
    }

    public String getHtml()
    {
        return html;
    }

    public void setHtml(String html)
    {
        this.html = html;
    }

    public long getDate()
    {
        return date;
    }

    public String getDateTime()
    {
        if (date==0) return "0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        String str = sdf.format(date);
        return str;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getUrl()
    {
        return url;
    }
}
