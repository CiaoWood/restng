package com.microdev.automation.restng.dataprovider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.microdev.automation.restng.util.basic.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wuchao on 17/6/15.
 */
public class JsonDataProvider {

    private String fileName;

    private String matcher = "";

    /**
     * @param fileName excel文件名
     */
    public JsonDataProvider(String fileName) {
        this.fileName = fileName;
    }

    public JsonDataProvider(String fileName, String matcher) {
        this.fileName = fileName;
        this.matcher = matcher;
    }

    public Object[][] getData() throws IOException {
//        String filePath = FileUtils.getPath(this.fileName, ".json");
//        InputStream inputStream = new FileInputStream(filePath);
        InputStream inputStream = FileUtils.getInputStream(this.fileName, ".json");
        String text = IOUtils.toString(inputStream, "utf8");
        List<Map<String, Object>> testcases = JSON.parseObject(text, new TypeReference<List<Map<String, Object>>>() {
        });

        List<Map<String, Object>> matched;
        if (matcher.matches("[a-zA-Z0-9]+:[^:{}]+")) {
            matched = new ArrayList<>();
            String key = matcher.split(":")[0];
            String value = matcher.split(":")[1];
            for (Map<String, Object> testcase : testcases) {
                if (value.equals("" + testcase.get(key))) {
                    matched.add(testcase);
                }
            }
        } else {
            matched = testcases;
        }
        Object[][] data = new Object[matched.size()][1];
        for (int i = 0, len = matched.size(); i < len; i++) {
            Map<String, Object> tmp = matched.get(i);
            for (Map.Entry<String, Object> entry : tmp.entrySet()) {
                if (entry.getValue() instanceof JSONObject) {
                    tmp.put(entry.getKey(), JSON.toJSONString(entry.getValue()));
                }
            }
            data[i][0] = tmp;
        }
        return data;
    }
}
