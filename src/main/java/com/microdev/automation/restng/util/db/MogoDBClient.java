package com.microdev.automation.restng.util.db;

import com.google.common.base.Preconditions;
import com.microdev.automation.restng.env.Property;
import com.mongodb.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by zhouhongfei on 2017/11/21.
 */
public class MogoDBClient {

    private static final Logger logger = LoggerFactory.getLogger(MogoDBClient.class);
    private static final String MONGO_KEY_IP = "mongo.ip";
    private static final String MONGOKEY_PORT = "mongo.port";
    private MongoClient mongoClient;

    public static MogoDBClient getMogoDBClient() {
        String ip = Property.dbGet(MONGO_KEY_IP);
        String port = Property.dbGet(MONGOKEY_PORT);
        Preconditions.checkArgument(StringUtils.isNotBlank(ip), "未配置mongo.ip");
        Preconditions.checkArgument(StringUtils.isNotBlank(port), "未配置mongo.port");
        MogoDBClient client = new MogoDBClient();
        client.initMogo(ip, port);
        return client;
    }

    public static MogoDBClient getMogoDBClient(String ip, String port) {
        Preconditions.checkArgument(StringUtils.isNotBlank(ip), "未配置mongo.ip");
        Preconditions.checkArgument(StringUtils.isNotBlank(port), "未配置mongo.port");
        MogoDBClient client = new MogoDBClient();
        client.initMogo(ip, port);
        return client;
    }

    public static boolean insert(String dbName, String tableName, Object obj) {
        MogoDBClient client = getMogoDBClient();
        DB db = client.mongoClient.getDB(dbName);
        DBCollection collection = db.getCollection(tableName);
        collection.insert(new DBObject[]{new BasicDBObject(Object2Map(obj))});
        client.closeMogo();
        return true;
    }

    public static List<Map<String, Object>> query(String dbName, String tableName, Object obj) {
        List<Map<String, Object>> mapList = new ArrayList();
        MogoDBClient client = getMogoDBClient();
        DB db = client.mongoClient.getDB(dbName);
        DBCollection collection = db.getCollection(tableName);
        DBCursor limit = collection.find(new BasicDBObject(Object2Map(obj))).limit(1000);
        Iterator iterator = limit.iterator();
        while (iterator.hasNext()) {
            DBObject next = (DBObject) iterator.next();
            Map result = next.toMap();
            Map<String, Object> lineMap = new HashMap();
            Iterator iterator1 = result.entrySet().iterator();

            while (iterator1.hasNext()) {
                Map.Entry<String, Object> next1 = (Map.Entry) iterator1.next();
                String key = (String) next1.getKey();
                Object value = next1.getValue();
                lineMap.put(key, value);
            }

            mapList.add(lineMap);
        }
        client.closeMogo();

        return mapList;
    }

    public static boolean update(String dbName, String tableName, Object whatobj, Object whereobj) {
        MogoDBClient client = getMogoDBClient();
        DB db = client.mongoClient.getDB(dbName);
        DBCollection collection = db.getCollection(tableName);
        collection.update(new BasicDBObject(Object2Map(whereobj)), new BasicDBObject(Object2Map(whatobj)));
        client.closeMogo();
        return true;

    }

    public static boolean delete(String dbName, String tableName, Object obj) {
        MogoDBClient client = getMogoDBClient();
        DB db = client.mongoClient.getDB(dbName);
        DBCollection collection = db.getCollection(tableName);
        collection.remove(new BasicDBObject(Object2Map(obj)));
        client.closeMogo();
        return true;

    }

    public static Map<String, Object> Object2Map(Object obj) {

        if (obj == null) {
            return null;
        }
        if (obj instanceof Map) {
            return (Map) obj;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();

        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    private void initMogo(String ip, String port) {
        mongoClient = new MongoClient(new ServerAddress(ip, Integer.parseInt(port)));
    }

    private void closeMogo() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}

