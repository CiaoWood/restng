package com.microdev.automation.restng;

import com.google.common.base.Preconditions;
import com.microdev.automation.restng.dataprovider.DataProviderFactory;
import com.microdev.automation.restng.env.Property;
import com.microdev.automation.restng.keydrive.engine.AbstractEngine;
import com.microdev.automation.restng.keydrive.engine.ExecuteEngine;
import com.microdev.automation.restng.util.http.HttpUtil;
import io.restassured.RestAssured;
import jxl.read.biff.BiffException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by wuchao on 17/6/29.
 */
public class RestNG {
    private static final Logger logger = LoggerFactory.getLogger(RestNG.class);
    private static final String BANNER = " \n" +
            "_______  _______  _______ _________ _        _______ \n" +
            "(  ____ )(  ____ \\(  ____ \\\\__   __/( (    /|(  ____ \\\n" +
            "| (    )|| (    \\/| (    \\/   ) (   |  \\  ( || (    \\/\n" +
            "| (____)|| (__    | (_____    | |   |   \\ | || |      \n" +
            "|     __)|  __)   (_____  )   | |   | (\\ \\) || | ____ \n" +
            "| (\\ (   | (            ) |   | |   | | \\   || | \\_  )\n" +
            "| ) \\ \\__| (____/\\/\\____) |   | |   | )  \\  || (___) |\n" +
            "|/   \\__/(_______/\\_______)   )_(   |/    )_)(_______)";

    public static void init() {
        logger.info(BANNER);
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        String baseURI = Property.get(Property.BASIC_URL_KEY);
        if (StringUtils.isNotBlank(baseURI)) {
            RestAssured.baseURI = HttpUtil.addHttpProtocol(baseURI);
            logger.info("Use base uri {}", baseURI);
        }
        String port = Property.get(Property.BASIC_PORT_KEY);
        if (NumberUtils.isDigits(port)) {
            RestAssured.port = Integer.parseInt(port);
            logger.info("Use port {}", port);
        }
        String proxy = Property.get(Property.PROXY);
        if (StringUtils.isNotBlank(proxy)) {
            RestAssured.proxy(proxy);
        }
    }

    public static void run(Map<String, String> data) {
        AbstractEngine executeEngine = ExecuteEngine.getExecuteEngine(data);
        Preconditions.checkNotNull(executeEngine);
        executeEngine.run();
    }

    @BeforeSuite
    public void before() {
        init();
    }

    @DataProvider
    public Object[][] xls(Method m) throws IOException, BiffException {
        return DataProviderFactory.excelData(m);
    }

    @DataProvider
    public Object[][] csv(Method m) throws IOException {
        return DataProviderFactory.csvData(m);
    }

    @DataProvider
    public Object[][] url(Method m) {
        return DataProviderFactory.urlData(m);
    }

    @DataProvider
    public Object[][] json(Method m) throws IOException {
        return DataProviderFactory.jsonData(m);
    }

    @DataProvider
    public Object[][] restng(Method m) throws IOException, BiffException {
        return DataProviderFactory.data(m);
    }
}
