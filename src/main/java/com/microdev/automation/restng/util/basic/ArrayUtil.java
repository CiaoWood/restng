package com.microdev.automation.restng.util.basic;

import org.apache.commons.lang3.ArrayUtils;
import org.jooq.Condition;

import java.util.HashMap;
import java.util.Map;

import static org.jooq.impl.DSL.field;

/**
 * Created by wuchao on 17/7/25.
 */
public class ArrayUtil {

    private ArrayUtil() {
    }

    /**
     * 将不定参数数组转化为hashmap
     *
     * @param array
     * @return
     */
    public static Map<String, Object> array2HashMap(String[] array) {
        HashMap<String, Object> hashMap = new HashMap();
        for (int i = 0, len = array.length; i + 1 < len; i += 2) {
            hashMap.put(array[i], array[i + 1]);
        }
        return hashMap;
    }

    public static Condition array2Condition(String[] array) {
        if (ArrayUtils.isEmpty(array) || array.length < 1) return null;
        Condition condition = field(array[0]).eq(array[1]);
        for (int i = 2, len = array.length; i < len; i += 2) {
            condition = condition.and(field(array[i]).eq(array[i + 1]));
        }
        return condition;
    }

}
