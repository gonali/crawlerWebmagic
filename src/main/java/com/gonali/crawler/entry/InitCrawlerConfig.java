package com.gonali.crawler.entry;


import com.gonali.crawler.parser.analysis.BaseTemplate;
import com.gonali.crawler.utils.BloomFilter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class InitCrawlerConfig {

    private static  List<String> postRegex;
    private static List<BaseTemplate> listTemplate;
    private static List<String> regexList;
    private static List<String> protocols;
    private static int recallDepth;
    private static BloomFilter sparkBloomFilter;

    public List<String> getPostRegex() {
        return postRegex;
    }

    public void setPostRegex(List<String> postRegex) {
        this.postRegex = postRegex;
    }

    public List<BaseTemplate> getListTemplate() {
        return listTemplate;
    }

    public void setListTemplate(List<BaseTemplate> listTemplate) {
        this.listTemplate = listTemplate;
    }

    public List<String> getRegexList() {
        return regexList;
    }

    public void setRegexList(List<String> regexList) {
        this.regexList = regexList;
    }

    public List<String> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<String> protocols) {
        this.protocols = protocols;
    }

    public int getRecalldepth() {
        return recallDepth;
    }

    public void setRecalldepth(int recalldepth) {
        this.recallDepth = recalldepth;
    }

    public static BloomFilter getSparkBloomFilter() {
        return sparkBloomFilter;
    }

    public static void setSparkBloomFilter(BloomFilter sparkBloomFilter) {
        InitCrawlerConfig.sparkBloomFilter = sparkBloomFilter;
    }


    public InitCrawlerConfig(String appname, int recallDepth, String templatesDir, String clickregexDir, String protocolDir, String postregexDir)   {

        //读取模板
        listTemplate = new ArrayList<>();
        String str;
//        File files = new File("C:\\temp\\templates");
        File files = new File("/home/TianyuanPan/IdeaProjects/webmagicDemo/templates/");
//        File files = new File(templatesDir);
        File[] templateFiles = files.listFiles();
        List<File> fileList = new ArrayList<>();
        for(File templateFile : templateFiles)    {
            List<String> tokens = new ArrayList<>();
            String domain = new String();
            try {
                InputStream in = new FileInputStream(templateFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                domain = reader.readLine();
                while ((str = reader.readLine()) != null) tokens.add(str);
                reader.close();
                listTemplate.add(new BaseTemplate(domain,tokens));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setListTemplate(listTemplate);

        /*//读取点击标签
        List<String> regexs = new ArrayList<String>();
        File regexs_file = new File("C:\\temp\\regexs");
        String regexStr;
        try {
            InputStream in = new FileInputStream(regexs_file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while((regexStr = reader.readLine()) != null)   {
                regexs.add(regexStr);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setRegexList(regexs);

        //读取过滤协议
        List<String> protocols = new ArrayList<String>();
        File protocols_file = new File("C:\\temp\\protocols");
        String protocol;
        try {
            InputStream in = new FileInputStream(regexs_file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while((protocol = reader.readLine()) != null)   {
                regexs.add(protocol);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setProtocols(protocols);

        //读取过滤后缀
        List<String> postRegexs = new ArrayList<String>();
        File postRegexs_file = new File("C:\\temp\\postRegexs");
        String postRegex;
        try {
            InputStream in = new FileInputStream(regexs_file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while((postRegex = reader.readLine()) != null)   {
                regexs.add(postRegex);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setPostRegex(postRegexs)*/;
    }
}
