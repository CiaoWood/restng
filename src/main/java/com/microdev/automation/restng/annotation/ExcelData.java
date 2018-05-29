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
public @interface ExcelData {
    String name();

    String sheet();

    /**
     * 感兴趣的行号，如果指定，则只返回指定行的数据。支持单个数字和区间(格式为a-b)的组合，英文逗号分隔。不指定时返回所有行。
     * 注意行号1对应Excel里的第二行，即第一行数据。示例："1,3,6-8"表示的是1,3,6,7,8行
     */
    String rows() default "";
}
