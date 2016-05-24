package com.gonali.crawler.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/11/26.
 */
public class TokenUtil
{

    final static String DATE_MATCHER = "\\S?(\\d{4}(\\-|年|\\\\|/|\\.)\\d{1,2}(\\-|月|\\\\|/|\\.)\\d{1,2}(日)?)\\S?";
    final static String LINK_MACHER = ".*href=('|\")(\\S*)('|\").*";
    final static String IMG_MACHER = ".*src=('|\")(\\S*)('|\").*";
    final static String TITLE_MACHER = ".*title=('|\")([^'^\"]*)('|\").*";


    /**
     * 判断字符串是否满足时间格式
     *
     * @param str
     * @return
     */
    public static Boolean isDate(String str)
    {
        return Pattern.matches(DATE_MATCHER, str);
    }


    /**
     * 提取字符串中的时间并将其转化为时间戳
     *
     * @param str
     * @return
     */
    public static long getDateFormString(String str)
    {
        String dateString = getRegexContent(DATE_MATCHER, 1, str);
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



    /**
     * input：token
     * return：Int
     * <img 标签为3
     * <a>标签为2
     * <>开始标签为1
     * 文字内容为0
     * </>结束标签为-1
     */
    public static int getMarketSort(String token)
    {
        if (token.startsWith("<img")) return 3;
        if (token.startsWith("<a")) return 2;
        if (token.startsWith("</")) return -1;
        if (token.startsWith("<")) return 1;
        return 0;
    }


    /**
     * 根据正则表达式从String中提取相应内容
     *
     * @param regex      正则表达式
     * @param groupIndex 所要提取的内容的index
     * @param str        数据源
     * @return
     */
    public static String getRegexContent(String regex, int groupIndex, String str)
    {
        String text = str.replaceAll("\\r|\\n", "");
        //匹配url
        Pattern compile = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher(text);
        if (matcher.find()) return matcher.group(groupIndex);
        else return null;

    }


    public static String getTokenTitle(String str)
    {
        return getRegexContent(TITLE_MACHER, 2, str);
    }


    public static String getLinkURL(String str)
    {
        return getRegexContent(LINK_MACHER, 2, str);
    }


    public static String getImgURL(String str)
    {
        return getRegexContent(IMG_MACHER, 2, str);
    }


    /**
     * 拼接url
     *
     * @param mainUrl
     * @param newUrk
     * @return
     */
    public static String joinURL(String mainUrl, String newUrk)
    {

        URL url;
        String q = "";
        try {
            url = new URL(new URL(mainUrl), newUrk);
            q = url.toExternalForm();
        } catch (MalformedURLException e) {

        }
        if (q.indexOf("#") != -1) q = q.replaceAll("^(.+?)#.*?$", "$1");
        if (q.contains("../")) q=q.replaceAll("../","");
        return q;
    }

}