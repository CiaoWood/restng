package com.microdev.automation.restng.util.testng;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * @author wuchao
 */
public class TestRetryAnalyzer implements IRetryAnalyzer {
    public static int retryMaxLimit = 3;
    int counter = 1;

    @Override
    public boolean retry(ITestResult result) {
        if (counter < retryMaxLimit) {
            counter++;
            return true;
        }
        return false;
    }
}