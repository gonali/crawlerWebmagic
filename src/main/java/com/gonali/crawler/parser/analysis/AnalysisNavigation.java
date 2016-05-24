package com.gonali.crawler.parser.analysis;



import com.gonali.crawler.parser.tool.AnalysisTool;
import com.gonali.crawler.parser.tool.JsoupHtml;
import com.gonali.crawler.parser.urljudge.JudgeURL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/10/16.
 */


public class AnalysisNavigation
{
    final static String DATE_MATCHER = "\\S?(\\d{4}(\\-|年|\\\\|/|\\.)\\d{1,2}(\\-|月|\\\\|/|\\.)\\d{1,2}(日)?)\\S?";
    final static String NEXTPAGR_URL_MATCHER1 = ".*(next|last|page).*";
    final static String NEXTPAGR_URL_MATCHER2 = ".*(\\d+|index)_\\d+.\\w+";
    final static String NEXTPAGR_URL_MATCHER3 = ".*(下页|下一页|后页).*";
    final static String NEXTPAGR_URL_MATCHER4 = ".*(next|last|page).*";

    private List<String> newTokens = new ArrayList<String>();//获取历史tokens
    private List<BaseAnalysisURL> ansTokens = new ArrayList<BaseAnalysisURL>();
    private List<String> dateTokens = new ArrayList<String>();
    private List<String> ansTag = new ArrayList<String>();
    private List<String> dateTag = new ArrayList<String>();

    private List<Integer> startList = new ArrayList<Integer>();


    private String domain;//域名
    private String fromURL;
    private String rootURL;
    private BaseURL baseURL;
    private String pageHtml;
    private long depth;

    /**
     * 新得到的URl
     */
    private List<BaseAnalysisURL> newBaseURL = new ArrayList<>();



    /**
     * 初始化
     * @return
     */
    public   AnalysisNavigation  initial(String url,String html)
    {
        newTokens.clear();
        ansTokens.clear();
        dateTokens.clear();
        ansTag.clear();
        dateTag.clear();
        startList.clear();
        this.fromURL = url;
        this.domain = AnalysisTool.getDomain(fromURL);
        this.pageHtml=html;

//        rootURL=baseURL.get

        return this;
    }



    public  List<BaseAnalysisURL> getUrlList(String url,String html) throws IOException
    {
        initial(url, html);


        //将网页预处理，转换为token列表
        newTokens = JsoupHtml.getHtmlTokens(pageHtml);

        //返回为下一页链接，其它链接存入ansTokens
        findLink();

        //将获取到的内容分块
        findBlock();

        //获取
        getAnsList();


        return newBaseURL;
    }




    /**
     * 判断是否该标签为下一页
     * @param url
     * @param title
     * @return true：下一页 false：不是
     */
    static Boolean isNextPage(String url, String title)
    {
        if (Pattern.matches(NEXTPAGR_URL_MATCHER1, url)) return true;
        if (Pattern.matches(NEXTPAGR_URL_MATCHER2, url)) return true;
        if (title.contains("下页|下一页")) return true;

        return false;
    }


    /**
     * 将爬取到的连接添加到待分析队列中
     * @param url
     * @param title
     * @param date
     * @param xpath
     */
    private void addAnsToken(String url,String title,long date,String xpath)
    {
        BaseAnalysisURL baseAnalysisURL =new BaseAnalysisURL(url,title,date,null);
        ansTokens.add(baseAnalysisURL);
        ansTag.add(xpath);
    }


    private void findLink()
    {

        //初始化
        List<String> xpathList = new ArrayList<>();
        String newToken, title, url, xpath;
        long date;

        int newTag = 0;
        int newLength = newTokens.size();



        //遍历token寻找链接
        while (true)
        {
            //当遍历完成返回
            if ((newTag == newLength)) break;

            //获取新的token
            newToken = newTokens.get(newTag);

            //判断token的类型
            int sort = AnalysisTool.getMarketSort(newToken);
            switch (sort)
            {
                case -1:
                    if (xpathList.size() > 0) xpathList.remove(xpathList.size() - 1);
                    break;
                case 1:
                    xpathList.add(newToken);
                    break;

                //为a标签
                case 2:
                {
                    //提取连接的title
                    title = findTitle(newTag, newToken);
                    if (title != null)
                    {
                        //寻找日期
                        date = findDate(newTag);

                        //提取url
                        url = getMarketUrl(newToken);

                        if (url == null) break;

                        //判断是否为下一页
                        if (isNextPage(url,title))
                        {
                            BaseAnalysisURL baseAnalysisURL =new BaseAnalysisURL(url,title,date,null);
                            newBaseURL.add(baseAnalysisURL);
                            break;
                        }

                        xpath = AnalysisTool.getXpath(xpathList);
                        addAnsToken(url,title,date,xpath);


                    }
                }
                break;

            }
            newTag++;
        }
    }



