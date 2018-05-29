package com.microdev.automation.restng.dataprovider;

import java.util.List;

import static io.restassured.RestAssured.when;

/**
 * Created by wuchao on 17/7/26.
 */
public class UrlDataProvider {

    private String path;
    private String key;

    public UrlDataProvider(String path, String key) {
        this.path = path;
        this.key = key;
    }


    public Object[][] getData() {
        List<Object> values = when().get(path).then().extract().path(key);
        int argSize = 1;
        Object[][] data = new Object[values.size()][argSize];
        for (int i = 0; i < values.size(); i++) {
            data[i][0] = values.get(i).toString();
        }
        return data;
    }
}
