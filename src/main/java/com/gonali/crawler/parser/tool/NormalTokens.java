package com.gonali.crawler.parser.tool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/15.
 */
public class NormalTokens
{

    public static List<String> getHtmlTokens(String html) throws IOException
    {
        return TokensHtml(sortJsoup(html));
    }

    /***
     * 获取标签的类型
     * 输入标签
     * 分为3类：
     * -1：需删除该标签下一个内容块：<script>,<style>等
     * 0：仅删除该标签 如:<img><p><strong>等
     * 1：获取该标签
     * 2:为<img />等单独标签需转换乘<img></img>
     */
    static int getMarketSort(String str)
    {
        String delMarket = "(<(\\?|o:p|!|meta|br|hr|span|link|input|p|/(o:p|strong|p|span|a)).*)";
        if (str.matches(delMarket)) return 0;
        if (str.endsWith("/>")) return 2;
        return 1;
    }

    static String getMarket(String token)
    {
        String sub;
        if (token.contains(" ")) sub = token.split(" ")[0];
        else sub = token.split(">")[0];
        sub = sub.substring(1);
        return sub;
    }


    static String cleanMarket(String str)
    {
        String ans = str.replaceAll("\\r|\\n", " ");
        int i;
        for (i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) != ' ') break;
        }
        str = str.substring(i);
        return ans;
    }


    static String cleanContent(String str)
    {
        String ans;
        ans = str.replaceAll("&.*;|　| |\\r|\\t|\\n|\\?", "");
        if (ans.equals("") || ans.contains(">") || ans.contains("function")) return null;
        else return ans;
    }

    public static List<String> TokensHtml(String html) throws IOException
    {
        List<String> HtmlTokens = new ArrayList<String>();
        int length = html.length();
        int i = 0;
        int tag = 0;//0为标记,1为需要的内容,-1为不需要的内容，
        int sort;
        String temp;
        String sub;
        String nowStr;
        String marketName;
        temp = "";
        char newChar;
        while (i < length)
        {
            newChar = html.charAt(i);
            //判断获取状态
            switch (tag)
            {
                //正在获取标签
                case 0:
                {
                    temp += newChar;
                    //遇到标签的结束符
                    if (newChar == '>')
                    {
                        temp = cleanMarket(temp);
                        sort = getMarketSort(temp);
                        switch (sort)
                        {
                            case 0:
                                tag = 1;
                                break;
                            case 1:
                            {
                                HtmlTokens.add(temp);
                                tag = 1;
                            }
                            break;
                            case 2:
                            {
                                HtmlTokens.add(temp.replaceAll("/>", ">"));
                                HtmlTokens.add("</" + getMarket(temp) + ">");
                                tag = 1;
                            }

                        }
                        temp = "";
                    }
                }
                break;
                case 1:
                {
                    if (newChar == '<')
                    {
                        temp = cleanContent(temp);
                        if (temp != null) HtmlTokens.add(temp);
                        temp = "";
                        tag = 0;
                    }
                    temp += newChar;
                }
                break;
            }
            i++;
        }

          return HtmlTokens;


    }




    public static String sortJsoup(String html) throws IOException
    {
        Document parse = Jsoup.parse(html);
        parse.select("script").remove();
        parse.select("style").remove();
        html = parse.toString();
        return html;
    }


}
