package com.microdev.automation.restng.demo;

import com.microdev.automation.restng.annotation.*;
import com.microdev.automation.restng.validation.JsonValidator;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.put;
import static org.hamcrest.Matchers.equalTo;

public class DataProvidersTest extends DemoBase {

    @Test
    @Story("简单测试")
    public void simpleDemoTest() {
        put("{url.demo.hello}").then().body(JsonValidator.contain("{\"hello\": \"world\"}"));
    }

    @Test
    @Story("真实接口测试")
    @Headers(userId = "164017580", auth = "userId") // 改接口无法使用这个功能
    public void cardListTest() {
        given().when().get("{url.financeaccount-data-manager.list}").then().extract();
    }

    @Test(dataProvider = "url"/* or restng */, description = "检查宠物和主人的对应关系是否正确, 宠物名通过/pets接口动态获取")
    @Description("URL数据驱动")
    @UrlData(path = "http://localhost:19999/pets", key = "name")
    @Feature("数据驱动")
    public void urlDataProviderTest(String name) {
        given().
                baseUri(baseUri).
                when().
                get(name + "/owner").
                then().
                statusCode(200).
                body("name", equalTo(owners.get(name)));
    }

    @StatusCode
    private void runTest(Map<String, String> ownership) {
        given().
                baseUri(baseUri).
                when().
                get(ownership.get("pet") + "/owner").
                then().
                body("name", equalTo(ownership.get("owner")));
    }

    @Test(dataProvider = "xls"/* or restng */, description = "查宠物和主人的对应关系是否正确, 宠物名从excel(2007以前的版本，07开始往后不支持)文件中获取")
    @Description("EXCEL数据驱动检")
    //name对应的是文件名（不需要加后缀.xls），文件位置相对于src/test/resources/data
    //可以通过rows选取特定行（从1开始，忽略标题行），如"1,2-4,5"选取的是第1,2,3,4,5行数据
    @ExcelData(name = "demo/ownership", sheet = "demo")
    @Feature("数据驱动")
    public void xlsDataProviderTest(Map<String, String> ownership) {
        runTest(ownership);
    }

    @Test(dataProvider = "xls"/* or restng */, description = "检查宠物和主人的对应关系是否正确, 宠物名从excel(2007以前的版本，07开始往后不支持)文件中获取")
    @Description("EXCEL关键字驱动")
    @Feature("关键字驱动")
    @ExcelData(name = "demo/ownership", sheet = "key")
    public void xlsDataProviderKeyDriveTest(Map<String, String> ownership) {
        run(ownership);
    }


    @Test(dataProvider = "csv"/* or restng */, description = "检查宠物和主人的对应关系是否正确, 宠物名从excel(2007以前的版本，07开始往后不支持)文件中获取")
    @Feature("数据驱动")
    @Description("CSV数据驱动")
    //name对应的是文件名（不需要加后缀.csv），文件位置相对于src/test/resources/data，和xls一样支持用rows指定行号
    @CsvData(value = "demo/ownership", rows = "1")
    public void csvDataProviderTest(Map<String, String> ownership) {
        runTest(ownership);
    }


    @Test(dataProvider = "json"/* or restng */, description = "检查宠物和主人的对应关系是否正确, 宠物名从json文件中获取")
    @Feature("数据驱动")
    @Description("JSON数据驱动")
    //name对应的是文件名（不需要加后缀.json），文件位置相对于src/test/resources/data
    @JsonData("demo/ownership")
    public void jsonDataProviderTest1(Map<String, String> ownership) {
        runTest(ownership);
    }

    @Test(dataProvider = "json"/* or restng */, description = "检查宠物和主人的对应关系是否正确, 宠物名从json文件中获取")
    @Feature("数据驱动")
    @Description("JSON数据驱动")
    //name对应的是文件名（不需要加后缀.json），文件位置相对于src/test/resources/data
    @JsonData(files = {"demo/ownership", "demo/ownership"})
    public void jsonDataProviderTest2(Map<String, String> ownership) {
        runTest(ownership);
    }

    @Test(dataProvider = "json"/* or restng */, description = "检查宠物和主人的对应关系是否正确, 宠物名从json文件中获取")
    @Feature("数据驱动")
    @Description("JSON数据驱动-matcher")
    //name对应的是文件名（不需要加后缀.json），文件位置相对于src/test/resources/data
    @JsonData(files = {"demo/ownership"}, matcher = "pet:dogA")
    public void jsonDataProviderStringMatcherTest(Map<String, String> ownership) {
        runTest(ownership);
    }

    @Test(dataProvider = "json"/* or restng */, description = "检查宠物和主人的对应关系是否正确, 宠物名从json文件中获取")
    @Feature("数据驱动")
    @Description("JSON数据驱动-matcher-boolean")
    //name对应的是文件名（不需要加后缀.json），文件位置相对于src/test/resources/data
    @JsonData(files = {"demo/ownership"}, matcher = "boolean:true")
    public void jsonDataProviderBooleanMatcherTest(Map<String, String> ownership) {
        runTest(ownership);
    }

    @Test(dataProvider = "json"/* or restng */, description = "检查宠物和主人的对应关系是否正确, 宠物名从json文件中获取")
    @Feature("数据驱动")
    @Description("JSON数据驱动-matcher-numer")
    //name对应的是文件名（不需要加后缀.json），文件位置相对于src/test/resources/data
    @JsonData(value = "demo/ownership", matcher = "number:1.0")
    public void jsonDataProviderNumberMatcherTest(Map<String, String> ownership) {
        runTest(ownership);
    }
}
