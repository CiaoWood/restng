package com.microdev.automation.restng.util.basic;

import java.util.List;

/**
 * Created by wuchao on 17/7/25.
 */
public class ListUtil {

    private ListUtil() {
    }

    public static boolean isNotEmpty(List list) {
        return null != list && list.size() > 0;
    }

}
