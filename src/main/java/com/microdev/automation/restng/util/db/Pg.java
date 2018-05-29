package com.microdev.automation.restng.util.db;

import com.google.common.base.Preconditions;
import com.jcabi.aspects.Cacheable;
import com.microdev.automation.restng.env.Property;
import com.microdev.automation.restng.exceptions.RestNgDBException;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * Created by tuzhoulin on 2018/1/16.
 */
public class Pg {

    private static final Logger logger = LoggerFactory.getLogger(Pg.class);

    private Pg() {
    }

    @Cacheable(lifetime = 20, unit = TimeUnit.MINUTES)
    public static DSLContext pg(String prop) {
        String url = Property.dbGet(prop + ".url");
        Preconditions.checkArgument(StringUtils.isNotBlank(url), "com.microdev.automation.restng.ut.db.properties没有设置pg.url");
        String username = Property.dbGet(prop + ".username");
        String password = Property.dbGet(prop + ".password");
        try {
            return DSL.using(DriverManager.getConnection(url, username, password), SQLDialect.POSTGRES);
        } catch (SQLException e) {
            logger.error("pg connection error {}", e);
            throw new RestNgDBException(e.getLocalizedMessage());
        }
    }

}
