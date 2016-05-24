package com.gonali.crawler.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;


/**
 * Created by 黄海 on 15-12-30.
 */

public class JedisPoolUtils implements Serializable {
    /*    private static JedisPool pool;
        private static Jedis jedis;

        public JedisPoolUtils() throws FileNotFoundException,IOException{
            makepool();
        }

        public static void makepool() throws FileNotFoundException, IOException {

            String redisHost = ConfigUtils.getResourceBundle().getString("REDIS_HOSTNAME");
            int    redisPort = Integer.parseInt(ConfigUtils.getResourceBundle().getString("REDIS_PORT"));

            if (pool == null) {
                JedisPoolConfig conf = new JedisPoolConfig();
                conf.setMaxTotal(-1);
                conf.setMaxWaitMillis(60000L);
                pool = new JedisPool(conf, redisHost, redisPort,100000);
            }
        }

        public  static JedisPool getJedisPool() {
            return pool;
        }

        public static Jedis getJedis()    {
            if(jedis == null)    {
                jedis = getJedisPool().getResource();
            }
            return jedis;
        }*/
    private static JedisPool pool;
    private  Jedis jedis;


    private JedisPoolUtils() {

    }


    private static void makepool() throws FileNotFoundException, IOException {

        String redisHost = ConfigUtils.getResourceBundle().getString("REDIS_HOSTNAME");
        int redisPort = Integer.parseInt(ConfigUtils.getResourceBundle().getString("REDIS_PORT"));

        if (pool == null) {
            JedisPoolConfig conf = new JedisPoolConfig();
            conf.setMaxTotal(-1);
            conf.setMaxWaitMillis(60000L);
            pool = new JedisPool(conf, redisHost, redisPort, 100000);
        }
    }

    public static JedisPool getJedisPool() throws FileNotFoundException, IOException {
        if (pool == null)
            makepool();
        return pool;
    }

    public static Jedis getJedis() throws FileNotFoundException, IOException{
        JedisPoolUtils jedisPoolUtils = new JedisPoolUtils();
        if (jedisPoolUtils.jedis == null) {
            jedisPoolUtils.jedis = getJedisPool().getResource();
        }
        return jedisPoolUtils.jedis;
    }

    public static void cleanAll() {

        pool.close();
    }

    public static void cleanJedis(Jedis jedis) {
        jedis.close();
    }
}

