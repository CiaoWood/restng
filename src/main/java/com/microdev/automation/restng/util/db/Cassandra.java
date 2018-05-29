package com.microdev.automation.restng.util.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.microdev.automation.restng.env.Property;
import com.microdev.automation.restng.exceptions.RestNgDBException;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wuchao on 17/6/28.
 */
public class Cassandra extends CassandraUtils {

    private static final String PROP_CASSANDRA_IP = "cassandra.ip";
    private static final String PROP_CASSANDRA_PORT = "cassandra.port";
    private static final String PROP_CASSANDRA_USERNAME = "cassandra.username";
    private static final String PROP_CASSANDRA_PASS = "cassandra.password";

    public static Cassandra getInstance() {
        String ip = Property.dbGet(PROP_CASSANDRA_IP);
        Preconditions.checkArgument(StringUtils.isNotBlank(ip), "com.microdev.automation.restng.ut.db.properties没有设置cassandra.ip");
        String port = Property.dbGet(PROP_CASSANDRA_PORT);
        Preconditions.checkArgument(NumberUtils.isDigits(port), "com.microdev.automation.restng.ut.db.properties没有设置cassandra.port");
        String username = Property.dbGet(PROP_CASSANDRA_USERNAME);
        String password = Property.dbGet(PROP_CASSANDRA_PASS);
        return getInstance(ip, port, username, password);
    }

    public static Cassandra getInstance(String ip, String port, String username, String password) {
        Cassandra cassandra = new Cassandra();
        cassandra.init(ip, port, username, password);
        return cassandra;
    }

    public static ResultSet delete(String dbName, String tableName, String... whereSeries) {
        Cassandra cassandra = getInstance();
        Delete.Where where = deleteWhere(QueryBuilder.delete().from(dbName, tableName), whereSeries);
        ResultSet result = cassandra.connect.execute(where);
        cassandra.close();
        return result;
    }

    private static Delete.Where deleteWhere(Delete from, String[] whereSeries) {
        if (whereSeries.length > 1) {
            Delete.Where where = from.where(QueryBuilder.eq(whereSeries[0], whereSeries[1]));
            for (int i = 2, len = whereSeries.length; i + 1 < len; i += 2) {
                where.and(QueryBuilder.eq(whereSeries[i], whereSeries[i + 1]));
            }
            return where;
        } else {
            throw new RestNgDBException("安全考虑,delete不能没有where条件!");
        }
    }

    private static Select.Where getWhere(Select from, Object[] whereSeries) {
        if (whereSeries.length > 1) {
            Select.Where where = from.where(QueryBuilder.eq((String) whereSeries[0], whereSeries[1]));
            for (int i = 2, len = whereSeries.length; i + 1 < len; i += 2) {
                where.and(QueryBuilder.eq((String) whereSeries[i], whereSeries[i + 1]));
            }
            return where;
        } else {
            throw new RestNgDBException("性能考虑,select不能没有where条件!");
        }
    }

    public static void insert(String dbName, String tableName, String initData) {
        Cassandra cassandra = getInstance();
        List<Map<String, Object>> insertedData = JSON.parseObject(initData, new TypeReference<List<Map<String, Object>>>() {
        });
        insertedData.forEach(Cassandra::transferCassandraType);
        cassandra.insert(dbName, tableName, insertedData);
        cassandra.close();
    }

    public static List<Map<String, Object>> execute(String sentence) {
        Cassandra cassandra = getInstance();
        ResultSet resultSet = cassandra.connect.execute(sentence);
        cassandra.close();
        return resultSet2List(resultSet);
    }

    public static ResultSet select(String dbName, String tableName, String selectWhat, Object... whereSeries) {
        Cassandra cassandra = getInstance();
        Select.Builder selectBuilder = selectWhat.equals("*") ? QueryBuilder.select() : QueryBuilder.select(selectWhat);
        Select.Where where = getWhere(selectBuilder.from(dbName, tableName), whereSeries);
        ResultSet resultSet = cassandra.connect.execute(where);
        cassandra.close();
        return resultSet;
    }

    public static ResultSet selectAll(String dbName, String tableName, String... whereSeries) {
        return select(dbName, tableName, "*", whereSeries);
    }

    public static void transferCassandraType(Map<String, Object> map) {
        CassandraUtils cassandraUtils = new CassandraUtils();
        cassandraUtils.tranCassandraType(map);
    }

    public static List<Map<String, Object>> resultSet2List(ResultSet resultSet) {
        List<Map<String, Object>> list = new ArrayList<>();
        ColumnDefinitions columnDefinitions = resultSet.getColumnDefinitions();

        Iterator<Row> iterator1 = resultSet.iterator();
        while (iterator1.hasNext()) {
            Map<String, Object> map = new HashedMap();
            Iterator<ColumnDefinitions.Definition> iterator2 = columnDefinitions.iterator();
            Row next = iterator1.next();

            while (iterator2.hasNext()) {
                ColumnDefinitions.Definition next1 = iterator2.next();
                Object object = next.getObject(next1.getName());
                map.put(next1.getName(), object);
            }
            list.add(map);
        }

        return list;
    }
}
