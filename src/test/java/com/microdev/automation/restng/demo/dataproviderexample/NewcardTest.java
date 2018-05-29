//package com.microdev.automation.restng.ut.demo.dataproviderexample;
//
//import com.microdev.automation.restng.RestNG;
//import com.microdev.automation.restng.com.microdev.automation.restng.ut.util.com.microdev.automation.restng.ut.db.Mysql;
//import com.google.common.collect.ImmutableMap;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
//
//import java.com.microdev.automation.restng.ut.util.List;
//
//import static com.microdev.automation.restng.validation.JsonValidator.contain;
//import static io.restassured.RestAssured.given;
//import static io.restassured.RestAssured.when;
//import static org.hamcrest.Matchers.lessThan;
//import static org.testng.AssertJUnit.assertEquals;
//
///**
// * Created by wuchao on 17/7/17.
// */
//public class NewcardTest extends RestNG {
//    private static final Logger logger = LoggerFactory.getLogger(NewcardTest.class);
//    private static final String expected = "{\"code\": \"0\",\"message\": \"添加新卡成功\"}";
//
//    @DataProvider(name = "newcards", parallel = true)
//    public static Object[][] CardData() {
//        List<Integer> bankIds = when().get("{url.data-maker.bankidquery}").then().extract().path("bankid");
//        Object[][] data = new Object[5][3];
//        for (int i = 0; i < 5; i++) {
//            data[i][0] = bankIds.get(i);
//            data[i][1] = RandomStringUtils.randomNumeric(4); // 随机数字
//            data[i][2] = RandomStringUtils.randomAlphanumeric(10); // 随机字母数字
//        }
//        return data;
//    }
//
//    //TODO 改成mock
//    @Test(dataProvider = "newcards", enabled = false)
//    public void newcarddataDriveTest(Integer bankid, String cardnum, String nameoncard) {
//        given().body(getBody(bankid, cardnum, nameoncard)).when().post("{url.data-maker.newcard}").then().body(contain(expected)).time(lessThan(10L));
//    }
//
//    private Object getBody(Integer bankid, String cardnum, String nameoncard) {
//        return ImmutableMap.of("bankid", bankid, "cardnum", cardnum, "nameoncard", nameoncard, "userid", "100");
//    }
//
//    @AfterMethod
//    public void teardown(Object[] parameters) {
//        int num = Mysql.delete("businessbill", "T_CardWallet_00", "BankId", parameters[0].toString(), "CardNo", parameters[1].toString());
//        assertEquals(1, num);
//    }
//
//}
