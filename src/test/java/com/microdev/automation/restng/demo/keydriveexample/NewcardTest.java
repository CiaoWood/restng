//package com.microdev.automation.restng.ut.demo.keydriveexample;
//
//import com.microdev.automation.restng.RestNG;
//import com.microdev.automation.restng.annotation.ExcelData;
//import com.microdev.automation.restng.com.microdev.automation.restng.ut.dataprovider.DataProviderFactory;
//import com.microdev.automation.restng.com.microdev.automation.restng.ut.util.com.microdev.automation.restng.ut.db.Mysql;
//import io.qameta.allure.Feature;
//import io.qameta.allure.Story;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.Test;
//
//import java.com.microdev.automation.restng.ut.util.HashMap;
//
//import static org.testng.AssertJUnit.assertEquals;
//
///**
// * Created by wuchao on 17/7/17.
// */
//public class NewcardTest extends RestNG {
//
//    @Story("keydrive")
//    @Feature("制造新卡")
//    @Test(dataProviderClass = DataProviderFactory.class, dataProvider = "restng")
//    @ExcelData(name = "newcard", sheet = "keydrive")
//    public void newcardeyDriveTest(HashMap<String, String> data) {
//        RestNG.run(data);
//    }
//
//    @AfterMethod
//    public void cleanCard(Object[] args) {
//        HashMap<String, String> data = (HashMap<String, String>) args[0];
//        int num = Mysql.delete("businessbill", "T_CardWallet_00", "BankId", data.get("body:bankid"), "CardNo", data.get("body:cardnum"));
//        assertEquals(num, 1);
//    }
//
//}
