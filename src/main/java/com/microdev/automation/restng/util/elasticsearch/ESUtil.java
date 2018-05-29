package com.microdev.automation.restng.util.elasticsearch;

import com.microdev.automation.restng.Constance;
import com.microdev.automation.restng.util.datetime.DateUtil;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ESUtil {

    private static final String DEFAULT_SIZE = "25";

    private ESUtil() {
    }

    public static List<HashMap<String, Object>> searchMsg(String msg) {
        return searchMsg(msg, DateUtil.today());
    }

    public static List<HashMap<String, Object>> searchMsg(String msg, String day) {
        return searchMsgSize(msg, day, DEFAULT_SIZE);
    }

    public static List<HashMap<String, Object>> searchMsgSize(String msg, String size) {
        return searchMsgSize(msg, DateUtil.today(), size);
    }

    public static List<HashMap<String, Object>> searchMsgSize(String msg, String day, String size) {
        return search(String.format("{\"query\":{\"bool\":{\"must\":[{\"query_string\":{\"default_field\":\"logs.message\",\"query\":\"message:%s\"}}],\"must_not\":[],\"should\":[]}},\"from\":0,\"size\":%s,\"sort\":[],\"aggs\":{}}", msg, size), day);
    }

    public static List<HashMap<String, Object>> search(String bodyString, String day) {

        List<HashMap<String, Object>> jsonArray =
                given().
                        header("Authorization", Constance.ES_AUTH).
                        body(bodyString).
                        when().
                        post(Constance.ES_URL + "fc-" + day + "/_search").
                        path("hits.hits._source");

        return jsonArray;
    }

    public static List<HashMap<String, Object>> search(String bodyString) {
        return search(bodyString, DateUtil.today());
    }

}
