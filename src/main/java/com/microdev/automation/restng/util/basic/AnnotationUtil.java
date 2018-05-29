package com.microdev.automation.restng.util.basic;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by wuchao on 17/8/21.
 */
public class AnnotationUtil {

    private AnnotationUtil() {
    }

    public static Annotation getAnnotation(int layout, Class annotationClass) {

        Method method = ReflectUtil.getCallerMethod(layout);

        if (null != method) {
            return method.getAnnotation(annotationClass);
        }
        return null;

    }

    public static String getAnnotationValue(int layout, Class annotationClass) {
        Annotation annotation = getAnnotation(layout, annotationClass);
        if (null != annotation) {
            try {
                return (String) annotation.getClass().getMethod("value").invoke(annotation);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
