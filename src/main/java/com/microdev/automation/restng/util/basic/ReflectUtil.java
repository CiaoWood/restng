package com.microdev.automation.restng.util.basic;

import java.lang.reflect.Method;

/**
 * Created by wuchao on 17/8/21.
 */
public class ReflectUtil {

    private ReflectUtil() {
    }

    public static Method getCallerMethod(int layer) {
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        String className = stackTraces[layer].getClassName();
        String methodName = stackTraces[layer].getMethodName();
        try {
            Class<?> klass = Class.forName(className);
            Method[] methods = klass.getMethods();
            for (Method method : methods) {
                if (method.getName() == methodName) {
                    return method;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
