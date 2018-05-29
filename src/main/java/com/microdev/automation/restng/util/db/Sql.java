package com.microdev.automation.restng.util.db;

import com.google.common.base.Preconditions;
import com.jcabi.aspects.Cacheable;
import com.microdev.automation.restng.annotation.MysqlDB;
import com.microdev.automation.restng.env.Property;
import com.microdev.automation.restng.exceptions.RestNgDBException;
import com.microdev.automation.restng.util.basic.AnnotationUtil;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * Created by wuchao on 17/6/28.
 */
public class Sql {
    private static final Logger logger = LoggerFactory.getLogger(Sql.class);

    private Sql() {
    }

    @Cacheable(lifetime = 20, unit = TimeUnit.MINUTES)
    public static DSLContext mysql(String prop) {
        String url = Property.dbGet(prop + ".url");
        Preconditions.checkArgument(StringUtils.isNotBlank(url), "com.microdev.automation.restng.ut.db.properties没有设置mysql.url");
        String username = Property.dbGet(prop + ".username");
        String password = Property.dbGet(prop + ".password");
        try {
            return DSL.using(DriverManager.getConnection(url, username, password), SQLDialect.MYSQL);
        } catch (SQLException e) {
            logger.error("sql connection error {}", e);
            throw new RestNgDBException(e.getLocalizedMessage());
        }
    }

    public static DSLContext mysql() {
        String prop = Mysql.PROP;
        String database = AnnotationUtil.getAnnotationValue(5, MysqlDB.class);
        if (StringUtils.isNotBlank(database)) prop = database;
        return mysql(prop);
    }

}
