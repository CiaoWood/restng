package com.microdev.automation.restng.util.assertion;

import com.alibaba.fastjson.JSON;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

/**
 * Created by wuchao on 17/6/28.
 */
public class AssertUtil {

    private AssertUtil() {
    }

    public static void assertEqual(String actual, String expected) {
        assertThat(actual, sameJSONAs(expected).allowingExtraUnexpectedFields().allowingAnyArrayOrdering());
    }

    public static void assertEqual(Map actual, Map expected) {
        assertEqual(JSON.toJSONString(actual), JSON.toJSONString(expected));
    }
}
