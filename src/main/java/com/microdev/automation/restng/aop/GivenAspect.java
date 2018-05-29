package com.microdev.automation.restng.aop;

import com.microdev.automation.restng.env.Property;
import com.microdev.automation.restng.env.Property;
import io.restassured.specification.RequestSpecification;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


/**
 * @author wuchao
 * @date 17/6/27
 */
@Aspect
public class GivenAspect {

    @Pointcut("execution(* io.restassured.RestAssured.given(..))")
    public void execGivenPointcut() {
        // TO BE RESEARCHED
    }

    @Around("execGivenPointcut()") // && @within(com.microdev.automation.tryout.annotation.Authorization)
    public Object givenPointcutAround(final ProceedingJoinPoint pjp) throws Throwable {
        RequestSpecification requestSpecification = (RequestSpecification) pjp.proceed();
        if (Property.getBoolean(Property.LOG_REQUEST_PARAMS)) {
            requestSpecification = requestSpecification.log().params();
        }
        if (Property.getBoolean(Property.LOG_REQUEST_BODY)) {
            requestSpecification = requestSpecification.log().body();
        }
        if (Property.getBoolean(Property.LOG_REQUEST_HEADERS)) {
            requestSpecification = requestSpecification.log().headers();
        }
        if (Property.getBoolean(Property.LOG_REQUEST_COOKIES)) {
            requestSpecification = requestSpecification.log().cookies();
        }
        if (Property.getBoolean(Property.LOG_REQUEST_PATH)) {
            requestSpecification = requestSpecification.log().uri().log().method();
        }
        return requestSpecification;
    }

}
