package com.gonali.crawler.parser.analysis;



import com.gonali.crawler.parser.tool.AnalysisTool;
import com.gonali.crawler.parser.tool.NormalTokens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/10/16.
 */


public class WholeSiteAnalysisNavigation {

    private List<BaseAnalysisURL> ansTokens = new ArrayList<BaseAnalysisURL>();
    private List<String> newTokens = new ArrayList<>();

    private String fromURL;
    private String rootURL;
    private String pageHtml;
    private String text;
    private long depth;



    /**
     * 初始化
     *
     * @return
     */
    public WholeSiteAnalysisNavigation initial(String url, String html) {
        ansTokens.clear();
        newTokens.clear();
        this.fromURL = url;
        this.pageHtml = html;


        return this;
    }


    public String getText() {
        return text;
    }

    public List<BaseAnalysisURL> getUrlList(String url, String html) throws IOException {
        initial(url, html);

        //将网页预处理，转换为token列表

        newTokens = NormalTokens.getHtmlTokens(pageHtml);

        //返回为下一页链接，其它链接存入ansTokens
        AnalysisHtml();

        return ansTokens;
    }


    private void addAnsToken(String url, String title, long date) {
        BaseAnalysisURL baseAnalysisURL = new BaseAnalysisURL(url, title, date, null);
        ansTokens.add(baseAnalysisURL);
    }


    private void AnalysisHtml() {

        String newToken, title, url, xpath;
        long date;
        int newTag = 0;
        int newLength = newTokens.size();
        text="";

        //遍历token寻找链接
        while (true) {
            //当遍历完成返回
            if ((newTag == newLength)) break;

            //获取新的token
            newToken = newTokens.get(newTag);

            //判断token的类型
            int sort = AnalysisTool.getMarketSort(newToken);
            switch (sort) {
                //为a标签
                case 2:
                 {
                    //提取连接的title
                    title = findTitle(newTag, newToken);
                    if (title != null) {
                        //寻找日期
                        date = findDate(newTag);

                        //提取url
                        url = getMarketUrl(newToken);

                        if (url == null) break;
                        addAnsToken(url, title, date);

                    }
                }
                break;
                case 3:
                {
                    url=getImgUrl(newToken);
                    addAnsToken(url,null,0);
                }break;
                case 0:
                {
                    text+=newToken;
                }break;
            }
            newTag++;
        }
    }




    private String getImgUrl(String str) {
        String TITLE_MACHER = ".*src=('|\")(\\S*)('|\").*";
        String url = null;
        String text = str.replaceAll("\\r|\\n", "");

        //匹配url
        Pattern compile = Pattern.compile(TITLE_MACHER, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher(text);
        if (matcher.find()) {
            //拼接url
            url = AnalysisTool.joinUrl(fromURL, matcher.group(2));
        }
        return url;
    }





    /**
     * 在tag上下10个token内寻找最近的时间
     *
     * @param tag 当前标签位置
     * @return
     */
    private long findDate(int tag) {
        //初始化
        String token;
        int nowTag;

        //在标签所在的位置周围寻找日期
        for (int i = 1; i <= 10; i++) {
            nowTag = tag + i + 1;//从a标签后两个开始找时间
            if (nowTag >= newTokens.size()) break;
            token = newTokens.get(nowTag);
            if (AnalysisTool.isDate(token)) return AnalysisTool.getDateFormString(token);

            nowTag = tag - i;
            if (nowTag < 0) break;
            token = newTokens.get(nowTag);
            if (AnalysisTool.isDate(token)) return AnalysisTool.getDateFormString(token);
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
    private String findTitle(int tag, String newToken) {
        String title = AnalysisTool.getMarketTitle(newToken);
        if (title == null) {
            if (AnalysisTool.getMarketSort(newTokens.get(tag + 1)) == 0) {
                title = newTokens.get(tag + 1);
            }
        }
        return title;
    }




    private String getMarketUrl(String str) {
        String TITLE_MACHER = ".*href=('|\")(\\S*)('|\").*";
        String url = null;
        String text = str.replaceAll("\\r|\\n", "");

        //匹配url
        Pattern compile = Pattern.compile(TITLE_MACHER, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher(text);
        if (matcher.find()) {
            //拼接url
            url = AnalysisTool.joinUrl(fromURL, matcher.group(2));
        }
        return url;
    }


}
