package com.microdev.automation.restng.util.basic;

import com.alibaba.fastjson.JSONArray;

public class JsonUtil {

    private JsonUtil() {
    }

    public static boolean isJsonArrayNotEmpty(JSONArray jsonArray) {
        return null != jsonArray && !jsonArray.isEmpty();
    }

    public static boolean isJsonArrayEmpty(JSONArray jsonArray) {
        return null == jsonArray || jsonArray.isEmpty();
    }
}
