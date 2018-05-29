//package com.microdev.automation.restng.ut.demo.dataproviderexample;
//
//import com.microdev.automation.restng.RestNG;
//import com.microdev.automation.restng.annotation.UrlData;
//import com.microdev.automation.restng.com.microdev.automation.restng.ut.dataprovider.DataProviderFactory;
//import com.microdev.automation.restng.com.microdev.automation.restng.ut.util.com.microdev.automation.restng.ut.db.Mysql;
//import com.google.common.collect.ImmutableMap;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.testng.annotations.Test;
//
//import static com.microdev.automation.restng.validation.JsonValidator.containJson;
//import static io.restassured.RestAssured.given;
//import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
//import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
//import static org.testng.AssertJUnit.assertEquals;
//
///**
// * Created by wuchao on 17/7/17.
// */
//    public class GoNewcardTest extends RestNG {
//        private static final Logger logger = LoggerFactory.getLogger(GoNewcardTest.class);
//    private static final String expected = "{\"code\": \"0\",\"message\": \"添加新卡成功\"}";
//
//    //TODO 改成mock
//    //@Test(dataProviderClass = DataProviderFactory.class, dataProvider = "urlData", enabled = false)
//    @UrlData(path = "{url.data-maker.bankidquery}", key = "bankid")
//    public void newcarddataDriveTest(String bankid) {
//        ImmutableMap input = getBody(bankid);
//        given().body(input).when().post("{url.data-maker.newcard}").then().body(containJson(expected));
//        teardown(input);
//    }
//
//    private ImmutableMap getBody(String bankid) {
//        return ImmutableMap.of("bankid", bankid, "cardnum", randomNumeric(4), "nameoncard", randomAlphanumeric(10), "userid", "100");
//    }
//
//    public void teardown(ImmutableMap parameters) {
//        int num = Mysql.delete("businessbill", "T_CardWallet_00", "BankId", parameters.get("bankid").toString(), "CardNo", parameters.get("cardnum").toString());
//        assertEquals(num, 1);
//    }
//
//}
