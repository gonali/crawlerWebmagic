package com.gonali.crawler.parser.analysis;



import com.gonali.crawler.utils.HtmlChangeTokens;
import com.gonali.crawler.utils.TokenUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/16.
 */


public class WholeSiteAnalysis implements Serializable
{
    private static List<BaseURL> ansTokens;
    private static String text;
    private static String fromURL;


    public List<BaseURL> getUrlList(String url, String html) throws IOException
    {
        this.fromURL = url;
        ansTokens = new ArrayList<>();
        //将网页预处理，转换为token列表

        HtmlChangeTokens htmlChangeTokens=new HtmlChangeTokens();
        List<String>  newTokens = htmlChangeTokens.getHtmlTokens(html);

        //返回为下一页链接，其它链接存入ansTokens
        AnalysisHtml(newTokens);
        return ansTokens;
    }


    private void addAnsToken(String url, String title, long date)
    {
        if (url==null||url.length()==0) return;
        BaseURL BaseURL = new BaseURL(url, title, date, null);
        ansTokens.add(BaseURL);
    }


    private void AnalysisHtml(List<String> newTokens)
    {
        String newToken, title, url;
        long date;
        text = "";

        //遍历token寻找链接
        for (int i = 0; i < newTokens.size(); i++) {

            //获取新的token
            newToken = newTokens.get(i);

            //判断token的类型
            int sort = TokenUtil.getMarketSort(newToken);
            switch (sort) {
                //为a标签
                case 2: {
                    //提取连接的title
                    title = findTitle(i, newTokens);

                    //提取url
                    url = TokenUtil.joinURL(fromURL, TokenUtil.getLinkURL(newToken));

                    //寻找日期
                    date = findDate(i,newTokens);
                    addAnsToken(url, title, date);

                }
                break;
                case 3: {
                    url = TokenUtil.joinURL(fromURL, TokenUtil.getImgURL(newToken));
                    addAnsToken(url, null, 0);
                }
                break;
                case 0: {
                    text += newToken;
                }
                break;
            }
        }

    }


    /**
     * 在tag上下10个token内寻找最近的时间
     *
     * @param tag 当前标签位置
     * @return
     */
    private long findDate(int tag,List<String>newTokens)
    {
        //初始化
        String token;
        int nowTag;
        //在标签所在的位置周围寻找日期
        for (int i = 1; i <= 10; i++) {
            nowTag = tag + i + 1;//从a标签后两个开始找时间
            if (nowTag < newTokens.size()) {
                token = newTokens.get(nowTag);
                if (TokenUtil.isDate(token)) return TokenUtil.getDateFormString(token);
            }

            nowTag = tag - i;
            if (nowTag >=0) {
                token = newTokens.get(nowTag);
                if (TokenUtil.isDate(token)) return TokenUtil.getDateFormString(token);
            }
        }
        return 0;
    }


    /**
     * input
     * tag:当前a标签在tokenlist中的位置
     * newToken:a标签的标签内容
     * <p/>
     * return
     * 返回寻找到的title
     */
    private String findTitle(int tag,List<String>newTokens)
    {

        String title = TokenUtil.getTokenTitle(newTokens.get(tag));
        if (title == null&&(tag+1)<newTokens.size()) {
            if (TokenUtil.getMarketSort(newTokens.get(tag + 1)) == 0) {
                title = newTokens.get(tag + 1);
            }
        }
        return title;
    }

    public String getText()
    {
        return text;
    }
}
