package com.microdev.automation.restng.util.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by wuchao on 17/6/27.
 */
public class AspectUtil {

    private AspectUtil() {
    }

    public static Object getAnnotationValue(final ProceedingJoinPoint pjp, Class annotationClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object myAnnotation = getAnnotation(pjp, annotationClass);
        Method valueMethod = annotationClass.getMethod("value");
        return valueMethod.invoke(myAnnotation);
    }

    public static Object getAnnotation(final ProceedingJoinPoint pjp, Class annotationClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(annotationClass);
    }
}
