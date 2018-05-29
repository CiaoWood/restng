package com.microdev.automation.restng.util.http;

/**
 * Created by wuchao on 17/6/29.
 */
public class HttpUtil {

    private HttpUtil() {
    }

    public static String addHttpProtocol(String url) {
        if (url.startsWith("http")) {
            return url;
        } else {
            return "http://" + url;
        }
    }

}
