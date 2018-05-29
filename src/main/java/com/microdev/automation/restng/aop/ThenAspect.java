package com.microdev.automation.restng.aop;

import com.microdev.automation.restng.Constance;
import com.microdev.automation.restng.annotation.StatusCode;
import com.microdev.automation.restng.env.Property;
import com.microdev.automation.restng.util.aop.AspectUtil;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.microdev.automation.restng.env.Property.*;

/**
 * Created by wuchao on 17/6/27.
 */
@Aspect
public class ThenAspect {
    private static final Logger logger = LoggerFactory.getLogger(ThenAspect.class);

    private static final int DEFAULT_STATUS_CODE = -1;
    private int statusCode = DEFAULT_STATUS_CODE;

    @Pointcut("execution(* io.restassured.internal.RestAssuredResponseImpl.then(..))")
    public void thenPointcut() {
        // TO BE RESEARCHED
    }

    @Pointcut("@annotation(com.microdev.automation.restng.annotation.StatusCode)")
    public void statusCodePointcut() {
        // TO BE ADDED
    }

    @Around("thenPointcut()") // && @within(com.microdev.automation.tryout.annotation.Authorization)
    public Object thenPointcutAround(final ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("[{}][restassured][出参]", Constance.LOG_TITLE_AOP);
        Object result = pjp.proceed();
        ValidatableResponse validatableResponse = (ValidatableResponse) result;
        if (statusCode > 0) {
            validatableResponse = validatableResponse.statusCode(statusCode);
        }
        log(validatableResponse);
        return validatableResponse;
    }

    @Around("statusCodePointcut()")
    public Object authorizationPointcutAround(final ProceedingJoinPoint pjp) throws Throwable {
        int statusCodeAnnotation = (int) AspectUtil.getAnnotationValue(pjp, StatusCode.class);
        logger.debug("[{}][restassured][status]{}", Constance.LOG_TITLE_AOP, statusCodeAnnotation);
        statusCode = statusCodeAnnotation;
        Object result = pjp.proceed();
        statusCode = DEFAULT_STATUS_CODE;
        return result;
    }

    private void log(ValidatableResponse validatableResponse) {
        Response response = validatableResponse.extract().response();
        if (Property.getBoolean(LOG_STATUS)) {
            logger.info("[Response Status] => {}", response.getStatusCode());
        }
        if (Property.getBoolean(LOG_HEADER)) {
            logger.info("[Response Header] => {}", response.getHeaders());
        }
        if (Property.getBoolean(LOG_BODY)) {
            String body = response.getBody().asString();
            logger.info("[Response Body] => {}", body);
        }
    }

}
