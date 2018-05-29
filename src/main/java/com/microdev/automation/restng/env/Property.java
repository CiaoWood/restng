package com.microdev.automation.restng.env;

import com.microdev.automation.restng.util.log.LOGGER;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;

/**
 * @author wuchao
 * @date 17/6/28
 */
public class Property {

    public static final String TAG = "service.tag";
    public static final String LOG_REQUEST_PARAMS = "log.request.params";
    public static final String LOG_REQUEST_BODY = "log.request.body";
    public static final String LOG_REQUEST_HEADERS = "log.request.headers";
    public static final String LOG_REQUEST_COOKIES = "log.request.cookies";
    public static final String LOG_REQUEST_PATH = "log.request.path";
    public static final String LOG_STATUS = "log.response.status";
    public static final String LOG_HEADER = "log.response.header";
    public static final String LOG_BODY = "log.response.body";
    public static final String PROXY = "proxy";
    public static final String BASIC_URL_KEY = "baseURI";
    public static final String BASIC_PORT_KEY = "port";
    public static final String REPLACEIP_IGNORE = "replaceIp.ignore";
    public static final String DEFAULT_ENV = "local";
    protected static String env;
    private static CompositeConfiguration appProperties;
    private static CompositeConfiguration dbProperties;

    static {
        URL defaultAppUrl = Thread.currentThread().getContextClassLoader().getResource(getFilename("app", DEFAULT_ENV));
        final String appFileName = getFilename("app", getEnv());
        URL appUrl = Thread.currentThread().getContextClassLoader().getResource(appFileName);
        appProperties = readProperties(appUrl, defaultAppUrl);
        URL defaultDbUrl = Thread.currentThread().getContextClassLoader().getResource(getFilename("db", DEFAULT_ENV));
        URL dbUrl = Thread.currentThread().getContextClassLoader().getResource(getFilename("db", getEnv()));
        dbProperties = readProperties(dbUrl, defaultDbUrl);
    }

    private Property() {
    }

    public static String getEnv() {
        if (StringUtils.isBlank(env)) {
            String envValue = System.getProperty(TAG);
            if (StringUtils.isNotBlank(envValue)) {
                env = envValue;
            } else {
                LOGGER.warn("没有设置{}环境变量,默认用local", TAG);
                env = DEFAULT_ENV;
            }
        }
        return env;
    }

    private static String getFilename(String filename, String env) {
        return String.format("%s/%s.properties", env, filename);
    }

    public static String get(String key) {
        if (null == appProperties) {
            return null;
        }
        return appProperties.getString(key);
    }

    public static Boolean getBoolean(String key) {
        if (null == appProperties) {
            return false;
        }
        return Boolean.valueOf(appProperties.getString(key));
    }

    public static String dbGet(String key) {
        if (null == dbProperties) {
            return null;
        }
        return dbProperties.getString(key);
    }

    private static CompositeConfiguration addProperty(CompositeConfiguration config, URL url) {
        if (null != url) {
            try {
                config.addConfiguration(new PropertiesConfiguration(url));
            } catch (ConfigurationException e) {
                final String ERROR_MSG = "读properties文件io异常";
                LOGGER.error(ERROR_MSG, e);
            }
        }
        return config;
    }

    private static CompositeConfiguration readProperties(URL... urls) {
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new SystemConfiguration());
        for (URL url : urls) {
            if (null != url) {
                addProperty(config, url);
            }
        }
        return config;
    }

    public static String[] readValues(String[] variables) {
        String[] values = new String[variables.length];
        for (int i = 0; i < variables.length; i++) {
            String value = get(variables[i]);
            if (StringUtils.isNotBlank(value)) {
                values[i] = value;
            } else {
                values[i] = "{" + variables[i] + "}";
            }
        }
        return values;
    }
}
