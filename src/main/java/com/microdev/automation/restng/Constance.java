package com.microdev.automation.restng;

import com.google.common.collect.ImmutableList;

/**
 * Created by wuchao on 17/7/18.
 */
public class Constance {

    public static final String TYPE = "type";
    public static final String METHOD = "method";
    public static final String URL = "url";
    public static final ImmutableList<String> REQ_BODY = ImmutableList.of("reqbody", "reqBody", "body");
    public static final String EXPECTED = "expected";
    public static final String TAG = "tag";
    public static final String LOG_TITLE_AOP = "AOP注入";
    public static final String ES_URL = "http://url of es/";
    public static final String ES_AUTH = "auth of es";

    private Constance() {
    }

}
