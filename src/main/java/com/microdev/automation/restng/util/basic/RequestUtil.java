package com.microdev.automation.restng.util.basic;

import org.apache.commons.lang3.StringUtils;

public class RequestUtil {
    private RequestUtil() {
    }

    public static boolean validHost(String host) {
        return StringUtils.isNotBlank(host) && host.matches("[0-9]+(?:\\.[0-9]+){3}:[0-9]+");
    }

}
