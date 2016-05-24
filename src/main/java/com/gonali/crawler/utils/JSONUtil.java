package com.gonali.crawler.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * JSON工具类，是通过封装jackson得到，为的是更简单的使用jackson
 * @author liguoyou
 * @ClassName JSONUtil
 * @date 2014-7-21 上午11:12:03
 *
 */
public class JSONUtil {

    /**
     * 对象转化为json数据
     * @author liguoyou
     * @date 2014-7-21 上午11:13:29
     *
     * @param object
     * @return
     *
     */
    public static String object2JacksonString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        }  catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("对象转化为json数据出错，可能是对象类型不匹配");
        }
    }

    /**
     * json数据转化为对象
     * @author liguoyou
     * @date 2014-7-21 上午11:13:54
     *
     * @param jsonString
     * @param destClass
     * @return
     *
     */
    public static Object jackson2Object(String jsonString, Class<?> destClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, destClass);
        }  catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("json数据转化为对象时出错，可能是对象类型不匹配");
        }
    }

    /**
     * 将list的json串转换为list对象
     * @Description jackson2List 是 JSONUtil 的方法
     * @Author liguoyou
     * @Version V1.0, 2015-3-13 下午2:52:43
     * @Modified 初次创建jackson2List方法
     * @param jsonString
     * @param collectionClass
     * @param elementClasses
     * @return
     */
    public static List jackson2List(String jsonString, Class<?> collectionClass, Class<?> elementClasses) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);

        List list = null;
        try
        {
            list = objectMapper.readValue(jsonString, javaType);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("json数据转化为对象时出错，可能是对象类型不匹配");
        }
        return list;
    }

}
