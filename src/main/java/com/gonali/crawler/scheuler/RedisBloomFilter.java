package com.gonali.crawler.scheuler;

import com.gonali.crawler.utils.BloomFilter;
import redis.clients.jedis.Jedis;

/**
 * Created by Administrator on 2016/5/19.
 */
public class RedisBloomFilter {

    public static boolean notExistInBloomHash(String url,Jedis jedis,BloomFilter bloomFilter) {
        //如果key取得的value是空，或者url不包含在哈希地址中，可以插入
        if(!bloomFilter.contains("redis:bloomfilter",url))    {
            bloomFilter.add("redis:bloomfilter",url);
            return true;
        }else {
            return false;
        }
    }
}
