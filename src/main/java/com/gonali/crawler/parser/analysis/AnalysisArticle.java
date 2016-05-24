package com.gonali.crawler.parser.analysis;



import com.gonali.crawler.parser.tool.AnalysisTool;
import com.gonali.crawler.parser.tool.JsoupHtml;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/10/16.
 */
public class AnalysisArticle implements Serializable{
    List<String> templateTokens;
    List<String> newTokens;
    List<String> ansTokens;
    List<String> ansTag;
    int textFirst = 0;

    public AnalysisArticle() {
        templateTokens = new ArrayList<String>();
        newTokens = new ArrayList<String>();
        ansTokens = new ArrayList<String>();
        ansTag = new ArrayList<String>();
    }


    static final String DATE_MATCHER = "(\\d{4}(\\-|年|\\\\|/|\\.)\\d{1,2}(\\-|月|\\\\|/|\\.)\\d{1,2}(日)?)";


    static String domain;


    static List<BaseTemplate> templateList;


    public BaseAnalysisURL analysisArticle(BaseAnalysisURL baseAnalysisURL, List<BaseTemplate> baseTemplates) throws IOException {
        BaseAnalysisURL url = baseAnalysisURL;
        long date;
        String text;
        String title;
        System.out.println("\n\r" + "analysis:  " + url.getUrl());
        domain = AnalysisTool.getDomain(url.getUrl());

        newTokens = JsoupHtml.getHtmlTokens(url.getHtml());

        if (AnalysisByTemplate(baseTemplates)) {
            text = getText();
            url.setText(text);
            date = getDate();
            if (date != 0) url.setDate(date);
            if (url.getTitle() == null || url.getTitle()=="") {
                title = getTitle();
                url.setTitle(title);
            }

        }
        return url;
    }


    boolean AnalysisByTemplate(List<BaseTemplate> baseTemplates) throws IOException {
        templateList = Templates.getTemplates(domain, baseTemplates);

        for (BaseTemplate baseTemplate : templateList) {
            templateTokens = baseTemplate.getTokens();
            //用模版做匹配
            if (findTemplates()) return true;
        }
        return false;
    }


    /**
     * 获取列表中标签的数量
     *
     * @param list
     * @return
     */
    static int getMarketNum(List<String> list) {
        int num = 0;
        for (int i = 0; i < list.size(); i++) {
            if (AnalysisTool.getMarketSort(list.get(i)) != 0) num++;
        }
        return num;

    }


    /**
     * 匹配模版
     *
     * @return
     */
    Boolean findTemplates() {
        List<String> xPath = new ArrayList<String>();
        int oldTag = 0;
        int equalNum = 0;
        int newTag = 0;
        int oldLenth = templateTokens.size();
        int newLength = newTokens.size();
        String newToken, oldToken, temp;

        ansTokens.clear();
        ansTag.clear();

        while (true) {
            if ((oldTag >= oldLenth) || templateTokens.size() <= oldTag || (newTag >= newLength) || newTokens.size() <= newTag)
                break;
            oldToken = templateTokens.get(oldTag);
            newToken = newTokens.get(newTag);


            //判断是否正确
            if (AnalysisTool.isEquals(oldToken, newToken)) {

                //更新xpath队列
                //如果遇到的是结束标签，队列减1，如果是开始标签入队
                if ((AnalysisTool.getMarketSort(oldToken) == -1) && (xPath.size() > 0)) xPath.remove(xPath.size() - 1);
                if (AnalysisTool.getMarketSort(oldToken) == 1) xPath.add(oldToken);
                if (AnalysisTool.getMarketSort(oldToken) != 0) equalNum++;
                oldTag++;
                // equalNum++;
                newTag++;
            }
            //不同
            else {
                if (AnalysisTool.getMarketSort(newToken) == 0) {
                    temp = "";
                    for (int i = 0; i < xPath.size(); i++)
                        temp += xPath.get(i) + "###split###";
                    ansTokens.add(newToken);
                    ansTag.add(temp);
                    if (AnalysisTool.getMarketSort(oldToken) == 0) {
                        //更新xpath队列
                        //如果遇到的是结束标签，队列减1，如果是开始标签入队
                        if (AnalysisTool.getMarketSort(oldToken) == -1) xPath.remove(xPath.size() - 1);
                        if (AnalysisTool.getMarketSort(oldToken) == 1) xPath.add(oldToken);
                        oldTag++;
                    }
                }
                newTag++;
            }
        }

        // System.out.println((equalNum + 0.0) / templateTokens.size());
        //判断模板匹配相似度
//        if ((equalNum + 0.0) / templateTokens.size() > 0.65) return true;
//        else return false;
        if ((equalNum + 0.0) / getMarketNum(templateTokens) > 0.65) return true;
        else return false;

    }


