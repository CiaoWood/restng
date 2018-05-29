package com.microdev.automation.restng.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wuchao on 17/6/26.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonData {
    /**
     * 指定一个文件时使用，优先
     */
    String value() default "";

    /**
     * 指定多个文件时使用
     */
    String[] files() default {};

    /**
     * 匹配用例，默认全部匹配。使用方法：matcher="key:value", 则只有key为value的记录才会被采用
     */
    String matcher() default "";
}
