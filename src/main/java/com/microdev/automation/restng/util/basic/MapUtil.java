package com.microdev.automation.restng.util.basic;

import com.google.common.collect.ImmutableList;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.Field;

import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.field;

/**
 * Created by wuchao on 17/7/25.
 */
public class MapUtil {

    private MapUtil() {
    }

    public static String[] map2Array(Map<String, String> map) {
        if (isEmpty(map)) return ArrayUtils.EMPTY_STRING_ARRAY;
        List<String> list = ListUtils.EMPTY_LIST;
        map.forEach((key, value) -> {
            list.add(key);
            list.add(value);
        });
        return list.toArray(new String[0]);
    }

    public static Condition map2Condition(Map<String, Object> map) {
        if (isEmpty(map) || map.size() < 1) return null;
        Condition condition = null;
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (first) {
                condition = field(entry.getKey()).eq(entry.getValue());
                first = false;
            } else {
                condition = condition.and(field(entry.getKey()).eq(entry.getValue()));
            }
        }
        return condition;
    }

    public static Map<Field, Object> map2Field(Map<String, Object> map) {
        if (isEmpty(map) || map.size() < 1) return null;
        Map<Field, Object> map2 = new HashedMap();
        map.forEach((k, v) -> {
            map2.put(field(k), v);
        });
        return map2;
    }

    public static String getFromMap(Map<String, String> data, ImmutableList<String> keys) {
        String value;
        if (keys != null && !keys.isEmpty()) {
            for (String key : keys) {
                value = data.get(key);
                if (StringUtils.isNotBlank(value)) {
                    return value;
                }
            }
        }
        return null;
    }

    public static boolean isEmpty(Map map) {
        return null == map || map.isEmpty();
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

}
