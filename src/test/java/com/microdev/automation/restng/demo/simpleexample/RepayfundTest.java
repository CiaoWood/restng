//package com.microdev.automation.restng.ut.demo.simpleexample;
//
//import com.microdev.automation.restng.RestNG;
//import com.microdev.automation.restng.annotation.ExcelData;
//import com.microdev.automation.restng.annotation.Headers;
//import com.microdev.automation.restng.annotation.JsonData;
//import com.microdev.automation.restng.com.microdev.automation.restng.ut.dataprovider.DataProviderFactory;
//import org.testng.annotations.Test;
//
//import java.io.IOException;
//import java.com.microdev.automation.restng.ut.util.HashMap;
//
//import static com.microdev.automation.restng.validation.JsonValidator.is;
//import static io.restassured.RestAssured.given;
//
///**
// * Created by wuchao on 17/7/17.
// */
//public class RepayfundTest extends RestNG {
//
//    @Test(dataProviderClass = DataProviderFactory.class, dataProvider = "restng", enabled = false)
//    @ExcelData(name="repayfund", sheet="1")
//    @Headers(userId = "data", auth = "userId")
//    public void payfundsTest(HashMap<String, String> data) throws IOException {
//        given().when().get("{url.repayfund.summary}").then().body("usableAmount", is(data.get("usableAmount")));
//    }
//
//    @Test(dataProviderClass = DataProviderFactory.class, dataProvider = "restng", enabled = false)
//    @ExcelData(name="repayfund", sheet="2") public void keyDriveTest(HashMap<String, String> data) {
//        RestNG.run(data);
//    }
//
//    @Test(dataProviderClass = DataProviderFactory.class, dataProvider = "restng", enabled = false)
//    @JsonData("repayfund") public void keyDrive_Json_Test(HashMap<String, String> data) {
//        RestNG.run(data);
//    }
//
//}
