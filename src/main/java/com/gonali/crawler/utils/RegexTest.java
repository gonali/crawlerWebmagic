package com.gonali.crawler.utils;

/**
 * Created by Administrator on 2016/4/28.
 */
public class RegexTest {
    public static void main(String[] args) {
        String s = "http://my.oschina.net/flashsword/blog/473107";
        boolean flag = s.matches("http://my\\.oschina\\.net/flashsword/blog/\\d+");
        System.out.println(flag);
    }
}