    //返回xpath的差异等级,值越低相似度越大
    int getDifferentLevel(String a, String b) {
        if ((a == null) || (b == null)) return 0;

        String[] aList = a.split("###split###");
        String[] bList = b.split("###split###");

        int aLength = aList.length;
        int bLength = bList.length;
        int min = aLength;
        if (min > bLength) min = bLength;

        int num = 0;
        for (int i = 0; i < min; i++) {
            if (!aList[i].equals(bList[i])) break;
            num++;
        }
        return num;
    }


    String getTitle() {
        String title = "";
        int max = 0;
        int key;
        String str;
        for (int i = 0; i < textFirst; i++) {
            str = ansTokens.get(i);
            key = str.length();
            if (str.contains("来源") || str.contains("作者")) key -= 30;
            for (int j = 0; j < str.length(); j++) {
                if (str.charAt(j) >= '0' && str.charAt(j) <= '9') key--;
            }
            if (str.contains("<<") || str.contains("《")) key += 5;
            if (key > max) {
                max = key;
                title = str;
            }
        }
        return title;
    }


     long getDate() {
        String dateStr = null;
        String copydateStr=null;
        String str;
        for (int i = 0; i < ansTokens.size(); i++) {
            str = ansTokens.get(i);

            if (str.contains("发布时间") || str.contains("发布日期") || str.contains("日期") || str.contains("时间")) {
                Pattern compile = Pattern.compile(".*" + DATE_MATCHER + ".*", Pattern.CASE_INSENSITIVE);
                Matcher matcher = compile.matcher(str);
                if (matcher.find()) {
                    dateStr = matcher.group(1);
                    break;
                }
            }
            if (copydateStr==null) {
                if (Pattern.matches(DATE_MATCHER + ".*", str)) {
                    Pattern compile = Pattern.compile(DATE_MATCHER + ".*", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = compile.matcher(str);
                    if (matcher.find()) {
                        copydateStr = matcher.group(1);
                    }
                }
            }
        }
        if (dateStr==null) dateStr=copydateStr;

        return AnalysisTool.getDateFormString(dateStr);
    }



    /**
     * 判断字符串是否为正文
     *
     * @param text
     * @return
     */
    static Boolean isText(String text) {
        if (text == null) return false;
        if (text.contains(",")) return true;
        if (text.contains("。")) return true;
        if (text.contains("，")) return true;
        if (text.contains(";")) return true;
        if (text.contains("；")) return true;
        //     if (text.contains("、")) return true;


        return false;

    }


    String getText() {

        List<Integer> valueList = new ArrayList<Integer>();
        List<Integer> startList = new ArrayList<Integer>();
        List<String> ansList = new ArrayList<String>();

        //获取相邻差异

        String str = "";
        for (int i = 0; i < ansTag.size() - 1; i++) {
            valueList.add(getDifferentLevel(ansTag.get(i), ansTag.get(i + 1)));
        }


        //获取块开头
        int last = -1;
        for (int i = 0; i < valueList.size(); i++) {
            int value = valueList.get(i);
            if (last != value) {
                startList.add(i);
                last = value;
            }
        }
        startList.add(ansTokens.size());

        //获取text
        String text;
        String ansText = "";
        textFirst = 0;

        //循环块
        for (int i = 0; i < startList.size() - 1; i++) {
            text = "";
            //获取块内容
            for (int j = startList.get(i); j < startList.get(i + 1); j++) {
                if (j < ansTokens.size())
                    text += ansTokens.get(j);
                else break;
            }

            //如果为正文结束则跳出
            //     if (isEndText(text)) break;

            //如果已有正文开始则直接获取该块正文
            if (textFirst != 0) {
                ansText += text;
            }

            //判断是否为正文开始
            else {
                //如果包含标点，则说明为
                if (isText(text)) {
                    textFirst = i;
                    ansText += text;
                }
            }
        }

        return ansText;
    }


}
