package com.microdev.automation.restng.util.log;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wuchao on 17/8/21.
 */
public class LOGGER {

    private static final Logger log = LoggerFactory.getLogger(LOGGER.class);
    private static Gson gson = new Gson();

    private LOGGER() {
    }

    public static void info(Object obj) {
        String jsonObj = gson.toJson(obj);
        log.info(jsonObj);
    }

    public static void warn(Object obj) {
        String jsonObj = gson.toJson(obj);
        log.warn(jsonObj);
    }

    public static void error(Object obj) {
        String jsonObj = gson.toJson(obj);
        log.error(jsonObj);
    }

    public static void debug(Object obj) {
        String jsonObj = gson.toJson(obj);
        log.debug(jsonObj);
    }

    public static void info(String msg, Object obj) {
        log.info(msg, obj);
    }

    public static void warn(String msg, Object obj) {
        log.warn(msg, obj);
    }

    public static void error(String msg, Object obj) {
        log.error(msg, obj);
    }

    public static void debug(String msg, Object obj) {
        log.debug(msg, obj);
    }

}
