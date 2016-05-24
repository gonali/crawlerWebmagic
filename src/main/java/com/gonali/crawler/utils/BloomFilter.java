package com.gonali.crawler.utils;

import redis.clients.jedis.Jedis;

/**
 * 实现基于Redis的布隆过滤器
 */
public class BloomFilter {

    private int maxKey;
    private float errorRate;
    private int hashFunctionCount;
    private Jedis jedis;

    private int bitSize;

    private String defaultKey = "redis:bloomfilter";

    public BloomFilter(Jedis jedis, int timeout, float errorRate, int maxKey) {
        this.maxKey = maxKey;
        this.errorRate = errorRate;
        this.jedis = jedis;
        bitSize = calcOptimalM(maxKey, errorRate);
        hashFunctionCount = calcOptimalK(bitSize, maxKey);
    }


    /**
     * 当前key中添加标记
     *
     * @param bizId
     */
    public void add(String bizId) {
        add(defaultKey, bizId);
    }

    /**
     * 将URL标记到位数组
     *
     * @param key
     * @param bizId
     */
    public void add(String key, String bizId) {
        int[] offset = HashUtils.murmurHashOffset(bizId, hashFunctionCount, bitSize);
        for (int i : offset) {
            jedis.setbit(key, i, true);
        }
    }

    /**
     * 检测字符串是否存在于集合中
     *
     * @param key
     * @param bizId
     */
    public boolean contains(String key, String bizId) {
        int[] offset = HashUtils.murmurHashOffset(bizId, hashFunctionCount, bitSize);
        for (int i : offset) {
            if (!jedis.getbit(key, i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算M和K
     * M:位数组的大小
     * K:哈希函数的个数
     *
     * @param maxKey
     * @param errorRate
     * @return
     */
    public int calcOptimalM(int maxKey, float errorRate) {
        return (int) Math.ceil(maxKey
                * (Math.log(errorRate) / Math.log(0.6185)));
    }

    /**
     * 计算M和K
     * M:为数组的大小
     * K:哈希函数的个数
     *
     * @param bitSize
     * @param maxKey
     * @return
     */
    public int calcOptimalK(int bitSize, int maxKey) {
        return (int) Math.ceil(Math.log(2) * (bitSize / maxKey));
    }

    public int getMaxKey() {
        return maxKey;
    }

    public void setMaxKey(int maxKey) {
        this.maxKey = maxKey;
    }

    public float getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(float errorRate) {
        this.errorRate = errorRate;
    }

    public int getHashFunctionCount() {
        return hashFunctionCount;
    }

    public void setHashFunctionCount(int hashFunctionCount) {
        this.hashFunctionCount = hashFunctionCount;
    }

    public int getBitSize() {
        return bitSize;
    }

    public void setBitSize(int bitSize) {
        this.bitSize = bitSize;
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }
}
