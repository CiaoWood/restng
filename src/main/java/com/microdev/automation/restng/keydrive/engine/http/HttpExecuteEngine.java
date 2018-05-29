package com.microdev.automation.restng.keydrive.engine.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.microdev.automation.restng.Constance;
import com.microdev.automation.restng.env.Property;
import com.microdev.automation.restng.keydrive.engine.AbstractEngine;
import com.microdev.automation.restng.util.basic.MapUtil;
import com.microdev.automation.restng.util.http.HeaderUtil;
import com.microdev.automation.restng.validation.JsonValidator;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by wuchao on 17/7/18.
 */
public class HttpExecuteEngine extends AbstractEngine {
    private static final Logger logger = LoggerFactory.getLogger(HttpExecuteEngine.class);
    private static final String QUERY_PREFIX = "query";
    private static final String HEADER_PREFIX = "header";
    private static final String BODY_PREFIX = "body";
    private static final String SPLITTER = ":";
    private static final String KEY_AUTH = "authorization";
    private static final String KEY_AUTH_USERID = "userId";
    private Map<String, Object> headers;

    public HttpExecuteEngine(Map<String, String> data) {
        super(data);
    }

    @Override
    public void run() {
        RequestSpecification requestSpecification = given();
        if (StringUtils.isNotBlank(getReqBody())) {
            requestSpecification.body(getReqBody());
        }
        if (!MapUtil.isEmpty(getHeaderMap())) {
            requestSpecification.headers(getHeaderMap());
        }
        if (!MapUtil.isEmpty(getQueryMap())) {
            requestSpecification.queryParams(getQueryMap());
        }
        ValidatableResponse validatableResponse = requestSpecification.when().request(getMethod(), getUrl()).then();
        runExpected(validatableResponse);
        runPathExpected(validatableResponse);
    }

    private void runPathExpected(ValidatableResponse validatableResponse) {
        Map<String, Object> pathExpectMap = getMap(Constance.EXPECTED);
        if (!MapUtil.isEmpty(pathExpectMap)) {
            pathExpectMap.forEach((k, v) -> {
                String value = "";
                if (v != null) value = String.valueOf(v);
                Preconditions.checkArgument(StringUtils.isNotBlank(k));
                String type = StringUtils.substringAfter(k, SPLITTER);
                if (StringUtils.isBlank(value)) {
                    validatableResponse.body(k, notNullValue());
                } else if (StringUtils.isBlank(type)) {
                    validatableResponse.body(k, JsonValidator.is(value));
                } else {
                    Class typeClass;
                    type = StringUtils.capitalize(type);
                    try {
                        typeClass = Class.forName("java.lang." + type);
                    } catch (ClassNotFoundException e) {
                        typeClass = String.class;
                    }
                    Object expect = JSON.parseObject(value, typeClass);
                    validatableResponse.body(StringUtils.substringBefore(k, SPLITTER), equalTo(expect));
                }
            });
        }
    }

    private void runExpected(ValidatableResponse validatableResponse) {
        if (StringUtils.isNotBlank(getExpected())) {
            validatableResponse.body(JsonValidator.containJson(getExpected()));
        }
    }

    public String getMethod() {
        String methodString = data.get(Constance.METHOD);
        Preconditions.checkNotNull(methodString);
        return methodString;
    }

    public String getUrl() {
        String urlString = data.get(Constance.URL);
        Preconditions.checkNotNull(urlString);
        return urlString;
    }

    public String getReqBody() {
        String bodyString = MapUtil.getFromMap(data, Constance.REQ_BODY);
        if (StringUtils.isNotBlank(bodyString) && bodyString.startsWith("[")) return bodyString; // 如果是数组,直接返回
        JSONObject jsonObject;
        if (StringUtils.isNotBlank(bodyString)) {
            jsonObject = JSON.parseObject(bodyString);
        } else {
            jsonObject = new JSONObject();
        }
        Map<String, Object> bodyMap = getMap(BODY_PREFIX);
        if (!MapUtil.isEmpty(bodyMap)) {
            bodyMap.forEach(jsonObject::put);
        }
        return jsonObject.size() > 0 ? jsonObject.toJSONString() : null;
    }

    public String getExpected() {
        return data.get(Constance.EXPECTED);
    }

    public Map<String, Object> getQueryMap() {
        return getMap(QUERY_PREFIX);
    }

    public Map<String, Object> getHeaderMap() {
        if (headers == null) {
            headers = getMap(HEADER_PREFIX);
            String userId = String.valueOf(headers.get(HeaderUtil.PROP_KEY_USERID));
            if (StringUtils.isBlank(userId)) {
                userId = Property.get(HeaderUtil.PROP_KEY_USERID);
            }
            String authType = data.get(KEY_AUTH);
        }
        return headers;
    }

    private Map<String, Object> getMap(String prefix) {
        Set<String> keys = data.keySet();
        HashMap<String, Object> map = new HashMap();
        keys.forEach(key -> {
            if (key.startsWith(prefix)) {
                String keyAfter = StringUtils.substringAfter(key, SPLITTER);
                if (StringUtils.isNotBlank(keyAfter)) {
                    map.put(keyAfter, data.get(key));
                }
            }
        });
        return map;
    }
}
