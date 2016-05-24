package com.gonali.crawler.parser.urljudge;



import com.gonali.crawler.parser.tool.GetHtml;

import java.io.IOException;

/**
 * Created by Administrator on 2015/9/17.
 */
public class HtmlSort {

    /**
     * 返回网页类型
     * @param url
     * @param html
     * @return
     */
   public static int getHtmlSort(String url ,String html) {

        int sort;

        sort = JudgeURL.getSortByURL(url);
        if (sort==0)
        {
            try {
                sort = JudgeDensity.getSortByDensity(html);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return (sort);
    }

    public static void main(String[] args) throws IOException {
        String url="http://www.qxn.gov.cn/OrgArtList/QxnGov.XMJ/QxnGov.XMJ.Info/1.html";
        System.out.println(getHtmlSort(url, GetHtml.getHtmlFromUrl(url)));
    }
}
