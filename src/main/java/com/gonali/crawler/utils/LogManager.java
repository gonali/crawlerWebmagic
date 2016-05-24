package com.gonali.crawler.utils;

/**
 * Created by hadoop on 2015/11/9.
 */

import com.gonali.crawler.entry.ConfigLoader;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LogManager implements Serializable {
    private Logger logger;
    private String className;

    public LogManager(Class callingClass) {

        this.className = callingClass.getName();
        //  this.logger = Logger.getLogger(className);
    }

    public void logError(String msg) {
        if (shouldThisClassLog(className)) {
            System.out.println(msg);
            //logger.error(msg);
        }
    }

    public void logInfo(String msg) {
        if (shouldThisClassLog(className)) {
            System.out.println(msg);
            //logger.info(msg);
        }
    }

    public void logDebug(String msg) {
        if (shouldThisClassLog(className)) {
            System.out.println(msg);
            //logger.debug(msg);
        }
    }

    private static Set<String> loggingClasses = new HashSet<String>(Arrays.asList(
            ConfigLoader.class.getName()));

    private static boolean shouldThisClassLog(String className) {
        if (loggingClasses.contains(className)) {
            return true;
        } else {
            return false;
        }
    }
}
