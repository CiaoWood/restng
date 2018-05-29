package com.microdev.automation.restng.aop;

import com.google.common.base.Preconditions;
import com.microdev.automation.restng.Constance;
import com.microdev.automation.restng.env.Property;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;

/**
 * @author wuchao
 * @date 17/6/27
 */
@Aspect
public class RequestAspect {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RequestAspect.class);

    @Pointcut("execution(* io.restassured.internal.RequestSpecificationImpl.get(String,..)) || execution(* io.restassured.internal.RequestSpecificationImpl.post(String,..)) || execution(* io.restassured.internal.RequestSpecificationImpl.put(String,..)) || execution(* io.restassured.internal.RequestSpecificationImpl.delete(String,..)) || execution(* io.restassured.internal.RequestSpecificationImpl.patch(String,..))")
    public void methodPointcut() {
        // pointcut 方法 tobe researched
    }

    @Pointcut("execution(* io.restassured.internal.RequestSpecificationImpl.request(String, String, ..))")
    public void requestPointcut() {
        // pointcut 方法 tobe researched
    }

    @Around("methodPointcut()")
    public Object methodPointcutAround(final ProceedingJoinPoint pjp) throws Throwable {
        return proceedWithArgs(pjp, 0);
    }

    @Around("requestPointcut()")
    public Object requestPointcutAround(final ProceedingJoinPoint pjp) throws Throwable {
        return proceedWithArgs(pjp, 1);
    }

    private Object proceedWithArgs(ProceedingJoinPoint pjp, int index) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args.length > index) {
            String url = (String) args[index];
            String[] variables = StringUtils.substringsBetween(url, "{", "}");
            if (ArrayUtils.isNotEmpty(variables)) {
                String[] values = Property.readValues(variables);
                args[index] = fillValue(url, variables, values);
                logger.debug("[{}][request]注入后的url是{}", Constance.LOG_TITLE_AOP, args[index]);
                return pjp.proceed(args);
            }
        }
        return pjp.proceed();
    }

    private Object fillValue(String url, String[] variables, String[] values) {
        String filledUrl = url;
        if (ArrayUtils.isNotEmpty(variables) && ArrayUtils.isNotEmpty(values)) {
            Preconditions.checkArgument(variables.length == values.length);
            String[] searchList = new String[variables.length];
            for (int i = 0; i < variables.length; i++) {
                searchList[i] = "{" + variables[i] + "}";
            }
            filledUrl = StringUtils.replaceEach(filledUrl, searchList, values);
        }
        return filledUrl;
    }


}
