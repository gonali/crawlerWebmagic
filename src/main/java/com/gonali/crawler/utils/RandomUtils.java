package com.gonali.crawler.utils;

import java.util.Random;

/**
 * Created by TianyuanPan on 5/19/16.
 */
public class RandomUtils {

    private RandomUtils(){}

    /**
     *
     * @param length 表示生成字符串的长度
     * @return
     */
    public static String getRandomString(int length) {

        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /**
     *
     * @param n 最大值
     * @return long
     */
    public static long getRandomNumber(long n) {

        Random random = new Random();

        long bits, val;
        do {
            bits = (random.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (bits-val+(n-1) < 0L);
        return val;
    }

/*
    public static void main(String[] args) {
        System.out.println(getRandomString(32));
        System.out.println(getRandomNumber(145605982149L));
    }
*/

}
