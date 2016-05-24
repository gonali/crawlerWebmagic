package com.gonali.crawler.scheuler;

import com.gonali.crawler.utils.JedisPoolUtils;
import redis.clients.jedis.Jedis;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.Scheduler;

import java.io.IOException;

public class RedisScheduler implements Scheduler {
    private static final String QUEUE_PREFIX = "queue_";
    private static final String SET_PREFIX = "set_";


    @Override
    public void push(Request request, Task task) {

        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getJedis();//new JedisPoolUtils().getJedisPool().getResource();
            jedis.rpush(QUEUE_PREFIX + task.getUUID(), request.getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            JedisPoolUtils.cleanJedis(jedis);
        }

//        request.setMethod("GET");

    }


    @Override
    public Request poll(Task task) {
        Jedis jedis = null;
        String url = null;
        try {
            jedis = JedisPoolUtils.getJedis();//new JedisPoolUtils().getJedisPool().getResource();
            url = jedis.lpop(QUEUE_PREFIX+task.getUUID());

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            JedisPoolUtils.cleanJedis(jedis);
        }
        if (url==null) {
            return null;
        } else {
            return new Request(url);
        }

    }
}