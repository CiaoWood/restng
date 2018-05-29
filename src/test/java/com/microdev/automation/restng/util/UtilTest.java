package com.microdev.automation.restng.util;

import com.microdev.automation.restng.util.basic.RequestUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UtilTest {

    @Test
    public void testRequestUtil() {
        String var1 = "localhost:8089";
        Assert.assertFalse(RequestUtil.validHost(var1));
        String var2 = ":8080";
        Assert.assertFalse(RequestUtil.validHost(var2));
        String var3 = "10.82.3.2:";
        Assert.assertFalse(RequestUtil.validHost(var3));
        String var4 = "10.82.3.2:abc";
        Assert.assertFalse(RequestUtil.validHost(var4));
        String var5 = "10.82.3.2:8932";
        Assert.assertTrue(RequestUtil.validHost(var5));
    }

}
