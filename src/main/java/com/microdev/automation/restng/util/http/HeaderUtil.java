package com.microdev.automation.restng.util.http;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Created by wuchao on 17/6/28.
 */
public class HeaderUtil {
    public static final String PROP_KEY_AUTH = "Authorization";
    public static final String PROP_KEY_CONTENTTYPE = "Content-Type";
    public static final String PROP_KEY_USERID = "userId";
    private static final Logger logger = LoggerFactory.getLogger(HeaderUtil.class);

    private HeaderUtil() { // 防止实例化
    }

    public static String authBasicString(String tobeEncoded) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(tobeEncoded));
        String encoded = Base64.encodeBase64String(tobeEncoded.getBytes());
        return authString("Basic", encoded);
    }

    public static Header authBasicHeader(String tobeEncoded) {
        String content = authBasicString(tobeEncoded);
        return new Header(PROP_KEY_AUTH, content);
    }

    public static String authServiceString(String content) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(content));
        return authString("service", content);
    }

    public static Header authServiceHeader(String content) {
        String authContent = authServiceString(content);
        return new Header(PROP_KEY_AUTH, authContent);
    }

    public static String authString(String prefix, String content) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(prefix));
        return String.format("%s %s", prefix, content);
    }

    public static String getAuthHeaderValue(Headers headers) {
        for (Header header : headers) {
            if (header.getName().equals("Authorization")) {
                return header.getValue();
            }
        }
        return null;
    }

    public static String getHeaderValue(Headers headers, String propKey) {
        for (Header header : headers) {
            if (header.getName().equals(propKey)) {
                return header.getValue();
            }
        }
        return null;
    }

    public static RequestSpecification setHeader(RequestSpecification requestSpecification, Field f, String propKey, String annotationValue, String systemValue) throws IllegalAccessException {
        RequestSpecification rs = requestSpecification;
        String headerValue = HeaderUtil.getHeaderValue((Headers) f.get(rs), propKey);
        if (StringUtils.isBlank(headerValue)) {
            if (StringUtils.isNotBlank(annotationValue)) {
                logger.debug("[AOP注入][header][{}]使用注解值{}", propKey, annotationValue);
                rs = rs.header(propKey, annotationValue);
            } else if (StringUtils.isNotBlank(systemValue)) {
                logger.debug("[AOP注入][header][{}]使用系统值{}", propKey, systemValue);
                rs = rs.header(propKey, systemValue);
            }
        }
        return rs;
    }
}