    /**
     * 在tag上下10个token内寻找最近的时间
     * @param tag 当前标签位置
     * @return
     */
    private long findDate(int tag)
    {
        //初始化
        String token;
        int nowTag;

        //在标签所在的位置周围寻找日期
        for (int i = 1; i <= 10; i++)
        {
            nowTag=tag+i+1;//从a标签后两个开始找时间
            if (nowTag >= newTokens.size()) break;
            token = newTokens.get(nowTag);
            if (AnalysisTool.isDate(token)) return AnalysisTool.getDateFormString(token);

            nowTag=tag-i;
            if (nowTag<0) break;
            token=newTokens.get(nowTag);
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
    private String findTitle(int tag, String newToken)
    {
        String title = AnalysisTool.getMarketTitle(newToken);
        if (title == null)
        {
            if (AnalysisTool.getMarketSort(newTokens.get(tag + 1)) == 0)
            {
                title = newTokens.get(tag + 1);
            }
        }
        return title;
    }



    /**
     * 判断list中的url是否为需求内容
     * @param listMarket
     * @return
     */
    static boolean isContentMarket(List<BaseAnalysisURL> listMarket)
    {
        //如果为空则跳出
        if (listMarket.size() <= 0) return false;

        //初始化
        int articleNum = 0;
        int urlSum = listMarket.size();
        int titleNum = 0;
        String url;
        BaseAnalysisURL baseAnalysisURL;

        //统计文章链接个数
        for (int i = 0; i < listMarket.size(); i++)
        {
            baseAnalysisURL = listMarket.get(i);
            url = baseAnalysisURL.getUrl();
            if (JudgeURL.getSortByURL(url) == -1) articleNum++;
            titleNum += baseAnalysisURL.getTitle().length();
        }

        if ((articleNum + 0.0) / urlSum > 0.5) return true;
        if (titleNum / urlSum > 6) return true;

        return false;

    }




    /**
     * 根据xpath的差异度将内容分块
     */
    private void findBlock()
    {
        List<Integer> valueList = new ArrayList<Integer>();

        //获取相邻差异
        for (int i = 0; i < ansTag.size() - 1; i++)
        {
            valueList.add(AnalysisTool.getDifferentLevel(ansTag.get(i), ansTag.get(i + 1)));
        }

        //获取块开头
        int last = -1;
        int difference = 0;
        int i = 0;
        int value;
        startList.clear();
        startList.add(0);
        while (true)
        {
            if (i >= valueList.size()) break;
            if (i == 0) last = valueList.get(0);
            value = valueList.get(i);
            if (last < value) difference = value - last;
            else difference = last - value;
            //发现不同
            if ((difference >= 2))
            {
                i++;

                startList.add(i);
                last = valueList.get(i-1);
                continue;
            }
            last = valueList.get(i);
            i++;

        }
        startList.add(ansTag.size());
    }


    private String getMarketUrl(String str)
    {
        String TITLE_MACHER = ".*href=('|\")(\\S*)('|\").*";
        String url = null;
        String text = str.replaceAll("\\r|\\n", "");

        //匹配url
        Pattern compile = Pattern.compile(TITLE_MACHER, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher(text);
        if (matcher.find())
        {
            //拼接url
            url = AnalysisTool.joinUrl(fromURL, matcher.group(2));
            if (!AnalysisTool.getDomain(url).equals(domain)) url = null;
        }
        return url;
    }




    //获取最终导航页信息
    private void getAnsList()
    {

        List<BaseAnalysisURL> tempList = new ArrayList<BaseAnalysisURL>();
        int startTag, endTag;
        BaseAnalysisURL baseAnalysisURL;

        //循环获取块
        for (int i = 0; i < startList.size() - 1; i++)
        {
            //初始化
            tempList.clear();
            startTag = startList.get(i);
            endTag = startList.get(i + 1);

            //获取块
            for (int j = startTag; j < endTag; j++)
                tempList.add(ansTokens.get(j));

            //判断块是否为内容
            if (isContentMarket(tempList))
            {
                for (int j = 0; j < tempList.size(); j ++)
                {
                    baseAnalysisURL =tempList.get(j);
                    if (baseAnalysisURL.getUrl()==null) continue;

                    if (JudgeURL.getSortByURL(baseAnalysisURL.getUrl()) == 1)
                    {
                       continue;
                    }
                    newBaseURL .add(baseAnalysisURL);
                }
            }
        }


    }


}
