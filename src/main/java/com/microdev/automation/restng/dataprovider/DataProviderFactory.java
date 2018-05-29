package com.microdev.automation.restng.dataprovider;

import com.microdev.automation.restng.annotation.CsvData;
import com.microdev.automation.restng.annotation.ExcelData;
import com.microdev.automation.restng.annotation.JsonData;
import com.microdev.automation.restng.annotation.UrlData;
import com.microdev.automation.restng.env.Property;
import com.microdev.automation.restng.exceptions.RestNgException;
import com.google.common.base.Strings;
import jxl.read.biff.BiffException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuchao on 17/7/20.
 */
public class DataProviderFactory {

    public static final Logger logger = LoggerFactory.getLogger(DataProviderFactory.class);

    private DataProviderFactory() {
    }

    private static void whetherSkipAll() {
        if (Property.getBoolean("skip.all").equals(true)) {
            throw new RestNgException("skip all");
        }
    }

    /**
     * 综合的入口,所有的dataprovider都在里面
     *
     * @param m 方法对象
     * @return 对应的dataprovider
     * @throws IOException
     * @throws BiffException
     */
    @DataProvider(name = "restng")
    public static Object[][] data(Method m) throws IOException, BiffException {
        Annotation annotation = m.getAnnotation(ExcelData.class);
        if (annotation != null) return excelData(m);
        annotation = m.getAnnotation(UrlData.class);
        if (annotation != null) return urlData(m);
        annotation = m.getAnnotation(JsonData.class);
        if (annotation != null) return jsonData(m);
        annotation = m.getAnnotation(CsvData.class);
        if (annotation != null) return csvData(m);
        throw new RestNgException("缺乏必要的data注解,例如@ExcelData、@UrlData、@JsonData、@CsvData");
    }

    @DataProvider(name = "excel")
    public static Object[][] excelData(Method m) throws IOException, BiffException {
        ExcelData annotation = m.getAnnotation(ExcelData.class);
        whetherSkipAll();
        return new ExcelProvider(getFilePath(annotation.name(), ".xls"), annotation.sheet(), annotation.rows()).getExcelData();
    }

    /**
     * 按照参数变量查找文件
     *
     * @param name
     * @return
     */
    public static String getFilePath(String name, String fix) {
        final String env = System.getProperty(Property.TAG);
        if (StringUtils.isNotBlank(env)) {
            String filePath = String.format("data/%s/%s%s", env, name, fix);
            URL appUrl = Thread.currentThread().getContextClassLoader().getResource(filePath);
            if (null != appUrl) {
                return String.format("%s/%s", env, name);
            }
        }
        return name;
    }

    @DataProvider(name = "csv")
    public static Object[][] csvData(Method m) throws IOException {
        CsvData annotation = m.getAnnotation(CsvData.class);
        whetherSkipAll();
        return new CsvDataProvider(getFilePath(annotation.value(), ".csv"), annotation.rows(), annotation.skip()).getData();
    }

    @DataProvider(name = "url")
    public static Object[][] urlData(Method m) {
        UrlData annotation = m.getAnnotation(UrlData.class);
        whetherSkipAll();
        return new UrlDataProvider(annotation.path(), annotation.key()).getData();
    }

    @DataProvider(name = "json")
    public static Object[][] jsonData(final Method m) throws IOException {
        JsonData annotation = m.getAnnotation(JsonData.class);
        whetherSkipAll();
        String value = getFilePath(annotation.value(), ".json");
        String matcher = annotation.matcher();
        String[] files = annotation.files();
        if (!Strings.isNullOrEmpty(value)) {
            return new JsonDataProvider(value, matcher).getData();
        } else if (files.length > 0) {
            List<Object[]> cases = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                Object[][] tmp = new JsonDataProvider(files[i], matcher).getData();
                for (int j = 0; j < tmp.length; j++) {
                    cases.add(tmp[j]);
                }
            }
            Object[][] res = new Object[cases.size()][1];
            for (int k = 0; k < cases.size(); k++) {
                res[k] = cases.get(k);
            }
            return res;
        } else {
            return new Object[0][0];
        }
    }

}
