package com.microdev.automation.restng.aop;

import com.google.common.base.Preconditions;
import com.microdev.automation.restng.Constance;
import com.microdev.automation.restng.annotation.Authorization;
import com.microdev.automation.restng.annotation.ContentType;
import com.microdev.automation.restng.annotation.Headers;
import com.microdev.automation.restng.annotation.UserId;
import com.microdev.automation.restng.env.Property;
import com.microdev.automation.restng.exceptions.RestNgException;
import com.microdev.automation.restng.util.aop.AspectUtil;
import com.microdev.automation.restng.util.http.HeaderUtil;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;


/**
 * @author wuchao
 * @date 17/6/27
 */
@Aspect
public class WhenAspect {
    private static final Logger logger = LoggerFactory.getLogger(WhenAspect.class);

    private String authorization = null;
    private String contentType = null;
    private String userId = null;

    @Pointcut("execution(* io.restassured.internal.RequestSpecificationImpl.when(..))")
    public void execWhenPointcut() {
        // TO BE RESEARCHED
    }

    @Pointcut("execution(* * (..))" + " && @annotation(com.microdev.automation.restng.annotation.Headers)")
    public void headersPointcut() {
        // TO BE ADDED
    }

    @Pointcut("execution(* * (..))" + " && @annotation(com.microdev.automation.restng.annotation.Authorization)")
    public void authorizationPointcut() {
        // TO BE ADDED
    }

    @Pointcut("@annotation(com.microdev.automation.restng.annotation.ContentType)")
    public void contentTypePointcut() {
        // TO BE ADDED
    }

    @Pointcut("@annotation(com.microdev.automation.restng.annotation.UserId)")
    public void userIdPointcut() {
        // TO BE ADDED
    }

    @Around("execWhenPointcut()") // && @within(com.microdev.automation.tryout.annotation.Authorization)
    public Object whenPointcutAround(final ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        RequestSpecification requestSpecification = (RequestSpecification) result;
        Field f = requestSpecification.getClass().getDeclaredField("requestHeaders");
        if (f != null) {
            f.setAccessible(true);//Very important, this allows the setting to work.
            requestSpecification = HeaderUtil.setHeader(requestSpecification, f, HeaderUtil.PROP_KEY_AUTH, authorization, Property.get(HeaderUtil.PROP_KEY_AUTH));
            requestSpecification = HeaderUtil.setHeader(requestSpecification, f, HeaderUtil.PROP_KEY_CONTENTTYPE, contentType, Property.get(HeaderUtil.PROP_KEY_CONTENTTYPE));
            requestSpecification = HeaderUtil.setHeader(requestSpecification, f, HeaderUtil.PROP_KEY_USERID, userId, Property.get(HeaderUtil.PROP_KEY_USERID));
        }
        return requestSpecification;
    }

    @Around("authorizationPointcut()")
    public Object authorizationPointcutAround(final ProceedingJoinPoint pjp) throws Throwable {
        String authorizationAnnotation = (String) AspectUtil.getAnnotationValue(pjp, Authorization.class);
        logger.info("[{}][restassured][authorization]{}", Constance.LOG_TITLE_AOP, authorizationAnnotation);
        this.authorization = authorizationAnnotation;
        Object result = pjp.proceed();
        this.authorization = null;
        return result;
    }

    @Around("contentTypePointcut()")
    public Object contentTypePointcutAround(final ProceedingJoinPoint pjp) throws Throwable {
        String contentTypeAnnotation = (String) AspectUtil.getAnnotationValue(pjp, ContentType.class);
        logger.info("[{}][restassured][content-type]{}", Constance.LOG_TITLE_AOP, contentTypeAnnotation);
        this.contentType = contentTypeAnnotation;
        Object result = pjp.proceed();
        this.contentType = null;
        return result;
    }

    @Around("userIdPointcut()")
    public Object userIdPointcutAround(final ProceedingJoinPoint pjp) throws Throwable {
        String userIdAnnotation = (String) AspectUtil.getAnnotationValue(pjp, UserId.class);
        logger.info("[{}][restassured][userId]{}", Constance.LOG_TITLE_AOP, userIdAnnotation);
        this.userId = userIdAnnotation;
        Object result = pjp.proceed();
        this.userId = null;
        return result;
    }

    @Around("headersPointcut()")
    public Object headersPointcutAround(final ProceedingJoinPoint pjp) throws Throwable {
        Headers headers = (Headers) AspectUtil.getAnnotation(pjp, Headers.class);
        String userIdOfHeader = headers.userId();
        String contentTypeOfHeader = headers.contentType();
        if (StringUtils.isNotBlank(userIdOfHeader)) {
            if (userIdOfHeader.equals("data")) {
                Object[] args = pjp.getArgs();
                Preconditions.checkArgument(args.length > 0, "输入参数不合法!");
                HashMap<String, String> data;
                try {
                    data = (HashMap<String, String>) args[0];
                    Preconditions.checkNotNull(data);
                } catch (Exception ex) {
                    throw new RestNgException("数据驱动无法转化为HashMap格式");
                }
                userIdOfHeader = data.get(HeaderUtil.PROP_KEY_USERID);
                Preconditions.checkArgument(StringUtils.isNotBlank(userIdOfHeader), String.format("数据驱动必须包含%s的键", HeaderUtil.PROP_KEY_USERID));
            }
            this.userId = userIdOfHeader;
        }
        if (StringUtils.isNotBlank(contentTypeOfHeader)) {
            this.contentType = contentTypeOfHeader;
        }
        Object result = pjp.proceed();
        this.authorization = null;
        this.userId = null;
        return result;
    }

}
