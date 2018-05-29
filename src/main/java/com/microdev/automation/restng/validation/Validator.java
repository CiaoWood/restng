package com.microdev.automation.restng.validation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.microdev.automation.restng.exceptions.JsonValidException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Set;

/**
 * Created by wuchao on 2017/3/27.
 */
public class Validator {

    private Validator() {
    }

    public static ValidResult valid(String source, String validString) {
        try {
            if (StringUtils.startsWith(validString, "{")) {
                if (!StringUtils.startsWith(source, "{"))
                    throw new JsonValidException("校验是对象,结果却不是。");
                JSONObject validObject = JSON.parseObject(validString);
                JSONObject sourceObject = JSON.parseObject(source);
                validObject(sourceObject, validObject, "");
            } else if (StringUtils.startsWith(validString, "[")) {
                if (!StringUtils.startsWith(source, "["))
                    throw new JsonValidException("校验是数组,结果却不是。");
                JSONArray validArray = JSON.parseArray(validString);
                JSONArray sourceArray = JSON.parseArray(source);
                validArray(sourceArray, validArray, "");
            } else {
                return new ValidResult(false);
            }
        } catch (JsonValidException e) {
            return new ValidResult(false, e.getMessage());
        } catch (JSONException e) {
            return new ValidResult(false, e.getMessage());
        }
        return new ValidResult(true);
    }

    public static void valid(Object source, Object validation, String path) throws JsonValidException {
        if (validation instanceof JSONObject) {
            if (!(source instanceof JSONObject))
                throw new JsonValidException(String.format("校验%s是对象,结果却是%s。", ((JSONObject) validation).toJSONString(), JSON.toJSONString(source)));
            validObject((JSONObject) source, (JSONObject) validation, path);
        } else if (validation instanceof JSONArray) {
            if (!(source instanceof JSONArray)) throw new JsonValidException("校验是数组,结果却不是。");
            validArray((JSONArray) source, (JSONArray) validation, path);
        } else if (!compare(source, validation, path)) {
            throw new JsonValidException(path + "校验是" + JSON.toJSONString(validation) + ",结果却是" + JSON.toJSONString(source) + "。");
        }
    }

    private static void validArray(JSONArray sourceArray, JSONArray validArray, String path) throws JsonValidException {
        if (null == validArray) return;
        if (null == sourceArray) throw new JsonValidException("校验有值,结果却是空。");
        StringBuilder msg = new StringBuilder("");
        for (Object validObject : validArray) {
            boolean match = false;
            for (int i = 0; i < sourceArray.size(); i++) {
                try {
                    valid(sourceArray.get(i), validObject, path + "." + i);
                    match = true;
                    sourceArray.remove(i);
                    break; // 改continue为break
                } catch (JsonValidException e) {
                    msg.append(e.getMessage());
                }
            }
            if (!match) {
                if (!msg.toString().contains("我们保证数组校验的结果,但说明仅供参考")) {
                    msg.append("(我们保证数组校验的结果,但说明仅供参考)");
                }
                throw new JsonValidException(msg.toString());
            }
        }
    }

    private static void validObject(JSONObject sourceObject, JSONObject validObject, String path) throws JsonValidException {
        if (null == validObject) return;
        if (null == sourceObject) throw new JsonValidException("校验有值,结果却是空。");
        Set<String> validObjectKeys = validObject.keySet();
        for (String validObjectKey : validObjectKeys) {
            if (!sourceObject.containsKey(validObjectKey) && !isSpecial(validObjectKey))
                throw new JsonValidException("校验包含key " + validObjectKey + ",结果却没有。");
            Object validObjectValue = validObject.get(validObjectKey);
            Object sourceObjectValue = sourceObject.get(validObjectKey);
            if (isSpecial(validObjectKey)) {
                specialValid(validObjectKey, validObjectValue, sourceObject, path);
            } else {
                valid(sourceObjectValue, validObjectValue, path + "." + validObjectKey);
            }
        }
    }

    private static boolean compare(Object source, Object validObject, String path) throws JsonValidException {
        boolean result = false;
        if (source instanceof Number) {
            if (!(validObject instanceof Number)) {
                throw new JsonValidException(path + "校验不是数字,结果却是数字。");
            }
            Double sourceD = NumberUtils.toDouble(String.valueOf(source));
            Double expectedD = NumberUtils.toDouble(String.valueOf(validObject));
            result = sourceD.equals(expectedD);
        } else if (source instanceof String) {
            if (validObject == null) return validObject == source;
            String sourceTrim = source.toString().replaceAll("\\u00A0", " ");
            String validTrim = validObject.toString().replaceAll("\\u00A0", " ");
            result = validTrim.equals(sourceTrim);
        } else {
            result = validObject.equals(source);
        }
        return result;
    }

    private static boolean isSpecial(String key) {
        return key.charAt(0) == '$';
    }

    private static void specialValid(String key, Object validObjectValue, JSONObject sourceObject, String path) throws JsonValidException {
        if (key.equals("$isEmpty")) {
            if (validObjectValue.equals(true)) {
                if (!sourceObject.keySet().isEmpty()) {
                    throw new JsonValidException("校验路径" + path + "为空,结果非空!");
                }
            } else if (validObjectValue.equals(false)) {
                if (sourceObject.keySet().isEmpty()) {
                    throw new JsonValidException("校验路径" + path + "非空,结果却为空!");
                }
            } else {
                throw new JsonValidException("$isEmpty的值必须为true或者false。");
            }
        }
    }


}
