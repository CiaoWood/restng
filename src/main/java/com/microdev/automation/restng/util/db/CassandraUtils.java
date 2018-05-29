package com.microdev.automation.restng.util.db;


import com.microdev.automation.restng.exceptions.RestNgDBException;
import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Update;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.Map.Entry;

/**
 * Created by liumingming on 16/6/21.
 */
public class CassandraUtils {

    private static final String CLUSTER_SAPERATOR = ",";
    public Session connect;
    public Cluster build;
    public MappingManager manager;

    public CassandraUtils() {
        // 空构造函数
    }

    public static Object transNowTime2Date(String value) {
        if (value != null && value.toString().startsWith("now")) {
            if ("now".equalsIgnoreCase(value.toString())) {
                return new Date();
            }

            String timeType = value.toString().substring(value.toString().length() - 1, value.toString().length());
            int type;
            Calendar calendar;
            if ("m".equalsIgnoreCase(timeType)) {
                type = Integer.parseInt(value.toString().substring("now".length(), value.toString().length() - 1));
                calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(5, type);
                return calendar.getTime();
            }

            if ("d".equalsIgnoreCase(timeType)) {
                type = Integer.parseInt(value.toString().substring("now".length(), value.toString().length() - 1));
                calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(6, type);
                return calendar.getTime();
            }

            if ("h".equalsIgnoreCase(timeType)) {
                type = Integer.parseInt(value.toString().substring("now".length(), value.toString().length() - 1));
                calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(11, type);
                return calendar.getTime();
            }
        }

        return value;
    }

    public void close() {

        if (connect != null) {
            connect.close();
        }
        if (build != null) {
            build.close();
        }
    }

    public void init(String ip, String port, String username, String password) {
        Preconditions.checkArgument(StringUtils.isNotBlank(ip), "Cassandra ip 不能为空");
        int intPort = Integer.parseInt(port);
        Cluster.Builder clusterBuilder = Cluster.builder();
        if (ip.contains(CLUSTER_SAPERATOR)) {
            clusterBuilder = clusterBuilder.addContactPoints(ip.split(CLUSTER_SAPERATOR));
        } else {
            clusterBuilder = clusterBuilder.addContactPoint(ip);
        }
        if (username == null) {
            build = clusterBuilder.withPort(intPort).build();
        } else {
            build = clusterBuilder.withPort(intPort).withCredentials(username, password).build();
        }
        if (build == null) {
            throw new RestNgDBException("connect cassandra fail!");
        }
        connect = build.connect();
    }

    public void insert(String dbName, String tableName, List<Map<String, Object>> list) {
        if (list.size() <= 0) {
            throw new RestNgDBException("no data in source map!");
        }
        for (Map<String, Object> map : list) {
            List<String> keyList = new ArrayList<>();
            List<Object> valueList = new ArrayList<>();
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                Object value = next.getValue();
                keyList.add(key);
                valueList.add(value);

            }
            connect.execute(QueryBuilder.insertInto(dbName, tableName).values(keyList, valueList));
        }

    }

    public void insert(String sql) {
        connect.execute(sql);
    }

    public void delete(String dbName, String tableName, List<Map<Object, Object>> list) {
        if (list.size() <= 0) {
            throw new RestNgDBException("no data in source map!");
        }
        for (Map<Object, Object> map : list) {
            if (map.size() <= 0) {
                throw new RestNgDBException("no conditions found");
            }
            Iterator<Map.Entry<Object, Object>> iterator = map.entrySet().iterator();
            Map.Entry<Object, Object> next1 = iterator.next();
            Object key1 = next1.getKey();
            Object value1 = next1.getValue();
            Delete.Where where = QueryBuilder.delete().from(dbName, tableName)
                    .where(QueryBuilder.eq(key1.toString(), value1));
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> next = iterator.next();
                Object key = next.getKey();
                Object value = next.getValue();
                where.and(QueryBuilder.eq(key.toString(), value));
            }
            connect.execute(where);
        }

    }

    public void deleteAll(String dbName, String tableName) {
        connect.execute(QueryBuilder.truncate(dbName, tableName));
    }

    public void update(String dbName, String tableName, Map<String, Object> map, Map<String, Object> conditionMap) {

        Iterator<Map.Entry<String, Object>> iterator = conditionMap.entrySet().iterator();
        Update.Where where = QueryBuilder.update(dbName, tableName)
                .where(QueryBuilder.eq(iterator.next().getKey(), iterator.next().getValue()));
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            Object value = next.getValue();
            where.and(QueryBuilder.eq(key, value));
        }
        connect.execute(where);
    }

    public List<Map<String, Object>> query(String dbName, String tableName, Map<String, Object> conditionMap) {
        List<Map<String, Object>> list = new ArrayList<>();
        Iterator<Map.Entry<String, Object>> iterator = conditionMap.entrySet().iterator();

        Select.Where where = QueryBuilder.select().from(dbName, tableName).where();

        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            Object value = next.getValue();
            where.and(QueryBuilder.eq(key, value));
        }
        ResultSet resultSet = connect.execute(where);
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

    public List<Map<String, Object>> query(String sql) {
        List<Map<String, Object>> list = new ArrayList<>();
        ResultSet resultSet = connect.execute(sql);
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

    public void tranCassandraType(Map<String, Object> map) {
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry next = (Entry) iterator.next();
            if (next.getValue() != null) {
                if ("UUID.random".equals(next.getValue())) {
                    map.replace(String.valueOf(next.getKey()), UUIDs.timeBased());
                }

                if (next.getValue() instanceof Map) {
                    this.tranCassandraType((Map) next.getValue());
                }

                if (next.getValue().toString().contains("UUID.random:")) {
                    map.replace(String.valueOf(next.getKey()), UUID.fromString(next.getValue().toString().split(":")[1]));
                }
            }
        }

        transMapNowTimeValueObj(map);
    }

    private void transMapNowTimeValueObj(Map<String, Object> map) {
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry next = (Entry) iterator.next();
            String key = (String) next.getKey();
            Object value = next.getValue();
            if (value != null && value.toString().startsWith("now")) {
                value = transNowTime2Date(value.toString());
                map.replace(key, value);
            }
        }
    }

}
