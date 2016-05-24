package com.gonali.crawler.entry;

import com.gonali.crawler.model.CrawlerData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/3.
 */
public class ConfigLoader {

    public List<String> loadSeedConfig(String inputFilePath)    {
        String projPath = System.getProperty("user.dir");
        List<String> seedsList = new ArrayList<>();
        try {
            // read file content from file
            StringBuffer sb= new StringBuffer("");

            FileReader reader = new FileReader(projPath+"//data//seeds.txt");
            BufferedReader br = new BufferedReader(reader);

            String str = null;

            while((str = br.readLine()) != null) {
                seedsList.add(str);
            }
            br.close();
            reader.close();
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return seedsList;
    }

    //对种子对应的CrawlData进行赋值
    public List<CrawlerData> load(int depth,String tid,String startTime,int pass,String seedPath,String type) {
        List<String> seedingUrls = loadSeedConfig(seedPath);

        List <CrawlerData> crawlerDataList = new ArrayList<CrawlerData>();
        for(String seed : seedingUrls)  {
            CrawlerData crawlerData = new CrawlerData();
            crawlerData.setTid(tid);
            crawlerData.setUrl(seed);
            crawlerData.setStartTime(startTime);
            crawlerData.setPass(pass);
            crawlerData.setType(type);
            crawlerData.setRootUrl(seed);
            crawlerData.setFromUrl(seed);
            crawlerData.setDepthfromSeed(0);

            crawlerDataList.add(crawlerData);
        }
        return crawlerDataList;
    }

}
