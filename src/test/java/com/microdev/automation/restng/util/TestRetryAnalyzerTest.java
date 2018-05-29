package com.microdev.automation.restng.util;

import com.microdev.automation.restng.util.testng.TestRetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestRetryAnalyzerTest {

    int i = 0;

    @Test(retryAnalyzer = TestRetryAnalyzer.class)
    public void testAnalyzer() {
        if (i++ != 2) {
            Assert.assertTrue(false, "shall be retry");
        } else {
            Assert.assertTrue(true);
        }
    }

}