package com.microdev.automation.restng.property;

import com.microdev.automation.restng.RestNG;
import com.microdev.automation.restng.env.Property;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PropertyTest extends RestNG {

    @Test
    @Description("property基础测试")
    public void propertyTest() {
        Assert.assertEquals(Property.get("url.repayfund"), "http://repayfund");
        Assert.assertEquals(Property.get("url.repayfund.summary"), "http://repayfund/gateway/api/v1/users/me/repayfunds/summary");
    }

    @Test(enabled = false)
    @Description("property带上service.tag测试")
    public void propertyTagTest() {
        System.setProperty("service.tag", "it");
        Assert.assertEquals(Property.get("url.repayfund"), "http://it.repayfund:8080");
        Assert.assertEquals(Property.get("url.demo"), "http://www.mocky.io");
        Assert.assertEquals(Property.get("url.repayfund.summary"), "http://it.repayfund:8080/gateway/api/v1/users/me/repayfunds/summary");
    }

    @Test(enabled = false)
    @Description("property中文测试")
    public void propertyChineseTest() {
        System.setProperty("service.tag", "中文");
        Assert.assertEquals(Property.get("url.chinese"), "i am chinese");
        Assert.assertEquals(Property.get("url.demo"), "http://www.mocky.io");
    }

}
