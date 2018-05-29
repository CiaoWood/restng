package com.microdev.automation.restng.valid;

import com.microdev.automation.restng.validation.ValidResult;
import com.microdev.automation.restng.validation.Validator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ValidTest {

    @Test
    public void validTest() {
        String source = "{\"a\": 0, \"b\": 1, \"c\": 2.0}";
        String target = "{\"a\": 0.00, \"b\": 1.0, \"c\": 2.00}";
        ValidResult validResult = Validator.valid(source, target);
        Assert.assertTrue(validResult.result());
    }

    @Test
    public void validTest1() {
        String source = "{\"a\": 0, \"b\": 1, \"c\": 2.01}";
        String target = "{\"a\": 0.00, \"b\": 1.0, \"c\": 2.00}";
        ValidResult validResult = Validator.valid(source, target);
        Assert.assertFalse(validResult.result());
    }

}
