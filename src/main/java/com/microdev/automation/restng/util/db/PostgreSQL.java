package com.microdev.automation.restng.util.db;

import com.microdev.automation.restng.util.basic.MapUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;

import java.util.List;
import java.util.Map;

import static com.microdev.automation.restng.util.basic.ArrayUtil.array2Condition;
import static com.microdev.automation.restng.util.basic.MapUtil.map2Condition;
import static com.microdev.automation.restng.util.basic.MapUtil.map2Field;
import static org.jooq.impl.DSL.table;

/**
 * Created by wuchao on 17/8/18.
 */
public class PostgreSQL {

    public static final String PROP = "pg";
    private static String propActivated = PROP;

    private PostgreSQL() {
    }

    public static PostgreSQL reset(String prop) {
        propActivated = prop;
        return null;
    }

    public static PostgreSQL reset() {
        propActivated = PROP;
        return null;
    }

    /**
     * 执行sql语句
     *
     * @param sql
     * @return
     */
    public static Boolean execute(String sql) {
        try (DSLContext dsl = Pg.pg(propActivated)) {
            Result<Record> results = dsl.fetch(sql);
            return results.isNotEmpty();
        }
    }

    public static List<Map<String, Object>> query(String sql) {
        try (DSLContext dsl = Pg.pg(propActivated)) {
            return dsl.resultQuery(sql).fetchMaps();
        }
    }

    public static int delete(String dbName, String tableName, String... conditions) {
        String fullTableName = getFullTableName(dbName, tableName);
        try (DSLContext dsl = Pg.pg(propActivated)) {
            DeleteWhereStep deleteWhereStep = dsl.delete(table(fullTableName));
            Condition condition = array2Condition(conditions);
            if (null == condition) {
                return deleteWhereStep.execute();
            } else {
                return deleteWhereStep.where(condition).execute();
            }
        }

    }

    public static int delete(String dbName, String tableName, Map<String, Object> conditionMap) {
        String fullTableName = getFullTableName(dbName, tableName);
        try (DSLContext dsl = Pg.pg(propActivated)) {
            DeleteWhereStep deleteWhereStep = dsl.delete(table(fullTableName));
            Condition condition = map2Condition(conditionMap);
            if (null == condition) {
                return deleteWhereStep.execute();
            } else {
                return deleteWhereStep.where(condition).execute();
            }
        }
    }

    public static int update(String dbName, String tableName, Map<String, Object> whereMap, Map<String, Object> setMap) {
        Preconditions.checkArgument(MapUtil.isNotEmpty(setMap), "set map 不能为空!");
        String fullTableName = getFullTableName(dbName, tableName);
        try (DSLContext dsl = Pg.pg(propActivated)) {
            UpdateSetFirstStep updateSetFirstStep = dsl.update(table(fullTableName));
            Condition condition = map2Condition(whereMap);
            if (null == condition) {
                return updateSetFirstStep.set(map2Field(setMap)).execute();
            } else {
                return updateSetFirstStep.set(map2Field(setMap)).where(condition).execute();
            }
        }
    }

    private static String getFullTableName(String dbName, String tableName) {
        String fullTableName = StringUtils.isNotEmpty(dbName) ? Joiner.on('.').join(dbName, tableName) : tableName;
        Preconditions.checkState(StringUtils.isNotEmpty(fullTableName), "FullTableName不能为空!");
        return fullTableName;
    }
}
