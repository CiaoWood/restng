package com.microdev.automation.restng.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvData {

    /**
     * csv文件的名称（无后缀）
     */
    String value();

    /**
     * 感兴趣的行，支持格式如："1,2,4-6"
     */
    String rows() default "";

    /**
     * 跳过的行，默认不跳过。支持格式如："1,2,4-6"
     */
    String skip() default "";

}
