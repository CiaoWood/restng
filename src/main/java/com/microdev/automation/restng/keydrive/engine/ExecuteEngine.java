package com.microdev.automation.restng.keydrive.engine;

import com.microdev.automation.restng.Constance;
import com.microdev.automation.restng.exceptions.RestNgException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Map;


/**
 * Created by wuchao on 17/7/18.
 */
public class ExecuteEngine {
    private static final Logger logger = LoggerFactory.getLogger(ExecuteEngine.class);

    private ExecuteEngine() {
    }

    public static AbstractEngine getExecuteEngine(Map<String, String> data) {
        String type = data.get(Constance.TYPE);
        if (StringUtils.isBlank(type)) {
            type = "http";
        }
        Class executeEngineClass;
        try {
            executeEngineClass = Class.forName(String.format("com.microdev.automation.restng.keydrive.engine.http.%sExecuteEngine", StringUtils.capitalize(type)));
        } catch (ClassNotFoundException e) {
            String err = String.format("不支持的类型{%s}", type);
            logger.error(err);
            throw new RestNgException(err);
        }
        try {
            Constructor constructor = executeEngineClass.getConstructor(Map.class);
            return (AbstractEngine) constructor.newInstance(data);
        } catch (NoSuchMethodException e) {
            logger.error("executor没有map构造方法{}", executeEngineClass.toString());
        } catch (Exception e) {
            logger.error("executor引擎获取构造器异常{}", e);
        }
        return null;
    }
}
