package com.gonali.crawler.entry;

import com.gonali.crawler.model.CrawlData;

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
    public List<CrawlData> load(int depth,String tid,String startTime,int pass,String seedPath,String type) {
        List<String> seedingUrls = loadSeedConfig(seedPath);

        List <CrawlData> crawlDataList = new ArrayList<CrawlData>();
        for(String seed : seedingUrls)  {
            CrawlData crawlData = new CrawlData();
            crawlData.setTid(tid);
            crawlData.setUrl(seed);
            crawlData.setStartTime(startTime);
            crawlData.setPass(pass);
            crawlData.setType(type);
            crawlData.setRootUrl(seed);
            crawlData.setFromUrl(seed);
            crawlData.setDepthfromSeed(0);

            crawlDataList.add(crawlData);
        }
        return crawlDataList;
    }

}
