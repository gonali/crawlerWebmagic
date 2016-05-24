package com.gonali.crawler.parser.tool;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/11/26.
 */
public class AnalysisTool
{
    public static boolean isEquals(String oldToken, String newToken)
    {
        String subOld, subNew;
        if (oldToken.equals(newToken)) return true;
        if (getMarketSort(oldToken) != getMarketSort(newToken)) return false;
        subOld = getMarket(oldToken);
        subNew = getMarket(newToken);
        if (subOld.equals(subNew)) return true;
        return false;
    }


    /**
     * 拼接url
     * @param mainUrl
     * @param newUrk
     * @return
     */
    public static String joinUrl(String mainUrl, String newUrk)
    {
        URL url;
        String q = "";
        try
        {
            url = new URL(new URL(mainUrl), newUrk);
            q = url.toExternalForm();
        } catch (MalformedURLException e)
        {

        }
        if (q.indexOf("#") != -1) q = q.replaceAll("^(.+?)#.*?$", "$1");
        return q;
    }




    /**
     * 从标签中提取title
     * @param str 标签内容
     * @return title内容
     */
    public static String getMarketTitle(String str)
    {
        String TITLE_MACHER = ".*title=('|\")([^'^\"]*)('|\").*";
        String title = "";
        String text = str.replaceAll("\\r|\\n", "");
        Pattern compile = Pattern.compile(TITLE_MACHER, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher(text);
        if (matcher.find())
        {
            title = matcher.group(2).replaceAll("&.*;|　| |\\r|\\t|\\n|\\?", "");
        }
        if (title.length() > 0) return title;
        else return null;
    }


    /**
     * 比较两个xptah的相似级数
     * @param a
     * @param b
     * @return
     */
    public   static int getDifferentLevel(String a, String b)
    {
        String[] aList = a.split("###split###");
        String[] bList = b.split("###split###");
        int aLength = aList.length;
        int bLength = bList.length;
        int min = aLength;
        if (min > bLength) min = bLength;

        int num = 0;
        for (int i = 0; i < min; i++)
        {
            if (!aList[i].equals(bList[i])) break;
            num++;
        }
        return num;
    }



    /**
     *获取当前标签的Xpath
     * @param list 当前位置的list
     * @return 将list拼接为xpath，用###split###分割
     */
    public static String getXpath(List<String> list)
    {
        String xpath = "";
        for (int i = 0; i < list.size(); i++) xpath += list.get(i) + "###split###";
        return xpath;
    }



    /**
     * 判断字符串是否满足时间格式
     * @param str
     * @return
     */
    public static Boolean isDate(String str)
    {
        if (Pattern.matches(DATE_MATCHER, str))
        {
       //     System.out.println(str);
            return true;
        }
        return false;
    }



    /**
     * 提取字符串中的时间并将其转化为时间戳
     * @param str
     * @return
     */
    final static String DATE_MATCHER = "\\S?(\\d{4}(\\-|年|\\\\|/|\\.)\\d{1,2}(\\-|月|\\\\|/|\\.)\\d{1,2}(日)?)\\S?";
    public static   long getDateFormString(String str)
    {
        if (str==null) return 0;
        String dateString=null;
        Pattern compile = Pattern.compile(DATE_MATCHER, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher(str);
        if (matcher.find()) {

            dateString = matcher.group(1);

            if (dateString==null) return 0;
            //格式标准化
            dateString = dateString.replaceAll("\\\\|/|年|月|\\.", "-");
            dateString = dateString.replaceAll("日", "");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = simpleDateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long timeStemp = date.getTime();
            return timeStemp;
        }
        return 0;
    }



    public static String getMarket(String token)
    {
        String sub;
        if (token.contains(" ")) sub = token.split(" ")[0];
        else sub = token.split(">")[0];
        int i = getMarketSort(token);
        switch (i)
        {
            case 0:
                return token;
            case -1:
                sub = sub.substring(2);
                break;
            case 1:
                sub = sub.substring(1);
                break;
        }
        return sub;
    }


    /**
     * 获取域名
     * @param curl
     * @return
     */
    public static String getDomain(String curl)
    {
        URL url;
        String q = "";
        try
        {
            url = new URL(curl);
            q = url.getHost();
        } catch (MalformedURLException e)
        {
        }
        return q;
    }



    /**
     * input：token
     * return：Int
     * <a>标签为2
     * </>结束标签为-1
     * <>开始标签为1
     * 文字内容为1
     *
     * */
    public static int getMarketSort(String token)
    {
        if (token.startsWith("<a")) return 2;
        if (token.startsWith("</")) return -1;
        if (token.startsWith("<")) return 1;
        return 0;
    }


    public static String getSameString(String a, String b)
    {
        String ans = "";
        final int MAX_LCS_NUM = 200;
        int[][] LCS = new int[MAX_LCS_NUM][MAX_LCS_NUM];
        String[][] flag = new String[MAX_LCS_NUM][MAX_LCS_NUM];
        int aLength = a.length();
        int bLength = b.length();
        int i, j;


        //最大子序列初始化
        LCS[0][0] = 0;
        for (i = 0; i < MAX_LCS_NUM; i++)
            for (j = 0; j < MAX_LCS_NUM; j++)
            {
                LCS[i][j] = 0;
                flag[i][j] = "";
            }

        //动态规划求最大子序列
        for (i = 1; i <= aLength; i++)
        {
            for (j = 1; j <= bLength; j++)
            {
                if (a.charAt(i - 1) == b.charAt(j - 1))
                {
                    LCS[i][j] = LCS[i - 1][j - 1] + 1;
                    flag[i][j] = "left_up";
                } else
                {
                    if (LCS[i - 1][j] >= LCS[i][j - 1])
                    {
                        LCS[i][j] = LCS[i - 1][j];
                        flag[i][j] = "left";
                    } else
                    {
                        LCS[i][j] = LCS[i][j - 1];
                        flag[i][j] = "up";
                    }
                }
            }
        }


        return ans;

    }

}
